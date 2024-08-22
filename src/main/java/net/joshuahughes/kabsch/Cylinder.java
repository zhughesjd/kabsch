package net.joshuahughes.kabsch;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

public class Cylinder extends AbstractParametricDisplay
{
	@Override
	public RealMatrix getModel()
	{
		double r = .5;
		int cnt = 200;
		double diameter = 2*r;
		double length = 2*diameter;
		List<RealVector> modelPtList = new ArrayList<>();
		IntStream.range(0,cnt).forEach(i->
		{
			double z = -length/2   +   length*(double)i/(double)cnt;
			IntStream.range(0,cnt).forEach(j->
			{
				double cScale = (double)j/(double)cnt;
				double t = cScale*2*Math.PI;
				double y = r*Math.cos(t);
				double x = r*Math.sin(t);
				modelPtList.add(new ArrayRealVector(new double[] {x,y,z}));
			});
		});

		return MatrixUtils.createRealMatrix(modelPtList.stream().map(v->v.toArray()).toArray(double[][]::new));
	}
	public static void main(String[] args) 
	{
		new Cylinder().process();
	}
}
