package com.oblixorprime.engineersdecorreforged.rsgauges;

import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.Item.TooltipContext;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class SwitchLinkPearlItem extends Item {
   private static final String TAG_DIMENSION = "target_dimension";
   private static final String TAG_X = "target_x";
   private static final String TAG_Y = "target_y";
   private static final String TAG_Z = "target_z";
   private static final double MAX_REMOTE_DISTANCE = 128.0;

   public SwitchLinkPearlItem(Properties properties) {
      super(properties);
   }

   public static ItemStack linkedTo(Level level, BlockPos pos) {
      ItemStack stack = new ItemStack((ItemLike)ControlsModule.SWITCHLINK_PEARL.get());
      CompoundTag tag = new CompoundTag();
      tag.putString("target_dimension", level.dimension().location().toString());
      tag.putInt("target_x", pos.getX());
      tag.putInt("target_y", pos.getY());
      tag.putInt("target_z", pos.getZ());
      stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
      return stack;
   }

   public InteractionResult useOn(UseOnContext context) {
      ItemStack stack = context.getItemInHand();
      Player player = context.getPlayer();
      if (hasLink(stack) && player != null && player.isShiftKeyDown()) {
         Level level = context.getLevel();
         if (level.isClientSide) {
            return InteractionResult.SUCCESS;
         } else {
            return trigger(level, player, stack) ? InteractionResult.SUCCESS : InteractionResult.FAIL;
         }
      } else {
         return InteractionResult.PASS;
      }
   }

   public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
      ItemStack stack = player.getItemInHand(hand);
      if (!hasLink(stack) || !player.isShiftKeyDown()) {
         return InteractionResultHolder.pass(stack);
      } else if (level.isClientSide) {
         return InteractionResultHolder.success(stack);
      } else {
         return trigger(level, player, stack) ? InteractionResultHolder.success(stack) : InteractionResultHolder.fail(stack);
      }
   }

   @Override
   public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
      super.appendHoverText(stack, context, tooltip, flag);
      if (hasLink(stack)) {
         CompoundTag tag = linkTag(stack);
         ResourceLocation dimension = ResourceLocation.tryParse(tag.getString("target_dimension"));
         if (dimension != null) {
            BlockPos target = targetPos(tag);
            tooltip.add(
               Component.translatable("rsgauges.switchlinking.switchlink_pearl.tooltip.linkedblock", this.targetName(context, dimension, target))
                  .withStyle(ChatFormatting.AQUA)
            );
            tooltip.add(Component.literal(dimension + " " + target.toShortString()).withStyle(ChatFormatting.DARK_AQUA));
         }
      }
   }

   private static boolean trigger(Level level, Player player, ItemStack stack) {
      CompoundTag tag = linkTag(stack);
      ResourceLocation dimension = ResourceLocation.tryParse(tag.getString("target_dimension"));
      if (dimension != null && dimension.equals(level.dimension().location())) {
         BlockPos target = targetPos(tag);
         if (player != null && player.blockPosition().distSqr(target) > 16384.0) {
            message(player, "rsgauges.switchlinking.switchlink_pearl.use.toofaraway");
            return false;
         }

         if (level.isLoaded(target) && ControlsBlockTypes.triggerSwitchLinkTarget(level, target)) {
            return true;
         }

         message(player, "rsgauges.switchlinking.switchlink_pearl.use.targetgone");
         return false;
      } else {
         message(player, "rsgauges.switchlinking.switchlink_pearl.use.toofaraway");
         return false;
      }
   }

   private Component targetName(TooltipContext context, ResourceLocation dimension, BlockPos target) {
      Level level = context.level();
      if (level != null && dimension.equals(level.dimension().location()) && level.isLoaded(target)) {
         BlockState state = level.getBlockState(target);
         if (!state.isAir()) {
            return state.getBlock().getName();
         }
      }

      return Component.literal(target.toShortString());
   }

   private static boolean hasLink(ItemStack stack) {
      CompoundTag tag = linkTag(stack);
      return tag.contains("target_dimension") && tag.contains("target_x") && tag.contains("target_y") && tag.contains("target_z");
   }

   private static BlockPos targetPos(CompoundTag tag) {
      return new BlockPos(tag.getInt("target_x"), tag.getInt("target_y"), tag.getInt("target_z"));
   }

   private static CompoundTag linkTag(ItemStack stack) {
      CustomData data = (CustomData)stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
      return data.copyTag();
   }

   private static void message(Player player, String key) {
      if (player != null) {
         player.displayClientMessage(Component.translatable(key), true);
      }
   }
}
