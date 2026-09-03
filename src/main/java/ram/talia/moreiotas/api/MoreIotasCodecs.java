package ram.talia.moreiotas.api;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.network.codec.StreamCodec;
import org.ejml.simple.SimpleMatrix;

public class MoreIotasCodecs {
   public static final Codec<SimpleMatrix> SIMPLEMATRIX = Codec.DOUBLE.listOf().listOf().xmap(list -> {
      int rows = list.size();
      if (rows == 0) {
         return new SimpleMatrix(0, 0);
      }
      int cols = ((List)list.get(0)).size();
      SimpleMatrix matrix = SimpleMatrix.filled(rows, cols, 0.0);
      if (rows * cols != 0) {
         for (int i = 0; i < rows; i++) {
            List<Double> currentRow = (List<Double>)list.get(i);

            for (int j = 0; j < cols; j++) {
               matrix.set(i, j, currentRow.get(j));
            }
         }
      }

      return matrix;
   }, matrix -> {
      int rows = matrix.getNumRows();
      int cols = matrix.getNumCols();
      List<List<Double>> list = new ArrayList<>();
      if (rows * cols != 0) {
         for (int i = 0; i < rows; i++) {
            List<Double> currentRow = new ArrayList<>();

            for (int j = 0; j < cols; j++) {
               currentRow.add(matrix.get(i, j));
            }

            list.add(currentRow);
         }
      }

      return list;
   });
   public static final StreamCodec<ByteBuf, SimpleMatrix> SIMPLEMATRIX_STREAM = new StreamCodec<ByteBuf, SimpleMatrix>() {
      public SimpleMatrix decode(ByteBuf buffer) {
         int rows = buffer.readInt();
         int cols = buffer.readInt();
         SimpleMatrix matrix = SimpleMatrix.filled(rows, cols, 0.0);

         for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
               matrix.set(i, j, buffer.readDouble());
            }
         }

         return matrix;
      }

      public void encode(ByteBuf buffer, SimpleMatrix matrix) {
         buffer.writeInt(matrix.getNumRows());
         buffer.writeInt(matrix.getNumCols());
         int rows = matrix.getNumRows();
         int cols = matrix.getNumCols();
         double[][] data = matrix.toArray2();

         for (int i = 0; i < rows; i++) {
            double[] currentRow = data[i];

            for (int j = 0; j < cols; j++) {
               buffer.writeDouble(currentRow[j]);
            }
         }
      }
   };
}
