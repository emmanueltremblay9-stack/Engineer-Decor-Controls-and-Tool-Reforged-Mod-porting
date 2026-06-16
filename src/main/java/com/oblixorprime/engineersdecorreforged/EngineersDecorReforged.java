package com.oblixorprime.engineersdecorreforged;

import com.mojang.logging.LogUtils;
import com.oblixorprime.engineersdecorreforged.network.ModNetworking;
import com.oblixorprime.engineersdecorreforged.rsgauges.ControlsModule;
import com.oblixorprime.engineersdecorreforged.tools.EngineerToolsModule;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig.Type;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.capabilities.Capabilities.EnergyStorage;
import net.neoforged.neoforge.capabilities.Capabilities.FluidHandler;
import net.neoforged.neoforge.capabilities.Capabilities.ItemHandler;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.slf4j.Logger;

@Mod("engineers_decor_reforged")
public final class EngineersDecorReforged {
   public static final String MOD_ID = "engineers_decor_reforged";
   public static final Logger LOGGER = LogUtils.getLogger();
   private static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, "engineers_decor_reforged");
   public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MAIN_TAB = CREATIVE_TABS.register(
      "main",
      () -> CreativeModeTab.builder()
         .title(Component.translatable("itemGroup.engineers_decor_reforged"))
         .withTabsBefore(new ResourceKey[]{CreativeModeTabs.BUILDING_BLOCKS})
         .icon(() -> new ItemStack((ItemLike)ModBlocks.CLINKER_BRICK_BLOCK.get()))
         .displayItems((parameters, output) -> ModItems.ORDERED_ITEMS.forEach(item -> output.accept((ItemLike)item.get())))
         .build()
   );

   public EngineersDecorReforged(IEventBus modEventBus, ModContainer modContainer) {
      NeoForgeMod.enableMilkFluid();
      ControlsModule.init();
      EngineerToolsModule.init();
      ModBlocks.BLOCKS.register(modEventBus);
      ModItems.ITEMS.register(modEventBus);
      ModBlockEntities.BLOCK_ENTITY_TYPES.register(modEventBus);
      ModMenus.MENU_TYPES.register(modEventBus);
      CREATIVE_TABS.register(modEventBus);
      modEventBus.addListener(this::commonSetup);
      modEventBus.addListener(this::registerCapabilities);
      modEventBus.addListener(ModNetworking::register);
      modContainer.registerConfig(Type.COMMON, ReforgedConfig.SPEC);
   }

   private void commonSetup(FMLCommonSetupEvent event) {
      LOGGER.info(
         "Loaded Engineer's Decor & Controls Reforged with {} enabled blocks and {} enabled items.",
         ModBlocks.ORDERED_BLOCKS.size(),
         ModItems.ORDERED_ITEMS.size()
      );
   }

   private void registerCapabilities(RegisterCapabilitiesEvent event) {
      event.registerBlockEntity(ItemHandler.BLOCK, ModBlockEntities.MACHINE.get(), (machine, side) -> machine.itemHandler(side));
      event.registerBlockEntity(FluidHandler.BLOCK, ModBlockEntities.MACHINE.get(), (machine, side) -> machine.fluidHandler(side));
      event.registerBlockEntity(EnergyStorage.BLOCK, ModBlockEntities.MACHINE.get(), (machine, side) -> machine.energyStorage(side));
   }
}
