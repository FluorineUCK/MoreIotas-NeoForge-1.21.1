package ram.talia.moreiotas.api.casting.iota;

import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.iota.IotaType;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ram.talia.moreiotas.common.lib.hex.MoreIotasIotaTypes;

public class ItemTypeIota extends Iota {
   public final Either<Item, Block> type;
   public static IotaType<ItemTypeIota> TYPE = new IotaType<ItemTypeIota>() {
      private static final MapCodec<EncodedType> PAYLOAD_CODEC = RecordCodecBuilder.mapCodec(instance ->
         instance.group(
            BuiltInRegistries.ITEM.byNameCodec().optionalFieldOf("item").forGetter(EncodedType::item),
            BuiltInRegistries.BLOCK.byNameCodec().optionalFieldOf("block").forGetter(EncodedType::block)
         ).apply(instance, EncodedType::new)
      );
      public static final MapCodec<ItemTypeIota> CODEC = PAYLOAD_CODEC.flatXmap(
         ItemTypeIota::decodePayload,
         ItemTypeIota::encodePayload
      );
      public static final StreamCodec<RegistryFriendlyByteBuf, ItemTypeIota> STREAM_CODEC = ByteBufCodecs.STRING_UTF8
         .map(ItemTypeIota::fromString, ItemTypeIota::turnIntoString)
         .mapStream(buf -> buf);

      public MapCodec<ItemTypeIota> codec() {
         return CODEC;
      }

      public StreamCodec<RegistryFriendlyByteBuf, ItemTypeIota> streamCodec() {
         return STREAM_CODEC;
      }

      public int color() {
         return -87551;
      }
   };

   public ItemTypeIota(@NotNull Item item) {
      super(() -> MoreIotasIotaTypes.ITEM_TYPE);
      this.type = Either.left(item);
   }

   public ItemTypeIota(@NotNull Block block) {
      super(() -> MoreIotasIotaTypes.ITEM_TYPE);
      this.type = Either.right(block);
   }

   public Either<Item, Block> getEither() {
      return this.type;
   }

   @Nullable
   public Block getBlock() {
      return (Block)this.getEither().map(item -> item instanceof BlockItem blockItem ? blockItem.getBlock() : null, block -> block);
   }

   public Item getItem() {
      return (Item)this.getEither().map(item -> item, Block::asItem);
   }

   protected boolean toleratesOther(Iota that) {
      return typesMatch(this, that)
         && that instanceof ItemTypeIota dent
         && (Boolean)this.getEither().map(itemThis -> (Boolean)dent.getEither().map(itemThis::equals, blockThat -> {
            Item itemThat = blockThat.asItem();
            return itemThat.equals(Items.AIR) && !blockThat.equals(Blocks.AIR) ? false : itemThis.equals(itemThat);
         }), blockThis -> (Boolean)dent.getEither().map(itemThat -> {
            Item itemThis = blockThis.asItem();
            return itemThis.equals(Items.AIR) && !blockThis.equals(Blocks.AIR) ? false : itemThis.equals(itemThat);
         }, blockThis::equals));
   }

   public boolean isTruthy() {
      return (Boolean)this.getEither().map(item -> !item.equals(Items.AIR), block -> !block.defaultBlockState().isAir());
   }

   public Component display() {
      return ((MutableComponent)this.type.map(item -> item.getDescription().copy(), Block::getName)).withStyle(ChatFormatting.GOLD);
   }

   public int hashCode() {
      return this.type.hashCode();
   }

   public static ItemTypeIota fromString(String str) {
      if (str.startsWith("item:")) {
         ResourceLocation location = (ResourceLocation)ResourceLocation.read(str.substring(5)).getOrThrow();
         return new ItemTypeIota((Item)BuiltInRegistries.ITEM.get(location));
      } else {
         ResourceLocation location = (ResourceLocation)ResourceLocation.read(str.substring(6)).getOrThrow();
         return new ItemTypeIota((Block)BuiltInRegistries.BLOCK.get(location));
      }
   }

   public static String turnIntoString(ItemTypeIota iota) {
      return (String)iota.type.map(item -> {
         String constructed = "item:";
         return constructed.concat(BuiltInRegistries.ITEM.getKey(item).toString());
      }, block -> {
         String constructed = "block:";
         return constructed.concat(BuiltInRegistries.BLOCK.getKey(block).toString());
      });
   }

   private static DataResult<ItemTypeIota> decodePayload(EncodedType payload) {
      if (payload.item().isPresent() == payload.block().isPresent()) {
         return DataResult.error(() -> "Item type iota must contain exactly one of 'item' or 'block'");
      }
      if (payload.item().isPresent()) {
         return DataResult.success(new ItemTypeIota(payload.item().orElseThrow()));
      }
      return DataResult.success(new ItemTypeIota(payload.block().orElseThrow()));
   }

   private static DataResult<EncodedType> encodePayload(ItemTypeIota iota) {
      return iota.type.map(
         item -> DataResult.success(new EncodedType(Optional.of(item), Optional.empty())),
         block -> DataResult.success(new EncodedType(Optional.empty(), Optional.of(block)))
      );
   }

   private record EncodedType(Optional<Item> item, Optional<Block> block) {
   }
}
