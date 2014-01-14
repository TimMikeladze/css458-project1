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

public class TCSS458Paint extends JFrame {

    private int width;
    private int height;
    private int imageSize;
    private int[] pixels;
    private int[] color = new int[3];

    private File inputFile;

    public TCSS458Paint() {
        //inputFile = openFile();
        inputFile = new File("sphere2.txt");
        try {
            drawFile();
            getContentPane().add(createImageLabel(pixels));
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
                                drawLine(Float.parseFloat(parts[1].trim()),
                                        Float.parseFloat(parts[2]), Float.parseFloat(parts[3]),
                                        Float.parseFloat(parts[4]));

                                break;
                            case RGB:
                                setColor((int) Float.parseFloat(parts[1]),
                                        (int) Float.parseFloat(parts[2]),
                                        (int) Float.parseFloat(parts[3]));
                                break;
                            case TRIANGLE:
                                     drawTriangle(Float.parseFloat(parts[1]), Float.parseFloat(parts[2]), Float.parseFloat(parts[3]),
                                            Float.parseFloat(parts[4]),  Float.parseFloat(parts[5]),  Float.parseFloat(parts[6]));
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

    private float worldToScreenX(float x) {
        return (width - 1.0f) * (x + 1.0f) / 2.0f;
    }

    private float worldToScreenY(float x) {
        return (height - 1.0f) * (x + 1.0f) / 2.0f;
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

    private void drawLine(float x0, float y0, float x1, float y1) {
        x0 = (int) worldToScreenX(x0);
        y0 = (int) worldToScreenY(y0);

        x1 = (int) worldToScreenX(x1);
        y1 = (int) worldToScreenY(y1);

        int dx = (int) Math.abs(x1 - x0);
        int dy = (int) Math.abs(y1 - y0);

        int sx = (x0 < x1) ? 1 : -1;
        int sy = (y0 < y1) ? 1 : -1;

        int error = dx - dy;

        while (true) {
            drawPixel((int) x0, (int) y0, color[0], color[1], color[2]);
            if (x0 == x1 && y0 == y1) {
                break;
            }
            int error2 = 2 * error;

            if (error2 > -dy) {
                error = error - dy;
                x0 = x0 + sx;
            }

            if (error2 < dx) {
                error = error + dx;
                y0 = y0 + sy;
            }
        }
    }

    private void drawTriangle(float x0, float y0, float x1, float y1, float x2, float y2) {
        drawLine(x0, y0, x1, y1);
        drawLine(x1, y1, x2, y2);
        drawLine(x2, y2, x0, y0);
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