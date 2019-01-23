package funPart;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import matrix.Matrix;
import matrix.Vector;

public class Network implements Serializable {

	private static final long serialVersionUID = -4906457721942253673L;
	
	private static final String PATH_TO_RESOURCES = "res";
	
	/**
	 * Loads name.ser
	 * @param name the name
	 * @return name.ser
	 */
	public static Network deserialize(String name) {
		String path = PATH_TO_RESOURCES + "/" + name + ".ser";
		
		try {
			FileInputStream file = new FileInputStream(path);
			ObjectInputStream in = new ObjectInputStream(file);

			Network p = (Network) in.readObject();

			in.close();
			file.close();

			return p;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Network(String name, double learningRate, ActivationFunction af, int... colLens) {
		this.learningRate = learningRate;
		this.af = af;
		this.name = name;
		
		numCols = colLens.length; // because the last column is inferred from the first, and thus not specified

		unsquasheds = new Matrix[numCols];
		squasheds = new Matrix[numCols];

		weights = new Matrix[colLens.length - 1];

		// populate weights
		for (int i = 0; i < weights.length; i++)
			weights[i] = new Matrix(colLens[i+1], colLens[i]).randomize();
		
	}

	private final String name;
	private final int numCols;
	private ActivationFunction af;
	private final Matrix[] unsquasheds, squasheds;
	private final Matrix[] weights;
	private double learningRate;
	
	// https://bigtheta.io/2016/02/27/the-math-behind-backpropagation.html

	public void giveInfo(double[] info) {
		unsquasheds[0] = squasheds[0] = new Vector(info);

		for (int i = 1; i < numCols; i++) {
			if (Main.doDebug) System.out.println("i = " + i);
			unsquasheds[i] = Matrix.product(weights[i - 1], squasheds[i - 1]);
			squasheds[i] = af.apply(unsquasheds[i]);
		}
	}

	public void giveExpected(double[] info) {
		Matrix expected = new Vector(info);

//		Matrix cost = Matrix.getCost(squasheds[numCols-1], expected);

		Matrix[] grads = new Matrix[numCols-1];
		
		if (Main.doDebug) System.out.println("\nbase case (i = " + (numCols-1) + "):");

		// base case gradient
		grads[grads.length - 1] = Matrix.product(
				Matrix.diff(squasheds[numCols - 1], expected).getTranspose(),
				af.applyP(squasheds[numCols - 1]));
		
		if (Main.doDebug) System.out.println("\niterations:");
		
		// iterative step gradients
		for (int i = grads.length - 2; i > -1; i--) {
			if (Main.doDebug) System.out.print("i = " + i + ":\n");
			grads[i] = Matrix.product(
						grads[i+1],
						Matrix.product(
								weights[i+1],
								af.applyP(unsquasheds[i+1]) 
						));
		}
		
		if (Main.doDebug) System.out.println("\nweights:");
		
		// update weights
		for (int i = 0; i < numCols-1; i++) {
			weights[i] = Matrix.sum(
					weights[i], 
					Matrix.getFunction(Matrix.product(
							squasheds[i],
							grads[i]
						), (x) -> -learningRate * x).getTranspose());
		}
	}

	public double[] getOutput() {
		double[] output = new double[squasheds[numCols - 1].m]; // n is length
		for (int i = 0; i < output.length; i++)
			output[i] = squasheds[numCols - 1].vals[i][0];
		return output;
	}
	
	/**
	 * Stores this network as name.ser
	 * @param name the name
	 */
	public void serializeAs(String name) {
		
		String path = PATH_TO_RESOURCES + "/" + name + ".ser";
		try {
			File f = new File(path);
			f.createNewFile();
			FileOutputStream file = new FileOutputStream(f, false);

			ObjectOutputStream out = new ObjectOutputStream(file);

			out.writeObject(this);

			out.close();
			file.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Store this network as name.ser, where name is {@link #name}.
	 */
	public void serialize() {
		serializeAs(name);
	}
	
	public void setLearningRate(double lr) {
		learningRate = lr;
	}
	
	public void setActivationFunction(ActivationFunction af) {
		this.af = af;
	}

}
