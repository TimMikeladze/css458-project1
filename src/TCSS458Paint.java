import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 * @author Tim Mikeladze
 * 
 * 
 */
public class TCSS458Paint extends JFrame {
	
	private int width;
	private int height;
	private int imageSize;
	private int[] pixels;
	private int[] color = new int[3];
	
	private File inputFile;
	
	public TCSS458Paint() {
		inputFile = openFile();
		if (inputFile != null) {
			try {
				drawFile();
				getContentPane().add(createImageLabel(pixels));
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		else {
			JOptionPane.showMessageDialog(null, "No input file selected");
			System.exit(0);
		}
	}
	
	private void drawFile() throws IOException {
		if (inputFile != null) {
			BufferedReader br = new BufferedReader(new FileReader(inputFile));
			String line;
			int lineNumber = 1;
			
			while ((line = br.readLine()) != null) {
				String[] parts = line.split("\\s+");
				if (parts != null && parts.length > 0) {
					Inputs input = Inputs.find(parts[0]);
					if (input != null && input.getNumberOfParameters() == parts.length) {
						switch (input) {
							case DIMENSIONS:
								setDimensions((int) Float.parseFloat(parts[1]), (int) Float.parseFloat(parts[2]));
								break;
							case LINE:
								drawLine(worldToScreenX(Float.parseFloat(parts[1].trim())), worldToScreenY(Float.parseFloat(parts[2].trim())),
										worldToScreenX(Float.parseFloat(parts[3].trim())), worldToScreenY(Float.parseFloat(parts[4].trim())));
								break;
							case RGB:
								setColor((int) Float.parseFloat(parts[1]), (int) Float.parseFloat(parts[2]), (int) Float.parseFloat(parts[3]));
								break;
							case TRIANGLE:
								drawTriangle(worldToScreenX(Float.parseFloat(parts[1].trim())), worldToScreenY(Float.parseFloat(parts[2].trim())),
										worldToScreenX(Float.parseFloat(parts[3].trim())), worldToScreenY(Float.parseFloat(parts[4].trim())),
										worldToScreenX(Float.parseFloat(parts[5].trim())), worldToScreenY(Float.parseFloat(parts[6].trim())));
								break;
							default:
								break;
						}
					}
					else {
						throw new IOException("Error in input file syntax at line " + lineNumber);
					}
				}
				lineNumber++;
			}
			br.close();
		}
	}
	
	private int worldToScreenX(float x) {
		return (int) ((width - 1.0f) * (x + 1.0f) / 2.0f);
	}
	
	private int worldToScreenY(float x) {
		return (int) ((height - 1.0f) * (x + 1.0f) / 2.0f);
	}
	
	private void setDimensions(int width, int height) {
		this.width = width;
		this.height = height;
		imageSize = width * height;
		pixels = new int[imageSize * 3];
		
	}
	
	private int worldToScreenColor(int c) {
		return c * 255;
	}
	
	private void setColor(int r, int g, int b) {
		color[0] = worldToScreenColor(r);
		color[1] = worldToScreenColor(g);
		color[2] = worldToScreenColor(b);
	}
	
	private void drawLine(int x0, int y0, int x1, int y1) {
		int dx = Math.abs(x1 - x0);
		int dy = Math.abs(y1 - y0);
		
		int sx = (x0 < x1) ? 1 : -1;
		int sy = (y0 < y1) ? 1 : -1;
		
		int err = dx - dy;
		
		while (true) {
			drawPixel(x0, y0, color[0], color[1], color[2]);
			
			if (x0 == x1 && y0 == y1) {
				break;
			}
			
			int e = 2 * err;
			if (e > -dy) {
				err = err - dy;
				x0 = x0 + sx;
			}
			
			if (e < dx) {
				err = err + dx;
				y0 = y0 + sy;
			}
		}
	}
	
	private void drawTriangle(int x0, int y0, int x1, int y1, int x2, int y2) {
		drawLine(x0, y0, x1, y1);
		drawLine(x1, y1, x2, y2);
		drawLine(x2, y2, x0, y0);
		
		int maxX = Math.max(x0, Math.max(x1, x2));
		int minX = Math.min(x0, Math.min(x1, x2));
		int maxY = Math.max(y0, Math.max(y1, y2));
		int minY = Math.min(y0, Math.max(y1, y2));
		
		for (int x = minX; x <= maxX; x++) {
			for (int y = minY; y <= maxY; y++) {
				if (isInsideTriangle(x0, y0, x1, y1, x2, y2, x, y)) {
					drawPixel(x, y, color[0], color[1], color[2]);
				}
			}
		}
	}
	
	private boolean isInsideTriangle(int x0, int y0, int x1, int y1, int x2, int y2, int xTest, int yTest) {
		int s = (y0 * x2) - (x0 * y2) + (y2 - y0) * xTest + (x0 - x2) * yTest;
		int t = (x0 * y1) - (y0 * x1) + (y0 - y1) * xTest + (x1 - x0) * yTest;
		int area = (-y1 * x2) + y0 * (x2 - x1) + x0 * (y1 - y2) + x1 * y2;
		
		if (area < 0.0) {
			s = -s;
			t = -t;
			area = -area;
		}
		
		return (s < 0) == (t < 0) && (s > 0) && (t > 0) && (s + t) < area;
	}
	
	void drawPixel(int x, int y, int r, int g, int b) {
		pixels[(height - y - 1) * width * 3 + x * 3] = r;
		pixels[(height - y - 1) * width * 3 + x * 3 + 1] = g;
		pixels[(height - y - 1) * width * 3 + x * 3 + 2] = b;
	}
	
	private File openFile() {
		JFileChooser fc = new JFileChooser();
		fc.showOpenDialog(this);
		return fc.getSelectedFile();
	}
	
	private JLabel createImageLabel(int[] pixels) {
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		WritableRaster raster = image.getRaster();
		raster.setPixels(0, 0, width, height, pixels);
		JLabel label = new JLabel(new ImageIcon(image));
		return label;
	}
	
	public static void main(String args[]) {
		JFrame frame = new TCSS458Paint();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
}