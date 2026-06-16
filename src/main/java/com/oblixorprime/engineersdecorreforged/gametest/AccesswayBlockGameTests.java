package com.oblixorprime.engineersdecorreforged.gametest;

import com.oblixorprime.engineersdecorreforged.ModBlocks;
import com.oblixorprime.engineersdecorreforged.block.PortedBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoorHingeSide;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

@GameTestHolder("engineers_decor_reforged")
@PrefixGameTestTemplate(false)
public final class AccesswayBlockGameTests {
   private static final String TEMPLATE = "empty";
   private static final BlockPos LOWER_POS = new BlockPos(1, 1, 1);
   private static final BlockPos UPPER_POS = LOWER_POS.above();

   private AccesswayBlockGameTests() {
   }

   @GameTest(template = "empty", timeoutTicks = 80)
   public static void stacked_steel_mesh_fence_gate_segments_open_together(GameTestHelper helper) {
      Player player = helper.makeMockPlayer(GameType.CREATIVE);
      Block gate = (Block)ModBlocks.STEEL_MESH_FENCE_GATE.get();
      BlockState lower = (BlockState)((BlockState)((BlockState)gate.defaultBlockState().setValue(PortedBlocks.HORIZONTAL_FACING, Direction.NORTH))
            .setValue(PortedBlocks.OPEN, false))
         .setValue(PortedBlocks.SEGMENT, 0);
      BlockState upper = (BlockState)((BlockState)((BlockState)gate.defaultBlockState().setValue(PortedBlocks.HORIZONTAL_FACING, Direction.NORTH))
            .setValue(PortedBlocks.OPEN, false))
         .setValue(PortedBlocks.SEGMENT, 1);
      helper.setBlock(LOWER_POS, lower);
      helper.setBlock(UPPER_POS, upper);
      helper.useBlock(LOWER_POS, player);
      helper.assertBlockProperty(LOWER_POS, PortedBlocks.OPEN, true);
      helper.assertBlockProperty(UPPER_POS, PortedBlocks.OPEN, true);
      helper.useBlock(UPPER_POS, player);
      helper.assertBlockProperty(LOWER_POS, PortedBlocks.OPEN, false);
      helper.assertBlockProperty(UPPER_POS, PortedBlocks.OPEN, false);
      helper.succeed();
   }

   @GameTest(template = "empty", timeoutTicks = 40)
   public static void stacked_steel_mesh_fence_gate_segments_remove_together(GameTestHelper helper) {
      Block gate = (Block)ModBlocks.STEEL_MESH_FENCE_GATE.get();
      BlockState lower = (BlockState)((BlockState)((BlockState)gate.defaultBlockState().setValue(PortedBlocks.HORIZONTAL_FACING, Direction.NORTH))
            .setValue(PortedBlocks.OPEN, false))
         .setValue(PortedBlocks.SEGMENT, 0);
      BlockState upper = (BlockState)((BlockState)((BlockState)gate.defaultBlockState().setValue(PortedBlocks.HORIZONTAL_FACING, Direction.NORTH))
            .setValue(PortedBlocks.OPEN, false))
         .setValue(PortedBlocks.SEGMENT, 1);
      helper.setBlock(LOWER_POS, lower);
      helper.setBlock(UPPER_POS, upper);
      helper.setBlock(LOWER_POS, Blocks.AIR.defaultBlockState());
      helper.assertTrue(
         helper.getLevel().getBlockState(helper.absolutePos(UPPER_POS)).isAir(), "removing lower steel mesh fence gate segment should remove upper segment"
      );
      helper.setBlock(LOWER_POS, lower);
      helper.setBlock(UPPER_POS, upper);
      helper.setBlock(UPPER_POS, Blocks.AIR.defaultBlockState());
      helper.assertTrue(
         helper.getLevel().getBlockState(helper.absolutePos(LOWER_POS)).isAir(), "removing upper steel mesh fence gate segment should remove lower segment"
      );
      helper.succeed();
   }

   @GameTest(template = "empty", timeoutTicks = 80)
   public static void steel_double_t_support_updates_beam_and_pole_connectors(GameTestHelper helper) {
      Block support = (Block)ModBlocks.STEEL_DOUBLE_T_SUPPORT.get();
      BlockPos center = new BlockPos(2, 2, 2);
      helper.setBlock(center, (BlockState)support.defaultBlockState().setValue(PortedBlocks.EASTWEST, true));
      helper.setBlock(center.north(), support.defaultBlockState());
      helper.setBlock(center.south(), support.defaultBlockState());
      helper.setBlock(center.below(), ((PortedBlocks.CenteredPoleBlock)ModBlocks.THIN_STEEL_POLE.get()).defaultBlockState());
      helper.assertBlockProperty(center, PortedBlocks.LEFTBEAM, true);
      helper.assertBlockProperty(center, PortedBlocks.RIGHTBEAM, true);
      helper.assertBlockProperty(center, PortedBlocks.DOWNCONNECT, 1);
      helper.succeed();
   }

