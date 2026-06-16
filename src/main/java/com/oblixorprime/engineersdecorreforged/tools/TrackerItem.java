package com.oblixorprime.engineersdecorreforged.tools;

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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.Item.TooltipContext;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public class TrackerItem extends TooltipItem {
   private static final String TAG_DIMENSION = "target_dimension";
   private static final String TAG_X = "target_x";
   private static final String TAG_Y = "target_y";
   private static final String TAG_Z = "target_z";

   public TrackerItem(Properties properties) {
      super("tracker", properties);
   }

   public InteractionResult useOn(UseOnContext context) {
      ItemStack stack = context.getItemInHand();
      Level level = context.getLevel();
      if (level.isClientSide) {
         return InteractionResult.SUCCESS;
      }

      BlockPos pos = context.getClickedPos();
      CompoundTag tag = new CompoundTag();
      tag.putString("target_dimension", level.dimension().location().toString());
      tag.putInt("target_x", pos.getX());
      tag.putInt("target_y", pos.getY());
      tag.putInt("target_z", pos.getZ());
      stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
      if (context.getPlayer() != null) {
         context.getPlayer().displayClientMessage(Component.translatable("item.engineers_decor_reforged.tracker.msg.locationset"), true);
      }

      return InteractionResult.SUCCESS;
   }

   public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
      ItemStack stack = player.getItemInHand(hand);
      if (!player.isShiftKeyDown()) {
         return InteractionResultHolder.pass(stack);
      }

      if (!level.isClientSide) {
         stack.remove(DataComponents.CUSTOM_DATA);
         player.displayClientMessage(Component.translatable("item.engineers_decor_reforged.tracker.hint.cleared"), true);
      }

      return InteractionResultHolder.success(stack);
   }

   @Override
   public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
      super.appendHoverText(stack, context, tooltip, flag);
      CompoundTag tag = ((CustomData)stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY)).copyTag();
      ResourceLocation dimension = ResourceLocation.tryParse(tag.getString("target_dimension"));
      if (dimension != null) {
         tooltip.add(
            Component.translatable(
                  "item.engineers_decor_reforged.tracker.tip.target.location",
                  new Object[]{tag.getInt("target_x") + ", " + tag.getInt("target_y") + ", " + tag.getInt("target_z")}
               )
               .withStyle(ChatFormatting.AQUA)
         );
         tooltip.add(Component.literal(dimension.toString().replace("engineers_decor_reforged:", "")).withStyle(ChatFormatting.DARK_AQUA));
      }
   }
}
