package ram.talia.moreiotas.api.casting.iota;

import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.iota.IotaType;
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota;
import com.mojang.serialization.MapCodec;
import net.minecraft.ChatFormatting;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.codec.StreamCodec;
import org.ejml.simple.SimpleMatrix;
import org.jetbrains.annotations.NotNull;
import ram.talia.moreiotas.MoreIotas;
import ram.talia.moreiotas.MoreIotasConfig;
import ram.talia.moreiotas.api.MoreIotasCodecs;
import ram.talia.moreiotas.common.lib.hex.MoreIotasIotaTypes;

public class MatrixIota extends Iota {
   public final SimpleMatrix matrix;
   public static IotaType<MatrixIota> TYPE = new IotaType<MatrixIota>() {
      public static final MapCodec<MatrixIota> MAP_CODEC = MoreIotasCodecs.SIMPLEMATRIX.xmap(MatrixIota::new, MatrixIota::getMatrix).fieldOf("matrix");
      public static final StreamCodec<RegistryFriendlyByteBuf, MatrixIota> STREAM_CODEC = MoreIotasCodecs.SIMPLEMATRIX_STREAM
         .map(MatrixIota::new, MatrixIota::getMatrix)
         .mapStream(buf -> buf);

      public MapCodec<MatrixIota> codec() {
         return MAP_CODEC;
      }

      public StreamCodec<RegistryFriendlyByteBuf, MatrixIota> streamCodec() {
         return STREAM_CODEC;
      }

      public int color() {
         return -11141121;
      }
   };

   public MatrixIota(@NotNull SimpleMatrix matrix) throws MishapInvalidIota {
      super(() -> MoreIotasIotaTypes.MATRIX);
      this.matrix = matrix;
      if (matrix.getNumRows() > (Integer)MoreIotasConfig.maxMatrixSize.get() || matrix.getNumCols() > (Integer)MoreIotasConfig.maxMatrixSize.get()) {
         throw MishapInvalidIota.of(this, 0, "matrix.max_size", new Object[]{MoreIotasConfig.maxMatrixSize.get(), matrix.getNumRows(), matrix.getNumCols()});
      }
   }

   public SimpleMatrix getMatrix() {
      return this.matrix;
   }

   protected boolean toleratesOther(Iota that) {
      return typesMatch(that, this) && this.matrix.isIdentical(((MatrixIota)that).matrix, 1.0E-4);
   }

   public Component display() {
      MutableComponent out = Component.empty();
      out.append(String.format("(%d, %d)", this.matrix.getNumRows(), this.matrix.getNumCols()));
      if (!MoreIotas.matrixIsEmpty(this.matrix)) {
         out.append(" | ");
      }

      for (int r = 0; r < this.matrix.getNumRows(); r++) {
         for (int c = 0; c < this.matrix.getNumCols(); c++) {
            out.append(Component.literal(String.format("%.2f", this.matrix.get(r, c))).withStyle(ChatFormatting.GREEN));
            if (c < this.matrix.getNumCols() - 1) {
               out.append(", ");
            }
         }

         if (r < this.matrix.getNumRows() - 1) {
            out.append("; ");
         }
      }

      return Component.translatable("hexcasting.tooltip.list_contents", new Object[]{out}).withStyle(ChatFormatting.AQUA);
   }

   public int hashCode() {
      return this.matrix.hashCode();
   }

   public boolean isTruthy() {
      return !MoreIotas.matrixIsEmpty(this.matrix) && this.getMatrix().elementMaxAbs() > 1.0E-4;
   }
}
