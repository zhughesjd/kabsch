package net.joshuahughes.kabsch;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

public class Helix extends AbstractParametricDisplay
{
	@Override
	public RealMatrix getModel()
	{
		// https://en.wikipedia.org/wiki/Parametric_equation#Parametric_surfaces
		//********** create template ********
		double a = 2;
		double b = 3;
		int cnt = 500;
		List<RealVector> modelPtList = new ArrayList<RealVector>();
		IntStream.range(0,cnt).forEach( ndx ->
		{
				double t = 2*Math.PI*(double)ndx/(double)cnt;

				double x = a*Math.cos(t);
				double y = a*Math.sin(t);
				double z = b*t;
				modelPtList.add(new ArrayRealVector(new double[] {x,y,z}));
		});		
		return MatrixUtils.createRealMatrix(modelPtList.stream().map(v->v.toArray()).toArray(double[][]::new));
	}
	public static void main(String[] args) 
	{
		new Helix().process();
	}
}
