package com.oblixorprime.engineersdecorreforged;

import com.oblixorprime.engineersdecorreforged.utility.MachineBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.BlockEntityType.Builder;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModBlockEntities {
   public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(
      Registries.BLOCK_ENTITY_TYPE, "engineers_decor_reforged"
   );
   public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<MachineBlockEntity>> MACHINE = BLOCK_ENTITY_TYPES.register(
      "machine",
      () -> Builder.of(
            MachineBlockEntity::new,
            new Block[]{
               (Block)ModBlocks.METAL_CRAFTING_TABLE.get(),
               (Block)ModBlocks.LABELED_CRATE.get(),
               (Block)ModBlocks.FACTORY_HOPPER.get(),
               (Block)ModBlocks.FACTORY_DROPPER.get(),
               (Block)ModBlocks.FACTORY_PLACER.get(),
               (Block)ModBlocks.SMALL_BLOCK_BREAKER.get(),
               (Block)ModBlocks.SMALL_WASTE_INCINERATOR.get(),
               (Block)ModBlocks.SMALL_LAB_FURNACE.get(),
               (Block)ModBlocks.SMALL_ELECTRICAL_FURNACE.get(),
               (Block)ModBlocks.SMALL_MINERAL_SMELTER.get(),
               (Block)ModBlocks.SMALL_FREEZER.get(),
               (Block)ModBlocks.FLUID_BARREL.get(),
               (Block)ModBlocks.SMALL_FLUID_FUNNEL.get(),
               (Block)ModBlocks.PASSIVE_FLUID_ACCUMULATOR.get(),
               (Block)ModBlocks.SMALL_SOLAR_PANEL.get(),
               (Block)ModBlocks.SMALL_MILKING_MACHINE.get(),
               (Block)ModBlocks.SMALL_TREE_CUTTER.get()
            }
         )
         .build(null)
   );

   private ModBlockEntities() {
   }
}
