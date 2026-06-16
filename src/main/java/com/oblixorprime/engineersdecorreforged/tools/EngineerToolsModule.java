package com.oblixorprime.engineersdecorreforged.tools;

import com.oblixorprime.engineersdecorreforged.ModItems;
import net.minecraft.world.food.FoodProperties.Builder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Item.Properties;
import net.neoforged.neoforge.registries.DeferredItem;

public final class EngineerToolsModule {
   public static final DeferredItem<Item> REDIA_TOOL = ModItems.registerItem(
      "redia_tool", () -> new RediaToolItem(new Properties().stacksTo(1).durability(1561).rarity(Rarity.RARE))
   );
   public static final DeferredItem<Item> CRUSHING_HAMMER = ModItems.registerItem(
      "crushing_hammer", () -> new CrushingHammerItem(new Properties().stacksTo(1).durability(640).rarity(Rarity.UNCOMMON))
   );
   public static final DeferredItem<Item> TRACKER = ModItems.registerItem(
      "tracker", () -> new TrackerItem(new Properties().stacksTo(1).rarity(Rarity.UNCOMMON))
   );
   public static final DeferredItem<Item> ARIADNE_COAL = ModItems.registerItem(
      "ariadne_coal", () -> new AriadneCoalItem(new Properties().stacksTo(16).rarity(Rarity.UNCOMMON))
   );
   public static final DeferredItem<Item> STIMPACK = ModItems.registerItem(
      "stimpack", () -> new AutoStimPackItem(new Properties().stacksTo(1).durability(16).rarity(Rarity.UNCOMMON))
   );
   public static final DeferredItem<Item> SLEEPING_BAG = ModItems.registerItem(
      "sleeping_bag", () -> new SleepingBagItem(new Properties().stacksTo(1).durability(48).rarity(Rarity.UNCOMMON))
   );
   public static final DeferredItem<Item> MATERIAL_BOX = ModItems.registerItem(
      "material_box", () -> new MaterialBoxItem(new Properties().stacksTo(1).rarity(Rarity.UNCOMMON))
   );
   public static final DeferredItem<Item> DIVING_CAPSULE = ModItems.registerItem(
      "diving_capsule", () -> new DivingCapsuleItem(new Properties().stacksTo(1).durability(24).rarity(Rarity.UNCOMMON))
   );
   public static final DeferredItem<Item> MUSLI_BAR_PRESS = ModItems.registerItem(
      "musli_bar_press", () -> new MusliBarPressItem(new Properties().stacksTo(1).durability(128).rarity(Rarity.UNCOMMON))
   );
   public static final DeferredItem<Item> MUSLI_BAR = ModItems.registerItem(
      "musli_bar", () -> new TooltipItem("musli_bar", new Properties().food(new Builder().nutrition(6).saturationModifier(0.65F).fast().build()))
   );
   public static final DeferredItem<Item> CHARGED_LAPIS_SQUEEZER = ModItems.registerItem(
      "charged_lapis_squeezer", () -> new ChargedLapisSqueezerItem(new Properties().stacksTo(1).durability(64).rarity(Rarity.UNCOMMON))
   );
   public static final DeferredItem<Item> CHARGED_LAPIS = ModItems.registerItem(
      "charged_lapis", () -> new ChargedLapisItem(new Properties().rarity(Rarity.UNCOMMON))
   );
   public static final DeferredItem<Item> IRON_GRIT = ModItems.registerItem("iron_grit", () -> new TooltipItem("iron_grit", new Properties()));
   public static final DeferredItem<Item> GOLD_GRIT = ModItems.registerItem("gold_grit", () -> new TooltipItem("gold_grit", new Properties()));

   private EngineerToolsModule() {
   }

   public static void init() {
   }
}
