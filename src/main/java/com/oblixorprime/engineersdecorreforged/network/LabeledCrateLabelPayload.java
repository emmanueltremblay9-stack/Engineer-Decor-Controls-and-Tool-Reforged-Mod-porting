package com.oblixorprime.engineersdecorreforged.network;

import com.oblixorprime.engineersdecorreforged.utility.MachineBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type;
import net.minecraft.resources.ResourceLocation;

public record LabeledCrateLabelPayload(BlockPos pos, String line0, String line1) implements CustomPacketPayload {
   public static final Type<LabeledCrateLabelPayload> TYPE = new Type(ResourceLocation.fromNamespaceAndPath("engineers_decor_reforged", "labeled_crate_label"));
   public static final StreamCodec<RegistryFriendlyByteBuf, LabeledCrateLabelPayload> STREAM_CODEC = new StreamCodec<RegistryFriendlyByteBuf, LabeledCrateLabelPayload>() {
      public LabeledCrateLabelPayload decode(RegistryFriendlyByteBuf buffer) {
         return new LabeledCrateLabelPayload(buffer.readBlockPos(), buffer.readUtf(MachineBlockEntity.LABEL_MAX_UTF16_UNITS), buffer.readUtf(MachineBlockEntity.LABEL_MAX_UTF16_UNITS));
      }

      public void encode(RegistryFriendlyByteBuf buffer, LabeledCrateLabelPayload payload) {
         buffer.writeBlockPos(payload.pos());
         buffer.writeUtf(MachineBlockEntity.sanitizeLabelLine(payload.line0()), MachineBlockEntity.LABEL_MAX_UTF16_UNITS);
         buffer.writeUtf(MachineBlockEntity.sanitizeLabelLine(payload.line1()), MachineBlockEntity.LABEL_MAX_UTF16_UNITS);
      }
   };

   public Type<? extends CustomPacketPayload> type() {
      return TYPE;
   }
}
