package com.oblixorprime.engineersdecorreforged.tools;

import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;

public class ChargedLapisSqueezerItem extends TooltipItem {
   public static final int DURABILITY = 64;

   public ChargedLapisSqueezerItem(Properties properties) {
      super("charged_lapis_squeezer", properties);
   }

   public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
      ItemStack stack = player.getItemInHand(hand);
      if (level.isClientSide) {
         return InteractionResultHolder.success(stack);
      }

      if (player.getHealth() <= 4.0F) {
         message(player, "item.engineers_decor_reforged.charged_lapis_squeezer.msg.lowhealth");
         return InteractionResultHolder.fail(stack);
      }

      if (player.experienceLevel < 1 && !player.getAbilities().instabuild) {
         message(player, "item.engineers_decor_reforged.charged_lapis_squeezer.msg.noxp");
         return InteractionResultHolder.fail(stack);
      }

      ItemStack lapis = findLapis(player.getInventory());
      if (lapis.isEmpty() && !player.getAbilities().instabuild) {
         message(player, "item.engineers_decor_reforged.charged_lapis_squeezer.msg.nolapis");
         return InteractionResultHolder.fail(stack);
      }

      if (!player.getAbilities().instabuild) {
         lapis.shrink(1);
         player.giveExperienceLevels(-1);
         player.hurt(level.damageSources().generic(), 2.0F);
      }

      ItemStack charged = new ItemStack((ItemLike)EngineerToolsModule.CHARGED_LAPIS.get());
      if (!player.getInventory().add(charged)) {
         player.drop(charged, false);
      }

      if (!player.getAbilities().instabuild) {
         stack.hurtAndBreak(1, player, hand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);
      }

      level.playSound(null, player.blockPosition(), SoundEvents.GRINDSTONE_USE, player.getSoundSource(), 0.6F, 1.0F);
      return InteractionResultHolder.success(stack);
   }

   private static ItemStack findLapis(Inventory inventory) {
      for (int i = 0; i < inventory.getContainerSize(); i++) {
         ItemStack stack = inventory.getItem(i);
         if (stack.is(Items.LAPIS_LAZULI)) {
            return stack;
         }
      }

      return ItemStack.EMPTY;
   }

   private static void message(Player player, String key) {
      player.displayClientMessage(Component.translatable(key), true);
   }
}
