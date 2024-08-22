package net.joshuahughes.kabsch;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.RotationConvention;
import org.apache.commons.math3.geometry.euclidean.threed.RotationOrder;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.jzy3d.chart.Chart;
import org.jzy3d.chart.factories.AWTChartFactory;
import org.jzy3d.colors.Color;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.primitives.Scatter;
import org.jzy3d.plot3d.rendering.canvas.Quality;
public abstract class AbstractParametricDisplay
{
	public static Random rnd = new Random(314159);

	public abstract RealMatrix getModel();
	public RealMatrix getData()
	{
		RealMatrix model= getModel();
		List<RealVector> modelPtList = IntStream.range(0, model.getRowDimension()).mapToObj(i->new ArrayRealVector(model.getRow(i))).collect(Collectors.toList());
		int featureCnt = modelPtList.get(0).getDimension();
		RealMatrix template = MatrixUtils.createRealMatrix(modelPtList.stream().map(v->v.toArray()).toArray(double[][]::new));

		//********** data with rotation and offset ********
		Rotation rotation = new Rotation(RotationOrder.XYZ,RotationConvention.VECTOR_OPERATOR, Math.PI/3d, Math.PI/4d, Math.PI/5d);
		RealMatrix translation = createMatrix(new double[] {1,2,3},modelPtList.size());
		RealMatrix q = new Array2DRowRealMatrix(template.getRowDimension(),template.getColumnDimension());
		IntStream.range(0, template.getRowDimension()).forEach(i->
		{
			double[] rotPnt = new double[q.getColumnDimension()]; 
			rotation.applyTo(template.getRow(i), rotPnt);
			RealVector rotTransPnt = new ArrayRealVector(rotPnt).add(translation.getRowVector(0));
			q.setRowVector(i, rotTransPnt.add(new ArrayRealVector(rnd.doubles(featureCnt).map(d->d*.2d).toArray())));
		});
		return q;
	}
	public void process() 
	{process(getModel(), getData());}
	public void process(RealMatrix model,RealMatrix data)
	{
		Algorithm algorithm = new Algorithm(model, data);

		Chart chart = new AWTChartFactory().newChart(Quality.Advanced()).black();
		chart.addMouse();
		chart.add(new Scatter(convert(algorithm.getP().getData()),Color.GREEN));
		chart.add(new Scatter(convert(data.getData()),Color.WHITE));
		chart.add(new Scatter(convert(algorithm.getProjectedQ().getData()),Color.RED));

		JFrame frame = new JFrame();
		JPanel pnl = new JPanel(new BorderLayout());
		pnl.add((Component)chart.getCanvas(),BorderLayout.CENTER);
		frame.setContentPane(pnl);
		frame.setSize(500,500);
		frame.setVisible(true);
		//********** compare calculated mean to offset and create origin centered points 
		System.out.println("***********************************************");
		System.out.println("*******      true vs computed mean      *******");
		System.out.println("***********************************************");
		//		System.out.println(Arrays.toString(translation.getRow(0)));
		System.out.println(Arrays.toString(algorithm.getCentroidp().toArray()));
		System.out.println();
		System.out.println();
		System.out.println("***********************************************");
		System.out.println("*******     true vs computed angles     *******");
		System.out.println("***********************************************");
		//	System.out.println(Arrays.toString(rotation.getAngles(RotationOrder.XYZ, RotationConvention.VECTOR_OPERATOR)));
		System.out.println(Arrays.toString(new Rotation(algorithm.getRotationMatrix().getData(),.01).getAngles(RotationOrder.XYZ, RotationConvention.VECTOR_OPERATOR)));

	}
	private static RealMatrix createMatrix(double[] row, int columnCnt)
	{
		return MatrixUtils.createRealMatrix(IntStream.range(0, columnCnt).mapToObj(i->row).toArray(double[][]::new));
	}

	public static List<Coord3d> convert(double[][] ds)
	{
		return Arrays.stream(ds).map(a->new Coord3d(a)).collect(Collectors.toList());
	}
}
