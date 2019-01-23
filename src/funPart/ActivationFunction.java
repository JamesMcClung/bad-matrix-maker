package funPart;
import java.io.Serializable;

import matrix.Matrix;

public class ActivationFunction implements Serializable {
	private static final long serialVersionUID = -525451769080455189L;

	public static final ActivationFunction sigmoid = new ActivationFunction((x) -> (1 + Math.exp(-x)), (x) -> {
		double t = Math.exp(-x);
		return t / sq(1 + t);
	});

	public static final ActivationFunction tanh = new ActivationFunction(Math::tanh, (x) -> 1 - sq(Math.tanh(x)));

	public ActivationFunction(Function af, Function afp) {
		this.af = af;
		this.afp = afp;
	}

	private final Function af, afp;

	public double apply(double x) {
		return af.apply(x);
	}

	public Matrix apply(Matrix a) {
		return Matrix.getFunction(a, af);
	}

	public double applyP(double x) {
		return afp.apply(x);
	}

	public Matrix applyP(Matrix a) {
		if (a.n == 1) {
			Matrix ans = new Matrix(a.m, a.m);
			for (int i = 0; i < a.m; i++)
				ans.vals[i][i] = afp.apply(a.vals[i][0]);
			return ans;
		}
		return Matrix.getFunction(a, afp);
	}

	// http://cs231n.github.io/neural-networks-1/#actfun

	private static double sq(double x) {
		return x * x;
	}
	
	@FunctionalInterface
	public interface Function extends Serializable {
		double apply(double x);
	}

}
