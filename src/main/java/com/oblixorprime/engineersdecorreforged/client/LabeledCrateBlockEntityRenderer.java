package com.oblixorprime.engineersdecorreforged.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.oblixorprime.engineersdecorreforged.utility.MachineBlockEntity;
import com.oblixorprime.engineersdecorreforged.utility.MachineBlocks;
import com.oblixorprime.engineersdecorreforged.utility.MachineKind;
import java.util.List;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Font.DisplayMode;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider.Context;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

public final class LabeledCrateBlockEntityRenderer implements BlockEntityRenderer<MachineBlockEntity> {
   private static final int TEXT_COLOR = 1118481;
   private final Font font;

   public LabeledCrateBlockEntityRenderer(Context context) {
      this.font = context.getFont();
   }

   public void render(MachineBlockEntity machine, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
      if (machine.kind() == MachineKind.LABELED_CRATE) {
         List<String> lines = machine.labelLines();
         if (!lines.stream().allMatch(String::isBlank)) {
            BlockState state = machine.getBlockState();
            Direction facing = state.hasProperty(MachineBlocks.HORIZONTAL_FACING)
               ? (Direction)state.getValue(MachineBlocks.HORIZONTAL_FACING)
               : Direction.NORTH;
            poseStack.pushPose();
            poseStack.translate(0.5, 0.5, 0.5);
            poseStack.mulPose(Axis.YP.rotationDegrees(rotationFor(facing)));
            poseStack.translate(0.0, -0.012, -0.565);
            poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
            int widest = Math.max(this.font.width(lines.get(0)), this.font.width(lines.get(1)));
            float scale = Math.min(0.0105F, 0.6F / Math.max(1, widest));
            poseStack.scale(scale, -scale, scale);
            this.drawCentered(lines.get(0), -8.0F, poseStack, buffer);
            this.drawCentered(lines.get(1), 5.0F, poseStack, buffer);
            poseStack.popPose();
         }
      }
   }

   private void drawCentered(String text, float y, PoseStack poseStack, MultiBufferSource buffer) {
      if (!text.isBlank()) {
         float x = -this.font.width(text) / 2.0F;
         this.font.drawInBatch(text, x, y, 1118481, false, poseStack.last().pose(), buffer, DisplayMode.POLYGON_OFFSET, 0, 15728880);
      }
   }

   private static float rotationFor(Direction facing) {
      return switch (facing) {
         case SOUTH -> 180.0F;
         case WEST -> 90.0F;
         case EAST -> -90.0F;
         default -> 0.0F;
      };
   }
}
