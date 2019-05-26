package draw;

import Jama.Matrix;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CG {
    Color[] colors = {Color.BLACK, Color.BLUE, Color.CYAN, Color.DARK_GRAY, Color.GRAY, Color.GREEN, Color.LIGHT_GRAY, Color.MAGENTA, Color.ORANGE, Color.PINK, Color.red, Color.YELLOW};
    private Frame frame;
    private BufferedImage image;
    private int width, height;
    private int color;
    private Cli cli;
    private int currentId;
    private boolean isFindNear;
    private Point findNearPoint;


    CG(Frame f) throws IOException {
        frame = f;
        frame.setCG(this);
        frame.InitFrame();
        isFindNear = false;
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
        if (cli != null) {
            if (cli.shapes != null)
                cli.shapes.clear();
            if (cli.shapesColor != null)
                cli.shapesColor.clear();
            if (cli.rotateMsg != null)
                cli.rotateMsg.clear();
            if (cli.clipMsg != null)
                cli.clipMsg.clear();
            if (cli.scaleMsg != null)
                cli.scaleMsg.clear();
        }
        setColor(0);
        image = new BufferedImage(width, height + 1, BufferedImage.TYPE_INT_RGB);
        clearCanvas();
    }

    void clearCanvas() {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height + 1; j++) {
                image.setRGB(i, j, 0xffffffff);
            }
        }
        // this.drawDashs();
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

    private void drawDashs() {
        for (int i = 0; i <= width; i += 100) {
            for (int j = 0; j <= height; j += 2) {
                drawPixelLine(new Point(i, j));
            }
        }
        for (int i = 0; i <= height; i += 100) {
            for (int j = 0; j <= width; j += 2) {
                drawPixelLine(new Point(j, i));
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

    private boolean drawPixel(Point point) {
        if (cli != null && cli.rotateMsg.containsKey(currentId)) {
            //System.out.printf("before rotate, x=%d, y=%d\n", point.x, point.y);
            point = rotateOrScale(point, "rotate");
            //System.out.printf("after rotate, x=%d, y=%d\n", point.x, point.y);
        }
        if (cli != null && cli.scaleMsg.containsKey(currentId)) {
            point = rotateOrScale(point, "scale");
        }
        if (isFindNear) {
            return Math.abs(point.x - findNearPoint.x) <= 10 && Math.abs((point.y - findNearPoint.y)) <= 10;
        }
        if (point.x > 0 && point.y > 0 && point.x < width && point.y < height) {
            image.setRGB(point.x, height - point.y, color);
        }
        return false;
    }

    private boolean drawPixelLine(Point point) {
        if (isFindNear) {
            return Math.abs(point.x - findNearPoint.x) <= 10 && Math.abs((point.y - findNearPoint.y)) <= 10;
        }
        if (point.x > 0 && point.y > 0 && point.x < width && point.y < height) {
            image.setRGB(point.x, height - point.y, color);
        }
        return false;
    }

    boolean drawLine(Point a, Point b, String algorithm) {
        if (cli != null && cli.rotateMsg.containsKey(currentId)) {
            a = rotateOrScale(a, "rotate");
            b = rotateOrScale(b, "rotate");
        }
        if (cli != null && cli.scaleMsg.containsKey(currentId)) {
            a = rotateOrScale(a, "scale");
            b = rotateOrScale(b, "scale");
        }
        if (cli != null && cli.clipMsg.containsKey(currentId)) {
            ClipWindow clipWindow = cli.clipMsg.get(currentId);
            //System.out.println("before clip, point a=" + a.toString() + ", b=" + b.toString());
            if (algorithm.equals("Liang-Barsky")) {
                if (!LiangBarsky(clipWindow, a, b)) {
                    return false;
                }
                // System.out.println("after clip, point a=" + a.toString() + ", b=" + b.toString());
            } else if (!CohenSutherland(clipWindow, a, b)) {
                return false;
            }
        }
        if (algorithm.equals("Bresenham")) {
            return drawLineBresenham(a, b);
        } else {
            return drawLineDDA(a, b);
        }
    }

    private boolean drawLineDDA(Point a, Point b) {
        if (a.x == b.x) {
            if (a.y > b.y) {
                Point tmp = a;
                a = b;
                b = tmp;
            }
            for (int i = a.y; i < b.y; i++) {
                if (this.drawPixelLine(new Point(a.x, i)))
                    return true;
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
                    if (this.drawPixelLine(new Point(i, (int) (a.y + (i - a.x) * k))))
                        return true;
                }
            } else {
                if (a.y > b.y) {
                    Point tmp = a;
                    a = b;
                    b = tmp;
                }
                for (int i = a.y; i < b.y; i++) {
                    if (this.drawPixelLine(new Point((int) ((i - a.y) / k + a.x), i))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean drawLineBresenham(Point a, Point b) { // 73 230 174 77
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
                if (this.drawPixelLine(new Point(x, y)))
                    return true;
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
                if (this.drawPixelLine(new Point(x, y)))
                    return true;
                if (D > 0) {
                    x = x + xi;
                    D = D - 2 * dy;
                }
                D = D + 2 * dx;
            }
        }
        return false;
    }

    boolean drawPloygon(Point[] points, String algorithm) {
        for (int i = 0; i < points.length - 1; i++) {
            if (this.drawLine(points[i], points[i + 1], algorithm))
                return true;
        }
        return this.drawLine(points[0], points[points.length - 1], algorithm);
    }

    private boolean ellipseDraw4Points(Point center, int x, int y) {
        return this.drawPixel(new Point(center.x + x, center.y + y)) ||
                this.drawPixel(new Point(center.x + x, center.y - y)) ||
                this.drawPixel(new Point(center.x - x, center.y + y)) ||
                this.drawPixel(new Point(center.x - x, center.y - y));
    }

    boolean drawEllipse(Point center, int rx, int ry) {
        int rx2 = rx * rx;
        int ry2 = ry * ry;
        double pk = ry2 - rx2 * (ry - 0.25);
        int x = 0, y = ry;
        while (ry2 * x < rx2 * y) {
            if (pk < 0) {
                pk = pk + (ry2 * ((2 * x) + 3));
                x++;
            } else {
                pk = pk + (ry2 * ((2 * x) + 3)) + (rx2 * ((-2 * y) + 2));
                x++;
                y--;
            }
            if (this.ellipseDraw4Points(center, x, y))
                return true;
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
            if (this.ellipseDraw4Points(center, x, y))
                return true;
        }
        return false;
    }

    void drawCurve(Point[] points, String algorithm) {
        if (algorithm.equals("B-spline")) {
            drawCurveBSpline(points);
        } else { // Bezier
            drawCurveBezier(points);
        }
    }

    private void drawCurveBezier(Point[] points) {

    }

    private void drawCurveBSpline(Point[] points) {

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

    private short CohenSutherlandNum(ClipWindow clipWindow, Point point) {
        short symbol = 0x0000;
        if (point.y > clipWindow.getYwmax()) {
            symbol |= 0x0008;
        } else if (point.y < clipWindow.getYwmin()) {
            symbol |= 0x0004;
        }
        if (point.x > clipWindow.getXwmax()) {
            symbol |= 0x0002;
        } else if (point.x < clipWindow.getXwmin()) {
            symbol |= 0x0001;
        }
        return symbol;
    }

    private boolean CohenSutherland(ClipWindow clipWindow, Point a, Point b) {
        short aNum = CohenSutherlandNum(clipWindow, a);
        short bNum = CohenSutherlandNum(clipWindow, b);
        if ((aNum & bNum) != 0)
            return false;
        while ((aNum != 0 || bNum != 0)) {
            if ((aNum & bNum) != 0)
                return false;
            short num;
            Point tmpPoint;
            int flag;
            if (aNum != 0) {
                flag = 0;
                num = aNum;
                tmpPoint = a;
            } else {
                flag = 1;
                num = bNum;
                tmpPoint = b;
            }
            int ax = a.x;
            int ay = a.y;
            int bx = b.x;
            int by = b.y;
            if ((num & 1) != 0) {
                tmpPoint.x = clipWindow.getXwmin();
                tmpPoint.y = (by - ay) * (tmpPoint.x - ax) / (bx - ax) + ay;
            } else if ((num & 2) != 0) {
                tmpPoint.x = clipWindow.getXwmax();
                tmpPoint.y = (by - ay) * (tmpPoint.x - ax) / (bx - ax) + ay;
            } else if ((num & 4) != 0) {
                tmpPoint.y = clipWindow.getYwmin();
                tmpPoint.x = (bx - ax) * (tmpPoint.y - ay) / (by - ay) + ax;
            } else if ((num & 8) != 0) {
                tmpPoint.y = clipWindow.getYwmax();
                tmpPoint.x = (bx - ax) * (tmpPoint.y - ay) / (by - ay) + ax;
            }
            if (flag == 0) {
                aNum = CohenSutherlandNum(clipWindow, a);
            } else {
                bNum = CohenSutherlandNum(clipWindow, b);
            }
        }
        return true;
    }

    private boolean LiangBarsky(ClipWindow clipWindow, Point a, Point b) {
        double u0 = 0.0;
        double u1 = 1.0;
        double xdelta = b.x - a.x;
        double ydelta = b.y - a.y;
        double p = 0, q = 0;
        for (int i = 0; i < 4; i++) {
            if (i == 0) {
                p = -xdelta;
                q = a.x - clipWindow.getXwmin();
            }
            if (i == 1) {
                p = xdelta;
                q = clipWindow.getXwmax() - a.x;
            }
            if (i == 2) {
                p = -ydelta;
                q = a.y - clipWindow.getYwmin();
            }
            if (i == 3) {
                p = ydelta;
                q = clipWindow.getYwmax() - a.y;
            }
            double r = q / p;
            if (p == 0 && q < 0)
                return false;
            if (p < 0) {
                if (r > u1)
                    return false;
                else if (r > u0)
                    u0 = r;
            } else if (p > 0) {
                if (r < u0)
                    return false;
                else if (r < u1)
                    u1 = r;
            }
        }
        int aa = a.x, bb = a.y;
        a.x = (int) (a.x + u0 * xdelta);
        a.y = (int) (a.y + u0 * ydelta);
        b.x = (int) (aa + u1 * xdelta);
        b.y = (int) (bb + u1 * ydelta);
        return true;
    }

    int findNearShape(Point point) {
        isFindNear = true;
        this.setFindNearPoint(point);
        for (Map.Entry<Integer, String[]> entry : cli.shapes.entrySet()) {
            int id = entry.getKey();
            this.setCurrentId(id);
            String[] commands = entry.getValue();
            switch (commands[0]) {
                case "drawLine": {
                    int x1 = (int) Double.parseDouble(commands[2]);
                    int y1 = (int) Double.parseDouble(commands[3]);
                    int x2 = (int) Double.parseDouble(commands[4]);
                    int y2 = (int) Double.parseDouble(commands[5]);
                    if (drawLine(new Point(x1, y1), new Point(x2, y2), "default")) {
                        isFindNear = false;
                        return id;
                    }
                    break;
                }
                case "drawPolygon": {
                    int n = Integer.parseInt(commands[2]);
                    Point[] points = new Point[n];
                    for (int i = 0; i < n; i++) {
                        points[i] = new Point((int) Double.parseDouble(commands[4 + 2 * i]),
                                (int) Double.parseDouble(commands[4 + 2 * i + 1]));
                    }
                    if (drawPloygon(points, commands[3])) {
                        isFindNear = false;
                        return id;
                    }
                    break;
                }
                case "drawEllipse": {
                    // TODO:
                    if (drawEllipse(new Point((int) Double.parseDouble(commands[2]), (int) Double.parseDouble(commands[3])),
                            (int) Double.parseDouble(commands[4]), (int) Double.parseDouble(commands[5]))) {
                        isFindNear = false;
                        return id;
                    }
                    break;
                }
                case "drawCurve": {
                    // TODO:
                    break;
                }
            }
            this.setCurrentId(0x7fffffff);
        }
        isFindNear = false;
        return 0x7fffffff;
    }

    Object[] findLinesInClipWindow(ClipWindow clipWindow) {
        ArrayList<Integer> ret = new ArrayList<>();
        Map shapesM = cli.shapes;
        for (Map.Entry<Integer, String[]> entry : (Set<Map.Entry<Integer, String[]>>) shapesM.entrySet()) {
            if (entry.getValue()[0].equals("drawLine")) {
                String[] command = entry.getValue();
                int id;
                try {
                    id = Integer.parseInt(command[1]);
                    Point a = new Point((int) Double.parseDouble(command[2]), (int) Double.parseDouble(command[3]));
                    Point b = new Point((int) Double.parseDouble(command[4]), (int) Double.parseDouble(command[5]));
                    if (!CohenSutherland(clipWindow, a, b))
                        continue;
                    short aNum = CohenSutherlandNum(clipWindow, a);
                    short bNum = CohenSutherlandNum(clipWindow, b);
                    if ((aNum & bNum) == 0)
                        ret.add(id);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }
        return ret.toArray();
    }

    private void setFindNearPoint(Point findNearPoint) {
        this.findNearPoint = findNearPoint;
    }
}
