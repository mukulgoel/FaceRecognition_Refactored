package support;

import imagecomputation.JPGFile;
import imagecomputation.PPMFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import eigencomputation.EigenFaceComputation;
import eigencomputation.EigenFile;
import eigencomputation.ImageFilter;

public class FaceBundleUtility {
	protected static final String PNM_EXTENSION = "pnm";
	protected static final String PPM_EXTENSION = "ppm";
	protected static final String JPEG_EXTENSION = "jpeg";
	protected static final String JPG_EXTENSION = "jpg";
	protected static final int MAGIC_SETNR = 16;
	public File root_dir;
	public FaceBundle[] faceBundle = null;
	public int USE_CACHE = 1;

	public FaceBundleUtility() {
		super();
	}

	protected void saveBundle(File f, FaceBundle bundle)
			throws FileNotFoundException, IOException {
		f.createNewFile();
		FileOutputStream out = new FileOutputStream(f.getAbsolutePath());
		ObjectOutputStream fos = new ObjectOutputStream(out);
		fos.writeObject(bundle);
		fos.close();
		System.out.println("saved bundle ... ");
		f.getAbsolutePath();
	}

	protected FaceBundle readBundle(File f) throws FileNotFoundException,
			IOException, ClassNotFoundException {

		FileInputStream in = new FileInputStream(f);
		ObjectInputStream fo = new ObjectInputStream(in);
		FaceBundle bundle = (FaceBundle) fo.readObject();
		fo.close();
		System.out.println("read cached bundle..");

		return bundle;
	}

	protected FaceBundle computeBundle(String dir, String[] id)
			throws IllegalArgumentException, FileNotFoundException, IOException {

		EigenFile[] files = new EigenFile[MAGIC_SETNR];
		EigenFile file = null;
		String temp = null;
		int width = 0;
		int height = 0;
		int i = 0;

		for (i = 0; i < files.length; i++) {
			temp = id[i].toLowerCase();
			temp = temp.substring(temp.lastIndexOf('.') + 1, temp.length());
			if (temp.equals(JPG_EXTENSION) || temp.equals(JPEG_EXTENSION))
				file = new JPGFile(dir + id[i]);
			else if (temp.equals(PPM_EXTENSION) || temp.equals(PNM_EXTENSION))
				file = new PPMFile(dir + id[i]);
			if (file == null)
				throw new IllegalArgumentException(id[i]
						+ " is not an image file!");

			files[i] = file;

			if (i == 0) {
				width = files[i].getWidth();
				height = files[i].getHeight();
			}
			if ((width != files[i].getWidth())
					|| (height != files[i].getHeight()))
				throw new IllegalArgumentException(
						"All image files must have the same width and height!");
		}

		// Then construct our big double[][] array - MxN^2

		double[][] face_v = new double[MAGIC_SETNR][width * height];
		System.out.println("Generating bundle of (" + face_v.length + " x "
				+ face_v[0].length + "), h:" + height + " w:" + width);

		for (i = 0; i < files.length; i++) {
			System.arraycopy(files[i].getDouble(), 0, face_v[i], 0,
					face_v[i].length);
			face_v[i] = files[i].getDouble();
		}

		// the computation!
		return EigenFaceComputation.submit(face_v, width, height, id);

	}

	public void readFaceBundles(String n) throws FileNotFoundException,
			IOException, IllegalArgumentException, ClassNotFoundException {

		root_dir = new File(n);

		File[] files = root_dir.listFiles(new ImageFilter());
		Vector<String> filenames = new Vector<String>();

		String[] set = new String[MAGIC_SETNR];

		int i = 0;

		// Sort the list of filenames.
		for (i = 0; i < files.length; i++) {
			filenames.addElement(files[i].getName());
		}
		Collections.sort((List<String>) filenames);

		faceBundle = new FaceBundle[(files.length / MAGIC_SETNR) + 1];

		for (i = 0; i < faceBundle.length; i++) {
			for (int j = 0; j < MAGIC_SETNR; j++) {
				if (filenames.size() > j + MAGIC_SETNR * i) {
					set[j] = (String) filenames.get(j + MAGIC_SETNR * i);
					System.out.println(" - " + set[j]);
				}
			}
			faceBundle[i] = submitSet(root_dir.getAbsolutePath() + "/", set);

		}
	}

	public FaceBundle submitSet(String dir, String[] files)
			throws FileNotFoundException, IOException,
			IllegalArgumentException, ClassNotFoundException {

		if (files.length != MAGIC_SETNR)
			throw new IllegalArgumentException("Can only accept a set of "
					+ MAGIC_SETNR + " files.");

		FaceBundle bundle = null;
		int i = 0;
		String name = "cache";
		for (i = 0; i < files.length; i++) {
			name = name + files[i].substring(0, files[i].indexOf('.'));
		}
		File f = new File(dir + name + ".cache");
		if (f.exists() && (USE_CACHE > 0)) /* it's cached */
			bundle = readBundle(f);
		else {

			bundle = computeBundle(dir, files);
			if (USE_CACHE > 0)
				saveBundle(f, bundle);
		}

		return bundle;
	}

}