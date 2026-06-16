package com.oblixorprime.engineersdecorreforged.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type;
import net.minecraft.resources.ResourceLocation;

public record MachineActionPayload(int containerId, int action, int valueA, int valueB) implements CustomPacketPayload {
   public static final Type<MachineActionPayload> TYPE = new Type(ResourceLocation.fromNamespaceAndPath("engineers_decor_reforged", "machine_action"));
   public static final StreamCodec<RegistryFriendlyByteBuf, MachineActionPayload> STREAM_CODEC = new StreamCodec<RegistryFriendlyByteBuf, MachineActionPayload>() {
      public MachineActionPayload decode(RegistryFriendlyByteBuf buffer) {
         return new MachineActionPayload(buffer.readVarInt(), buffer.readVarInt(), buffer.readVarInt(), buffer.readVarInt());
      }

      public void encode(RegistryFriendlyByteBuf buffer, MachineActionPayload payload) {
         buffer.writeVarInt(payload.containerId());
         buffer.writeVarInt(payload.action());
         buffer.writeVarInt(payload.valueA());
         buffer.writeVarInt(payload.valueB());
      }
   };

   public Type<? extends CustomPacketPayload> type() {
      return TYPE;
   }
}
