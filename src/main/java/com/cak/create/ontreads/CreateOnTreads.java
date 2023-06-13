package com.cak.create.ontreads;

import com.mojang.logging.LogUtils;
import com.simibubi.create.foundation.data.CreateRegistrate;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(CreateOnTreads.MODID)
public class CreateOnTreads
{
    public static final String MODID = "createontreads";
    public static final Logger LOGGER = LogUtils.getLogger();
    
     public static final CreativeModeTab BASE_CREATIVE_TAB = new CreativeModeTab("createontreads") {
        @Override
        public ItemStack makeIcon() {
            return OTBlocks.VEHICLE_CONTROLLER.asStack();
        }
    };
    
    public static CreateRegistrate REGISTRATE = CreateRegistrate.create(MODID);
    
    public CreateOnTreads() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(this::commonSetup);
        
        REGISTRATE.registerEventListeners(modEventBus);
        REGISTRATE.creativeModeTab(() -> BASE_CREATIVE_TAB);

        OTPartialModels.register();
        OTBlocks.register();
        OTBlockEntities.register();
        OTEntityTypes.register();
        OTContraptionType.register();
        
        MinecraftForge.EVENT_BUS.register(this);
    }
    
    public static ResourceLocation asResource(String id) {
        return new ResourceLocation(MODID, id);
    }
    
    private void commonSetup(final FMLCommonSetupEvent event) {
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
        }
    }
}
