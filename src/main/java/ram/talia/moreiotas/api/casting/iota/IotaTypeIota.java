package ram.talia.moreiotas.api.casting.iota;

import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.iota.IotaType;
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes;
import com.mojang.serialization.MapCodec;
import java.util.Objects;
import net.minecraft.ChatFormatting;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import ram.talia.moreiotas.common.lib.hex.MoreIotasIotaTypes;

public class IotaTypeIota extends Iota {
   public final IotaType<?> iotaType;
   public static IotaType<IotaTypeIota> TYPE = new IotaType<IotaTypeIota>() {
      public static final MapCodec<IotaTypeIota> MAP_CODEC = ResourceLocation.CODEC
         .xmap(
            location -> new IotaTypeIota(Objects.requireNonNull((IotaType<?>)HexIotaTypes.REGISTRY.get(location))),
            iotaTypeIota -> HexIotaTypes.REGISTRY.getKey(iotaTypeIota.iotaType)
         )
         .fieldOf("iotatype");
      public static final StreamCodec<RegistryFriendlyByteBuf, IotaTypeIota> STREAM_CODEC = ResourceLocation.STREAM_CODEC
         .map(
            location -> new IotaTypeIota((IotaType<?>)HexIotaTypes.REGISTRY.get(location)), iotaTypeIota -> HexIotaTypes.REGISTRY.getKey(iotaTypeIota.iotaType)
         )
         .mapStream(buf -> buf);

      public MapCodec<IotaTypeIota> codec() {
         return MAP_CODEC;
      }

      public StreamCodec<RegistryFriendlyByteBuf, IotaTypeIota> streamCodec() {
         return STREAM_CODEC;
      }

      public int color() {
         return -11193515;
      }
   };

   public IotaTypeIota(@NotNull IotaType<?> iotaType) {
      super(() -> MoreIotasIotaTypes.IOTA_TYPE);
      this.iotaType = iotaType;
   }

   public IotaType<?> getIotaType() {
      return this.iotaType;
   }

   protected boolean toleratesOther(Iota that) {
      return typesMatch(this, that) && that instanceof IotaTypeIota dent && this.getIotaType().equals(dent.getIotaType());
   }

   public boolean isTruthy() {
      return true;
   }

   public Component display() {
      ResourceLocation location = HexIotaTypes.REGISTRY.getKey(this.iotaType);

      assert location != null;

      return Component.translatable("hexcasting.iota.%s".formatted(location.toString())).withStyle(ChatFormatting.DARK_PURPLE);
   }

   public int hashCode() {
      return this.iotaType.hashCode();
   }
}
