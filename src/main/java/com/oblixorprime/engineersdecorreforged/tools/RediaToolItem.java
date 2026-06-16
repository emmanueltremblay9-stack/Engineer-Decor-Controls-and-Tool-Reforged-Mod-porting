package com.oblixorprime.engineersdecorreforged.tools;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.WallTorchBlock;
import net.minecraft.world.level.block.state.BlockState;

public class RediaToolItem extends TooltipItem {
   public RediaToolItem(Properties properties) {
      super("redia_tool", properties);
   }

   public InteractionResult useOn(UseOnContext context) {
      Player player = context.getPlayer();
      if (player == null) {
         return InteractionResult.PASS;
      } else {
         return player.isShiftKeyDown() ? placeTorch(context, player) : tillSoil(context, player);
      }
   }

   private static InteractionResult tillSoil(UseOnContext context, Player player) {
      Level level = context.getLevel();
      BlockPos pos = context.getClickedPos();
      if (context.getClickedFace() != Direction.DOWN && level.getBlockState(pos.above()).isAir()) {
         BlockState state = level.getBlockState(pos);
         if (!canTill(state)) {
            return fail(level, player, "item.engineers_decor_reforged.redia_tool.msg.badtarget");
         }

         if (!level.isClientSide) {
            level.setBlock(pos, Blocks.FARMLAND.defaultBlockState(), 11);
            level.playSound(null, pos, SoundEvents.HOE_TILL, player.getSoundSource(), 0.8F, 1.0F);
            damage(context.getItemInHand(), player, context.getHand());
         }

         return InteractionResult.SUCCESS;
      } else {
         return fail(level, player, "item.engineers_decor_reforged.redia_tool.msg.badtarget");
      }
   }

   private static InteractionResult placeTorch(UseOnContext context, Player player) {
      Level level = context.getLevel();
      Direction face = context.getClickedFace();
      if (face == Direction.DOWN) {
         return fail(level, player, "item.engineers_decor_reforged.redia_tool.msg.badtarget");
      }

      BlockPos placePos = context.getClickedPos().relative(face);
      BlockState marker = face == Direction.UP
         ? Blocks.TORCH.defaultBlockState()
         : (BlockState)Blocks.WALL_TORCH.defaultBlockState().setValue(WallTorchBlock.FACING, face);
      if (level.getBlockState(placePos).canBeReplaced()
         && level.getBlockState(context.getClickedPos()).isFaceSturdy(level, context.getClickedPos(), face)
         && marker.canSurvive(level, placePos)) {
         ItemStack torch = findTorch(player.getInventory());
         if (torch.isEmpty() && !player.getAbilities().instabuild) {
            return fail(level, player, "item.engineers_decor_reforged.redia_tool.msg.notorch");
         }

         if (!level.isClientSide) {
            level.setBlock(placePos, marker, 11);
            if (!player.getAbilities().instabuild) {
               torch.shrink(1);
            }

            level.playSound(null, placePos, SoundEvents.WOOD_PLACE, player.getSoundSource(), 0.7F, 1.0F);
            damage(context.getItemInHand(), player, context.getHand());
         }

         return InteractionResult.SUCCESS;
      } else {
         return fail(level, player, "item.engineers_decor_reforged.redia_tool.msg.badtarget");
      }
   }

   private static boolean canTill(BlockState state) {
      return state.is(Blocks.GRASS_BLOCK) || state.is(Blocks.DIRT) || state.is(Blocks.COARSE_DIRT) || state.is(Blocks.ROOTED_DIRT) || state.is(Blocks.PODZOL);
   }

   private static ItemStack findTorch(Inventory inventory) {
      for (int i = 0; i < inventory.getContainerSize(); i++) {
         ItemStack stack = inventory.getItem(i);
         if (stack.is(Items.TORCH)) {
            return stack;
         }
      }

      return ItemStack.EMPTY;
   }

   private static InteractionResult fail(Level level, Player player, String key) {
      if (!level.isClientSide) {
         player.displayClientMessage(Component.translatable(key), true);
      }

      return InteractionResult.FAIL;
   }

   private static void damage(ItemStack stack, Player player, InteractionHand hand) {
      if (!player.getAbilities().instabuild) {
         stack.hurtAndBreak(1, player, hand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);
      }
   }
}
