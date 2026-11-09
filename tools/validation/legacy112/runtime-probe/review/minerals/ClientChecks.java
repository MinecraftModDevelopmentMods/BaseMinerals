package review.minerals;

import com.google.gson.*;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Future;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.item.ItemStack;
import net.minecraft.world.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.IModGuiFactory;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.GuiConfigEntries.BooleanEntry;
import net.minecraftforge.fml.client.config.IConfigElement;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import zone.moddev.mc.baseminerals.util.BMiConfig;
import zone.moddev.mc.orespawn.api.client.*;

/** Automated UI checks, separate from the distributable mod and manual acceptance. */
@Mod(modid="basemineralsclientprobe",name="Base Minerals client checks",version="1",acceptableRemoteVersions="*",dependencies="required-after:basemineralsprobe")
public final class ClientChecks {
    @Mod.EventHandler public void init(FMLInitializationEvent event){if(Boolean.getBoolean("minerals.client"))MinecraftForge.EVENT_BUS.register(new Checks());}
    static void require(boolean value,String detail){if(!value)throw new AssertionError(detail);}
    public static final class Checks {
        long started=System.currentTimeMillis();int state,ticks;boolean finished;GuiScreen parent;GuiConfig screen;Future<?> check;net.minecraft.server.MinecraftServer integrated;
        @SubscribeEvent public void tick(TickEvent.ClientTickEvent event) throws Exception {
            if(event.phase!=TickEvent.Phase.END||finished)return;Minecraft mc=Minecraft.getMinecraft();
            try {
                if(state==0&&mc.currentScreen instanceof GuiMainMenu&&++ticks>=20) {
                    parent=mc.currentScreen;
                    IModGuiFactory factory=FMLClientHandler.instance().getGuiFactoryFor(Loader.instance().getIndexedModList().get("baseminerals"));
                    require(factory!=null&&factory.hasConfigGui(),"Forge config factory missing");
                    screen=(GuiConfig)factory.createConfigGui(parent);mc.displayGuiScreen(screen);state=1;ticks=0;
                } else if(state==1&&++ticks>=20) {
                    verifyConfig(screen,parent);
                    screenshot(mc,"forge-mods-config.png");entry(screen).valueButtonPressed(0);
                    invoke(screen,"keyTyped",new Class<?>[]{char.class,int.class},'\0',1);
                    require(mc.currentScreen==parent,"Escape lost parent");require(BMiConfig.getConfiguration().get("Metals","EnableLithium",true).getBoolean(),"Escape saved a cancelled switch");
                    WorldSettingsExtension extension=WorldSettingsExtensionRegistry.extensions().stream().filter(e->"baseminerals".equals(e.id().getNamespace())).findFirst().orElse(null);
                    require(extension!=null,"OreSpawn config entry missing");screen=(GuiConfig)extension.createScreen(parent);mc.displayGuiScreen(screen);state=2;ticks=0;
                } else if(state==2&&++ticks>=20) {
                    verifyConfig(screen,parent);screenshot(mc,"orespawn-mods-config.png");
                    entry(screen).valueButtonPressed(0);invoke(screen,"actionPerformed",new Class<?>[]{GuiButton.class},new GuiButton(2000,0,0,"Done"));
                    require(!BMiConfig.getConfiguration().get("Metals","EnableLithium",true).getBoolean(),"Done did not save requested switch");
                    require(BMiConfig.isEnabled("lithium")&&com.mcmoddev.lib.init.Materials.hasMaterial("lithium"),"GUI changed active registries");
                    require(new String(Files.readAllBytes(Paths.get("config/BaseMinerals.cfg")),StandardCharsets.UTF_8).contains("B:EnableLithium=false"),"Switch not persisted");
                    screen=new zone.moddev.mc.baseminerals.client.BaseMineralsConfigScreen(parent);mc.displayGuiScreen(screen);entry(screen).valueButtonPressed(0);
                    invoke(screen,"actionPerformed",new Class<?>[]{GuiButton.class},new GuiButton(2000,0,0,"Done"));mc.displayGuiScreen(parent);
                    mc.launchIntegratedServer("minerals-check","Base Minerals disposable checks",new WorldSettings(8675309L,GameType.CREATIVE,false,false,WorldType.DEFAULT));state=3;
                } else if(state==3&&mc.world!=null&&mc.player!=null&&check==null) {
                    for(net.minecraft.block.Block block:net.minecraftforge.fml.common.registry.ForgeRegistries.BLOCKS.getValues()) {
                        if(!"baseminerals".equals(block.getRegistryName().getNamespace()))continue;
                        for(net.minecraft.block.state.IBlockState variant:block.getBlockState().getValidStates())
                            require(mc.getBlockRendererDispatcher().getBlockModelShapes().getModelForState(variant)!=mc.getRenderItem().getItemModelMesher().getModelManager().getMissingModel(),"Missing baked block: "+variant);
                    }
                    for(net.minecraft.item.Item item:net.minecraftforge.fml.common.registry.ForgeRegistries.ITEMS.getValues()) {
                        if(!"baseminerals".equals(item.getRegistryName().getNamespace()))continue;
                        require(mc.getRenderItem().getItemModelMesher().getItemModel(new ItemStack(item))!=mc.getRenderItem().getItemModelMesher().getModelManager().getMissingModel(),"Missing baked item: "+item.getRegistryName());
                    }
                    OreModelChecks.verify(mc);
                    check=mc.getIntegratedServer().addScheduledTask(()->{try{BaseMineralsProbe.run(mc.getIntegratedServer());}catch(Exception e){throw new RuntimeException(e);}});
                } else if(state==3&&check!=null&&check.isDone()) {
                    check.get();screenshot(mc,"integrated-world.png");integrated=mc.getIntegratedServer();integrated.initiateShutdown();state=4;
                } else if(state==4&&integrated.isServerStopped()) {
                    report(true,"Config save/cancel/restart, both parents, baked models and integrated saved stacks");finished=true;mc.shutdown();
                }
                if(System.currentTimeMillis()-started>240000)throw new AssertionError("Client timeout, state="+state);
            } catch(Throwable failure){report(false,failure.toString());failure.printStackTrace();finished=true;mc.shutdown();}
        }
        static void verifyConfig(GuiConfig config,GuiScreen expectedParent) {
            require(config.parentScreen==expectedParent,"Config opened with the wrong parent");
            require(config.allRequireMcRestart,"Switches must require restart");
            Set<String> names=new HashSet<>();
            for(IConfigElement element:config.configElements) {
                names.add(element.getName());
                require(net.minecraft.client.resources.I18n.hasKey(element.getLanguageKey()),"Untranslated switch: "+element.getName());
            }
            require(names.size()==11&&names.contains("Enabled Lithium")&&names.contains("Enabled Silicon"),
                    "Unexpected 1.12 switches: "+names);
        }
        static BooleanEntry entry(GuiConfig config){for(int i=0;i<config.configElements.size();i++)if("EnableLithium".equals(config.configElements.get(i).getName()))return (BooleanEntry)config.entryList.listEntries.get(i);throw new AssertionError("Missing lithium switch");}
        static void invoke(GuiConfig config,String name,Class<?>[] types,Object...args)throws Exception {
            Method method;try{method=GuiConfig.class.getDeclaredMethod(name,types);}catch(NoSuchMethodException e){method=GuiConfig.class.getDeclaredMethod("keyTyped".equals(name)?"func_73869_a":"func_146284_a",types);}
            method.setAccessible(true);method.invoke(config,args);
        }
        static void screenshot(Minecraft mc,String name){net.minecraft.util.ScreenShotHelper.saveScreenshot(mc.gameDir,name,mc.displayWidth,mc.displayHeight,mc.getFramebuffer());}
        static void report(boolean pass,String detail)throws Exception {
            JsonObject result=new JsonObject();result.addProperty("passed",pass);result.addProperty("detail",detail);
            Files.write(Paths.get(System.getProperty("minerals.phase")+"-client.json"),(new GsonBuilder().setPrettyPrinting().create().toJson(result)+"\n").getBytes(StandardCharsets.UTF_8));
            System.out.println("BASE_MINERALS_CLIENT_"+(pass?"PASS":"FAIL")+" "+detail);
        }
    }
}
