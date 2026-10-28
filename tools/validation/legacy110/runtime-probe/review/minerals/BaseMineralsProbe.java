package review.minerals;

import com.google.gson.*;
import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.init.Materials;
import com.mcmoddev.lib.material.MMDMaterial;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.security.MessageDigest;
import java.util.*;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import zone.moddev.mc.baseminerals.util.Config;
import zone.moddev.mc.orespawn.api.OreSpawnApi;

/** Installed-jar assertions. This source set is excluded from all release archives. */
@Mod(modid="basemineralsprobe",name="Base Minerals checks",version="1",acceptableRemoteVersions="*",dependencies="required-after:baseminerals")
public final class BaseMineralsProbe {
    static final String[] ORES={"lithium","niter","salt","sulfur","phosphorus","potash","saltpeter"};
    static net.minecraft.world.DimensionType dimension;
    @Mod.EventHandler public void preInit(FMLPreInitializationEvent event) {
        if(Boolean.getBoolean("minerals.custom")) {
            dimension=net.minecraft.world.DimensionType.register("minerals_fixture","_minerals",42,FixtureDimension.class,false);
            DimensionManager.registerDimension(42,dimension);
        }
        if(Boolean.getBoolean("minerals.record")) RecordingPatterns.register();
    }
    public static final class FixtureDimension extends net.minecraft.world.WorldProviderSurface {
        @Override public net.minecraft.world.DimensionType getDimensionType(){return dimension;}
        @Override public net.minecraft.world.chunk.IChunkGenerator createChunkGenerator(){
            String layers=Boolean.getBoolean("minerals.negativeHosts")?"minecraft:bedrock,20*minecraft:stone,2*minecraft:obsidian,2*minecraft:water,2*minecraft:bedrock,2*minecraft:air,99*minecraft:stone":"minecraft:bedrock,127*minecraft:stone";
            if(Boolean.getBoolean("minerals.variantHosts")) {
                StringBuilder variants=new StringBuilder("minecraft:bedrock");
                for(int y=1;y<128;y++)variants.append(",minecraft:stone:").append(1+(y-1)%6);
                layers=variants.toString();
            }
            return new net.minecraft.world.gen.ChunkProviderFlat(world,world.getSeed(),false,"3;"+layers+";1;");
        }
    }
    @Mod.EventHandler public void started(FMLServerStartedEvent event) throws Exception {
        if(System.getProperty("minerals.phase")==null)return;
        MinecraftServer server=FMLCommonHandler.instance().getMinecraftServerInstance();
        if(server.isDedicatedServer()) {try{run(server);}finally{server.initiateShutdown();}}
    }
    static void require(boolean value,String detail,JsonArray failures){if(!value)failures.add(new JsonPrimitive(detail));}
    public static void run(MinecraftServer server) throws Exception {
        String phase=System.getProperty("minerals.phase");
        JsonObject report=new JsonObject(); JsonArray failures=new JsonArray();
        report.addProperty("phase",phase);report.addProperty("seed",server.worldServerForDimension(0).getSeed());
        Class<?> oldConsumer=Class.forName("review.minerals.LegacyConsumer");
        require(oldConsumer.getMethod("material").invoke(null)==Materials.getMaterialByName("lithium"),"Old binary linked to a different material registry",failures);
        require(oldConsumer.getMethod("proxy").invoke(null)==zone.moddev.mc.baseminerals.BaseMinerals.proxy,"Old binary linked to a different proxy",failures);
        require(oldConsumer.getMethod("reflected").invoke(null)==zone.moddev.mc.baseminerals.BaseMinerals.class,"Old reflection name did not relink",failures);
        require(com.mcmoddev.lib.util.ConfigBase.class.isAssignableFrom(Class.forName("review.minerals.LegacyConfiguration")),"Old configuration subclass lost its shared API",failures);
        report.addProperty("legacyBinarySharedState",failures.size()==0);
        for(String name:ORES) {
            boolean enabled=Config.isEnabled(name);
            require(Materials.hasMaterial(name)==enabled,"Material switch: "+name,failures);
            require(ForgeRegistries.BLOCKS.containsKey(new ResourceLocation("baseminerals",name+"_ore"))==enabled,"Ore registry: "+name,failures);
            if(enabled)require(Materials.getMaterialByName(name).hasItem(Names.POWDER),"Missing powder: "+name,failures);
        }
        for(String name:new String[]{"lithium","silicon"}) if(Config.isEnabled(name)) {
            MMDMaterial material=Materials.getMaterialByName(name);
            require(material.hasItem(Names.INGOT)&&material.hasItem(Names.NUGGET),"Metal forms missing: "+name,failures);
            require(material.getFluid()!=null&&material.getFluidBlock()!=null,"Molten forms missing: "+name,failures);
            if(material.getFluid()!=null) require(net.minecraftforge.fluids.FluidRegistry.getBucketFluids().contains(material.getFluid()),"Bucket missing: "+name,failures);
        }
        require(!ForgeRegistries.BLOCKS.containsKey(new ResourceLocation("baseminerals","charcoal_ore")),"Charcoal ore registered",failures);
        require(!ForgeRegistries.BLOCKS.containsKey(new ResourceLocation("baseminerals","silicon_ore")),"Silicon ore registered",failures);
        require(Materials.hasMaterial("charcoal"),"Base Metals shared charcoal missing",failures);
        if(Loader.isModLoaded("tconstruct")) CastingChecks.check(failures);
        require(OreSpawnApi.getActiveProfile(server).isPresent(),"No active OreSpawn profile",failures);
        if(OreSpawnApi.getActiveProfile(server).isPresent()) {
            JsonObject profile=OreSpawnApi.getActiveProfile(server).get().toJson(); report.add("profile",profile);
            if(!Loader.isModLoaded("mineralogy"))require(profile.getAsJsonObject("terrain_dimensions").entrySet().size()==0,"Ore-only mods activated strata",failures);
            if(Boolean.getBoolean("minerals.sample"))sample(server,profile,report,failures);
        }
        if(Boolean.getBoolean("minerals.saved"))saved(server.worldServerForDimension(0),phase,failures);
        report.add("recording",RecordingPatterns.report(failures));
        report.add("failures",failures);report.addProperty("passed",failures.size()==0);
        Files.write(Paths.get(phase+"-minerals.json"),(new GsonBuilder().setPrettyPrinting().create().toJson(report)+"\n").getBytes(StandardCharsets.UTF_8));
        System.out.println("BASE_MINERALS_RUNTIME_"+(failures.size()==0?"PASS":"FAIL")+" "+failures);
    }
    private static void sample(MinecraftServer server,JsonObject profile,JsonObject report,JsonArray failures) throws Exception {
        JsonObject dimensions=new JsonObject(); int width=Integer.getInteger("minerals.width",16);
        int origin="later".equals(System.getProperty("minerals.phase"))?128:64;
        int[] ids=Boolean.getBoolean("minerals.custom")?new int[]{42}:new int[]{0,-1,1};
        for(int id:ids) {
            if(DimensionManager.getWorld(id)==null)DimensionManager.initDimension(id);
            WorldServer world=server.worldServerForDimension(id);
            long start=System.nanoTime();
            for(int x=origin-2;x<origin+width+2;x++)for(int z=origin-2;z<origin+width+2;z++)world.getChunkFromChunkCoords(x,z);
            // Trigger population of the interior and its border before taking a census.
            long generated=System.nanoTime()-start;
            JsonObject counts=new JsonObject(),heights=new JsonObject(),shared=new JsonObject(),variants=new JsonObject(); MessageDigest digest=MessageDigest.getInstance("SHA-256"),sharedDigest=MessageDigest.getInstance("SHA-256");
            BlockPos.MutableBlockPos pos=new BlockPos.MutableBlockPos();
            for(int cx=origin;cx<origin+width;cx++)for(int cz=origin;cz<origin+width;cz++) {
                net.minecraft.world.chunk.Chunk chunk=world.getChunkFromChunkCoords(cx,cz);
                for(int x=0;x<16;x++)for(int z=0;z<16;z++)for(int y=0;y<256;y++) {
                    pos.setPos(cx*16+x,y,cz*16+z); Block block=chunk.getBlockState(pos).getBlock();ResourceLocation key=block.getRegistryName();
                    if(key!=null && Boolean.getBoolean("minerals.shared") &&
                        (("electricadvantage".equals(key.getResourceDomain()) && ("li_ore".equals(key.getResourcePath()) || "sulfur_ore".equals(key.getResourcePath()))) ||
                         ("mineralogy".equals(key.getResourceDomain()) && "sulfur_ore".equals(key.getResourcePath())) ||
                         ("baseminerals".equals(key.getResourceDomain()) && ("lithium_ore".equals(key.getResourcePath()) || "sulfur_ore".equals(key.getResourcePath()))))) {
                        increment(shared,key.toString());
                        sharedDigest.update((pos.getX()+","+y+","+pos.getZ()+":"+(key.getResourcePath().contains("sulfur")?"sulfur":"lithium")+"\n").getBytes(StandardCharsets.UTF_8));
                    }
                    if(key==null||!"baseminerals".equals(key.getResourceDomain())||!key.getResourcePath().endsWith("_ore"))continue;
                    String name=key.getResourcePath();
                    increment(counts,name); if(!heights.has(name))heights.add(name,new JsonObject()); increment(heights.getAsJsonObject(name),String.valueOf(y));
                    digest.update((pos.getX()+","+y+","+pos.getZ()+":"+name+"\n").getBytes(StandardCharsets.UTF_8));
                    require(id==0||id==42,"Ore in excluded dimension "+id,failures);
                    JsonObject rule=rule(profile,name);
                    require(rule!=null&&rule.get("enabled").getAsBoolean(),"Disabled/undeclared ore: "+name,failures);
                    if(rule!=null) {
                        JsonObject p=activePlacement(profile,rule,id);
                        require(p!=null&&p.get("enabled").getAsBoolean(),"No enabled placement: "+name,failures);
                        if(p!=null)require(y>=p.get("min_y").getAsInt()&&y<=p.get("max_y").getAsInt(),"Ore outside height: "+name+" at "+y,failures);
                    }
                    if(Boolean.getBoolean("minerals.negativeHosts"))require(y<21||y>28,"Ore replaced protected host at Y="+y,failures);
                    if(Boolean.getBoolean("minerals.variantHosts"))increment(variants,String.valueOf(1+(y-1)%6));
                }
            }
            if((id==0||id==42)&&!Boolean.getBoolean("minerals.noOres")) for(String name:ORES)
                if(Config.isEnabled(name)) {
                    JsonObject rule=rule(profile,name+"_ore");
                    if(rule!=null && rule.get("enabled").getAsBoolean() && !(Boolean.getBoolean("minerals.shared") && ("lithium".equals(name)||"sulfur".equals(name)))) require(counts.has(name+"_ore"),"No generated sample: "+name,failures);
                }
            JsonObject result=new JsonObject();result.add("counts",counts);result.add("sharedOutputs",shared);result.add("heights",heights);result.addProperty("chunks",width*width);
            if(Boolean.getBoolean("minerals.variantHosts")) {
                for(int variant=1;variant<=6;variant++)require(variants.has(String.valueOf(variant)),"No positive stone metadata sample: "+variant,failures);
                result.add("hostVariants",variants);
            }
            result.addProperty("generationNanos",generated);result.addProperty("digest",hex(digest.digest()));dimensions.add(String.valueOf(id),result);
            result.addProperty("sharedDigest",hex(sharedDigest.digest()));
            System.out.println("BASE_MINERALS_CENSUS dimension="+id+" chunks="+(width*width)+" counts="+counts);
        }
        report.add("dimensions",dimensions);
    }
    private static JsonObject rule(JsonObject profile,String block) {
        JsonObject ores=profile.getAsJsonObject("ores");
        JsonObject standard=ores.getAsJsonObject("baseminerals:legacy/"+block);
        if(standard!=null)return standard;
        // OS1 owns its converted IDs; match that saved declaration by its output.
        for(Map.Entry<String,JsonElement> entry:ores.entrySet()) {
            JsonObject candidate=entry.getValue().getAsJsonObject();
            if(candidate.has("block")&&("baseminerals:"+block).equals(candidate.get("block").getAsString()))return candidate;
        }
        return null;
    }
    private static JsonObject placement(JsonObject rule,int dimension) {
        String id=dimension==0?"minecraft:overworld":dimension==-1?"minecraft:the_nether":dimension==1?"minecraft:the_end":"minerals_fixture";
        if(rule.has("dimensions")&&rule.getAsJsonObject("dimensions").has(id))return rule.getAsJsonObject("dimensions").getAsJsonObject(id);
        return rule.getAsJsonObject("dimension_selectors").getAsJsonObject("orespawn:all_except_nether_end");
    }
    private static JsonObject activePlacement(JsonObject profile,JsonObject rule,int dimension) {
        if(Boolean.getBoolean("minerals.shared")&&rule.has("material")) {
            String domain=dimension==0?"minecraft:overworld":dimension==-1?"minecraft:the_nether":dimension==1?"minecraft:the_end":"minerals_fixture";
            JsonObject policies=profile.getAsJsonObject("ore_source_policies");
            JsonObject policy=policies==null?null:policies.getAsJsonObject(rule.get("material").getAsString()+"|"+domain);
            if(policy!=null&&"consolidated".equals(policy.get("mode").getAsString())) {
                JsonElement selected=policy.getAsJsonObject("placement_sources").get("orespawn:standard");
                if(selected!=null) {
                    JsonObject source=profile.getAsJsonObject("ores").getAsJsonObject(selected.getAsString());
                    if(source!=null)return placement(source,dimension);
                }
            }
        }
        return placement(rule,dimension);
    }
    private static void increment(JsonObject object,String key){object.addProperty(key,object.has(key)?object.get(key).getAsInt()+1:1);}
    private static String hex(byte[] data){StringBuilder s=new StringBuilder();for(byte b:data)s.append(String.format("%02x",b));return s.toString();}
    private static void saved(WorldServer world,String phase,JsonArray failures) {
        BlockPos position=new BlockPos(0,180,0);
        if("first".equals(phase)) {
            world.setBlockState(position,Blocks.CHEST.getDefaultState());TileEntityChest chest=(TileEntityChest)world.getTileEntity(position);int slot=0;
            for(String name:ORES)if(Config.isEnabled(name)) {
                ItemStack stack=Materials.getMaterialByName(name).getItemStack(Names.POWDER,3);
                stack.setStackDisplayName("Saved "+name);stack.getTagCompound().setString("BaseMineralsCheck",name);chest.setInventorySlotContents(slot++,stack);
            }
        }
        TileEntityChest chest=(TileEntityChest)world.getTileEntity(position);require(chest!=null,"Saved chest missing",failures);if(chest==null)return;int slot=0;
        for(String name:ORES)if(Config.isEnabled(name)) {
            ItemStack stack=chest.getStackInSlot(slot++);
            require(stack!=null&&stack.stackSize==3&&("Saved "+name).equals(stack.getDisplayName())&&name.equals(stack.getTagCompound().getString("BaseMineralsCheck")),"Saved stack changed: "+name,failures);
        }
    }
    private static final class CastingChecks {
        static void check(JsonArray failures) {
            for(String name:new String[]{"lithium","silicon"})if(Config.isEnabled(name)) {
                MMDMaterial material=Materials.getMaterialByName(name);
                net.minecraftforge.fluids.Fluid fluid=material.getFluid();
                require(fluid!=null,"Casting fluid missing: "+name,failures);
                if(fluid!=null)require(slimeknights.tconstruct.library.TinkerRegistry.getTableCasting((net.minecraft.item.ItemStack)null,fluid)!=null,"Ingot casting missing: "+name,failures);
                require(slimeknights.tconstruct.library.TinkerRegistry.getMelting(material.getItemStack(Names.INGOT))!=null,"Ingot melting missing: "+name,failures);
            }
        }
    }
}
