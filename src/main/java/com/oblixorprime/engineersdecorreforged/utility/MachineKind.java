package com.oblixorprime.engineersdecorreforged.utility;

public enum MachineKind {
   METAL_CRAFTING_TABLE("metal_crafting_table"),
   LABELED_CRATE("labeled_crate"),
   FACTORY_HOPPER("factory_hopper"),
   FACTORY_DROPPER("factory_dropper"),
   FACTORY_PLACER("factory_placer"),
   SMALL_BLOCK_BREAKER("small_block_breaker"),
   SMALL_WASTE_INCINERATOR("small_waste_incinerator"),
   SMALL_LAB_FURNACE("small_lab_furnace"),
   SMALL_ELECTRICAL_FURNACE("small_electrical_furnace"),
   SMALL_MINERAL_SMELTER("small_mineral_smelter"),
   SMALL_FREEZER("small_freezer"),
   FLUID_BARREL("fluid_barrel"),
   SMALL_FLUID_FUNNEL("small_fluid_funnel"),
   PASSIVE_FLUID_ACCUMULATOR("passive_fluid_accumulator"),
   SMALL_SOLAR_PANEL("small_solar_panel"),
   SMALL_MILKING_MACHINE("small_milking_machine"),
   SMALL_TREE_CUTTER("small_tree_cutter");

   private final String registryName;

   MachineKind(String registryName) {
      this.registryName = registryName;
   }

   public String registryName() {
      return this.registryName;
   }

   public boolean isFluidContainer() {
      return this == FLUID_BARREL
         || this == SMALL_FLUID_FUNNEL
         || this == PASSIVE_FLUID_ACCUMULATOR
         || this == SMALL_MILKING_MACHINE
         || this == SMALL_MINERAL_SMELTER;
   }
}
