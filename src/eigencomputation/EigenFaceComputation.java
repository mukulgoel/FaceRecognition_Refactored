package eigencomputation;

import support.BasicMatrixUtilities;
import support.FaceBundle;
import imagecomputation.ImageFileViewer;
import Jama.*;

public class EigenFaceComputation extends BasicMatrixUtilities {

	private final static int MAGIC_NR = 11;
	private static int faceCounter;
	private static int faceCounterInverse;
	private static int col;
	private static int rows;
	private static int pix;
	private static int image;
	private static int k;

	public static FaceBundle submit(double[][] face_v, int width, int height,
			String[] id) {

		int lengthOfEigenMatrix = width * height;
		int numberOfFaces = face_v.length;
		double[] avgF = new double[lengthOfEigenMatrix];
		double[][] faces = new double[numberOfFaces][lengthOfEigenMatrix];
		double[][] z, eigVector, tempVector, wk;
		double[] eigValue;
		int[] tempV, index;

		k = 0;

		ImageFileViewer simple = new ImageFileViewer();
		simple.setImage(face_v[0], width, height);
		calculateAverageFaces(face_v, lengthOfEigenMatrix, numberOfFaces, avgF);
		simple.setImage(avgF, width, height);
		calculateDistanceMatrix(face_v, lengthOfEigenMatrix, numberOfFaces,
				avgF);

		System.arraycopy(face_v, 0, faces, 0, face_v.length);

		simple.setImage(face_v[0], width, height);
		Matrix faceM = new Matrix(face_v, numberOfFaces, lengthOfEigenMatrix);
		Matrix faceM_transpose = faceM.transpose();

		Matrix covarM = faceM.times(faceM_transpose);
		z = covarM.getArray();

		System.out.println("Covariance matrix is " + z.length + " x "
				+ z[0].length); // printing size of covarinace matrix

		EigenvalueDecomposition E = covarM.eig();
		eigValue = getDiagonalMatrix(E.getD().getArray());
		eigVector = E.getV().getArray();

		printEigenValues(eigValue);
		printEigenVectors(eigVector);

		index = new int[numberOfFaces];
		tempVector = new double[numberOfFaces][numberOfFaces]; /*
		 * Temporary new
		 * eigVector
		 */

		// Face Counter matrix
		for (faceCounter = 0; faceCounter < numberOfFaces; faceCounter++) {
			index[faceCounter] = faceCounter;
		}

		doubleQuickSort(eigValue, index, 0, numberOfFaces - 1);

		// Put the index in inverse
		tempV = new int[numberOfFaces];
		for (faceCounterInverse = 0; faceCounterInverse < numberOfFaces; faceCounterInverse++) {
			tempV[numberOfFaces - 1 - faceCounterInverse] = index[faceCounterInverse];
		}

		index = tempV;

		/*
		 * Put the sorted eigenvalues in the appropiate columns.
		 */

		eigVector = computeEigenMatrix(numberOfFaces, eigVector, index,
				tempVector);
		tempVector = null;
		eigValue = null;

		Matrix eigVectorM = new Matrix(eigVector, numberOfFaces, numberOfFaces);
		eigVector = eigVectorM.times(faceM).getArray();

		computerEigenVector(numberOfFaces, eigVector);

		wk = new double[numberOfFaces][MAGIC_NR]; // M rows, 11 columns

		computerWeightVector(lengthOfEigenMatrix, numberOfFaces, faces,
				eigVector, wk);

		System.out.println("\nface space"); // displaying eigen vectors
		k = computeFaceSpace(k, wk);

		FaceBundle newFaceBundle = new FaceBundle(avgF, wk, eigVector, id);
		return newFaceBundle;
	}

	private static int computeFaceSpace(int k, double[][] wk) {
		for (faceCounter = 0; faceCounter < wk.length; faceCounter++) {
			System.out.println("\n");
			for (k = 0; k < wk[faceCounter].length; k++)
				System.out.print(wk[faceCounter][k] + " ");

		}
		return k;
	}

