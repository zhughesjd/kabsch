package net.joshuahughes.kabsch;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

public class Torus extends AbstractParametricDisplay
{
	@Override
	public RealMatrix getModel()
	{
		double R = 2d;
		double r = 1d/2d;
		int cnt = 500;
		List<RealVector> modelPtList = new ArrayList<RealVector>();
		IntStream.range(0,cnt).forEach( p ->
		{
			double u = 2*Math.PI*(double)p/(double)cnt;
			IntStream.range(0,cnt).forEach(q->
			{
				double t = 2*Math.PI*(double)q/(double)cnt;
				double x = Math.cos(t)*(R + r*Math.cos(u));
				double y = Math.sin(t)*(R + r*Math.cos(u));
				double z = r*Math.sin(u);
				modelPtList.add(new ArrayRealVector(new double[] {x,y,z}));
			});
		});		
		return MatrixUtils.createRealMatrix(modelPtList.stream().map(v->v.toArray()).toArray(double[][]::new));
	}
	public static void main(String[] args) 
	{
		new Torus().process();
	}
}