   @GameTest(template = "empty", timeoutTicks = 40)
   public static void slab_slices_select_vertical_part_from_click_height(GameTestHelper helper) {
      Player player = helper.makeMockPlayer(GameType.CREATIVE);
      Block slice = (Block)ModBlocks.OLD_INDUSTRIAL_WOOD_SLABSLICE.get();
      assertSlicePlacementPart(helper, player, slice, 0.05, 0, "low click should place the bottom slab slice");
      assertSlicePlacementPart(helper, player, slice, 0.52, 7, "middle click should place a middle slab slice");
      assertSlicePlacementPart(helper, player, slice, 0.98, 14, "high click should place the top slab slice");
      helper.succeed();
   }

   @GameTest(template = "empty", timeoutTicks = 40)
   public static void raised_catwalk_placement_selects_reachable_model_variant(GameTestHelper helper) {
      Player player = helper.makeMockPlayer(GameType.CREATIVE);
      Block catwalk = (Block)ModBlocks.STEEL_CATWALK_TA.get();
      BlockPos pos = new BlockPos(3, 2, 4);
      BlockState state = placementState(helper, player, catwalk, pos, 0.5);
      helper.assertTrue(state != null, "raised catwalk placement state should not be null");
      helper.assertValueEqual(
         (Integer)state.getValue(PortedBlocks.VARIANT),
         variantFor(helper.absolutePos(pos)),
         "raised catwalk placement should select its position-based visual variant"
      );
      helper.succeed();
   }

   @GameTest(template = "empty", timeoutTicks = 40)
   public static void iron_hatch_placement_reads_existing_redstone_power(GameTestHelper helper) {
      Player player = helper.makeMockPlayer(GameType.CREATIVE);
      Block hatch = (Block)ModBlocks.IRON_HATCH.get();
      BlockPos placePos = helper.absolutePos(LOWER_POS);
      BlockHitResult hit = new BlockHitResult(new Vec3(placePos.getX() + 0.5, placePos.getY() + 0.5, placePos.getZ() + 0.5), Direction.UP, placePos, false);
      BlockPlaceContext context = new BlockPlaceContext(helper.getLevel(), player, InteractionHand.MAIN_HAND, new ItemStack(hatch), hit);
      helper.getLevel().setBlock(context.getClickedPos().east(), Blocks.REDSTONE_BLOCK.defaultBlockState(), 3);
      helper.assertTrue(helper.getLevel().hasNeighborSignal(context.getClickedPos()), "iron hatch test fixture should place the hatch beside active redstone");
      BlockState state = hatch.getStateForPlacement(context);
      helper.assertTrue(state != null, "iron hatch placement state should not be null");
      helper.assertTrue((Boolean)state.getValue(PortedBlocks.POWERED), "iron hatch should initialize powered beside active redstone");
      helper.assertTrue((Boolean)state.getValue(PortedBlocks.OPEN), "iron hatch should initialize open beside active redstone");
      helper.succeed();
   }

   @GameTest(template = "empty", timeoutTicks = 40)
   public static void powered_iron_hatch_stays_open_when_used(GameTestHelper helper) {
      Player player = helper.makeMockPlayer(GameType.CREATIVE);
      Block hatch = (Block)ModBlocks.IRON_HATCH.get();
      helper.setBlock(
         LOWER_POS,
         (BlockState)((BlockState)((BlockState)hatch.defaultBlockState().setValue(PortedBlocks.HORIZONTAL_FACING, Direction.NORTH))
               .setValue(PortedBlocks.POWERED, true))
            .setValue(PortedBlocks.OPEN, true)
      );
      helper.useBlock(LOWER_POS, player);
      helper.assertBlockProperty(LOWER_POS, PortedBlocks.POWERED, true);
      helper.assertBlockProperty(LOWER_POS, PortedBlocks.OPEN, true);
      helper.succeed();
   }

