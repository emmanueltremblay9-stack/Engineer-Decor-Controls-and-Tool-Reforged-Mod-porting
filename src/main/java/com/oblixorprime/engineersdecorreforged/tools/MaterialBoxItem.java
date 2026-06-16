package com.oblixorprime.engineersdecorreforged.tools;

import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.Item.TooltipContext;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;

public class MaterialBoxItem extends TooltipItem {
   public static final int CAPACITY = 512;
   private static final String TAG_ITEM = "stored_item";
   private static final String TAG_COUNT = "stored_count";

   public MaterialBoxItem(Properties properties) {
      super("material_box", properties);
   }

   public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
      ItemStack box = player.getItemInHand(hand);
      if (hand != InteractionHand.MAIN_HAND) {
         return fail(level, player, box, "item.engineers_decor_reforged.material_box.msg.mainhand");
      }

      ItemStack offhand = player.getOffhandItem();
      return !offhand.isEmpty() ? store(level, player, box, offhand) : retrieve(level, player, box);
   }

   @Override
   public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
      super.appendHoverText(stack, context, tooltip, flag);
      int count = storedCount(stack);
      Item item = storedItem(stack);
      if (count > 0 && item != Items.AIR) {
         tooltip.add(
            Component.translatable("item.engineers_decor_reforged.material_box.tip.contents", new Object[]{count, item.getDescription()})
               .withStyle(ChatFormatting.AQUA)
         );
      }
   }

   private static InteractionResultHolder<ItemStack> store(Level level, Player player, ItemStack box, ItemStack source) {
      if (source.is(box.getItem()) || !source.isStackable()) {
         return fail(level, player, box, "item.engineers_decor_reforged.material_box.msg.unsupported");
      }

      if (!source.getComponentsPatch().isEmpty()) {
         return fail(level, player, box, "item.engineers_decor_reforged.material_box.msg.modified");
      }

      Item stored = storedItem(box);
      int count = storedCount(box);
      if (count > 0 && stored != source.getItem()) {
         return fail(level, player, box, "item.engineers_decor_reforged.material_box.msg.mismatch");
      }

      int moving = Math.min(source.getCount(), 512 - count);
      if (moving <= 0) {
         return fail(level, player, box, "item.engineers_decor_reforged.material_box.msg.full");
      }

      if (!level.isClientSide) {
         setStored(box, source.getItem(), count + moving);
         source.shrink(moving);
         player.displayClientMessage(Component.translatable("item.engineers_decor_reforged.material_box.msg.stored", new Object[]{moving}), true);
      }

      return InteractionResultHolder.success(box);
   }

   private static InteractionResultHolder<ItemStack> retrieve(Level level, Player player, ItemStack box) {
      if (!player.getOffhandItem().isEmpty()) {
         return fail(level, player, box, "item.engineers_decor_reforged.material_box.msg.offhand_full");
      }

      int count = storedCount(box);
      Item item = storedItem(box);
      if (count > 0 && item != Items.AIR) {
         int moving = Math.min(count, item.getDefaultInstance().getMaxStackSize());
         if (!level.isClientSide) {
            player.setItemInHand(InteractionHand.OFF_HAND, new ItemStack(item, moving));
            setStored(box, item, count - moving);
            player.displayClientMessage(Component.translatable("item.engineers_decor_reforged.material_box.msg.retrieved", new Object[]{moving}), true);
         }

         return InteractionResultHolder.success(box);
      } else {
         return fail(level, player, box, "item.engineers_decor_reforged.material_box.msg.empty");
      }
   }

   public static int storedCount(ItemStack stack) {
      MaterialBoxItem.StoredContent content = storedContent(stack);
      return content.item() == Items.AIR ? 0 : content.count();
   }

   public static Item storedItem(ItemStack stack) {
      return storedContent(stack).item();
   }

   private static MaterialBoxItem.StoredContent storedContent(ItemStack stack) {
      CompoundTag tag = data(stack);
      ResourceLocation id = ResourceLocation.tryParse(tag.getString("stored_item"));
      Item item = id == null ? Items.AIR : (Item)BuiltInRegistries.ITEM.get(id);
      int count = Math.min(CAPACITY, Math.max(0, tag.getInt("stored_count")));
      return item == Items.AIR ? new MaterialBoxItem.StoredContent(Items.AIR, 0) : new MaterialBoxItem.StoredContent(item, count);
   }

   private static void setStored(ItemStack stack, Item item, int count) {
      if (count > 0 && item != Items.AIR) {
         CompoundTag tag = new CompoundTag();
         tag.putString("stored_item", BuiltInRegistries.ITEM.getKey(item).toString());
         tag.putInt("stored_count", Math.min(count, 512));
         stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
      } else {
         stack.remove(DataComponents.CUSTOM_DATA);
      }
   }

   private static CompoundTag data(ItemStack stack) {
      return ((CustomData)stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY)).copyTag();
   }

   private record StoredContent(Item item, int count) {
   }

   private static InteractionResultHolder<ItemStack> fail(Level level, Player player, ItemStack box, String key) {
      if (!level.isClientSide) {
         player.displayClientMessage(Component.translatable(key), true);
      }

      return InteractionResultHolder.fail(box);
   }
}
