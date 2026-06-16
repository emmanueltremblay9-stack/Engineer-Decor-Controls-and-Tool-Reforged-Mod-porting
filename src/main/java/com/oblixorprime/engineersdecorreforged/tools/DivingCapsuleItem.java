package com.oblixorprime.engineersdecorreforged.tools;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.level.Level;

public class DivingCapsuleItem extends TooltipItem {
   private static final int COOLDOWN_TICKS = 400;
   private static final int AIR_BOOST = 140;

   public DivingCapsuleItem(Properties properties) {
      super("diving_capsule", properties);
   }

   public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
      ItemStack stack = player.getItemInHand(hand);
      if (level.isClientSide) {
         return InteractionResultHolder.success(stack);
      } else {
         return this.refill(level, player, stack) ? InteractionResultHolder.success(stack) : InteractionResultHolder.fail(stack);
      }
   }

   public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
      if (!level.isClientSide && entity instanceof Player player && player.getAirSupply() < 60 && !player.getCooldowns().isOnCooldown(this)) {
         this.refill(level, player, stack);
      }
   }

   private boolean refill(Level level, Player player, ItemStack stack) {
      if (!player.getCooldowns().isOnCooldown(this) && player.getAirSupply() < player.getMaxAirSupply()) {
         player.setAirSupply(Math.min(player.getMaxAirSupply(), player.getAirSupply() + 140));
         player.getCooldowns().addCooldown(this, 400);
         level.playSound(null, player.blockPosition(), SoundEvents.BUBBLE_COLUMN_UPWARDS_INSIDE, player.getSoundSource(), 0.6F, 1.25F);
         if (!player.getAbilities().instabuild) {
            damageOrConsume(stack, 1);
         }

         return true;
      } else {
         return false;
      }
   }

   private static void damageOrConsume(ItemStack stack, int amount) {
      int nextDamage = stack.getDamageValue() + amount;
      if (nextDamage >= stack.getMaxDamage()) {
         stack.shrink(1);
      } else {
         stack.setDamageValue(nextDamage);
      }
   }
}
