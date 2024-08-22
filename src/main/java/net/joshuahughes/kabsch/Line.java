package net.joshuahughes.kabsch;

import java.util.stream.IntStream;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

public class Line extends AbstractParametricDisplay
{
	@Override
	public RealMatrix getModel()
	{
		RealVector v0 = new ArrayRealVector(new double[] {10,40,-20});
		RealVector v1 = new ArrayRealVector(new double[] {30,-10,-20});
		RealVector angle = v1.subtract(v0);
		int cnt = 1000;
		RealMatrix model = new Array2DRowRealMatrix(cnt,v0.getDimension());
		IntStream.range(0,cnt).forEach(i->
		{
			double scale = (double )i/(double) cnt;
			model.setRowVector(i, v0.add(angle.mapMultiply(scale)));
		});
		return model;
	}
	public static void main(String[] args) 
	{
		new Line().process();
	}
}
