package net.joshuahughes.kabsch;

import java.util.stream.IntStream;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.stat.descriptive.moment.VectorialMean;
import org.apache.commons.math3.util.Pair;


//************************************************
//************************************************
//************************************************
// https://en.wikipedia.org/wiki/Kabsch_algorithm
//************************************************
//************************************************
//************************************************

public class Algorithm
{
	private RealVector centroidq;
	private RealVector centroidp;
	private RealMatrix P;
	private RealMatrix Q;
	private RealMatrix H;
	private SingularValueDecomposition svd;
	private double d;
	private RealMatrix projectedQ;
	private RealMatrix rotationMatrix;

	public Algorithm(RealMatrix p, RealMatrix q)
	
	{
		int featureCnt = q.getColumnDimension();
		Pair<RealMatrix, RealVector> pResult = getMeanResult(p);
		Pair<RealMatrix, RealVector> qResult = getMeanResult(q);
		centroidp = pResult.getSecond();
		centroidq = qResult.getSecond();
		P = pResult.getFirst();
		Q = qResult.getFirst();
		H = Q.transpose().multiply(P);
        svd = new SingularValueDecomposition(H);
        RealMatrix U = svd.getU();
        RealMatrix Vt = svd.getVT();
        d = new LUDecomposition(U.multiply(Vt)).getDeterminant();
		RealMatrix dMatrix = MatrixUtils.createRealIdentityMatrix( featureCnt );
		dMatrix.setEntry(featureCnt-1, featureCnt-1, d);
		rotationMatrix= U.multiply(dMatrix).multiply(Vt);
		projectedQ = Q.multiply(rotationMatrix);
	}
	public RealVector getCentroidq()
	{
		return centroidq;
	}
	public RealVector getCentroidp()
	{
		return centroidp;
	}
	public RealMatrix getP()
	{
		return P;
	}
	public RealMatrix getQ()
	{
		return Q;
	}
	public RealMatrix getH()
	{
		return H;
	}
	public SingularValueDecomposition getSvd()
	{
		return svd;
	}
	public double getD()
	{
		return d;
	}
	public RealMatrix getProjectedQ()
	{
		return projectedQ;
	}
	public RealMatrix getRotationMatrix()
	{
		return rotationMatrix;
	}
	public static Pair<RealMatrix,RealVector> getMeanResult(RealMatrix matrix)
	{
		VectorialMean vectorMean = new VectorialMean(matrix.getColumnDimension());
		IntStream.range(0, matrix.getRowDimension()).forEach(i->vectorMean.increment(matrix.getRow(i)));
		double[] mean = vectorMean.getResult();
		RealMatrix originCenteredResult = matrix.subtract(createMatrix(mean, matrix.getRowDimension()));
		return new Pair<RealMatrix,RealVector>(originCenteredResult,new ArrayRealVector(mean));
	
	}
	private static RealMatrix createMatrix(double[] row, int columnCnt)
	{
		return MatrixUtils.createRealMatrix(IntStream.range(0, columnCnt).mapToObj(i->row).toArray(double[][]::new));
	}
}
