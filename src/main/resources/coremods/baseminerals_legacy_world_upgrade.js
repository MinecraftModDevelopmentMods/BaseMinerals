var Opcodes = Java.type('org.objectweb.asm.Opcodes');
var InsnNode = Java.type('org.objectweb.asm.tree.InsnNode');
var InsnList = Java.type('org.objectweb.asm.tree.InsnList');
var JumpInsnNode = Java.type('org.objectweb.asm.tree.JumpInsnNode');
var LabelNode = Java.type('org.objectweb.asm.tree.LabelNode');
var MethodInsnNode = Java.type('org.objectweb.asm.tree.MethodInsnNode');
var VarInsnNode = Java.type('org.objectweb.asm.tree.VarInsnNode');
var UPGRADE = 'zone/moddev/mc/baseminerals/migration/LegacyWorldUpgrade';
var NBT = 'Lnet/minecraft/nbt/NBTTagCompound;';
var FIX_ARGS = '(Lcom/mojang/datafixers/DataFixer;Lcom/mojang/datafixers/DSL$TypeReference;' + NBT;

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

function initializeCoreMod() {
    return {
        'baseminerals_forge25_leaves_fixer': {
            'target': { 'type': 'CLASS', 'name': 'net.minecraft.util.datafix.fixes.LeavesFix$Section' },
            'transformer': function(classNode) {
                var patched = false;
                for (var i = 0; i < classNode.methods.size(); ++i) {
                    var method = classNode.methods.get(i);
                    if (method.desc !== '(Lcom/mojang/datafixers/Typed;)Lcom/mojang/datafixers/Typed;') continue;
                    for (var instruction = method.instructions.getFirst(); instruction !== null;
                            instruction = instruction.getNext()) {
                        // Some Forge installations already contain the corrected erased overload.
                        if (instruction.getOpcode() === Opcodes.INVOKEVIRTUAL
                                && instruction.owner === 'com/mojang/datafixers/Typed' && instruction.name === 'set'
                                && instruction.desc === '(Lcom/mojang/datafixers/OpticFinder;' +
                                        'Ljava/lang/Object;)Lcom/mojang/datafixers/Typed;') {
                            patched = true;
                            continue;
                        }
                        if (instruction.getOpcode() !== Opcodes.CHECKCAST
                                || instruction.desc !== 'com/mojang/datafixers/Typed') continue;
                        var call = instruction.getNext();
                        while (call !== null && call.getOpcode() < 0) call = call.getNext();
                        if (call === null || call.getOpcode() !== Opcodes.INVOKEVIRTUAL
                                || call.owner !== 'com/mojang/datafixers/Typed' || call.name !== 'set'
                                || call.desc !== '(Lcom/mojang/datafixers/OpticFinder;' +
                                        'Lcom/mojang/datafixers/Typed;)Lcom/mojang/datafixers/Typed;') continue;
                        method.instructions.remove(instruction);
                        call.desc = '(Lcom/mojang/datafixers/OpticFinder;' +
                                'Ljava/lang/Object;)Lcom/mojang/datafixers/Typed;';
                        patched = true;
                        break;
                    }
                }
                if (!patched) throw new Error('Base Minerals could not repair the Forge 25 leaves data fixer');
                return classNode;
            }
        },
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
                throw new Error('Base Minerals could not find the Forge 25 numeric state converter');
            }
        },
        'baseminerals_save_registry': {
            'target': { 'type': 'CLASS', 'name': 'net.minecraft.world.storage.SaveFormatOld' },
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
                throw new Error('Base Minerals could not find the Forge 25 world registry reader');
            }
        },
        'baseminerals_chunk_upgrade': {
            'target': { 'type': 'CLASS', 'name': 'net.minecraft.world.chunk.storage.AnvilChunkLoader' },
            'transformer': function(node) {
                var descriptor = '(Lnet/minecraft/world/dimension/DimensionType;'
                    + 'Lnet/minecraft/world/storage/WorldSavedDataStorage;II)' + NBT;
                var patched = 0;
                for (var i = 0; i < node.methods.size(); i++) {
                    var method = node.methods.get(i);
                    if (method.desc !== descriptor) continue;
                    for (var insn = method.instructions.getFirst(); insn !== null; ) {
                        var next = insn.getNext();
                        if (insn.getOpcode() === Opcodes.INVOKESTATIC && insn.owner === 'net/minecraft/nbt/NBTUtil') {
                            if (insn.desc === FIX_ARGS + 'II)' + NBT) {
                                method.instructions.insertBefore(insn, new VarInsnNode(Opcodes.ALOAD, 0));
                                method.instructions.set(insn, call('fixChunk', FIX_ARGS
                                    + 'IILnet/minecraft/world/chunk/storage/AnvilChunkLoader;)' + NBT));
                                patched++;
                            } else if (insn.desc === FIX_ARGS + 'I)' + NBT) {
                                method.instructions.set(insn, call('finishChunk', FIX_ARGS + 'I)' + NBT));
                                patched++;
                            }
                        }
                        insn = next;
                    }
                }
                if (patched !== 2) throw new Error('Base Minerals could not wrap both Forge 25 chunk upgrades');
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
                        prepare.add(call('migrateItems', '(Lnet/minecraft/nbt/INBTBase;)V'));
                        method.instructions.insert(insn, prepare);
                        patched++;
                    }
                }
                if (patched === 0) throw new Error('Base Minerals could not find the Forge 25 player data reader');
                return node;
            }
        },
        'baseminerals_molten_mineral_renderer': {
            'target': { 'type': 'CLASS', 'name': 'net.minecraft.client.renderer.BlockFluidRenderer' },
            'transformer': function(classNode) {
                var descriptor = '(Lnet/minecraft/world/IWorldReader;Lnet/minecraft/util/math/BlockPos;' +
                        'Lnet/minecraft/client/renderer/BufferBuilder;Lnet/minecraft/fluid/IFluidState;)Z';
                for (var i = 0; i < classNode.methods.size(); ++i) {
                    var method = classNode.methods.get(i);
                    if (method.desc !== descriptor) continue;
                    var flag = false, sprites = false, color = false;
                    for (var instruction = method.instructions.getFirst(); instruction !== null;
                            instruction = instruction.getNext()) {
                        if (!flag && instruction.getOpcode() === Opcodes.ISTORE && instruction.var === 5) {
                            var hookFlag = new InsnList();
                            hookFlag.add(new VarInsnNode(Opcodes.ALOAD, 4));
                            hookFlag.add(new VarInsnNode(Opcodes.ILOAD, 5));
                            hookFlag.add(new MethodInsnNode(Opcodes.INVOKESTATIC,
                                    'zone/moddev/mc/baseminerals/client/MineralFluidRenderer',
                                    'useOpaqueFluidPath', '(Lnet/minecraft/fluid/IFluidState;Z)Z', false));
                            hookFlag.add(new VarInsnNode(Opcodes.ISTORE, 5));
                            method.instructions.insert(instruction, hookFlag);
                            flag = true;
                        } else if (!sprites && instruction.getOpcode() === Opcodes.ASTORE && instruction.var === 6) {
                            var hookSprites = new InsnList();
                            hookSprites.add(new VarInsnNode(Opcodes.ALOAD, 4));
                            hookSprites.add(new VarInsnNode(Opcodes.ALOAD, 6));
                            hookSprites.add(new MethodInsnNode(Opcodes.INVOKESTATIC,
                                    'zone/moddev/mc/baseminerals/client/MineralFluidRenderer',
                                    'overrideSprites',
                                    '(Lnet/minecraft/fluid/IFluidState;[Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;)' +
                                    '[Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;', false));
                            hookSprites.add(new VarInsnNode(Opcodes.ASTORE, 6));
                            method.instructions.insert(instruction, hookSprites);
                            sprites = true;
                        } else if (!color && instruction.getOpcode() === Opcodes.ISTORE && instruction.var === 7) {
                            var hookColor = new InsnList();
                            hookColor.add(new VarInsnNode(Opcodes.ALOAD, 4));
                            hookColor.add(new VarInsnNode(Opcodes.ILOAD, 7));
                            hookColor.add(new MethodInsnNode(Opcodes.INVOKESTATIC,
                                    'zone/moddev/mc/baseminerals/client/MineralFluidRenderer',
                                    'overrideColor', '(Lnet/minecraft/fluid/IFluidState;I)I', false));
                            hookColor.add(new VarInsnNode(Opcodes.ISTORE, 7));
                            method.instructions.insert(instruction, hookColor);
                            color = true;
                        }
                    }
                    if (!flag || !sprites || !color) throw new Error('Base Minerals could not patch the Forge 25 fluid renderer');
                }
                return classNode;
            }
        }
    };
}
