package ram.talia.moreiotas.api.casting.iota;

import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.iota.IotaType;
import com.mojang.serialization.MapCodec;
import java.util.Objects;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import org.jetbrains.annotations.NotNull;
import ram.talia.moreiotas.common.lib.hex.MoreIotasIotaTypes;

public class EntityTypeIota extends Iota {
   public final EntityType<?> entityType;
   public static IotaType<EntityTypeIota> TYPE = new IotaType<EntityTypeIota>() {
      public static final MapCodec<EntityTypeIota> MAP_CODEC = ResourceLocation.CODEC
         .xmap(
            location -> new EntityTypeIota(Objects.requireNonNull((EntityType<?>)BuiltInRegistries.ENTITY_TYPE.get(location))),
            entityTypeIota -> BuiltInRegistries.ENTITY_TYPE.getKey(entityTypeIota.entityType)
         )
         .fieldOf("entityType");
      public static final StreamCodec<RegistryFriendlyByteBuf, EntityTypeIota> STREAM_CODEC = ResourceLocation.STREAM_CODEC
         .map(
            location -> new EntityTypeIota((EntityType<?>)BuiltInRegistries.ENTITY_TYPE.get(location)),
            entityTypeIota -> BuiltInRegistries.ENTITY_TYPE.getKey(entityTypeIota.entityType)
         )
         .mapStream(buf -> buf);

      public MapCodec<EntityTypeIota> codec() {
         return MAP_CODEC;
      }

      public StreamCodec<RegistryFriendlyByteBuf, EntityTypeIota> streamCodec() {
         return STREAM_CODEC;
      }

      public int color() {
         return -11184641;
      }
   };

   public EntityTypeIota(@NotNull EntityType<?> entityType) {
      super(() -> MoreIotasIotaTypes.ENTITY_TYPE);
      this.entityType = entityType;
   }

   public EntityType<?> getEntityType() {
      return this.entityType;
   }

   protected boolean toleratesOther(Iota that) {
      return typesMatch(this, that) && that instanceof EntityTypeIota dent && this.getEntityType().equals(dent.getEntityType());
   }

   public boolean isTruthy() {
      return true;
   }

   public Component display() {
      return this.entityType
         .getDescription()
         .copy()
         .append(" ")
         .append(Component.translatable("moreiotas.spelldata.entity_type"))
         .withStyle(ChatFormatting.DARK_AQUA);
   }

   public int hashCode() {
      return this.entityType.hashCode();
   }
}
