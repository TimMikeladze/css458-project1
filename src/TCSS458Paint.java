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
    private int[] color = { 0, 0, 0 };
    
    private File inputFile;
    
    public TCSS458Paint() {
        //inputFile = openFile();
        inputFile = new File("sphere.txt");
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
                                setDimensions((int) Float.parseFloat(parts[1]), (int) Float.parseFloat(parts[2]));
                                break;
                            case LINE:
                                drawLine(Float.parseFloat(parts[1].trim()), Float.parseFloat(parts[2]), Float.parseFloat(parts[3]),
                                        Float.parseFloat(parts[4]));
                                
                                break;
                            case RGB:
                                setColor((int) Float.parseFloat(parts[1]), (int) Float.parseFloat(parts[2]), (int) Float.parseFloat(parts[3]));
                                break;
                            case TRIANGLE:
                                //     drawTriangle((int) Float.parseFloat(parts[1]), (int) Float.parseFloat(parts[2]), (int) Float.parseFloat(parts[3]),
                                //           (int) Float.parseFloat(parts[4]), (int) Float.parseFloat(parts[5]), (int) Float.parseFloat(parts[6]));
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
    
    private void setColor(int r, int g, int b) {
        //  color[0] = r;
        //  color[1] = g;
        //  color[2] = b;
    }
    
    /*
    private void drawLine(float x0, float y0, float x1, float y1) {
        boolean isSteep = Math.abs(y1 - y0) > Math.abs(x1 - x0);
        float temp;
        
        if (isSteep) {
            temp = y0;
            x0 = y0;
            y0 = temp;
            
            temp = y1;
            x1 = y1;
            y1 = temp;
        }
        
        if (x0 > x1) {
            temp = x1;
            x0 = x1;
            x1 = temp;
            
            temp = y1;
            y0 = y1;
            y1 = temp;
        }
        
        float dx = x1 - x0;
        float dy = y1 - y0;
        float gradient = dy / dx;
        
        float xEnd = round(x0);
        float yEnd = y0 + gradient * (xEnd - x0);
        float xGap = getRFractionalPart(x0 + 0.5f);
        float xPxl1 = xEnd;
        float yPxl1 = getIntegerPart(yEnd);
        
        if(isSteep) {
            
        } else {
            
        }
        
    }
    
    private float round(float x) {
        return getIntegerPart(x) + 0.5f;
    }
    
    private float getIntegerPart(float x) {
        return x - (x % 1.0f);
    }
    
    private float getFractionalPart(float x) {
        return x % 1.0f;
    }
    
    private float getRFractionalPart(float x) {
        return 1.0f - getFractionalPart(x);
    }
    
    */
    
    private void drawLine(float x0, float y0, float x1, float y1) {
        //       System.out.println(x0 + " " + y0 + " " + x1 + " " + y1);
        
        x0 = worldToScreenX(x0);
        y0 = worldToScreenY(y0);
        
        x1 = worldToScreenX(x1);
        y1 = worldToScreenY(y1);
        
        //    System.out.println(x0 + " " + y0 + " " + x1 + " " + y1);
        
        System.out.println(pixels.length);
        
        float dx = Math.abs(x1 - x0);
        float dy = Math.abs(y1 - y0);
        float err1 = dx - dy;
        float err2;
        
        float sx = -1.0f;
        float sy = -1.0f;
        
        if (x0 < x1) {
            sx = 1.0f;
        }
        if (y0 < y1) {
            sy = 1.0f;
        }
        
        while (true) {
            drawPixel((int) x0, (int) y0, color[0], color[1], color[2]);
            if (x0 == x1 && y0 == y1) {
                break;
            }
            err2 = err1 * 2.0f;
            if (err2 > -dy) {
                err1 = err1 - dy;
                x0 = x0 + sx;
            }
            if (x0 == x1 && y0 == y1) {
                drawPixel((int) x0, (int) y0, color[0], color[1], color[2]);
                break;
            }
            if (err2 < dx) {
                err1 = err1 + dx;
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
    
    /*
    void createImage() {
        width = 512;
        height = 512;
        imageSize = width * height;
        pixels = new int[imageSize * 3];
        /*
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                drawPixel(x, y, x * 255 / width, y * 255 / height, 0);
            }
        }
        for (int d = 0; d < width; d++) {
            drawPixel(d, d, 255, 255, 255);
        }
    }
    */
    
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