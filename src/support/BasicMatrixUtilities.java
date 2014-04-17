package support;

public class BasicMatrixUtilities {

	protected static double[] getDiagonalMatrix(double[][] eigenMatrix) {

		double[] d = new double[eigenMatrix.length];
		for (int i = 0; i < eigenMatrix.length; i++)
			d[i] = eigenMatrix[i][i];
		return d;
	}

	static void divide(double[] vectorMatrix, double b) {

		for (int i = 0; i < vectorMatrix.length; i++)
			vectorMatrix[i] = vectorMatrix[i] / b;

	}

	static double sum(double[] a) {
		double b = a[0];
		for (int i = 0; i < a.length; i++)
			b += a[i];

		return b;
	}

	protected static double max(double[] a) {
		double b = a[0];
		for (int i = 0; i < a.length; i++)
			if (a[i] > b)
				b = a[i];

		return b;
	}

	public BasicMatrixUtilities() {
		super();
	}

}