   @GameTest(template = "empty", timeoutTicks = 40)
   public static void surface_mounted_blocks_drop_when_support_is_removed(GameTestHelper helper) {
      BlockPos supportPos = new BlockPos(2, 2, 2);
      BlockPos lightPos = supportPos.north();
      Block light = (Block)ModBlocks.IRON_BULB_LIGHT.get();
      helper.setBlock(supportPos, Blocks.STONE);
      helper.setBlock(lightPos, (BlockState)light.defaultBlockState().setValue(PortedBlocks.FACING, Direction.NORTH));
      helper.assertTrue(helper.getBlockState(lightPos).is(light), "surface-mounted fixture should start attached to its support");
      helper.setBlock(supportPos, Blocks.AIR);
      helper.runAfterDelay(1L, () -> {
         helper.assertTrue(helper.getBlockState(lightPos).isAir(), "surface-mounted fixture should drop when its supporting face is removed");
         helper.succeed();
      });
   }

   @GameTest(template = "empty", timeoutTicks = 80)
   public static void custom_doors_open_from_redstone_power(GameTestHelper helper) {
      BlockPos oldWoodDoorPos = new BlockPos(1, 1, 1);
      BlockPos metalDoorPos = new BlockPos(4, 1, 1);
      placeClosedDoor(helper, (Block)ModBlocks.OLD_INDUSTRIAL_WOOD_DOOR.get(), oldWoodDoorPos);
      placeClosedDoor(helper, (Block)ModBlocks.METAL_SLIDING_DOOR.get(), metalDoorPos);
      helper.setBlock(oldWoodDoorPos.east(), Blocks.REDSTONE_BLOCK);
      helper.setBlock(metalDoorPos.east(), Blocks.REDSTONE_BLOCK);
      helper.runAfterDelay(2L, () -> {
         assertDoorPoweredOpen(helper, oldWoodDoorPos, "old industrial wood door");
         assertDoorPoweredOpen(helper, metalDoorPos, "metal sliding door");
         helper.succeed();
      });
   }

   private static void assertSlicePlacementPart(GameTestHelper helper, Player player, Block block, double localY, int expectedPart, String message) {
      BlockState state = placementState(helper, player, block, LOWER_POS, localY);
      helper.assertTrue(state != null, "slab slice placement state should not be null");
      helper.assertValueEqual((Integer)state.getValue(PortedBlocks.PARTS), expectedPart, message);
   }

   private static BlockState placementState(GameTestHelper helper, Player player, Block block, BlockPos pos, double localY) {
      return placementState(helper, player, block, pos, localY, Direction.UP);
   }

   private static BlockState placementState(GameTestHelper helper, Player player, Block block, BlockPos pos, double localY, Direction face) {
      BlockPos absolutePos = helper.absolutePos(pos);
      Vec3 hitLocation = new Vec3(absolutePos.getX() + 0.5, absolutePos.getY() + localY, absolutePos.getZ() + 0.5);
      BlockHitResult hit = new BlockHitResult(hitLocation, face, absolutePos, false);
      BlockPlaceContext context = new BlockPlaceContext(helper.getLevel(), player, InteractionHand.MAIN_HAND, new ItemStack(block), hit);
      return block.getStateForPlacement(context);
   }

   private static int variantFor(BlockPos pos) {
      return Math.floorMod(pos.getX() * 31 + pos.getY() * 7 + pos.getZ(), 5);
   }

   private static void placeClosedDoor(GameTestHelper helper, Block door, BlockPos lowerPos) {
      BlockState base = (BlockState)((BlockState)((BlockState)((BlockState)door.defaultBlockState().setValue(DoorBlock.FACING, Direction.NORTH))
               .setValue(DoorBlock.HINGE, DoorHingeSide.LEFT))
            .setValue(DoorBlock.OPEN, false))
         .setValue(DoorBlock.POWERED, false);
      helper.setBlock(lowerPos, (BlockState)base.setValue(DoorBlock.HALF, DoubleBlockHalf.LOWER));
      helper.setBlock(lowerPos.above(), (BlockState)base.setValue(DoorBlock.HALF, DoubleBlockHalf.UPPER));
   }

   private static void assertDoorPoweredOpen(GameTestHelper helper, BlockPos lowerPos, String name) {
      helper.assertBlockProperty(lowerPos, DoorBlock.POWERED, true);
      helper.assertBlockProperty(lowerPos, DoorBlock.OPEN, true);
      helper.assertBlockProperty(lowerPos.above(), DoorBlock.POWERED, true);
      helper.assertBlockProperty(lowerPos.above(), DoorBlock.OPEN, true);
   }
}
