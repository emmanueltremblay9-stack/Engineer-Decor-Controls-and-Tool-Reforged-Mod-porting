package com.oblixorprime.engineersdecorreforged.tools;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.level.Level;

public class AutoStimPackItem extends TooltipItem {
   private static final int COOLDOWN_TICKS = 900;
   private static final int REGENERATION_TICKS = 300;
   private static final int MOVEMENT_SPEED_TICKS = 400;
   private static final int PROTECTION_TICKS = 200;

   public AutoStimPackItem(Properties properties) {
      super("stimpack", properties);
   }

   public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
      ItemStack stack = player.getItemInHand(hand);
      if (level.isClientSide) {
         return InteractionResultHolder.success(stack);
      } else {
         return this.inject(level, player, stack) ? InteractionResultHolder.success(stack) : InteractionResultHolder.fail(stack);
      }
   }

   public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
      if (!level.isClientSide && entity instanceof Player player && player.getHealth() <= 6.0F && !player.getCooldowns().isOnCooldown(this)) {
         this.inject(level, player, stack);
      }
   }

   private boolean inject(Level level, Player player, ItemStack stack) {
      if (player.getCooldowns().isOnCooldown(this)) {
         return false;
      }

      player.heal(6.0F);
      player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, REGENERATION_TICKS, 0));
      player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, MOVEMENT_SPEED_TICKS, 0));
      player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, PROTECTION_TICKS, 0));
      player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, PROTECTION_TICKS, 0));
      player.getCooldowns().addCooldown(this, COOLDOWN_TICKS);
      level.playSound(null, player.blockPosition(), SoundEvents.HONEY_DRINK, player.getSoundSource(), 0.7F, 1.2F);
      if (!player.getAbilities().instabuild) {
         damageOrConsume(stack, 1);
      }

      return true;
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
