package matrix;
public class Vector extends Matrix {

	public Vector(int n) {
		super(n, 1);
	}
	
	public Vector(double[] vals) {
		this(vals.length);
		for (int i = 0; i < vals.length; i++) {
			this.vals[i][0] = vals[i];
		}
	}
	
}