	private static void computerWeightVector(int lengthOfEigenMatrix,
			int numberOfFaces, double[][] faces, double[][] eigVector,
			double[][] wk) {

		double temp;
		for (image = 0; image < numberOfFaces; image++) {
			for (faceCounterInverse = 0; faceCounterInverse < MAGIC_NR; faceCounterInverse++) {
				temp = 0.0;
				for (pix = 0; pix < lengthOfEigenMatrix; pix++)
					temp += eigVector[faceCounterInverse][pix]
							* faces[image][pix];
				wk[image][faceCounterInverse] = Math.abs(temp);
			}
		}
	}

	private static void computerEigenVector(int numberOfFaces,
			double[][] eigVector) {
		double temp;
		for (image = 0; image < numberOfFaces; image++) {
			temp = max(eigVector[image]); // Our max
			for (pix = 0; pix < eigVector[0].length; pix++)
				// Normalize
				eigVector[image][pix] = Math.abs(eigVector[image][pix] / temp);
		}
	}

	private static double[][] computeEigenMatrix(int numberOfFaces,
			double[][] eigVector, int[] index, double[][] tempVector) {
		k = 0;
		for (col = numberOfFaces - 1; col >= 0; col--) {
			for (rows = 0; rows < numberOfFaces; rows++) {
				tempVector[rows][col] = eigVector[rows][index[col]];
			}
		}
		eigVector = tempVector;
		System.out.println("new sorted eigen vectors"); // displaying eigen
		// vectors
		k = computeFaceSpace(k, eigVector);
		return eigVector;
	}

	private static void calculateDistanceMatrix(double[][] face_v,
			int lengthOfEigenMatrix, int numberOfFaces, double[] avgF) {
		for (image = 0; image < numberOfFaces; image++) {

			for (pix = 0; pix < lengthOfEigenMatrix; pix++) {
				face_v[image][pix] = face_v[image][pix] - avgF[pix];
			}
		}
	}

	private static void calculateAverageFaces(double[][] face_v,
			int lengthOfEigenMatrix, int numberOfFaces, double[] avgF) {

		double temp;
		for (pix = 0; pix < lengthOfEigenMatrix; pix++) {
			temp = 0;
			for (image = 0; image < numberOfFaces; image++) {
				temp += face_v[image][pix];
			}
			avgF[pix] = temp / numberOfFaces;
		}
	}

	private static void printEigenVectors(double[][] eigVector) {
		int i;
		int k;
		System.out.println("eigen vectors"); // displaying eigen vectors
		for (i = 0; i < eigVector.length; i++) {
			System.out.println("\n");
			for (k = 0; k < eigVector[i].length; k++)
				System.out.print(eigVector[i][k] + " ");

		}
	}

	private static void printEigenValues(double[] eigValue) {
		int i;
		System.out.println("eigen values");
		for (i = 0; i < eigValue.length; i++)
			// displaying eigen values
			System.out.println(eigValue[i]);
	}

	static void doubleQuickSort(double a[], int index[], int lo0, int hi0) {
		int lo = lo0;
		int hi = hi0;
		double mid;

		if (hi0 > lo0) {
			mid = a[(lo0 + hi0) / 2];
			while (lo <= hi) {
				while ((lo < hi0) && (a[lo] < mid)) {
					++lo;
				}
				while ((hi > lo0) && (a[hi] > mid)) {
					--hi;
				}
				if (lo <= hi) {
					swap(a, index, lo, hi);
					++lo;
					--hi;
				}
			}
			if (lo0 < hi) {
				doubleQuickSort(a, index, lo0, hi);
			}

			if (lo < hi0) {
				doubleQuickSort(a, index, lo, hi0);
			}
		}
	}

	static private void swap(double a[], int[] index, int i, int j) {
		double T;
		T = a[i];
		a[i] = a[j];
		a[j] = T;
		// Index
		index[i] = i;
		index[j] = j;
	}
}
