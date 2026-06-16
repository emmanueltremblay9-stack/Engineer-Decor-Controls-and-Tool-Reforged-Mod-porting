package com.oblixorprime.engineersdecorreforged.tools;

import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.Item.TooltipContext;

public class TooltipItem extends Item {
   private final String helpKey;

   public TooltipItem(String itemName, Properties properties) {
      super(properties);
      this.helpKey = "item.engineers_decor_reforged." + itemName + ".help";
   }

   public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
      super.appendHoverText(stack, context, tooltip, flag);
      String text = Component.translatable(this.helpKey).getString();
      if (!this.helpKey.equals(text)) {
         for (String line : text.split("\\n")) {
            tooltip.add(Component.literal(line).withStyle(ChatFormatting.GRAY));
         }
      }
   }
}
