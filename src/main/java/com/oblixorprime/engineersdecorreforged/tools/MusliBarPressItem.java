package com.oblixorprime.engineersdecorreforged.tools;

import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;

public class MusliBarPressItem extends TooltipItem {
   private static final int OUTPUT_COUNT = 4;

   public MusliBarPressItem(Properties properties) {
      super("musli_bar_press", properties);
   }

   public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
      ItemStack press = player.getItemInHand(hand);
      Inventory inventory = player.getInventory();
      ItemStack output = new ItemStack((ItemLike)EngineerToolsModule.MUSLI_BAR.get(), 4);
      if (player.getAbilities().instabuild || hasItem(inventory, Items.BREAD) && hasItem(inventory, Items.APPLE) && hasItem(inventory, Items.WHEAT_SEEDS)) {
         if (!canFit(inventory, output, !player.getAbilities().instabuild)) {
            return fail(level, player, press, "item.engineers_decor_reforged.musli_bar_press.msg.full");
         }

         if (!level.isClientSide) {
            if (!player.getAbilities().instabuild) {
               consumeOne(inventory, Items.BREAD);
               consumeOne(inventory, Items.APPLE);
               consumeOne(inventory, Items.WHEAT_SEEDS);
            }

            inventory.add(output);
            if (!player.getAbilities().instabuild) {
               press.hurtAndBreak(1, player, hand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);
            }

            level.playSound(null, player.blockPosition(), SoundEvents.HONEY_BLOCK_PLACE, player.getSoundSource(), 0.6F, 1.1F);
            player.displayClientMessage(Component.translatable("item.engineers_decor_reforged.musli_bar_press.msg.pressed"), true);
         }

         return InteractionResultHolder.success(press);
      } else {
         return fail(level, player, press, "item.engineers_decor_reforged.musli_bar_press.msg.ingredients");
      }
   }

   private static boolean hasItem(Inventory inventory, Item item) {
      for (int i = 0; i < inventory.getContainerSize(); i++) {
         if (inventory.getItem(i).is(item)) {
            return true;
         }
      }

      return false;
   }

   private static void consumeOne(Inventory inventory, Item item) {
      for (int i = 0; i < inventory.getContainerSize(); i++) {
         ItemStack stack = inventory.getItem(i);
         if (stack.is(item)) {
            stack.shrink(1);
            return;
         }
      }
   }

   private static boolean canFit(Inventory inventory, ItemStack stack, boolean consumesIngredients) {
      int remaining = stack.getCount();
      int[] ingredients = consumesIngredients ? new int[]{1, 1, 1} : new int[]{0, 0, 0};

      for (int i = 0; i < inventory.items.size(); i++) {
         ItemStack existing = (ItemStack)inventory.items.get(i);
         int simulatedCount = simulatedCountAfterIngredientUse(existing, ingredients);
         remaining = accountOutputSpace(inventory, stack, existing, simulatedCount, remaining, true);
         if (remaining <= 0) {
            return true;
         }
      }

      for (int i = 0; i < inventory.armor.size(); i++) {
         simulatedCountAfterIngredientUse((ItemStack)inventory.armor.get(i), ingredients);
      }

      ItemStack offhand = (ItemStack)inventory.offhand.get(0);
      simulatedCountAfterIngredientUse(offhand, ingredients);
      return remaining <= 0;
   }

   private static int simulatedCountAfterIngredientUse(ItemStack stack, int[] ingredients) {
      int count = stack.getCount();
      if (count <= 0) {
         return 0;
      } else if (ingredients[0] > 0 && stack.is(Items.BREAD)) {
         ingredients[0]--;
         return count - 1;
      } else if (ingredients[1] > 0 && stack.is(Items.APPLE)) {
         ingredients[1]--;
         return count - 1;
      } else if (ingredients[2] > 0 && stack.is(Items.WHEAT_SEEDS)) {
         ingredients[2]--;
         return count - 1;
      } else {
         return count;
      }
   }

   private static int accountOutputSpace(
      Inventory inventory, ItemStack output, ItemStack existing, int simulatedCount, int remaining, boolean emptySlotCanAccept
   ) {
      if (simulatedCount <= 0) {
         return emptySlotCanAccept ? remaining - Math.min(output.getMaxStackSize(), inventory.getMaxStackSize()) : remaining;
      } else {
         return ItemStack.isSameItemSameComponents(existing, output)
            ? remaining - Math.max(0, Math.min(existing.getMaxStackSize(), inventory.getMaxStackSize()) - simulatedCount)
            : remaining;
      }
   }

   private static InteractionResultHolder<ItemStack> fail(Level level, Player player, ItemStack press, String key) {
      if (!level.isClientSide) {
         player.displayClientMessage(Component.translatable(key), true);
      }

      return InteractionResultHolder.fail(press);
   }
}
