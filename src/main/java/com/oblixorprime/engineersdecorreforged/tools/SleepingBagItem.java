package com.oblixorprime.engineersdecorreforged.tools;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

public class SleepingBagItem extends TooltipItem {
   public static final int MAX_DAMAGE = 4096;
   private static final long DAY_LENGTH = 24000L;
   private static final long REST_START = 12542L;
   private static final long REST_END = 23459L;
   private static final int COOLDOWN_TICKS = 400;

   public SleepingBagItem(Properties properties) {
      super("sleeping_bag", properties);
   }

   @Override
   public boolean isBarVisible(ItemStack stack) {
      return false;
   }

   @Override
   public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
      ItemStack stack = player.getItemInHand(hand);
      if (level.isClientSide) {
         return InteractionResultHolder.success(stack);
      }

      if (level.dimension() != Level.OVERWORLD) {
         return fail(player, stack, "item.engineers_decor_reforged.sleeping_bag.msg.dimension");
      }

      long dayTime = Math.floorMod(level.getDayTime(), 24000L);
      if (dayTime >= 12542L && dayTime <= 23459L) {
         AABB area = AABB.ofSize(player.position(), 16.0, 8.0, 16.0);
         if (!level.getEntitiesOfClass(Monster.class, area, LivingEntity::isAlive).isEmpty()) {
            return fail(player, stack, "item.engineers_decor_reforged.sleeping_bag.msg.unsafe");
         }

         if (level instanceof ServerLevel serverLevel) {
            long nextMorning = (serverLevel.getDayTime() / 24000L + 1L) * 24000L;
            serverLevel.setDayTime(nextMorning);
         }

         player.getCooldowns().addCooldown(this, 400);
         if (!player.getAbilities().instabuild) {
            stack.hurtAndBreak(1, player, hand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);
         }

         level.playSound(null, player.blockPosition(), SoundEvents.WOOL_PLACE, player.getSoundSource(), 0.8F, 0.8F);
         player.displayClientMessage(Component.translatable("item.engineers_decor_reforged.sleeping_bag.msg.rested"), true);
         return InteractionResultHolder.success(stack);
      } else {
         return fail(player, stack, "item.engineers_decor_reforged.sleeping_bag.msg.daytime");
      }
   }

   private static InteractionResultHolder<ItemStack> fail(Player player, ItemStack stack, String key) {
      player.displayClientMessage(Component.translatable(key), true);
      return InteractionResultHolder.fail(stack);
   }
}
