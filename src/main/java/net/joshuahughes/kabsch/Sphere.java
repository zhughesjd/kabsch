package net.joshuahughes.kabsch;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

public class Sphere extends AbstractParametricDisplay
{
	@Override
	public RealMatrix getModel()
	{
		int x0 = 10;
		int y0 = 40;
		int z0 = -10;
		double r = 5;
		int cnt = 200;
		List<RealVector> pointList = new ArrayList<>();
		IntStream.range(0,cnt).forEach(i->
		{
			double theta = Math.PI*(double)i/(double)cnt;
			IntStream.range(0,cnt).forEach(j->
			{
				double phi = (2*Math.PI)*(double)(double)j/(double)cnt;
				
				double x = x0 + r*Math.sin(theta)*Math.cos(phi);
				double y = y0 + r*Math.sin(theta)*Math.sin(phi);
				double z = z0 + r*Math.cos(theta);
				
				pointList.add(new ArrayRealVector(new double[] {x,y,z}));
			});
		});
		return new Array2DRowRealMatrix( pointList.stream().map(v->v.toArray()).toArray(double[][]::new));
	}
	public static void main(String[] args) 
	{
		new Sphere().process();
	}
}
