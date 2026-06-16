package com.oblixorprime.engineersdecorreforged.utility;

import java.util.List;
import java.util.Objects;
import net.minecraft.core.BlockPos;

public final class LabeledCrateLabelClientBridge {
   private static LabeledCrateLabelClientBridge.Opener opener = (pos, lines) -> {};

   private LabeledCrateLabelClientBridge() {
   }

   public static void setOpener(LabeledCrateLabelClientBridge.Opener opener) {
      LabeledCrateLabelClientBridge.opener = Objects.requireNonNull(opener);
   }

   public static void open(BlockPos pos, List<String> lines) {
      opener.open(pos, lines);
   }

   @FunctionalInterface
   public interface Opener {
      void open(BlockPos var1, List<String> var2);
   }
}
