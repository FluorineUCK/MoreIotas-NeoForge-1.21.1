package ram.talia.moreiotas.api.casting.iota;

import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.iota.IotaType;
import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import ram.talia.moreiotas.common.lib.hex.MoreIotasIotaTypes;

public class ItemStackIota extends Iota {
   public final ItemStack itemStack;
   public static IotaType<ItemStackIota> TYPE = new IotaType<ItemStackIota>() {
      public static final MapCodec<ItemStackIota> MAP_CODEC = ItemStack.CODEC.xmap(ItemStackIota::new, ItemStackIota::getItemStack).fieldOf("itemstack");
      public static final StreamCodec<RegistryFriendlyByteBuf, ItemStackIota> STREAM_CODEC = ItemStack.STREAM_CODEC
         .map(ItemStackIota::new, ItemStackIota::getItemStack)
         .mapStream(buf -> buf);

      public MapCodec<ItemStackIota> codec() {
         return MAP_CODEC;
      }

      public StreamCodec<RegistryFriendlyByteBuf, ItemStackIota> streamCodec() {
         return STREAM_CODEC;
      }

      public int color() {
         return -14774017;
      }
   };
   private static final String TAG_STACK_ID = "moreiotas:stack_id";
   private static final String TAG_STACK_COUNT = "moreiotas:stack_count";
   private static final String TAG_STACK_TAG = "moreiotas:stack_tag";

   private ItemStackIota(ItemStack stack) {
      super(() -> MoreIotasIotaTypes.ITEM_STACK);
      this.itemStack = stack;
   }

   public ItemStack getItemStack() {
      return this.itemStack;
   }

   protected boolean toleratesOther(Iota iota) {
      return iota instanceof ItemStackIota iiota && ItemStack.matches(this.getItemStack(), iiota.getItemStack());
   }

   public boolean isTruthy() {
      return !this.getItemStack().isEmpty();
   }

   public static ItemStackIota createFiltered(ItemStack originalStack) {
      ItemStack stack = originalStack.copy();
      return new ItemStackIota(stack);
   }

   public Component display() {
      return this.itemStack.isEmpty()
         ? Component.translatable("moreiotas.tooltip.stack.empty").withStyle(Style.EMPTY.withColor(2003199))
         : Component.translatable("moreiotas.tooltip.stack.format", new Object[]{this.itemStack.getCount(), this.itemStack.getDisplayName()})
            .withStyle(Style.EMPTY.withColor(2003199));
   }

   public int hashCode() {
      return this.itemStack.hashCode();
   }
}
