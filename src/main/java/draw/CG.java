package draw;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class CG {
    private Frame frame;
    private BufferedImage image;

    private int width, height;
    private int color;
    private Cli cli;
    private int rotate_x;
    private int rotate_y;
    private int rotate_r;

    public CG(Frame f) throws IOException {
        frame = f;
        frame.setCG(this);
        frame.InitFrame();
        this.setRotate_r(0x7fffffff);
    }

    public Cli getCli() {
        return cli;
    }

    public void setCli(Cli cc) {
        cli = cc;
    }

    public void resetCanvas(int w, int h) {
        width = w;
        height = h;
        image = new BufferedImage(width, height + 1, BufferedImage.TYPE_INT_RGB);
        setColor(0);
        clearCanvas();
    }

    public void clearCanvas() {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height + 1; j++) {
                image.setRGB(i, j, 0xffffffff);
            }
        }
        // drawDashs();
        this.showImage();
    }

    public String saveCanvas(String name) {
        try {
            ImageIO.write(image, "bmp", new File(name + ".bmp"));
            return System.getProperty("user.dir") + "\\" + name + ".bmp";
        } catch (IOException e) {
            return null;
        }
    }

    public void setColor(int r, int g, int b) {
        color = (0xFF << 24) | (r << 16) | (g << 8) | b;
        frame.setSlider(r, g, b);
    }

    public int getColor() {
        return color;
    }

    public void setColor(int c) {
        color = c;
        frame.setSlider((c & 0xff0000) >> 16, (c & 0xff00) >> 8, c & 0xff);
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage bufferedImage) {
        image = bufferedImage;
        width = image.getWidth();
        height = image.getHeight();
        showImage();
    }

    public void drawDashs() {
        for (int i = 0; i <= width; i += 100) {
            for (int j = 0; j <= height; j += 2) {
                drawPixel(new Point(i, j));
            }
        }
        for (int i = 0; i <= height; i += 100) {
            for (int j = 0; j <= width; j += 2) {
                drawPixel(new Point(j, i));
            }
        }
    }

    private void drawPixel(Point point) {
        if (point.x > 0 && point.y > 0 && point.x < width && point.y < height) {
            image.setRGB(point.x, height - point.y, color);
        }
    }

    public void drawLine(Point a, Point b, String algorithm) {
        if (algorithm.equals("Bresenham")) {
            drawLineBresenham(a, b);
        } else {
            drawLineDDA(a, b);
        }
    }

    private void drawLineDDA(Point a, Point b) {
        if (a.x == b.x) {
            if (a.y > b.y) {
                Point tmp = a;
                a = b;
                b = tmp;
            }
            for (int i = a.y; i < b.y; i++) {
                this.drawPixel(new Point(a.x, i));
            }
        } else {
            double k = ((double) b.y - a.y) / (double) (b.x - a.x);
            if (Math.abs(k) <= 1) {
                if (a.x > b.x) {
                    Point tmp = a;
                    a = b;
                    b = tmp;
                }
                for (int i = a.x; i < b.x; i++) {
                    this.drawPixel(new Point(i, (int) (a.y + (i - a.x) * k)));
                }
            } else {
                if (a.y > b.y) {
                    Point tmp = a;
                    a = b;
                    b = tmp;
                }
                for (int i = a.y; i < b.y; i++) {
                    this.drawPixel(new Point((int) ((i - a.y) / k + a.x), i));
                }
            }
        }
    }

    private void drawLineBresenham(Point a, Point b) { // 73 230 174 77
        // System.err.println(String.format("drawLine from (%d, %d) to (%d, %d)", a.x,
        // a.y, b.x, b.y));
        if (Math.abs(b.y - a.y) < Math.abs(b.x - a.x)) {
            if (a.x > b.x) {
                Point tmp = a;
                a = b;
                b = tmp;
            }
            int dx = b.x - a.x;
            int dy = b.y - a.y;
            int yi = 1;
            if (dy < 0) {
                yi = -1;
                dy = -dy;
            }
            int D = 2 * dy - dx;
            int y = a.y;

            for (int x = a.x; x < b.x; x++) {
                this.drawPixel(new Point(x, y));
                if (D > 0) {
                    y = y + yi;
                    D = D - 2 * dx;
                }
                D = D + 2 * dy;
            }
        } else {
            if (a.y > b.y) {
                Point tmp = a;
                a = b;
                b = tmp;
            }
            int dx = b.x - a.x;
            int dy = b.y - a.y;
            int xi = 1;
            if (dx < 0) {
                xi = -1;
                dx = -dx;
            }
            int D = 2 * dx - dy;
            int x = a.x;

            for (int y = a.y; y < b.y; y++) {
                this.drawPixel(new Point(x, y));
                if (D > 0) {
                    x = x + xi;
                    D = D - 2 * dy;
                }
                D = D + 2 * dx;
            }
        }
    }

    public void drawPloygon(Point[] points, String algorithm) {
        for (int i = 0; i < points.length - 1; i++) {
            this.drawLine(points[i], points[i + 1], algorithm);
        }
        this.drawLine(points[0], points[points.length - 1], algorithm);
    }

    private void ellipseDraw4Points(Point center, int x, int y) {
        this.drawPixel(new Point(center.x + x, center.y + y));
        this.drawPixel(new Point(center.x + x, center.y - y));
        this.drawPixel(new Point(center.x - x, center.y + y));
        this.drawPixel(new Point(center.x - x, center.y - y));
    }

    public void drawEllipse(Point center, int rx, int ry) {
        int rx2 = rx * rx;
        int ry2 = ry * ry;
        double pk = ry2 - rx2 * (ry - 1.0 / 4.0);
        int x = 0, y = ry;
        System.out.printf("center.x = %d, center.y = %d, rx = %d, ry = %d\n", center.x, center.y, rx, ry);
        while (ry2 * x < rx2 * y) {
            //System.out.println("[DEBUG] in drawEllipse, x= " + x + ", y= " + y);
            if (pk < 0) {
                pk = pk + (ry2 * ((2 * x) + 3));
                x++;
            } else {
                pk = pk + (ry2 * ((2 * x) + 3)) + (rx2 * ((-2 * y) + 2));
                x++;
                y--;
            }
            this.ellipseDraw4Points(center, x, y);
        }
        pk = ry2 * Math.pow(x + 1.0 / 2.0, 2) + rx2 * (y - 1) - rx2 * ry2;
        while (y >= 0) {
            if (pk > 0) {
                //pk = pk - 2 * rx2 * (y + 1) - rx2;
                pk += rx2 * ((-2 * y) + 3);
                y--;
            } else {
                //pk = pk + 2 * ry2 * (x + 1) - 2 * rx2 * (y + 1) - rx2;
                pk += (rx2 * ((-2 * y) + 3)) + (rx2 * ((2 * x) + 2));
                x++;
                y--;
            }
            this.ellipseDraw4Points(center, x, y);
        }
    }

    public Point translate(Point a, int xx, int yy) {
        return new Point(a.x + xx, a.y + yy);
    }

    public void showImage() {
        frame.updateImage(image);
    }

    public int getRotate_x(){
        return rotate_x;
    }

    public void setRotate_x(int rotate_x) {
        this.rotate_x = rotate_x;
    }

    public void setRotate_y(int rotate_y) {
        this.rotate_y = rotate_y;
    }

    public void setRotate_r(int rotate_r) {
        this.rotate_r = rotate_r;
    }

    public int getRotate_y() {
        return rotate_y;
    }

    public int getRotate_r() {
        return rotate_r;
    }
}
