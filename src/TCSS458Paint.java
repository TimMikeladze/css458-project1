import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
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
	
	private Range[] scanline;
	private File inputFile;
	private double xDegrees;
	private double yDegrees;
	
	private float[][] zBuffer;
	
	private Inputs currentProjection;
	
	public TCSS458Paint() {
		openFile();
		if (inputFile != null) {
			setFocusable(true);
			addKeyListener(this);
			try {
				drawFile();
			} catch (IOException e) {
				System.exit(0);
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
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
		
	}
	
	private void drawFile() throws IOException {
		if (inputFile != null) {
			BufferedReader br = new BufferedReader(new FileReader(inputFile));
			String line;
			int lineNumber = 1;
			
			Vector4f sa = new Vector4f(0.5f, 0.5f, 0.5f, 1f);
			Vector4f sb = new Vector4f(0.5f, -0.5f, 0.5f, 1f);
			Vector4f sc = new Vector4f(-0.5f, -0.5f, 0.5f, 1f);
			Vector4f sd = new Vector4f(-0.5f, 0.5f, 0.5f, 1f);
			Vector4f se = new Vector4f(0.5f, -0.5f, -0.5f, 1f);
			Vector4f sf = new Vector4f(0.5f, 0.5f, -0.5f, 1f);
			Vector4f sg = new Vector4f(-0.5f, -0.5f, -0.5f, 1f);
			Vector4f sh = new Vector4f(-0.5f, 0.5f, -0.5f, 1f);
			
			Vector4f wa = new Vector4f(-0.5f, -0.5f, 0.5f, 1);
			Vector4f wb = new Vector4f(0.5f, -0.5f, 0.5f, 1);
			Vector4f wc = new Vector4f(0.5f, -0.5f, -0.5f, 1);
			Vector4f wd = new Vector4f(-0.5f, -0.5f, -0.5f, 1);
			Vector4f we = new Vector4f(-0.5f, 0.5f, 0.5f, 1);
			Vector4f wf = new Vector4f(0.5f, 0.5f, 0.5f, 1);
			Vector4f wg = new Vector4f(0.5f, 0.5f, -0.5f, 1);
			Vector4f wh = new Vector4f(-0.5f, 0.5f, -0.5f, 1);
			
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
								Vector4f line1 = new Vector4f(Float.parseFloat(parts[1]), Float.parseFloat(parts[2]), Float.parseFloat(parts[3]), 1);
								Vector4f line2 = new Vector4f(Float.parseFloat(parts[4]), Float.parseFloat(parts[5]), Float.parseFloat(parts[6]), 1);
								
								drawLine(line1, line2);
								break;
							case RGB:
								setColor(Float.parseFloat(parts[1]), Float.parseFloat(parts[2]), Float.parseFloat(parts[3]));
								break;
							case TRIANGLE:
								Vector4f tri1 = new Vector4f(Float.parseFloat(parts[1]), Float.parseFloat(parts[2]), Float.parseFloat(parts[3]), 1);
								Vector4f tri2 = new Vector4f(Float.parseFloat(parts[4]), Float.parseFloat(parts[5]), Float.parseFloat(parts[6]), 1);
								Vector4f tri3 = new Vector4f(Float.parseFloat(parts[7]), Float.parseFloat(parts[8]), Float.parseFloat(parts[9]), 1);
								
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
							case SOLID_CUBE:
								drawTriangle(sa, sb, sc);
								drawTriangle(sa, sd, sc);
								drawTriangle(sa, sb, se);
								drawTriangle(sa, sf, se);
								drawTriangle(sc, sd, sg);
								drawTriangle(sd, sh, sg);
								drawTriangle(sg, sh, se);
								drawTriangle(sh, se, sf);
								drawTriangle(se, sb, sc);
								drawTriangle(sc, sg, se);
								drawTriangle(sd, sa, sf);
								drawTriangle(sh, sf, sd);
								break;
							case WIREFRAME_CUBE:
								drawLine(wa, wb);
								drawLine(wb, wc);
								drawLine(wc, wd);
								drawLine(wd, wa);
								drawLine(we, wf);
								drawLine(wf, wg);
								drawLine(wg, wh);
								drawLine(wh, we);
								drawLine(wa, we);
								drawLine(wb, wf);
								drawLine(wc, wg);
								drawLine(wd, wh);
								break;
							case LOOKAT:
								Transformations.setLookAt(Float.parseFloat(parts[1]), Float.parseFloat(parts[2]), Float.parseFloat(parts[3]),
										Float.parseFloat(parts[4]), Float.parseFloat(parts[5]), Float.parseFloat(parts[6]),
										Float.parseFloat(parts[7]), Float.parseFloat(parts[8]), Float.parseFloat(parts[9]));
								break;
							case ORTHOGRAPHIC:
								if (currentProjection == null) {
									Transformations.setProjection(Inputs.ORTHOGRAPHIC, Float.parseFloat(parts[1]), Float.parseFloat(parts[2]),
											Float.parseFloat(parts[3]), Float.parseFloat(parts[4]), Float.parseFloat(parts[5]),
											Float.parseFloat(parts[6]));
								} else {
									Transformations.setProjection(currentProjection, Float.parseFloat(parts[1]), Float.parseFloat(parts[2]),
											Float.parseFloat(parts[3]), Float.parseFloat(parts[4]), Float.parseFloat(parts[5]),
											Float.parseFloat(parts[6]));
								}
								break;
							case FRUSTUM:
								if (currentProjection == null) {
									Transformations.setProjection(Inputs.FRUSTUM, Float.parseFloat(parts[1]), Float.parseFloat(parts[2]),
											Float.parseFloat(parts[3]), Float.parseFloat(parts[4]), Float.parseFloat(parts[5]),
											Float.parseFloat(parts[6]));
								} else {
									Transformations.setProjection(currentProjection, Float.parseFloat(parts[1]), Float.parseFloat(parts[2]),
											Float.parseFloat(parts[3]), Float.parseFloat(parts[4]), Float.parseFloat(parts[5]),
											Float.parseFloat(parts[6]));
								}
								
								break;
							default:
								break;
						}
					} else {
						throw new IOException("Error in input file syntax at line " + lineNumber);
					}
				}
				lineNumber++;
			}
			br.close();
		}
		
	}
	
	private void setDimensions(int width, int height) {
		this.width = width;
		this.height = height;
		imageSize = width * height;
		pixels = new int[imageSize * 3];
		zBuffer = new float[height][width];
		
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				drawPixel(x, y, 255, 255, 255);
			}
		}
		
		for (int x = 0; x < height; x++) {
			for (int y = 0; y < width; y++) {
				zBuffer[x][y] = Float.MAX_VALUE;
			}
		}
		
		setPreferredSize(new Dimension(width, height));
	}
	
	private int worldToScreenX(float x) {
		return (int) ((width - 1.0f) * (x + 1.0f) / 2.0f);
	}
	
	private int worldToScreenY(float x) {
		return Math.round((height - 1.0f) * (x + 1.0f) / 2.0f);
	}
	
	private int worldToScreenColor(float c) {
		return (int) (c * 255);
	}
	
	private void setColor(float r, float g, float b) {
		color[0] = worldToScreenColor(r);
		color[1] = worldToScreenColor(g);
		color[2] = worldToScreenColor(b);
	}
	
	private void drawTriangle(Vector4f a, Vector4f b, Vector4f c) {
		scanline = new Range[height];
		
		a = Transformations.calculatePoint(a);
		b = Transformations.calculatePoint(b);
		c = Transformations.calculatePoint(c);
		
		int x0 = worldToScreenX(a.getX());
		int y0 = worldToScreenY(a.getY());
		float z0 = a.getZ();
		
		int x1 = worldToScreenX(b.getX());
		int y1 = worldToScreenY(b.getY());
		float z1 = b.getZ();
		
		int x2 = worldToScreenX(c.getX());
		int y2 = worldToScreenY(c.getY());
		float z2 = c.getZ();
		
		drawLine(x0, y0, z0, x1, y1, z1, true);
		drawLine(x0, y0, z0, x2, y2, z2, true);
		drawLine(x1, y1, z1, x2, y2, z2, true);
		
		int maxY = Range.findLargest(y0, y1, y2);
		int minY = Range.findSmallest(y0, y1, y2);
		
		for (int i = minY; i <= maxY; i++) {
			if (scanline[i] != null) {
				drawLine(scanline[i].getMin(), i, scanline[i].getMin2(), scanline[i].getMax(), i, scanline[i].getMax2(), false);
			}
		}
	}
	
	private void drawLine(Vector4f a, Vector4f b) {
		Vector4f point1 = Transformations.calculatePoint(a);
		Vector4f point2 = Transformations.calculatePoint(b);
		
		int x1 = worldToScreenX(point1.getX());
		int y1 = worldToScreenY(point1.getY());
		float z1 = point1.getZ();
		
		int x2 = worldToScreenX(point2.getX());
		int y2 = worldToScreenY(point2.getY());
		float z2 = point2.getZ();
		
		drawLine(x1, y1, z1, x2, y2, z2, false);
	}
	
	private void drawLine(int px0, int py1, float pz1, int px1, int py2, float pz2, boolean isTriangle) {
		float slope;
		
		if (px1 == px0) {
			slope = Float.MAX_VALUE;
		} else {
			slope = (float) (py2 - py1) / (px1 - px0);
		}
		
		int startPoint;
		int endPoint;
		float z;
		float slopeZ;
		float startPointZ;
		float endPointZ;
		
		if (slope >= -1 && slope <= 1) {
			float y;
			if (px0 < px1) {
				startPoint = px0;
				startPointZ = pz1;
				endPoint = px1;
				endPointZ = pz2;
				y = py1;
			} else {
				startPoint = px1;
				startPointZ = pz2;
				endPoint = px0;
				endPointZ = pz1;
				y = py2;
			}
			
			z = startPointZ;
			
			slopeZ = (endPointZ - startPointZ) / (endPoint - startPoint);
			
			for (int x = startPoint; x <= endPoint; x++) {
				int newY = Math.round(y);
				if (newY >= height) {
					newY = height - 1;
				} else if (newY < 0) {
					newY = 0;
				}
				
				if (isTriangle) {
					if (scanline[newY] == null) {
						scanline[newY] = new Range(x, x, z, z);
					} else {
						if (scanline[newY].getMin() > x) {
							scanline[newY].setMin(x);
							scanline[newY].setMin2(z);
						} else if (scanline[newY].getMax() < x) {
							scanline[newY].setMax(x);
							scanline[newY].setMax2(z);
						}
					}
				} else if (zBuffer[newY][x] > z) {
					zBuffer[newY][x] = z;
					drawPixel(x, newY, color[0], color[1], color[2]);
				}
				
				y += slope;
				z += slopeZ;
			}
		} else if (slope > 1 || slope < -1) {
			
			float x;
			if (py1 < py2) {
				startPoint = py1;
				startPointZ = pz1;
				endPoint = py2;
				endPointZ = pz2;
				x = px0;
			} else {
				startPoint = py2;
				startPointZ = pz2;
				endPoint = py1;
				endPointZ = pz1;
				x = px1;
			}
			
			z = startPointZ;
			
			slopeZ = (endPointZ - startPointZ) / (endPoint - startPoint);
			
			for (int y = startPoint; y <= endPoint; y++) {
				
				int newX = Math.round(x);
				
				if (newX >= width) {
					newX = width - 1;
				} else if (newX < 0) {
					newX = 0;
				}
				
				if (isTriangle) {
					if (scanline[y] == null) {
						scanline[y] = new Range(newX, newX, z, z);
					} else {
						if (scanline[y].getMin() > newX) {
							scanline[y].setMin(newX);
							scanline[y].setMin2(z);
						} else if (scanline[y].getMax() < newX) {
							scanline[y].setMax(newX);
							scanline[y].setMax2(z);
						}
					}
				} else if (zBuffer[y][newX] > z) {
					zBuffer[y][newX] = z;
					drawPixel(newX, y, color[0], color[1], color[2]);
				}
				
				x += 1 / slope;
				z += slopeZ;
			}
		}
	}
	
	private void drawPixel(int x, int y, int r, int g, int b) {
		pixels[(height - y - 1) * width * 3 + x * 3] = r;
		pixels[(height - y - 1) * width * 3 + x * 3 + 1] = g;
		pixels[(height - y - 1) * width * 3 + x * 3 + 2] = b;
	}
	
	public void openFile() {
		JFileChooser fc = new JFileChooser();
		fc.showOpenDialog(this);
		inputFile = fc.getSelectedFile();
		if (inputFile != null) {
			reset();
		}
	}
	
	public void reset() {
		width = 0;
		height = 0;
		imageSize = 0;
		pixels = null;
		color = new int[3];
		scanline = null;
		xDegrees = 0;
		yDegrees = 0;
		zBuffer = null;
		currentProjection = null;
		
		Transformations.reset();
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		float deg;
		
		if (e.getKeyCode() == KeyEvent.VK_UP) {
			yDegrees--;
			deg = (float) Math.toRadians(yDegrees);
			float[][] data = { { 1, 0, 0, 0 }, { 0, (float) Math.cos(deg), (float) -Math.sin(deg), 0 },
					{ 0, (float) Math.sin(deg), (float) Math.cos(deg), 0 }, { 0, 0, 0, 1 } };
			Transformations.setRotationMatrixY(new Matrix(data));
			repaint();
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			yDegrees++;
			deg = (float) Math.toRadians(yDegrees);
			float[][] data = { { 1, 0, 0, 0 }, { 0, (float) Math.cos(deg), (float) -Math.sin(deg), 0 },
					{ 0, (float) Math.sin(deg), (float) Math.cos(deg), 0 }, { 0, 0, 0, 1 } };
			Transformations.setRotationMatrixY(new Matrix(data));
			repaint();
		} else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			xDegrees--;
			deg = (float) Math.toRadians(xDegrees);
			float[][] data = { { (float) Math.cos(deg), 0, (float) Math.sin(deg), 0 }, { 0, 1, 0, 0 },
					{ (float) -Math.sin(deg), 0, (float) Math.cos(deg), 0 }, { 0, 0, 0, 1 } };
			Transformations.setRotationMatrixX(new Matrix(data));
			repaint();
			
		} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			xDegrees++;
			deg = (float) Math.toRadians(xDegrees);
			float[][] data = { { (float) Math.cos(deg), 0, (float) Math.sin(deg), 0 }, { 0, 1, 0, 0 },
					{ (float) -Math.sin(deg), 0, (float) Math.cos(deg), 0 }, { 0, 0, 0, 1 } };
			Transformations.setRotationMatrixX(new Matrix(data));
			repaint();
		} else if (e.getKeyCode() == KeyEvent.VK_P) {
			currentProjection = Inputs.FRUSTUM;
			repaint();
		} else if (e.getKeyCode() == KeyEvent.VK_O) {
			currentProjection = Inputs.ORTHOGRAPHIC;
			repaint();
		} else if (!e.isShiftDown()) {
			if (e.getKeyCode() == KeyEvent.VK_L) {
				float width = Math.abs(Transformations.getRight() - Transformations.getLeft());
				Transformations.setLeft(Transformations.getLeft() + width * 0.1f);
				repaint();
			} else if (e.getKeyCode() == KeyEvent.VK_R) {
				float width = Math.abs(Transformations.getRight() - Transformations.getLeft());
				Transformations.setRight(Transformations.getRight() - width * 0.1f);
				repaint();
			} else if (e.getKeyCode() == KeyEvent.VK_B) {
				float height = Math.abs(Transformations.getTop() - Transformations.getBottom());
				Transformations.setBottom(Transformations.getBottom() + height * 0.1f);
				repaint();
			} else if (e.getKeyCode() == KeyEvent.VK_T) {
				float height = Math.abs(Transformations.getTop() - Transformations.getBottom());
				Transformations.setTop(Transformations.getTop() - height * 0.1f);
				repaint();
			} else if (e.getKeyCode() == KeyEvent.VK_N) {
				float depth = Math.abs(Transformations.getFar() - Transformations.getNear());
				Transformations.setNear(Transformations.getNear() + depth * 0.1f);
				repaint();
			} else if (e.getKeyCode() == KeyEvent.VK_F) {
				float depth = Math.abs(Transformations.getFar() - Transformations.getNear());
				Transformations.setFar(Transformations.getFar() - depth * 0.1f);
				repaint();
			}
		} else if (e.isShiftDown()) {
			if (e.getKeyCode() == KeyEvent.VK_L) {
				float width = Math.abs(Transformations.getRight() - Transformations.getLeft());
				Transformations.setLeft(Transformations.getLeft() - width * 0.1f);
				repaint();
			} else if (e.getKeyCode() == KeyEvent.VK_R) {
				float width = Math.abs(Transformations.getRight() - Transformations.getLeft());
				Transformations.setRight(Transformations.getRight() + width * 0.1f);
				repaint();
			} else if (e.getKeyCode() == KeyEvent.VK_B) {
				float height = Math.abs(Transformations.getTop() - Transformations.getBottom());
				Transformations.setBottom(Transformations.getBottom() - height * 0.1f);
				repaint();
			} else if (e.getKeyCode() == KeyEvent.VK_T) {
				float height = Math.abs(Transformations.getTop() - Transformations.getBottom());
				Transformations.setTop(Transformations.getTop() + height * 0.1f);
				repaint();
			} else if (e.getKeyCode() == KeyEvent.VK_N) {
				float depth = Math.abs(Transformations.getFar() - Transformations.getNear());
				if (Transformations.getNear() - depth * 0.10 > 0) {
					Transformations.setNear(Transformations.getNear() - depth * 0.1f);
				}
				repaint();
			} else if (e.getKeyCode() == KeyEvent.VK_F) {
				float depth = Math.abs(Transformations.getFar() - Transformations.getNear());
				Transformations.setFar(Transformations.getFar() + depth * 0.1f);
				repaint();
			}
		}
		
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		
	}
	
	public static void main(String args[]) {
		final TCSS458Paint panel = new TCSS458Paint();
		
		JFrame frame = new JFrame("TCSS458Paint");
		
		JMenuBar menubar = new JMenuBar();
		JMenu menu = new JMenu("File");
		JMenuItem file = new JMenuItem("Open file");
		file.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				panel.openFile();
				panel.repaint();
			}
			
		});
		menu.add(file);
		JMenuItem reset = new JMenuItem("Reset");
		reset.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				panel.reset();
				panel.repaint();
			}
			
		});
		menu.add(reset);
		menubar.add(menu);
		frame.setJMenuBar(menubar);
		
		frame.getContentPane()
				.add(panel);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}
}