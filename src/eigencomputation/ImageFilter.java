package eigencomputation;

import java.io.File;
import java.io.FileFilter;

public class ImageFilter implements FileFilter {
	private static final String PNM_EXTENSION = "pnm";
	private static final String PPM_EXTENSION = "ppm";
	private static final String JPEG_EXTENSION = "jpeg";
	private static final String JPG_EXTENSION = "jpg";

	public boolean accept(File f) {

		if (f.isDirectory()) {
			return true;
		}

		String extension = f.getName();
		int i = extension.lastIndexOf('.');

		if (i > 0 && i < extension.length() - 1) {
			extension = extension.substring(i + 1).toLowerCase();
		}

		if (extension != null) {
			if ((extension.equals(PPM_EXTENSION))
					|| (extension.equals(PNM_EXTENSION))
					|| (extension.equals(JPG_EXTENSION))
					|| (extension.equals(JPEG_EXTENSION))) {
				return true;
			} else {
				return false;
			}
		}

		return false;
	}
}