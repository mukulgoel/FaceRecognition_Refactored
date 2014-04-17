package imagecomputation;

import java.io.*;

import com.sun.image.codec.jpeg.*;

import eigencomputation.EigenFile;

import java.awt.image.*;

public class JPGFile implements EigenFile {

	private byte bytes[] = null; // bytes which make up binary PPM image
	private double doubles[] = null;
	private String filename = null; // filename for PPM image
	private int height = 0;
	private int width = 0;

	public JPGFile(String filename) throws FileNotFoundException, IOException {
		this.filename = filename;
		readImage();
	}

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}

	public byte[] getBytes() {
		return bytes;
	}

	public double[] getDouble() {
		return doubles;
	}

	public static void writeImage(String newFileName, byte[] rawData,
			int width, int height) throws FileNotFoundException, IOException {

		FileOutputStream fOut = new FileOutputStream(newFileName);
		JPEGImageEncoder jpeg_encode = JPEGCodec.createJPEGEncoder(fOut);

		int ints[] = new int[rawData.length];
		for (int i = 0; i < rawData.length; i++)
			ints[i] = 255 << 24 | (int) (rawData[i] & 0xff) << 16
					| (int) (rawData[i] & 0xff) << 8
					| (int) (rawData[i] & 0xff);

		BufferedImage image = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		image.setRGB(0, 0, width, height, ints, 0, width);

		jpeg_encode.encode(image);
		fOut.close();
	}

	private void readImage() throws FileNotFoundException, IOException {

		FileInputStream fIn = new FileInputStream(filename);
		JPEGImageDecoder jpeg_decode = JPEGCodec.createJPEGDecoder(fIn);
		BufferedImage image = jpeg_decode.decodeAsBufferedImage();

		width = image.getWidth();
		height = image.getHeight();

		int[] rgbdata = new int[width * height];

		image.getRGB(0, 0, width, height, rgbdata, 0, width);

		bytes = new byte[rgbdata.length];
		doubles = new double[rgbdata.length];

		for (int i = 0; i < bytes.length; i++) {
			bytes[i] = (byte) (rgbdata[i] & 0xFF);
			doubles[i] = (double) (rgbdata[i]);
		}
	}
}
