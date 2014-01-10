import java.awt.*;
import java.awt.image.*;
import javax.swing.*;

public class TCSS458Paint extends JFrame
{
    int width;
    int height;
    int imageSize;
    int[] pixels;    
    
    void drawPixel(int x, int y, int r, int g, int b) {
        pixels[(height-y-1)*width*3+x*3] = r;
        pixels[(height-y-1)*width*3+x*3+1] = g;
        pixels[(height-y-1)*width*3+x*3+2] = b;                
    }
    
    void createImage() {
    
        width = 512;
        height = 512;
        imageSize = width * height;
        pixels = new int[imageSize * 3];
        
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                drawPixel( x, y, x*255/width, y*255/height, 0);
            }
        }
        for (int d = 0; d < width; d++) {
            drawPixel( d, d, 255, 255, 255);
        }
    }

    public TCSS458Paint() {
        createImage();
        getContentPane().add( createImageLabel(pixels) );
    }

    private JLabel createImageLabel(int[] pixels) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        WritableRaster raster = image.getRaster();
        raster.setPixels(0, 0, width, height, pixels);
        JLabel label = new JLabel( new ImageIcon(image) );
        return label;
    }

    public static void main(String args[]) {
        JFrame frame = new TCSS458Paint();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo( null );
        frame.setVisible( true );
    }
}