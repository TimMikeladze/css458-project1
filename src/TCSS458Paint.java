import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * @author Tim Mikeladze
 * 
 * 
 */
public class TCSS458Paint extends JPanel implements KeyListener {
	
	private int width;
	private int height;
	private int imageSize;
	private int[] pixels;
	private int[] color = new int[3];
	
	private Range[] coords;
	private File inputFile;
	private double xDegrees;
	private double yDegrees;
	
	public TCSS458Paint() {
		
		inputFile = openFile();
		
		if (inputFile != null) {
			setFocusable(true);
			addKeyListener(this);
			try {
				drawFile();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	@Override
	public void paintComponent(Graphics g) {
		try {
			drawFile();
			BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			WritableRaster raster = image.getRaster();
			raster.setPixels(0, 0, width, height, pixels);
			g.drawImage(image, 0, 0, null);
		}
		catch (IOException e) {
			e.printStackTrace();
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
								Point3f point1 = Transformations.calculatePoint(new Point3f(Float.parseFloat(parts[1]), Float.parseFloat(parts[2]),
										Float.parseFloat(parts[3])));
								Point3f point2 = Transformations.calculatePoint(new Point3f(Float.parseFloat(parts[4]), Float.parseFloat(parts[5]),
										Float.parseFloat(parts[6])));
								
								drawLine(point1, point2);
								break;
							case RGB:
								setColor(Float.parseFloat(parts[1]), Float.parseFloat(parts[2]), Float.parseFloat(parts[3]));
								break;
							case TRIANGLE:
								Point3f tri1 = Transformations.calculatePoint(new Point3f(Float.parseFloat(parts[1]), Float.parseFloat(parts[2]),
										Float.parseFloat(parts[3])));
								Point3f tri2 = Transformations.calculatePoint(new Point3f(Float.parseFloat(parts[4]), Float.parseFloat(parts[5]),
										Float.parseFloat(parts[6])));
								Point3f tri3 = Transformations.calculatePoint(new Point3f(Float.parseFloat(parts[7]), Float.parseFloat(parts[8]),
										Float.parseFloat(parts[9])));
								
								drawTriangle(tri1, tri2, tri3);
								break;
							case LOAD_IDENTITY_MATRIX:
								Transformations.init();
								break;
							case ROTATEX:
								Transformations.rotateX(Float.parseFloat(parts[1]));
								break;
							case ROTATEY:
								Transformations.rotateY(Float.parseFloat(parts[1]));
								break;
							case ROTATEZ:
								Transformations.rotateZ(Float.parseFloat(parts[1]));
								break;
							case SCALE:
								Transformations.scale(Float.parseFloat(parts[1]), Float.parseFloat(parts[2]), Float.parseFloat(parts[3]));
								break;
							case TRANSLATE:
								Transformations.translate(Float.parseFloat(parts[1]), Float.parseFloat(parts[2]), Float.parseFloat(parts[3]));
								break;
							case WIREFRAME_CUBE:
								Point3f a = Transformations.calculatePoint(new Point3f(-0.5f, -0.5f, 0.5f));
								Point3f b = Transformations.calculatePoint(new Point3f(0.5f, -0.5f, 0.5f));
								Point3f c = Transformations.calculatePoint(new Point3f(0.5f, -0.5f, -0.5f));
								Point3f d = Transformations.calculatePoint(new Point3f(-0.5f, -0.5f, -0.5f));
								Point3f e = Transformations.calculatePoint(new Point3f(-0.5f, 0.5f, 0.5f));
								Point3f f = Transformations.calculatePoint(new Point3f(0.5f, 0.5f, 0.5f));
								Point3f g = Transformations.calculatePoint(new Point3f(0.5f, 0.5f, -0.5f));
								Point3f h = Transformations.calculatePoint(new Point3f(-0.5f, 0.5f, -0.5f));
								
								drawLine(a, b);
								drawLine(b, c);
								drawLine(c, d);
								drawLine(d, a);
								drawLine(e, f);
								drawLine(f, g);
								drawLine(g, h);
								drawLine(h, e);
								drawLine(a, e);
								drawLine(b, f);
								drawLine(c, g);
								drawLine(d, h);
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
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				drawPixel(x, y, 255, 255, 255);
			}
		}
		setPreferredSize(new Dimension(width, height));
		
	}
	
	private int worldToScreenColor(float c) {
		return (int) (c * 255);
	}
	
	private void setColor(float r, float g, float b) {
		color[0] = worldToScreenColor(r);
		color[1] = worldToScreenColor(g);
		color[2] = worldToScreenColor(b);
	}
	
	private void drawLine(Point3f a, Point3f b) {
		drawLine(worldToScreenX(a.getX()), worldToScreenY(a.getY()), worldToScreenX(b.getX()), worldToScreenY(b.getY()));
	}
	
	private void drawTriangle(Point3f a, Point3f b, Point3f c) {
		drawTriangle(worldToScreenX(a.getX()), worldToScreenY(a.getY()), worldToScreenX(b.getX()), worldToScreenY(b.getY()),
				worldToScreenX(c.getX()), worldToScreenY(c.getY()));
	}
	
	private void drawTriangle(int sx0, int sy0, int sx1, int sy1, int sx2, int sy2) {
		
		// initialize array to keep track of all pixel positions
		coords = new Range[height];
		
		drawLine(sx0, sy0, sx1, sy1, true);
		drawLine(sx0, sy0, sx2, sy2, true);
		drawLine(sx1, sy1, sx2, sy2, true);
		
		int largest_y = findLargestNum(sy0, sy1, sy2);
		int smallest_y = findSmallestNum(sy0, sy1, sy2);
		
		for (int i = smallest_y; i <= largest_y; i++) {
			// only draw the lines that we've covered/initialized
			if (coords[i] != null) {
				drawLine(coords[i].getMin(), i, coords[i].getMax(), i);
			}
		}
	}
	
	private int findLargestNum(int sy0, int sy1, int sy2) {
		return Math.max(Math.max(sy0, sy1), Math.max(sy1, sy2));
	}
	
	private int findSmallestNum(int sy0, int sy1, int sy2) {
		return Math.min(Math.min(sy0, sy2), Math.min(sy1, sy2));
	}
	
	private void drawLine(int sx1, int sy1, int sx2, int sy2) {
		drawLine(sx1, sy1, sx2, sy2, false);
	}
	
	private void drawLine(int sx1, int sy1, int sx2, int sy2, boolean triangle) {
		
		float slope;
		
		// avoids division by 0 and sets appropriate slope value
		if (sx2 - sx1 == 0) {
			slope = Float.MAX_VALUE;
		}
		else {
			slope = (float) (sy2 - sy1) / (sx2 - sx1);
		}
		
		int start, end;
		if (slope >= -1 && slope <= 1) {
			float y;
			// choose start and end points
			if (sx1 < sx2) {
				start = sx1;
				end = sx2;
				y = sy1;
			}
			else {
				start = sx2;
				end = sx1;
				y = sy2;
			}
			
			for (int x = start; x <= end; x++) {
				
				int newY = Math.round(y);
				
				if (triangle) {
					// just keep track of where pixels would be placed
					
					if (coords[newY] == null) {
						coords[newY] = new Range(x, x);
					}
					else {
						if (coords[newY].getMin() > x) {
							coords[newY].setMin(x);
						}
						else if (coords[newY].getMax() < x) {
							coords[newY].setMax(x);
						}
					}
				}
				else {
					drawPixel(x, newY, color[0], color[1], color[2]);
				}
				
				y += slope;
			}
			
		}
		else if (slope > 1 || slope < -1) {
			
			float x;
			// choose start and end points
			if (sy1 < sy2) {
				start = sy1;
				end = sy2;
				x = sx1;
			}
			else {
				start = sy2;
				end = sy1;
				x = sx2;
			}
			
			for (int y = start; y <= end; y++) {
				
				int newX = Math.round(x);
				
				if (triangle) {
					// keep track of where the pixels would be placed
					if (coords[y] == null) {
						coords[y] = new Range(newX, newX);
					}
					else {
						if (coords[y].getMin() > newX) {
							coords[y].setMin(newX);
						}
						else if (coords[y].getMax() < newX) {
							coords[y].setMax(newX);
						}
					}
				}
				else {
					drawPixel(newX, y, color[0], color[1], color[2]);
				}
				x += 1 / slope;
			}
		}
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
	
	@Override
	public void keyPressed(KeyEvent e) {
		
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		float deg;
		
		if (e.getKeyCode() == KeyEvent.VK_UP) {
			yDegrees--;
			deg = (float) Math.toRadians(yDegrees);
			float[][] data = { { 1, 0, 0, 0 }, { 0, (float) Math.cos(deg), (float) -Math.sin(deg), 0 },
					{ 0, (float) Math.sin(deg), (float) Math.cos(deg), 0 }, { 0, 0, 0, 1 } };
			Transformations.setRotationMatrixY(new Matrix(data));
			repaint();
		}
		else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			yDegrees++;
			deg = (float) Math.toRadians(yDegrees);
			float[][] data = { { 1, 0, 0, 0 }, { 0, (float) Math.cos(deg), (float) -Math.sin(deg), 0 },
					{ 0, (float) Math.sin(deg), (float) Math.cos(deg), 0 }, { 0, 0, 0, 1 } };
			Transformations.setRotationMatrixY(new Matrix(data));
			repaint();
		}
		else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			xDegrees--;
			deg = (float) Math.toRadians(xDegrees);
			float[][] data = { { (float) Math.cos(deg), 0, (float) Math.sin(deg), 0 }, { 0, 1, 0, 0 },
					{ (float) -Math.sin(deg), 0, (float) Math.cos(deg), 0 }, { 0, 0, 0, 1 } };
			Transformations.setRotationMatrixX(new Matrix(data));
			repaint();
			
		}
		else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			xDegrees++;
			deg = (float) Math.toRadians(xDegrees);
			float[][] data = { { (float) Math.cos(deg), 0, (float) Math.sin(deg), 0 }, { 0, 1, 0, 0 },
					{ (float) -Math.sin(deg), 0, (float) Math.cos(deg), 0 }, { 0, 0, 0, 1 } };
			Transformations.setRotationMatrixX(new Matrix(data));
			repaint();
		}
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		
	}
	
	public static void main(String args[]) {
		JFrame frame = new JFrame("TCSS458Paint");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane()
				.add(new TCSS458Paint());
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
}