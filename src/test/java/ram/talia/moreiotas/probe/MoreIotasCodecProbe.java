package ram.talia.moreiotas.probe;

import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import org.ejml.simple.SimpleMatrix;
import ram.talia.moreiotas.api.MoreIotasCodecs;

/** Regression probe for matrix dimensions that the legacy NBT serializer preserved. */
public final class MoreIotasCodecProbe {
    private MoreIotasCodecProbe() {
    }

    public static void main(String[] args) {
        verifyRoundTrip(new SimpleMatrix(new double[][] {
            {1.25, -2.5, 3.75},
            {4.0, 5.5, -6.25}
        }));
        verifyRoundTrip(new SimpleMatrix(0, 0));
        System.out.println("[MOREIOTAS-PROBE] matrix_codec=PASS dense=2x3 empty=0x0");
    }

    private static void verifyRoundTrip(SimpleMatrix original) {
        JsonElement encoded = MoreIotasCodecs.SIMPLEMATRIX
            .encodeStart(JsonOps.INSTANCE, original)
            .getOrThrow();
        SimpleMatrix decoded = MoreIotasCodecs.SIMPLEMATRIX
            .parse(JsonOps.INSTANCE, encoded)
            .getOrThrow();

        if (decoded.numRows() != original.numRows() || decoded.numCols() != original.numCols()) {
            throw new AssertionError(
                "Matrix dimensions changed: expected "
                    + original.numRows() + "x" + original.numCols()
                    + ", got " + decoded.numRows() + "x" + decoded.numCols()
                    + ", encoded=" + encoded
            );
        }
        if (original.getNumElements() > 0 && !decoded.isIdentical(original, 0.0)) {
            throw new AssertionError("Matrix values changed during codec round-trip: " + encoded);
        }
    }

}
