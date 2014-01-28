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

    private File inputFile;

    public TCSS458Paint() {
        //TODO fix
        setPreferredSize(new Dimension(512, 512));

        setFocusable(true);
        addKeyListener(this);
        //inputFile = openFile();
        inputFile = new File("sphere2.txt");
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
                                setDimensions((int) Float.parseFloat(parts[1]),
                                        (int) Float.parseFloat(parts[2]));
                                break;
                            case LINE:
                                drawLine(worldToScreenX(Float.parseFloat(parts[1])),
                                        worldToScreenY(Float.parseFloat(parts[2])),
                                        worldToScreenX(Float.parseFloat(parts[3])),
                                        worldToScreenY(Float.parseFloat(parts[4])));
                                break;
                            case RGB:
                                setColor(Float.parseFloat(parts[1]),
                                        Float.parseFloat(parts[2]), Float.parseFloat(parts[3]));
                                break;
                            case TRIANGLE:
                                drawTriangle(worldToScreenX(Float.parseFloat(parts[1])),
                                        worldToScreenY(Float.parseFloat(parts[2])),
                                        worldToScreenX(Float.parseFloat(parts[3])),
                                        worldToScreenY(Float.parseFloat(parts[4])),
                                        worldToScreenX(Float.parseFloat(parts[5])),
                                        worldToScreenY(Float.parseFloat(parts[6])));
                                break;
                            default:
                                break;
                        }
                    }
                    else {
                        throw new IOException("Error in input file syntax at line "
                                + lineNumber);
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

    private int worldToScreenColor(float c) {
        return (int) (c * 255);
    }

    private void setColor(float r, float g, float b) {
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

    private boolean isInsideTriangle(int x0, int y0, int x1, int y1, int x2, int y2, int xTest,
            int yTest) {
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

    @Override
    public void keyPressed(KeyEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void keyReleased(KeyEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void keyTyped(KeyEvent arg0) {
        // TODO Auto-generated method stub

    }

    public static void main(String args[]) {
        JFrame frame = new JFrame("TCSS458 Paint");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new TCSS458Paint());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

}