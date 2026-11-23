var Opcodes = Java.type('org.objectweb.asm.Opcodes');
var InsnNode = Java.type('org.objectweb.asm.tree.InsnNode');
var InsnList = Java.type('org.objectweb.asm.tree.InsnList');
var JumpInsnNode = Java.type('org.objectweb.asm.tree.JumpInsnNode');
var LabelNode = Java.type('org.objectweb.asm.tree.LabelNode');
var MethodInsnNode = Java.type('org.objectweb.asm.tree.MethodInsnNode');
var VarInsnNode = Java.type('org.objectweb.asm.tree.VarInsnNode');
var UPGRADE = 'zone/moddev/mc/baseminerals/migration/LegacyWorldUpgrade';
var NBT = 'Lnet/minecraft/nbt/CompoundNBT;';
var FIX_ARGS = '(Lcom/mojang/datafixers/DataFixer;Lnet/minecraft/util/datafix/DefaultTypeReferences;' + NBT;

function call(name, descriptor) {
    return new MethodInsnNode(Opcodes.INVOKESTATIC, UPGRADE, name, descriptor, false);
}

function previousOpcode(insn) {
    var previous = insn.getPrevious();
    while (previous !== null && previous.getOpcode() < 0) previous = previous.getPrevious();
    return previous;
}

function isNbtRead(insn) {
    var previous = previousOpcode(insn);
    return insn.getOpcode() === Opcodes.ASTORE && previous !== null
        && previous.getOpcode() === Opcodes.INVOKESTATIC
        && previous.owner === 'net/minecraft/nbt/CompressedStreamTools'
        && previous.desc.endsWith(')' + NBT);
}

