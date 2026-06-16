package com.oblixorprime.engineersdecorreforged.block;

import com.mojang.serialization.MapCodec;
import com.oblixorprime.engineersdecorreforged.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.util.Mth;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.DoorHingeSide;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public final class PortedBlocks {
   public static final DirectionProperty FACING = BlockStateProperties.FACING;
   public static final DirectionProperty HORIZONTAL_FACING = BlockStateProperties.HORIZONTAL_FACING;
   public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
   public static final BooleanProperty OPEN = BlockStateProperties.OPEN;
   public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
   public static final BooleanProperty NORTH = BooleanProperty.create("north");
   public static final BooleanProperty EAST = BooleanProperty.create("east");
   public static final BooleanProperty SOUTH = BooleanProperty.create("south");
   public static final BooleanProperty WEST = BooleanProperty.create("west");
   public static final BooleanProperty LEFT_RAILING = BooleanProperty.create("left_railing");
   public static final BooleanProperty RIGHT_RAILING = BooleanProperty.create("right_railing");
   public static final BooleanProperty EASTWEST = BooleanProperty.create("eastwest");
   public static final BooleanProperty LEFTBEAM = BooleanProperty.create("leftbeam");
   public static final BooleanProperty RIGHTBEAM = BooleanProperty.create("rightbeam");
   public static final IntegerProperty DOWNCONNECT = IntegerProperty.create("downconnect", 0, 2);
   public static final IntegerProperty TVARIANT = IntegerProperty.create("tvariant", 0, 3);
   public static final IntegerProperty PARTS = IntegerProperty.create("parts", 0, 14);
   public static final IntegerProperty VARIANT = IntegerProperty.create("variant", 0, 4);
   public static final IntegerProperty SEGMENT = IntegerProperty.create("segment", 0, 1);
   public static final IntegerProperty ARIADNE_MARKER_ROTATION = IntegerProperty.create("rotation", 0, 7);
   public static final EnumProperty<PortedBlocks.SimpleHalf> HALF = EnumProperty.create("half", PortedBlocks.SimpleHalf.class);
   public static final EnumProperty<DoorHingeSide> HINGE = BlockStateProperties.DOOR_HINGE;
   private static final VoxelShape FLOOR_GRATING_SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0);
   private static final VoxelShape CATWALK_FLOOR_SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 3.0, 16.0);
   private static final VoxelShape RAISED_CATWALK_SHAPE = Block.box(0.0, 14.0, 0.0, 16.0, 16.0, 16.0);
   private static final VoxelShape NORTH_RAIL_SHAPE = Block.box(0.0, 3.0, 0.0, 16.0, 16.0, 2.0);
   private static final VoxelShape EAST_RAIL_SHAPE = Block.box(14.0, 3.0, 0.0, 16.0, 16.0, 16.0);
   private static final VoxelShape SOUTH_RAIL_SHAPE = Block.box(0.0, 3.0, 14.0, 16.0, 16.0, 16.0);
   private static final VoxelShape WEST_RAIL_SHAPE = Block.box(0.0, 3.0, 0.0, 2.0, 16.0, 16.0);
   private static final VoxelShape TABLE_SHAPE = Shapes.or(
      Block.box(0.0, 14.0, 0.0, 16.0, 16.0, 16.0),
      new VoxelShape[]{
         Block.box(1.0, 0.0, 1.0, 4.0, 14.0, 4.0),
         Block.box(12.0, 0.0, 1.0, 15.0, 14.0, 4.0),
         Block.box(1.0, 0.0, 12.0, 4.0, 14.0, 15.0),
         Block.box(12.0, 0.0, 12.0, 15.0, 14.0, 15.0)
      }
   );
   private static final VoxelShape STOOL_SHAPE = Shapes.or(
      Block.box(4.0, 7.0, 4.0, 12.0, 10.0, 12.0),
      new VoxelShape[]{Block.box(7.0, 0.0, 7.0, 9.0, 7.0, 9.0), Block.box(4.0, 0.0, 7.0, 12.0, 1.0, 9.0), Block.box(7.0, 0.0, 4.0, 9.0, 1.0, 12.0)}
   );

   private PortedBlocks() {
   }

   private static VoxelShape centeredAxisShape(Direction facing, double min, double max) {
      return switch (facing.getAxis()) {
         case X -> Block.box(0.0, min, min, 16.0, max, max);
         case Y -> Block.box(min, 0.0, min, max, 16.0, max);
         case Z -> Block.box(min, min, 0.0, max, max, 16.0);
         default -> throw new MatchException(null, null);
      };
   }

   private static VoxelShape facePlateShape(Direction facing, double min, double max, double thickness) {
      return switch (facing) {
         case NORTH -> Block.box(min, min, 16.0 - thickness, max, max, 16.0);
         case SOUTH -> Block.box(min, min, 0.0, max, max, thickness);
         case WEST -> Block.box(16.0 - thickness, min, min, 16.0, max, max);
         case EAST -> Block.box(0.0, min, min, thickness, max, max);
         case UP -> Block.box(min, 0.0, min, max, thickness, max);
         case DOWN -> Block.box(min, 16.0 - thickness, min, max, 16.0, max);
         default -> throw new MatchException(null, null);
      };
   }

   private static VoxelShape horizontalPlateShape(Direction facing, double thickness) {
      return switch (facing) {
         case NORTH -> Block.box(0.0, 0.0, 0.0, 16.0, 16.0, thickness);
         case SOUTH -> Block.box(0.0, 0.0, 16.0 - thickness, 16.0, 16.0, 16.0);
         case WEST -> Block.box(0.0, 0.0, 0.0, thickness, 16.0, 16.0);
         case EAST -> Block.box(16.0 - thickness, 0.0, 0.0, 16.0, 16.0, 16.0);
         default -> Block.box(0.0, 0.0, 0.0, 16.0, thickness, 16.0);
      };
   }

   private static VoxelShape horizontalHalfShape(Direction facing) {
      return switch (facing) {
         case NORTH -> Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 8.0);
         case SOUTH -> Block.box(0.0, 0.0, 8.0, 16.0, 16.0, 16.0);
         case WEST -> Block.box(0.0, 0.0, 0.0, 8.0, 16.0, 16.0);
         case EAST -> Block.box(8.0, 0.0, 0.0, 16.0, 16.0, 16.0);
         default -> Shapes.block();
      };
   }

   private static VoxelShape catwalkShape(BlockState state, boolean includeFloor) {
      VoxelShape shape = includeFloor ? CATWALK_FLOOR_SHAPE : Shapes.empty();
      if ((Boolean)state.getValue(NORTH)) {
         shape = Shapes.or(shape, NORTH_RAIL_SHAPE);
      }

      if ((Boolean)state.getValue(EAST)) {
         shape = Shapes.or(shape, EAST_RAIL_SHAPE);
      }

      if ((Boolean)state.getValue(SOUTH)) {
         shape = Shapes.or(shape, SOUTH_RAIL_SHAPE);
      }

      if ((Boolean)state.getValue(WEST)) {
         shape = Shapes.or(shape, WEST_RAIL_SHAPE);
      }

      return shape;
   }

   private static VoxelShape railingShape(BlockState state) {
      VoxelShape shape = Shapes.empty();
      boolean any = (Boolean)state.getValue(NORTH) || (Boolean)state.getValue(EAST) || (Boolean)state.getValue(SOUTH) || (Boolean)state.getValue(WEST);
      if (!any || (Boolean)state.getValue(NORTH)) {
         shape = Shapes.or(shape, NORTH_RAIL_SHAPE);
      }

      if ((Boolean)state.getValue(EAST)) {
         shape = Shapes.or(shape, EAST_RAIL_SHAPE);
      }

      if ((Boolean)state.getValue(SOUTH)) {
         shape = Shapes.or(shape, SOUTH_RAIL_SHAPE);
      }

      if ((Boolean)state.getValue(WEST)) {
         shape = Shapes.or(shape, WEST_RAIL_SHAPE);
      }

      return shape;
   }

   private static VoxelShape catwalkStairShape(Direction facing) {
      return switch (facing) {
         case NORTH -> Shapes.or(Block.box(0.0, 0.0, 8.0, 16.0, 5.0, 16.0), Block.box(0.0, 5.0, 0.0, 16.0, 10.0, 8.0));
         case SOUTH -> Shapes.or(Block.box(0.0, 0.0, 0.0, 16.0, 5.0, 8.0), Block.box(0.0, 5.0, 8.0, 16.0, 10.0, 16.0));
         case WEST -> Shapes.or(Block.box(8.0, 0.0, 0.0, 16.0, 5.0, 16.0), Block.box(0.0, 5.0, 0.0, 8.0, 10.0, 16.0));
         case EAST -> Shapes.or(Block.box(0.0, 0.0, 0.0, 8.0, 5.0, 16.0), Block.box(8.0, 5.0, 0.0, 16.0, 10.0, 16.0));
         default -> CATWALK_FLOOR_SHAPE;
      };
   }

   private static VoxelShape sideRailShape(Direction facing, boolean left) {
      Direction side = switch (facing) {
         case NORTH -> left ? Direction.WEST : Direction.EAST;
         case SOUTH -> left ? Direction.EAST : Direction.WEST;
         case WEST -> left ? Direction.SOUTH : Direction.NORTH;
         case EAST -> left ? Direction.NORTH : Direction.SOUTH;
         default -> left ? Direction.WEST : Direction.EAST;
      };

      return switch (side) {
         case NORTH -> NORTH_RAIL_SHAPE;
         case SOUTH -> SOUTH_RAIL_SHAPE;
         case WEST -> WEST_RAIL_SHAPE;
         case EAST -> EAST_RAIL_SHAPE;
         default -> Shapes.empty();
      };
   }

   public static class AriadneMarkerBlock extends PortedBlocks.DirectionalPortBlock {
      public static final MapCodec<PortedBlocks.AriadneMarkerBlock> CODEC = simpleCodec(PortedBlocks.AriadneMarkerBlock::new);

      public AriadneMarkerBlock(Properties properties) {
         super(properties);
         this.registerDefaultState((BlockState)this.defaultBlockState().setValue(PortedBlocks.ARIADNE_MARKER_ROTATION, 0));
      }

      @Override
      protected MapCodec<? extends DirectionalBlock> codec() {
         return CODEC;
      }

      @Override
      protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
         builder.add(new Property[]{FACING, PortedBlocks.ARIADNE_MARKER_ROTATION});
      }

      protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
         Direction facing = (Direction)state.getValue(FACING);
         BlockPos supportPos = pos.relative(facing.getOpposite());
         return level.getBlockState(supportPos).isFaceSturdy(level, supportPos, facing);
      }

      protected BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
         return direction == ((Direction)state.getValue(FACING)).getOpposite() && !this.canSurvive(state, level, pos) ? Blocks.AIR.defaultBlockState() : state;
      }

      protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
         return PortedBlocks.facePlateShape((Direction)state.getValue(FACING), 0.0, 16.0, 0.1);
      }

      protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
         return Shapes.empty();
      }
   }

   public static class CatwalkBlock extends Block implements SimpleWaterloggedBlock {
      public CatwalkBlock(Properties properties) {
         super(properties);
         this.registerDefaultState(
            (BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(PortedBlocks.NORTH, false))
                        .setValue(PortedBlocks.EAST, false))
                     .setValue(PortedBlocks.SOUTH, false))
                  .setValue(PortedBlocks.WEST, false))
               .setValue(PortedBlocks.WATERLOGGED, false)
         );
      }

      protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
         builder.add(new Property[]{PortedBlocks.NORTH, PortedBlocks.EAST, PortedBlocks.SOUTH, PortedBlocks.WEST, PortedBlocks.WATERLOGGED});
      }

      public BlockState getStateForPlacement(BlockPlaceContext context) {
         return this.connectedState(
            (BlockState)this.defaultBlockState()
               .setValue(PortedBlocks.WATERLOGGED, context.getLevel().getFluidState(context.getClickedPos()).getType() == Fluids.WATER),
            context.getLevel(),
            context.getClickedPos()
         );
      }

      public FluidState getFluidState(BlockState state) {
         return state.getValue(PortedBlocks.WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
      }

      protected BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
         if ((Boolean)state.getValue(PortedBlocks.WATERLOGGED)) {
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
         }

         return direction.getAxis().isHorizontal() ? (BlockState)state.setValue(this.connectionProperty(direction), this.connectsTo(neighborState)) : state;
      }

      protected BlockState connectedState(BlockState state, BlockGetter level, BlockPos pos) {
         return (BlockState)((BlockState)((BlockState)((BlockState)state.setValue(PortedBlocks.NORTH, this.connectsTo(level.getBlockState(pos.north()))))
                  .setValue(PortedBlocks.EAST, this.connectsTo(level.getBlockState(pos.east()))))
               .setValue(PortedBlocks.SOUTH, this.connectsTo(level.getBlockState(pos.south()))))
            .setValue(PortedBlocks.WEST, this.connectsTo(level.getBlockState(pos.west())));
      }

      protected boolean connectsTo(BlockState neighborState) {
         Block block = neighborState.getBlock();
         return block instanceof PortedBlocks.CatwalkBlock
            || block instanceof PortedBlocks.CatwalkStairsBlock
            || block instanceof PortedBlocks.FenceGateSegmentBlock;
      }

      private BooleanProperty connectionProperty(Direction direction) {
         return switch (direction) {
            case NORTH -> PortedBlocks.NORTH;
            case SOUTH -> PortedBlocks.SOUTH;
            case WEST -> PortedBlocks.WEST;
            case EAST -> PortedBlocks.EAST;
            default -> PortedBlocks.NORTH;
         };
      }

      protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
         return PortedBlocks.catwalkShape(state, true);
      }

      protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
         return this.getShape(state, level, pos, context);
      }
   }

   public static class CatwalkStairsBlock extends HorizontalDirectionalBlock implements SimpleWaterloggedBlock {
      public static final MapCodec<PortedBlocks.CatwalkStairsBlock> CODEC = simpleCodec(PortedBlocks.CatwalkStairsBlock::new);

      public CatwalkStairsBlock(Properties properties) {
         super(properties);
         this.registerDefaultState(
            (BlockState)((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any())
                        .setValue(PortedBlocks.HORIZONTAL_FACING, Direction.NORTH))
                     .setValue(PortedBlocks.LEFT_RAILING, false))
                  .setValue(PortedBlocks.RIGHT_RAILING, false))
               .setValue(PortedBlocks.WATERLOGGED, false)
         );
      }

      protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
         return CODEC;
      }

      protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
         builder.add(new Property[]{PortedBlocks.HORIZONTAL_FACING, PortedBlocks.LEFT_RAILING, PortedBlocks.RIGHT_RAILING, PortedBlocks.WATERLOGGED});
      }

      public BlockState getStateForPlacement(BlockPlaceContext context) {
         Direction facing = context.getHorizontalDirection().getOpposite();
         return this.withSideRailings(
            (BlockState)((BlockState)this.defaultBlockState().setValue(PortedBlocks.HORIZONTAL_FACING, facing))
               .setValue(PortedBlocks.WATERLOGGED, context.getLevel().getFluidState(context.getClickedPos()).getType() == Fluids.WATER),
            context.getLevel(),
            context.getClickedPos()
         );
      }

      public FluidState getFluidState(BlockState state) {
         return state.getValue(PortedBlocks.WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
      }

      protected BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
         if ((Boolean)state.getValue(PortedBlocks.WATERLOGGED)) {
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
         }

         Direction facing = (Direction)state.getValue(PortedBlocks.HORIZONTAL_FACING);
         if (direction == facing.getCounterClockWise()) {
            return (BlockState)state.setValue(PortedBlocks.LEFT_RAILING, this.connectsToSideRailing(neighborState));
         } else {
            return direction == facing.getClockWise()
               ? (BlockState)state.setValue(PortedBlocks.RIGHT_RAILING, this.connectsToSideRailing(neighborState))
               : state;
         }
      }

      private BlockState withSideRailings(BlockState state, BlockGetter level, BlockPos pos) {
         Direction facing = (Direction)state.getValue(PortedBlocks.HORIZONTAL_FACING);
         return (BlockState)((BlockState)state.setValue(
               PortedBlocks.LEFT_RAILING, this.connectsToSideRailing(level.getBlockState(pos.relative(facing.getCounterClockWise())))
            ))
            .setValue(PortedBlocks.RIGHT_RAILING, this.connectsToSideRailing(level.getBlockState(pos.relative(facing.getClockWise()))));
      }

      private boolean connectsToSideRailing(BlockState neighborState) {
         Block block = neighborState.getBlock();
         return block instanceof PortedBlocks.RailingBlock || block instanceof PortedBlocks.FenceGateSegmentBlock;
      }

      protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
         VoxelShape shape = PortedBlocks.catwalkStairShape((Direction)state.getValue(PortedBlocks.HORIZONTAL_FACING));
         if ((Boolean)state.getValue(PortedBlocks.LEFT_RAILING)) {
            shape = Shapes.or(shape, PortedBlocks.sideRailShape((Direction)state.getValue(PortedBlocks.HORIZONTAL_FACING), true));
         }

         if ((Boolean)state.getValue(PortedBlocks.RIGHT_RAILING)) {
            shape = Shapes.or(shape, PortedBlocks.sideRailShape((Direction)state.getValue(PortedBlocks.HORIZONTAL_FACING), false));
         }

         return shape;
      }

      protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
         return this.getShape(state, level, pos, context);
      }
   }

   public static class CenteredPoleBlock extends PortedBlocks.DirectionalPortBlock {
      private final double min;
      private final double max;

      public CenteredPoleBlock(Properties properties, double min, double max) {
         super(properties);
         this.min = min;
         this.max = max;
      }

      protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
         return PortedBlocks.centeredAxisShape((Direction)state.getValue(FACING), this.min, this.max);
      }

      protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
         return this.getShape(state, level, pos, context);
      }
   }

   public static class DirectionalPortBlock extends DirectionalBlock {
      public static final MapCodec<PortedBlocks.DirectionalPortBlock> CODEC = simpleCodec(PortedBlocks.DirectionalPortBlock::new);

      public DirectionalPortBlock(Properties properties) {
         super(properties);
         this.registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue(FACING, Direction.NORTH));
      }

      protected MapCodec<? extends DirectionalBlock> codec() {
         return CODEC;
      }

      protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
         builder.add(new Property[]{FACING});
      }

      public BlockState getStateForPlacement(BlockPlaceContext context) {
         return (BlockState)this.defaultBlockState().setValue(FACING, context.getClickedFace());
      }
   }

   public static class FenceGateSegmentBlock extends HorizontalDirectionalBlock {
      public static final MapCodec<PortedBlocks.FenceGateSegmentBlock> CODEC = simpleCodec(PortedBlocks.FenceGateSegmentBlock::new);

      public FenceGateSegmentBlock(Properties properties) {
         super(properties);
         this.registerDefaultState(
            (BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(PortedBlocks.HORIZONTAL_FACING, Direction.NORTH))
                  .setValue(PortedBlocks.OPEN, false))
               .setValue(PortedBlocks.SEGMENT, 0)
         );
      }

      protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
         return CODEC;
      }

      protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
         builder.add(new Property[]{PortedBlocks.HORIZONTAL_FACING, PortedBlocks.OPEN, PortedBlocks.SEGMENT});
      }

      public BlockState getStateForPlacement(BlockPlaceContext context) {
         BlockState below = context.getLevel().getBlockState(context.getClickedPos().below());
         return below.getBlock() instanceof PortedBlocks.FenceGateSegmentBlock && below.getValue(PortedBlocks.SEGMENT) == 0
            ? (BlockState)((BlockState)((BlockState)this.defaultBlockState()
                     .setValue(PortedBlocks.HORIZONTAL_FACING, (Direction)below.getValue(PortedBlocks.HORIZONTAL_FACING)))
                  .setValue(PortedBlocks.OPEN, (Boolean)below.getValue(PortedBlocks.OPEN)))
               .setValue(PortedBlocks.SEGMENT, 1)
            : (BlockState)((BlockState)this.defaultBlockState().setValue(PortedBlocks.HORIZONTAL_FACING, context.getHorizontalDirection().getOpposite()))
               .setValue(PortedBlocks.SEGMENT, 0);
      }

      protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
         if (!level.isClientSide) {
            this.setOpen(level, pos, state, !(Boolean)state.getValue(PortedBlocks.OPEN));
         }

         return InteractionResult.SUCCESS;
      }

      private void setOpen(Level level, BlockPos pos, BlockState state, boolean open) {
         level.setBlock(pos, (BlockState)state.setValue(PortedBlocks.OPEN, open), 10);
         BlockPos linkedPos = state.getValue(PortedBlocks.SEGMENT) == 0 ? pos.above() : pos.below();
         BlockState linkedState = level.getBlockState(linkedPos);
         if (linkedState.getBlock() instanceof PortedBlocks.FenceGateSegmentBlock
            && linkedState.getValue(PortedBlocks.SEGMENT) != state.getValue(PortedBlocks.SEGMENT)) {
            level.setBlock(linkedPos, (BlockState)linkedState.setValue(PortedBlocks.OPEN, open), 10);
            level.updateNeighborsAt(linkedPos, this);
         }

         level.updateNeighborsAt(pos, this);
      }

      protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
         if (!state.is(newState.getBlock())) {
            BlockPos linkedPos = state.getValue(PortedBlocks.SEGMENT) == 0 ? pos.above() : pos.below();
            BlockState linkedState = level.getBlockState(linkedPos);
            if (linkedState.getBlock() instanceof PortedBlocks.FenceGateSegmentBlock
               && linkedState.getValue(PortedBlocks.SEGMENT) != state.getValue(PortedBlocks.SEGMENT)) {
               level.removeBlock(linkedPos, false);
            }
         }

         super.onRemove(state, level, pos, newState, movedByPiston);
      }

      protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
         Direction facing = (Direction)state.getValue(PortedBlocks.HORIZONTAL_FACING);
         return facing.getAxis() == Axis.Z ? Block.box(0.0, 0.0, 7.0, 16.0, 16.0, 9.0) : Block.box(7.0, 0.0, 0.0, 9.0, 16.0, 16.0);
      }

      protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
         return state.getValue(PortedBlocks.OPEN) ? Shapes.empty() : this.getShape(state, level, pos, context);
      }
   }

   public static class FixedShapeBlock extends Block {
      public static final MapCodec<PortedBlocks.FixedShapeBlock> CODEC = simpleCodec(properties -> new PortedBlocks.FixedShapeBlock(properties, Shapes.block()));
      private final VoxelShape shape;
      private final boolean emptyCollision;

      public FixedShapeBlock(Properties properties, VoxelShape shape) {
         this(properties, shape, false);
      }

      public FixedShapeBlock(Properties properties, VoxelShape shape, boolean emptyCollision) {
         super(properties);
         this.shape = shape;
         this.emptyCollision = emptyCollision;
      }

      protected MapCodec<? extends Block> codec() {
         return CODEC;
      }

      protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
         return this.shape;
      }

      protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
         return this.emptyCollision ? Shapes.empty() : this.shape;
      }
   }

   public static class FloorGratingBlock extends PortedBlocks.FixedShapeBlock {
      public FloorGratingBlock(Properties properties) {
         super(properties, PortedBlocks.FLOOR_GRATING_SHAPE);
      }
   }

   public static class HatchBlock extends HorizontalDirectionalBlock {
      public static final MapCodec<PortedBlocks.HatchBlock> CODEC = simpleCodec(PortedBlocks.HatchBlock::new);

      public HatchBlock(Properties properties) {
         super(properties);
         this.registerDefaultState(
            (BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(PortedBlocks.HORIZONTAL_FACING, Direction.NORTH))
                  .setValue(PortedBlocks.OPEN, false))
               .setValue(PortedBlocks.POWERED, false)
         );
      }

      protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
         return CODEC;
      }

      protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
         builder.add(new Property[]{PortedBlocks.HORIZONTAL_FACING, PortedBlocks.OPEN, PortedBlocks.POWERED});
      }

      public BlockState getStateForPlacement(BlockPlaceContext context) {
         boolean powered = context.getLevel().hasNeighborSignal(context.getClickedPos());
         return (BlockState)((BlockState)((BlockState)this.defaultBlockState()
                  .setValue(PortedBlocks.HORIZONTAL_FACING, context.getHorizontalDirection().getOpposite()))
               .setValue(PortedBlocks.POWERED, powered))
            .setValue(PortedBlocks.OPEN, powered);
      }

      protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
         if (!level.isClientSide) {
            boolean open = (Boolean)state.getValue(PortedBlocks.POWERED) || !(Boolean)state.getValue(PortedBlocks.OPEN);
            if (open != (Boolean)state.getValue(PortedBlocks.OPEN)) {
               this.setOpen(level, pos, state, open);
            }
         }

         return InteractionResult.SUCCESS;
      }

      protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
         if (!level.isClientSide) {
            boolean powered = level.hasNeighborSignal(pos);
            if (powered != (Boolean)state.getValue(PortedBlocks.POWERED)) {
               this.setOpen(level, pos, (BlockState)state.setValue(PortedBlocks.POWERED, powered), powered);
            }
         }
      }

      private void setOpen(Level level, BlockPos pos, BlockState state, boolean open) {
         level.setBlock(pos, (BlockState)state.setValue(PortedBlocks.OPEN, open), 10);
         level.updateNeighborsAt(pos, this);
      }

      protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
         return state.getValue(PortedBlocks.OPEN)
            ? PortedBlocks.horizontalPlateShape((Direction)state.getValue(PortedBlocks.HORIZONTAL_FACING), 2.0)
            : Block.box(0.0, 14.0, 0.0, 16.0, 16.0, 16.0);
      }

      protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
         return state.getValue(PortedBlocks.OPEN) ? Shapes.empty() : this.getShape(state, level, pos, context);
      }
   }

   public static class HorizontalFacingBlock extends HorizontalDirectionalBlock {
      public static final MapCodec<PortedBlocks.HorizontalFacingBlock> CODEC = simpleCodec(PortedBlocks.HorizontalFacingBlock::new);

      public HorizontalFacingBlock(Properties properties) {
         super(properties);
         this.registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue(PortedBlocks.HORIZONTAL_FACING, Direction.NORTH));
      }

      protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
         return CODEC;
      }

      protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
         builder.add(new Property[]{PortedBlocks.HORIZONTAL_FACING});
      }

      public BlockState getStateForPlacement(BlockPlaceContext context) {
         return (BlockState)this.defaultBlockState().setValue(PortedBlocks.HORIZONTAL_FACING, context.getHorizontalDirection().getOpposite());
      }
   }

   public static class PartsBlock extends Block {
      public PartsBlock(Properties properties) {
         super(properties);
         this.registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue(PortedBlocks.PARTS, 0));
      }

      protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
         builder.add(new Property[]{PortedBlocks.PARTS});
      }

      public BlockState getStateForPlacement(BlockPlaceContext context) {
         double localY = context.getClickLocation().y - Math.floor(context.getClickLocation().y);
         int part = Mth.clamp((int)Math.floor(localY * 15.0), 0, 14);
         return (BlockState)this.defaultBlockState().setValue(PortedBlocks.PARTS, part);
      }

      protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
         int y = (Integer)state.getValue(PortedBlocks.PARTS);
         return Block.box(0.0, y, 0.0, 16.0, Math.min(16.0, y + 2.0), 16.0);
      }

      protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
         return this.getShape(state, level, pos, context);
      }
   }

   public static class RailingBlock extends PortedBlocks.CatwalkBlock {
      public RailingBlock(Properties properties) {
         super(properties);
      }

      @Override
      protected boolean connectsTo(BlockState neighborState) {
         Block block = neighborState.getBlock();
         return block instanceof PortedBlocks.RailingBlock || block instanceof PortedBlocks.FenceGateSegmentBlock;
      }

      @Override
      protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
         return PortedBlocks.railingShape(state);
      }

      @Override
      protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
         return this.getShape(state, level, pos, context);
      }
   }

   public static class SimpleDoorLikeBlock extends HorizontalDirectionalBlock {
      public static final MapCodec<PortedBlocks.SimpleDoorLikeBlock> CODEC = simpleCodec(PortedBlocks.SimpleDoorLikeBlock::new);

      public SimpleDoorLikeBlock(Properties properties) {
         super(properties);
         this.registerDefaultState(
            (BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any())
                           .setValue(PortedBlocks.HORIZONTAL_FACING, Direction.NORTH))
                        .setValue(PortedBlocks.HALF, PortedBlocks.SimpleHalf.LOWER))
                     .setValue(PortedBlocks.HINGE, DoorHingeSide.LEFT))
                  .setValue(PortedBlocks.OPEN, false))
               .setValue(PortedBlocks.POWERED, false)
         );
      }

      protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
         return CODEC;
      }

      protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
         builder.add(new Property[]{PortedBlocks.HORIZONTAL_FACING, PortedBlocks.HALF, PortedBlocks.HINGE, PortedBlocks.OPEN, PortedBlocks.POWERED});
      }

      public BlockState getStateForPlacement(BlockPlaceContext context) {
         boolean powered = context.getLevel().hasNeighborSignal(context.getClickedPos());
         return (BlockState)((BlockState)((BlockState)this.defaultBlockState()
                  .setValue(PortedBlocks.HORIZONTAL_FACING, context.getHorizontalDirection().getOpposite()))
               .setValue(PortedBlocks.POWERED, powered))
            .setValue(PortedBlocks.OPEN, powered);
      }

      protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
         if (!level.isClientSide) {
            level.setBlock(pos, (BlockState)state.cycle(PortedBlocks.OPEN), 10);
            level.updateNeighborsAt(pos, this);
         }

         return InteractionResult.SUCCESS;
      }

      protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
         return state.getValue(PortedBlocks.OPEN)
            ? PortedBlocks.horizontalPlateShape((Direction)state.getValue(PortedBlocks.HORIZONTAL_FACING), 2.0)
            : Block.box(0.0, 14.0, 0.0, 16.0, 16.0, 16.0);
      }

      protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
         return state.getValue(PortedBlocks.OPEN) ? Shapes.empty() : this.getShape(state, level, pos, context);
      }
   }

   public enum SimpleHalf implements StringRepresentable {
      UPPER("upper"),
      LOWER("lower");

      private final String name;

      SimpleHalf(String name) {
         this.name = name;
      }

      public String getSerializedName() {
         return this.name;
      }
   }

   public static class StoolBlock extends PortedBlocks.FixedShapeBlock {
      public StoolBlock(Properties properties) {
         super(properties, PortedBlocks.STOOL_SHAPE);
      }
   }

   public static class SupportBlock extends Block {
      public SupportBlock(Properties properties) {
         super(properties);
         this.registerDefaultState(
            (BlockState)((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(PortedBlocks.EASTWEST, false))
                     .setValue(PortedBlocks.LEFTBEAM, false))
                  .setValue(PortedBlocks.RIGHTBEAM, false))
               .setValue(PortedBlocks.DOWNCONNECT, 0)
         );
      }

      protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
         builder.add(new Property[]{PortedBlocks.EASTWEST, PortedBlocks.LEFTBEAM, PortedBlocks.RIGHTBEAM, PortedBlocks.DOWNCONNECT});
      }

      public BlockState getStateForPlacement(BlockPlaceContext context) {
         Direction direction = context.getHorizontalDirection();
         return this.connectedSupportState(
            (BlockState)this.defaultBlockState().setValue(PortedBlocks.EASTWEST, direction.getAxis() == Axis.X), context.getLevel(), context.getClickedPos()
         );
      }

      protected BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
         return !direction.getAxis().isHorizontal() && direction != Direction.DOWN ? state : this.connectedSupportState(state, level, pos);
      }

      private BlockState connectedSupportState(BlockState state, BlockGetter level, BlockPos pos) {
         boolean eastWest = (Boolean)state.getValue(PortedBlocks.EASTWEST);
         Direction leftDirection = eastWest ? Direction.NORTH : Direction.WEST;
         Direction rightDirection = eastWest ? Direction.SOUTH : Direction.EAST;
         return (BlockState)((BlockState)((BlockState)state.setValue(
                  PortedBlocks.LEFTBEAM, this.connectsToSupport(level.getBlockState(pos.relative(leftDirection)))
               ))
               .setValue(PortedBlocks.RIGHTBEAM, this.connectsToSupport(level.getBlockState(pos.relative(rightDirection)))))
            .setValue(PortedBlocks.DOWNCONNECT, this.downConnection(level.getBlockState(pos.below())));
      }

      private boolean connectsToSupport(BlockState neighborState) {
         return neighborState.getBlock() instanceof PortedBlocks.SupportBlock;
      }

      private int downConnection(BlockState neighborState) {
         Block block = neighborState.getBlock();
         if (block == ModBlocks.THIN_STEEL_POLE.get()) {
            return 1;
         } else {
            return block == ModBlocks.THICK_STEEL_POLE.get() ? 2 : 0;
         }
      }

      protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
         VoxelShape beam = state.getValue(PortedBlocks.EASTWEST) ? Block.box(0.0, 6.0, 6.0, 16.0, 10.0, 10.0) : Block.box(6.0, 6.0, 0.0, 10.0, 10.0, 16.0);
         if ((Integer)state.getValue(PortedBlocks.DOWNCONNECT) > 0) {
            beam = Shapes.or(beam, Block.box(6.0, 0.0, 6.0, 10.0, 6.0, 10.0));
         }

         return beam;
      }

      protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
         return this.getShape(state, level, pos, context);
      }
   }

   public static class SurfaceMountedBlock extends PortedBlocks.DirectionalPortBlock {
      private final double min;
      private final double max;
      private final double thickness;
      private final boolean emptyCollision;

      public SurfaceMountedBlock(Properties properties, double min, double max, double thickness, boolean emptyCollision) {
         super(properties);
         this.min = min;
         this.max = max;
         this.thickness = thickness;
         this.emptyCollision = emptyCollision;
      }

      protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
         Direction facing = (Direction)state.getValue(FACING);
         BlockPos supportPos = pos.relative(facing.getOpposite());
         return level.getBlockState(supportPos).isFaceSturdy(level, supportPos, facing);
      }

      protected BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
         return direction == ((Direction)state.getValue(FACING)).getOpposite() && !this.canSurvive(state, level, pos) ? Blocks.AIR.defaultBlockState() : state;
      }

      protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
         return PortedBlocks.facePlateShape((Direction)state.getValue(FACING), this.min, this.max, this.thickness);
      }

      protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
         return this.emptyCollision ? Shapes.empty() : this.getShape(state, level, pos, context);
      }
   }

   public static class TableBlock extends PortedBlocks.FixedShapeBlock {
      public TableBlock(Properties properties) {
         super(properties, PortedBlocks.TABLE_SHAPE);
      }
   }

   public static class VariantOnlyBlock extends Block {
      public VariantOnlyBlock(Properties properties) {
         super(properties);
         this.registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue(PortedBlocks.VARIANT, 0));
      }

      protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
         builder.add(new Property[]{PortedBlocks.VARIANT});
      }

      public BlockState getStateForPlacement(BlockPlaceContext context) {
         BlockPos pos = context.getClickedPos();
         int variant = Math.floorMod(pos.getX() * 31 + pos.getY() * 7 + pos.getZ(), 5);
         return (BlockState)this.defaultBlockState().setValue(PortedBlocks.VARIANT, variant);
      }

      protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
         return PortedBlocks.RAISED_CATWALK_SHAPE;
      }

      protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
         return this.getShape(state, level, pos, context);
      }
   }

   public static class VariantSlabBlock extends SlabBlock {
      public VariantSlabBlock(Properties properties) {
         super(properties);
         this.registerDefaultState((BlockState)this.defaultBlockState().setValue(PortedBlocks.TVARIANT, 0));
      }

      protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
         super.createBlockStateDefinition(builder);
         builder.add(new Property[]{PortedBlocks.TVARIANT});
      }

      public BlockState getStateForPlacement(BlockPlaceContext context) {
         BlockState state = super.getStateForPlacement(context);
         int variant = Math.floorMod(context.getClickedPos().getX() * 31 + context.getClickedPos().getY() * 7 + context.getClickedPos().getZ(), 4);
         return state == null ? null : (BlockState)state.setValue(PortedBlocks.TVARIANT, variant);
      }
   }

   public static class VerticalSlabBlock extends PortedBlocks.HorizontalFacingBlock {
      public VerticalSlabBlock(Properties properties) {
         super(properties);
      }

      protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
         return PortedBlocks.horizontalHalfShape((Direction)state.getValue(PortedBlocks.HORIZONTAL_FACING));
      }

      protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
         return this.getShape(state, level, pos, context);
      }
   }
}
