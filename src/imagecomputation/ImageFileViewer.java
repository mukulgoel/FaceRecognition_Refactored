package imagecomputation;

import java.awt.*;
import java.awt.event.*;

import eigencomputation.EigenFile;

public class ImageFileViewer extends Frame implements WindowListener,
		ActionListener, MouseListener {

	/**
	 * AutoGenerated serialVersionUID
	 */
	private static final long serialVersionUID = -3562605871498173726L;
	private static final String IMAGE_VIEWER_MESSAGE = "PPM/JPG File Viewer (Click on canvas to load image)";
	private static final int CANVAS_HEIGHT = 300;
	private static final int CANVAS_WIDTH = 400;
	private static final int PANNEL_HEIGHT = 400;
	private static final int PANNEL_WIDTH = 500;
	private static final String PNM_EXTENSION = "pnm";
	private static final String PPM_EXTENSION = "ppm";
	private static final String JPEG_EXTENSION = "jpeg";
	private static final String JPG_EXTENSION = "jpg";

	Button ExitButton;
	ImageCanvas canvas;

	public ImageFileViewer() {
		super(IMAGE_VIEWER_MESSAGE);
		setup();
	}

	public void setup() {

		Panel CanvasPanel; // panel for drawing canvas
		Panel ButtonPanel; // main button panel
		setSize(PANNEL_WIDTH, PANNEL_HEIGHT);
		setLayout(new BorderLayout());

		this.setBackground(Color.gray);
		this.setForeground(Color.black);

		ExitButton = new Button("Exit");
		ExitButton.addActionListener(this);

		canvas = new ImageCanvas();
		canvas.addMouseListener(this);
		canvas.setSize(CANVAS_WIDTH, CANVAS_HEIGHT);
		canvas.setBackground(Color.black);
		canvas.setForeground(Color.white);

		ButtonPanel = new Panel();
		ButtonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		ButtonPanel.add(ExitButton);

		CanvasPanel = new Panel();
		CanvasPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		CanvasPanel.add(canvas);

		add("North", new Label(""));
		add("South", ButtonPanel);
		add("Center", CanvasPanel);

		setVisible(true);
		addWindowListener(this);
	}

	public void windowClosing(WindowEvent event) {

		setVisible(false); // hide frame first...
		dispose(); // release resources and destroy frame
		System.exit(0); // exit program

	}

	@SuppressWarnings("deprecation")
	public void mousePressed(MouseEvent event) {

		FileDialog f = new FileDialog(this, "Open a JPG/PPM image file",
				FileDialog.LOAD);
		f.setFile("*.*");
		f.show();
		if (f.getFile() == null) {
			System.out.println("Image open cancelled.");
		} else {
			try {
				EigenFile file = null;
				String temp = (f.getDirectory() + f.getFile()).toLowerCase();

				temp = temp.substring(temp.lastIndexOf('.') + 1, temp.length());
				if (temp.equals(JPG_EXTENSION) || temp.equals(JPEG_EXTENSION))
					file = new JPGFile(f.getDirectory() + f.getFile());
				else if (temp.equals(PPM_EXTENSION)
						|| temp.equals(PNM_EXTENSION))
					file = new PPMFile(f.getDirectory() + f.getFile());

				setImage(file.getBytes(), file.getWidth(), file.getHeight());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public void setImage(byte[] b, int w, int h) {
		canvas.readImage(b, w, h);
	}

	public void setImage(int[] b, int w, int h) {
		canvas.readImage(b, w, h);
	}

	public void setImage(double[] b, int w, int h) {
		canvas.readImage(b, w, h);
	}

	public void actionPerformed(ActionEvent event) {
		Object source = event.getSource();

		if (source == ExitButton) {
			setVisible(false); // hide frame first...
			dispose(); // release resources and destroy frame
			System.exit(0); // exit program
		}
	}

	public void windowDeiconified(WindowEvent event) {
	}

	public void windowIconified(WindowEvent event) {
	}

	public void windowActivated(WindowEvent event) {
	}

	public void windowDeactivated(WindowEvent event) {
	}

	public void windowOpened(WindowEvent event) {
	}

	public void windowClosed(WindowEvent event) {
	}

	public void mouseReleased(MouseEvent event) {
	}

	public void mouseMoved(MouseEvent event) {
	}

	public void mouseEntered(MouseEvent event) {
	}

	public void mouseExited(MouseEvent event) {
	}

	public void mouseClicked(MouseEvent event) {
	}

	public void mouseDragged(MouseEvent event) {
	}
}