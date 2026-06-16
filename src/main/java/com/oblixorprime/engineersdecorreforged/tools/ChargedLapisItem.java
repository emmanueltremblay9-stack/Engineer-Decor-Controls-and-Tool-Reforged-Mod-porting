package com.oblixorprime.engineersdecorreforged.tools;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.level.Level;

public class ChargedLapisItem extends TooltipItem {
   public ChargedLapisItem(Properties properties) {
      super("charged_lapis", properties);
   }

   public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
      ItemStack stack = player.getItemInHand(hand);
      if (level.isClientSide) {
         return InteractionResultHolder.success(stack);
      }

      player.giveExperienceLevels(1);
      player.removeAllEffects();
      player.heal(3.0F);
      if (!player.getAbilities().instabuild) {
         stack.shrink(1);
      }

      level.playSound(null, player.blockPosition(), SoundEvents.EXPERIENCE_ORB_PICKUP, player.getSoundSource(), 0.6F, 1.0F);
      return InteractionResultHolder.success(stack);
   }
}
