package ram.talia.moreiotas;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.ModConfigSpec.Builder;
import net.neoforged.neoforge.common.ModConfigSpec.DoubleValue;
import net.neoforged.neoforge.common.ModConfigSpec.IntValue;

public class MoreIotasConfig {
   private static final Builder BUILDER = new Builder();
   public static final IntValue maxMatrixSize = BUILDER.comment("The maximum matrix width/height.").defineInRange("maxMatrixSize", 144, 3, 512);
   public static final IntValue maxStringSize = BUILDER.comment("The maximum length of a string.").defineInRange("maxStringSize", 1728, 1, 32768);
   public static final DoubleValue setBlockStringCost = BUILDER.comment("The cost for writing a string to a given block, in dust.")
      .defineInRange("setBlockStringCost", 0.01, 1.0E-4, 10000.0);
   public static final DoubleValue nameCost = BUILDER.comment("The cost of naming an entity with the Name spell.")
      .defineInRange("nameCost", 0.01, 1.0E-4, 10000.0);
   static final ModConfigSpec SPEC = BUILDER.build();

   private static boolean validateItemName(Object obj) {
      return obj instanceof String itemName && BuiltInRegistries.ITEM.containsKey(ResourceLocation.parse(itemName));
   }
}
