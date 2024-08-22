package net.joshuahughes.kabsch;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

public class Ellipsoid extends AbstractParametricDisplay
{
	@Override
	public RealMatrix getModel()
	{
		
		double a = 4.5;
		double b = 6;
		double c = 3;
		
		int cnt = 200;
		List<RealVector> modelPtList = new ArrayList<>();
		IntStream.range(0,cnt+1).forEach(t->
		{
			IntStream.range(0,cnt).forEach(p->
			{
				double theta = t*Math.PI/cnt;
				double psi = p*2*Math.PI/cnt;
				double x = a*Math.sin(theta)*Math.cos(psi);
				double y = b*Math.sin(theta)*Math.sin(psi);
				double z = c*Math.cos(theta);
				modelPtList.add(new ArrayRealVector(new double[] {x,y,z}));
			});
		});
		return MatrixUtils.createRealMatrix(modelPtList.stream().map(v->v.toArray()).toArray(double[][]::new));
	}
	public static void main(String[] args) 
	{
		new Ellipsoid().process();
	}
}