var FieldInsnNode = Java.type('org.objectweb.asm.tree.FieldInsnNode');
var MethodNode = Java.type('org.objectweb.asm.tree.MethodNode');
function initializeCoreMod() {
    return {
        'baseminerals_numeric_states': {
            'target': { 'type': 'CLASS', 'name': 'net.minecraft.util.datafix.fixes.BlockStateFlatteningMap' },
            'transformer': function(node) {
                var descriptor = '(I)Lcom/mojang/datafixers/Dynamic;';
                for (var i = 0; i < node.methods.size(); i++) {
                    var method = node.methods.get(i);
                    if (method.desc !== descriptor) continue;
                    var vanilla = new LabelNode();
                    var prefix = new InsnList();
                    prefix.add(new VarInsnNode(Opcodes.ILOAD, 0));
                    prefix.add(call('mineralState', descriptor));
                    prefix.add(new InsnNode(Opcodes.DUP));
                    prefix.add(new JumpInsnNode(Opcodes.IFNULL, vanilla));
                    prefix.add(new InsnNode(Opcodes.ARETURN));
                    prefix.add(vanilla);
                    prefix.add(new InsnNode(Opcodes.POP));
                    method.instructions.insert(prefix);
                    return node;
                }
                throw new Error('Base Minerals could not find the Forge 28 numeric state converter');
            }
        },
        'baseminerals_save_registry': {
            'target': { 'type': 'CLASS', 'name': 'net.minecraft.world.storage.SaveFormat' },
            'transformer': function(node) {
                var descriptor = '(Ljava/io/File;Lcom/mojang/datafixers/DataFixer;'
                    + 'Lnet/minecraft/world/storage/SaveHandler;)Lnet/minecraft/world/storage/WorldInfo;';
                for (var i = 0; i < node.methods.size(); i++) {
                    var method = node.methods.get(i);
                    if (method.desc !== descriptor) continue;
                    for (var insn = method.instructions.getFirst(); insn !== null; insn = insn.getNext()) {
                        if (!isNbtRead(insn)) continue;
                        var prepare = new InsnList();
                        prepare.add(new VarInsnNode(Opcodes.ALOAD, insn.var));
                        prepare.add(new VarInsnNode(Opcodes.ALOAD, 0));
                        prepare.add(new VarInsnNode(Opcodes.ALOAD, 2));
                        prepare.add(call('prepareWorld', '(' + NBT
                            + 'Ljava/io/File;Lnet/minecraft/world/storage/SaveHandler;)V'));
                        method.instructions.insert(insn, prepare);
                        return node;
                    }
                }
                throw new Error('Base Minerals could not find the Forge 28 world registry reader');
            }
        },
        'baseminerals_region_directory': {
            target: {type: 'CLASS', name: 'net.minecraft.world.chunk.storage.RegionFileCache'},
            transformer: function(node) {
                var field = null;
                for (var i = 0; i < node.fields.size(); ++i) {
                    var candidate = node.fields.get(i);
                    if (candidate.desc !== 'Ljava/io/File;') continue;
                    if (field !== null) throw new Error('Ambiguous Forge 28 region directory');
                    field = candidate;
                }
                if (field === null) throw new Error('Missing Forge 28 region directory');
                var getter = new MethodNode(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL,
                    'baseminerals$getRegionDirectory', '()Ljava/io/File;', null, null);
                getter.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
                getter.instructions.add(new FieldInsnNode(Opcodes.GETFIELD, node.name, field.name, field.desc));
                getter.instructions.add(new InsnNode(Opcodes.ARETURN));
                getter.maxStack = 1;
                getter.maxLocals = 1;
                node.methods.add(getter);
                return node;
            }
        },
        'baseminerals_chunk_upgrade': {
            target: {type: 'CLASS', name: 'net.minecraft.world.chunk.storage.ChunkLoader'},
            transformer: function(node) {
                var patched = 0;
                for (var i = 0; i < node.methods.size(); ++i) {
                    var method = node.methods.get(i);
                    if (method.desc !== '(Lnet/minecraft/world/dimension/DimensionType;Ljava/util/function/Supplier;' + NBT + ')' + NBT) continue;
                    for (var insn = method.instructions.getFirst(); insn !== null;) {
                        var next = insn.getNext();
                        if (insn.getOpcode() === Opcodes.INVOKESTATIC && insn.owner === 'net/minecraft/nbt/NBTUtil') {
                            if (insn.desc === FIX_ARGS + 'II)' + NBT) {
                                var directory = new InsnList();
                                directory.add(new VarInsnNode(Opcodes.ALOAD, 0));
                                directory.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL,
                                    'net/minecraft/world/chunk/storage/RegionFileCache',
                                    'baseminerals$getRegionDirectory', '()Ljava/io/File;', false));
                                method.instructions.insertBefore(insn, directory);
                                method.instructions.set(insn, call('fixChunk', FIX_ARGS + 'IILjava/io/File;)' + NBT));
                                patched++;
                            } else if (insn.desc === FIX_ARGS + 'I)' + NBT) {
                                method.instructions.set(insn, call('finishChunk', FIX_ARGS + 'I)' + NBT));
                                patched++;
                            }
                        }
                        insn = next;
                    }
                }
                if (patched !== 2) throw new Error('Base Minerals could not wrap both Forge 28 chunk upgrades');
                return node;
            }
        },
        'baseminerals_player_upgrade': {
            'target': { 'type': 'CLASS', 'name': 'net.minecraft.world.storage.SaveHandler' },
            'transformer': function(node) {
                var patched = 0;
                for (var i = 0; i < node.methods.size(); i++) {
                    var method = node.methods.get(i);
                    for (var insn = method.instructions.getFirst(); insn !== null; insn = insn.getNext()) {
                        if (!isNbtRead(insn)) continue;
                        var prepare = new InsnList();
                        prepare.add(new VarInsnNode(Opcodes.ALOAD, insn.var));
                        prepare.add(call('migrateItems', '(Lnet/minecraft/nbt/INBT;)V'));
                        method.instructions.insert(insn, prepare);
                        patched++;
                    }
                }
                if (patched === 0) throw new Error('Base Minerals could not find the Forge 28 player data reader');
                return node;
            }
        }
    };
}
