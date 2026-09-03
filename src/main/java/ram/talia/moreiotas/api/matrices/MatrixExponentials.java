package ram.talia.moreiotas.api.matrices;

import org.ejml.simple.SimpleMatrix;

public class MatrixExponentials {
   public static SimpleMatrix matrixExponential(SimpleMatrix A) {
      double c0 = 1.0;
      double c1 = 0.5;
      double c2 = 0.12;
      double c3 = 0.01833333333333333;
      double c4 = 0.0019927536231884053;
      double c5 = 1.630434782608695E-4;
      double c6 = 1.0351966873706E-5;
      double c7 = 5.175983436853E-7;
      double c8 = 2.0431513566525E-8;
      double c9 = 6.306022705717593E-10;
      double c10 = 1.4837700484041396E-11;
      double c11 = 2.5291534915979653E-13;
      double c12 = 2.8101705462199615E-15;
      double c13 = 1.5440497506703084E-17;
      int j = Math.max(0, 1 + (int)Math.floor(Math.log(A.elementMaxAbs()) / Math.log(2.0)));
      SimpleMatrix As = (SimpleMatrix)A.scale(1.0 / Math.pow(2.0, j));
      int n = A.getNumRows();
      SimpleMatrix As_2 = (SimpleMatrix)As.mult(As);
      SimpleMatrix As_4 = (SimpleMatrix)As_2.mult(As_2);
      SimpleMatrix As_6 = (SimpleMatrix)As_4.mult(As_2);
      SimpleMatrix U = (SimpleMatrix)((SimpleMatrix)((SimpleMatrix)((SimpleMatrix)SimpleMatrix.identity(n).scale(1.0)).plus(As_2.scale(0.12)))
            .plus(As_4.scale(0.0019927536231884053)))
         .plus(
            ((SimpleMatrix)((SimpleMatrix)((SimpleMatrix)((SimpleMatrix)SimpleMatrix.identity(n).scale(1.0351966873706E-5))
                        .plus(As_2.scale(2.0431513566525E-8)))
                     .plus(As_4.scale(1.4837700484041396E-11)))
                  .plus(As_6.scale(2.8101705462199615E-15)))
               .mult(As_6)
         );
      SimpleMatrix V = (SimpleMatrix)((SimpleMatrix)((SimpleMatrix)((SimpleMatrix)SimpleMatrix.identity(n).scale(0.5)).plus(As_2.scale(0.01833333333333333)))
            .plus(As_4.scale(1.630434782608695E-4)))
         .plus(
            ((SimpleMatrix)((SimpleMatrix)((SimpleMatrix)((SimpleMatrix)SimpleMatrix.identity(n).scale(5.175983436853E-7))
                        .plus(As_2.scale(6.306022705717593E-10)))
                     .plus(As_4.scale(2.5291534915979653E-13)))
                  .plus(As_6.scale(1.5440497506703084E-17)))
               .mult(As_6)
         );
      SimpleMatrix AV = (SimpleMatrix)As.mult(V);
      SimpleMatrix N = (SimpleMatrix)U.plus(AV);
      SimpleMatrix D = (SimpleMatrix)U.minus(AV);
      SimpleMatrix F = (SimpleMatrix)D.solve(N);

      for (int k = 0; k < j; k++) {
         F = (SimpleMatrix)F.mult(F);
      }

      return F;
   }
}
