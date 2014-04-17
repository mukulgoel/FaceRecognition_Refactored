package eigencomputation;

import imagecomputation.JPGFile;
import imagecomputation.PPMFile;

import java.io.*;
import support.FaceBundleUtility;

public class EigenFaceCreator extends FaceBundleUtility {

	public static double THRESHOLD = 3.0;
	public double DISTANCE = Double.MAX_VALUE;

	public String checkAgainst(String imageName) throws FileNotFoundException,
	IOException {
		String id = null;
		if (faceBundle != null) {
			double small = Double.MAX_VALUE;
			int idx = -1;
			double[] imageArray = readImage(imageName);
			for (int i = 0; i < faceBundle.length; i++) {
				faceBundle[i].submitFace(imageArray);
				if (small > faceBundle[i].calculateEigenDistance()) {
					small = faceBundle[i].calculateEigenDistance();
					idx = i;
				}
			}
			DISTANCE = small;
			if (small < THRESHOLD)
				id = faceBundle[idx].getID();
		}
		return id;
	}

	public double[] readImage(String imageName) throws FileNotFoundException,
	IllegalArgumentException, IOException {

		EigenFile file = null;
		String temp = imageName.toLowerCase();
		temp = temp.substring(temp.lastIndexOf('.') + 1, temp.length());

		if (temp.equals(JPG_EXTENSION))
			file = new JPGFile(imageName);
		else if (temp.equals(PPM_EXTENSION) || temp.equals(PNM_EXTENSION))
			file = new PPMFile(imageName);
		if (file == null)
			throw new IllegalArgumentException(imageName
					+ " is not an image file!");
		return file.getDouble();
	}

}
