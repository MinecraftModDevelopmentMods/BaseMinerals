package review.minerals;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;
import zone.moddev.mc.orespawn.api.*;

/** Synthetic provider contract fixture. This is not the Electric Advantage mod. */
@Mod(modid="electricadvantage",name="Synthetic source-policy fixture",version="test",dependencies="required-after:baseminerals")
public final class SourceFixture {
    private Block lithium;
    private Block sulfur;

    @Mod.EventHandler public void preInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent public void registerBlocks(RegistryEvent.Register<Block> event) {
        lithium = new Block(Material.ROCK).setRegistryName("electricadvantage", "li_ore");
        sulfur = new Block(Material.ROCK).setRegistryName("electricadvantage", "sulfur_ore");
        event.getRegistry().registerAll(lithium, sulfur);
    }

    @SubscribeEvent public void registerItems(RegistryEvent.Register<Item> event) {
        for (Block block : new Block[]{lithium, sulfur}) {
            Item item = new ItemBlock(block).setRegistryName(block.getRegistryName());
            event.getRegistry().register(item);
            OreDictionary.registerOre(block == lithium ? "oreLithium" : "oreSulfur", item);
        }
    }
    @Mod.EventHandler public void init(FMLInitializationEvent event) {
        WorldgenProvider.Builder provider=WorldgenProvider.builder("electricadvantage",1);
        for(String material:new String[]{"lithium","sulfur"}) {
            String block="lithium".equals(material)?"li_ore":"sulfur_ore";
            provider.ore(new ResourceLocation("electricadvantage","ore/"+material),new ResourceLocation("electricadvantage",block),ore->ore
                .enabled(false).material(new ResourceLocation("orespawn",material)).nativeGeneration(false).retrogen(false)
                .dimension(new ResourceLocation("minecraft","overworld"),p->p.yRange(1,31).attempts(1).quantity(8).pattern(OrePattern.DEFAULT)
                    .placementChannel(new ResourceLocation("orespawn","standard")).hostBlock(new ResourceLocation("minecraft","stone"))));
        }
        if(!OreSpawnApi.enqueue(provider.build()))throw new IllegalStateException("Synthetic provider rejected");
    }
}
