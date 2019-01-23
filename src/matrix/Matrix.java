package matrix;

import java.io.Serializable;

import funPart.ActivationFunction;
import funPart.Main;

public class Matrix implements Serializable {
	private static final long serialVersionUID = 5492669479511724288L;

	public static Matrix sum(Matrix a, Matrix b) {
		
		if (Main.doDebug) System.out.printf("%dx%d + %dx%d%n", a.m, a.n, b.m, b.n);
		assertValidDim(a.m, b.m);
		assertValidDim(a.n, b.n);
		
		Matrix sum = new Matrix(a.m, a.n);
		
		for (int i = 0; i < a.m; i++)
			for (int j = 0; j < a.n; j++)
				sum.vals[i][j] = a.vals[i][j] + b.vals[i][j];
		
		return sum;
	}
	
	public static Matrix diff(Matrix a, Matrix b) {
		if (Main.doDebug) System.out.printf("%dx%d - %dx%d%n", a.m, a.n, b.m, b.n);
		assertValidDim(a.m, b.m);
		assertValidDim(a.n, b.n);
		
		Matrix sum = new Matrix(a.m, a.n);
		
		for (int i = 0; i < a.m; i++)
			for (int j = 0; j < a.n; j++)
				sum.vals[i][j] = a.vals[i][j] - b.vals[i][j];
		
		return sum;
	}
	
	public static Matrix product(Matrix a, Matrix b) {
		int m = a.m, n = b.n;
		Matrix product = new Matrix(m, n);

		if (Main.doDebug) System.out.printf("%dx%d * %dx%d%n", a.m, a.n, b.m, b.n);
		
		assertValidDim(a.n, b.m);
		
		for (int i = 0; i < m; i++)
			for (int j = 0; j < n; j++)
				for (int k = 0; k < a.n; k++)
					product.vals[i][j] 
							+= a.vals[i][k] 
									* b.vals[k][j];
		
//		System.out.println(product instanceof Matrix);
		return product;
	}
	
//	public static Matrix pointwiseProduct(Matrix a, Matrix b) {
//		assertValidDim(a.m, b.m);
//		assertValidDim(a.n, b.n);
//		
//		System.out.printf("%dx%d .* %dx%d%n", a.m, a.n, b.m, b.n);
//		
//		Matrix pp = new Matrix(a.m, a.n);
//		
//		for (int i = 0; i < a.m; i++)
//			for (int j = 0; j < a.n; j++)
//				pp.vals[i][j] = a.vals[i][j] * b.vals[i][j];
//		
//		return pp;
//	}
	
	public static Matrix getFunction(Matrix a, ActivationFunction.Function f) {
		Matrix ans = new Matrix(a.m, a.n);

		for (int i = 0; i < a.m; i++)
			for (int j = 0; j < a.n; j++)
				ans.vals[i][j] = f.apply(a.vals[i][j]);

		return ans;
	}
	
//	public static Matrix getCost(Matrix actual, Matrix expected) {
//		Matrix cost = new Matrix(actual.m, actual.n);
//		for (int i = 0; i < cost.m; i++)
//			for (int j = 0; j < cost.n; j++)
//				cost.vals[i][j] = .5 * sq(actual.vals[i][j] - expected.vals[i][j]);
//		
//		return cost;
//	}
	
	
	public Matrix(int m, int n) {
		vals = new double[m][n];
		this.m = m;
		this.n = n;
	}
	
	public final int m, n;
	public final double[][] vals;
	
	public Matrix randomize() {
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				vals[i][j] = Math.random();
			}
		}
		return this;
	}
	
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				sb.append(vals[i][j] + " ");
			}
			sb.append("\n");
		}
		return sb.toString();
	}
	
	public Matrix getTranspose() {
		Matrix t = new Matrix(n, m);
		for (int i = 0; i < m; i++)
			for (int j = 0; j < n; j++) 
				t.vals[j][i] = vals[i][j];
		return t;
	}
	

	private static void assertValidDim(int m1, int m2) {
		if (m1 != m2)
			throw new RuntimeException("dimension mismatch: " + m1 + " != " + m2);
	}
}
