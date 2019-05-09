package draw;

import Jama.Matrix;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class CG {
    private Frame frame;
    private BufferedImage image;

    private int width, height;
    private int color;
    private Cli cli;
    private int currentId;

    CG(Frame f) throws IOException {
        frame = f;
        frame.setCG(this);
        frame.InitFrame();
    }

    Cli getCli() {
        return cli;
    }

    void setCli(Cli cc) {
        cli = cc;
    }

    void resetCanvas(int w, int h) {
        width = w;
        height = h;
        image = new BufferedImage(width, height + 1, BufferedImage.TYPE_INT_RGB);
        setColor(0);
        clearCanvas();
    }

    void clearCanvas() {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height + 1; j++) {
                image.setRGB(i, j, 0xffffffff);
            }
        }
        this.drawDashs();
        this.showImage();
    }

    String saveCanvas(String name) {
        try {
            ImageIO.write(image, "bmp", new File(name + ".bmp"));
            return System.getProperty("user.dir") + "\\" + name + ".bmp";
        } catch (IOException e) {
            return null;
        }
    }

    void setColor(int r, int g, int b) {
        color = (0xFF << 24) | (r << 16) | (g << 8) | b;
        frame.setSlider(r, g, b);
    }

    int getColor() {
        return color;
    }

    void setColor(int c) {
        color = c;
        frame.setSlider((c & 0xff0000) >> 16, (c & 0xff00) >> 8, c & 0xff);
    }

    int getHeight() {
        return height;
    }

    int getWidth() {
        return width;
    }

    BufferedImage getImage() {
        return image;
    }

    void setImage(BufferedImage bufferedImage) {
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

    private Point rotateOrScale(Point point, String type) {
        HashMap tmpMap;
        if (type.equals("rotate")) {
            tmpMap = cli.rotateMsg;
        } else tmpMap = cli.scaleMsg;
        double[][] p = {{point.x, point.y, 1}};
        Matrix matrix = (new Matrix(p)).times((Matrix) tmpMap.get(currentId));
        return new Point((int) matrix.get(0, 0), (int) matrix.get(0, 1));
    }

    private void drawPixel(Point point) {
        if (cli != null && cli.rotateMsg.containsKey(currentId)) {
            //System.out.printf("before rotate, x=%d, y=%d\n", point.x, point.y);
            point = rotateOrScale(point, "rotate");
            //System.out.printf("after rotate, x=%d, y=%d\n", point.x, point.y);
        }
        if (cli != null && cli.scaleMsg.containsKey(currentId)) {
            point = rotateOrScale(point, "scale");
        }
        if (point.x > 0 && point.y > 0 && point.x < width && point.y < height) {
            image.setRGB(point.x, height - point.y, color);
        }
    }

    void drawLine(Point a, Point b, String algorithm) {
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

    void drawPloygon(Point[] points, String algorithm) {
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

    void drawEllipse(Point center, int rx, int ry) {
        int rx2 = rx * rx;
        int ry2 = ry * ry;
        double pk = ry2 - rx2 * (ry - 0.25);
        int x = 0, y = ry;
        //System.out.printf("center.x = %d, center.y = %d, rx = %d, ry = %d\n", center.x, center.y, rx, ry);
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
        pk = ry2 * Math.pow((double) x + 0.5, 2) + rx2 * (y - 1) * (y - 1) - rx2 * ry2;
        while (y >= 0) {
            if (pk > 0) {
                //pk = pk - 2 * rx2 * (y + 1) - rx2;
                pk += rx2 * ((-2 * y) + 3);
                y--;
            } else {
                //pk = pk + 2 * ry2 * (x + 1) - 2 * rx2 * (y + 1) - rx2;
                pk += (rx2 * ((-2 * y) + 3)) + (ry2 * ((2 * x) + 2));
                x++;
                y--;
            }
            this.ellipseDraw4Points(center, x, y);
        }
    }

    public Point translate(Point a, int xx, int yy) {
        return new Point(a.x + xx, a.y + yy);
    }

    void showImage() {
        frame.updateImage(image);
    }

    public int getCurrentId() {
        return currentId;
    }

    void setCurrentId(int currentId) {
        this.currentId = currentId;
    }
}
