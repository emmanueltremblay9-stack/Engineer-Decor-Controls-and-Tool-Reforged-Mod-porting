package com.oblixorprime.engineersdecorreforged;

import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.ModConfigSpec.BooleanValue;
import net.neoforged.neoforge.common.ModConfigSpec.Builder;

public final class ReforgedConfig {
   public static final ModConfigSpec SPEC;
   public static final BooleanValue ENABLE_DECOR_BLOCKS;
   public static final BooleanValue ENABLE_UTILITY_BLOCKS;
   public static final BooleanValue ENABLE_REDSTONE_CONTROLS;
   public static final BooleanValue ENABLE_GAUGES;
   public static final BooleanValue ENABLE_INDICATORS;
   public static final BooleanValue ENABLE_SENSORS;
   public static final BooleanValue ENABLE_WIRELESS_CONTROLS;
   public static final BooleanValue ENABLE_SOUND_INDICATORS;
   public static final BooleanValue ENABLE_STYLE_INDUSTRIAL;
   public static final BooleanValue ENABLE_STYLE_RETRO_INDUSTRIAL;
   public static final BooleanValue ENABLE_STYLE_RUSTIC;
   public static final BooleanValue ENABLE_STYLE_OLD_FANCY;
   public static final BooleanValue ENABLE_STYLE_GLASS;

   private ReforgedConfig() {
   }

   static {
      Builder builder = new Builder();
      builder.push("content");
      ENABLE_DECOR_BLOCKS = builder.comment("Enables Engineer's Decor decorative registrations. Requires restart after changing.")
         .define("enableDecorBlocks", true);
      ENABLE_UTILITY_BLOCKS = builder.comment("Enables utility-machine registrations where implemented. Requires restart after changing.")
         .define("enableUtilityBlocks", true);
      ENABLE_REDSTONE_CONTROLS = builder.comment("Enables RsGauges switch/control registrations. Requires restart after changing.")
         .define("enableRedstoneControls", true);
      ENABLE_GAUGES = builder.comment("Enables redstone gauge registrations. Requires restart after changing.").define("enableGauges", true);
      ENABLE_INDICATORS = builder.comment("Enables indicator and alarm registrations. Requires restart after changing.").define("enableIndicators", true);
      ENABLE_SENSORS = builder.comment("Enables sensor registrations. Requires restart after changing.").define("enableSensors", true);
      ENABLE_WIRELESS_CONTROLS = builder.comment("Enables wireless-control item/block registrations. Requires restart after changing.")
         .define("enableWirelessControls", true);
      ENABLE_SOUND_INDICATORS = builder.comment("Enables sound-indicator registrations. Requires restart after changing.")
         .define("enableSoundIndicators", true);
      builder.pop();
      builder.push("styles");
      ENABLE_STYLE_INDUSTRIAL = builder.comment("Enables industrial RsGauges style registrations. Requires restart after changing.")
         .define("enableStyleIndustrial", true);
      ENABLE_STYLE_RETRO_INDUSTRIAL = builder.comment("Enables retro industrial RsGauges style registrations. Requires restart after changing.")
         .define("enableStyleRetroIndustrial", true);
      ENABLE_STYLE_RUSTIC = builder.comment("Enables rustic RsGauges style registrations. Requires restart after changing.").define("enableStyleRustic", true);
      ENABLE_STYLE_OLD_FANCY = builder.comment("Enables old fancy RsGauges style registrations. Requires restart after changing.")
         .define("enableStyleOldFancy", true);
      ENABLE_STYLE_GLASS = builder.comment("Enables glass RsGauges style registrations. Requires restart after changing.").define("enableStyleGlass", true);
      builder.pop();
      SPEC = builder.build();
   }
}
