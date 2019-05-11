package draw;


import Jama.Matrix;

import java.awt.*;
import java.util.HashMap;
import java.util.Map.Entry;

class Cli {
    HashMap<Integer, Matrix> rotateMsg;
    HashMap<Integer, Matrix> scaleMsg;
    HashMap<Integer, ClipWindow> clipMsg;
    private String[] command;
    private String commandLine;
    private CG cg;
    private String errInfo;
    private int lastColor;
    private boolean reDrawFlag;
    private HashMap<Integer, String[]> shapes;
    private HashMap<Integer, Integer> shapesColor;

    Cli(CG c) {
        cg = c;
        shapes = new HashMap<>();
        shapesColor = new HashMap<>();
        rotateMsg = new HashMap<>();
        scaleMsg = new HashMap<>();
        clipMsg = new HashMap<>();
        reDrawFlag = false;
        cg.setCli(this);
    }

    public void updateCli(String line) {
        errInfo = null;
        commandLine = line;
        command = line.split("\\s+");
        if (!this.commandResolve()) {
            if (errInfo == null)
                System.err.println("command cannot be resolved");
            else
                System.err.println("wrong parameters for command " + errInfo);
        }
    }

    public void updateCli(String[] cmd) {
        command = cmd.clone();
        if (!this.commandResolve()) {
            if (errInfo == null)
                System.err.println("command cannot be resolved");
            else
                System.err.println("wrong parameters for command " + errInfo);
        }
    }

    public void redraw() {
        reDrawFlag = true;
        cg.clearCanvas();
        for (Entry<Integer, String[]> entry : shapes.entrySet()) {
            cg.setColor(shapesColor.get(entry.getKey()));
            updateCli(entry.getValue());
        }
        cg.setColor(lastColor);
        reDrawFlag = false;
    }

    private boolean shapesPut(int id) {
        if (reDrawFlag)
            return true;
        if (shapes.containsKey(id)) {
            return false;
        } else {
            shapes.put(id, command);
            shapesColor.put(id, cg.getColor());
            return true;
        }
    }

    private boolean commandResolve() {
        switch (command[0]) {
            // resetCanvas width height
            case "resetCanvas": {
                if (command.length != 3) {
                    errInfo = "resetCanvas.\nUsage: resetCanvas canvas_width canvas_height";
                    return false;
                }
                try {
                    int width = Integer.parseInt(command[1]);
                    int height = Integer.parseInt(command[2]);
                    cg.resetCanvas(width, height);
                } catch (NumberFormatException e) {
                    errInfo = "drawPolygon.\ncanvas_width and canvas_height values must be integers";
                    return false;
                }
                break;
            }
            // saveCanvas name
            case "saveCanvas": {
                if (command.length != 2) {
                    errInfo = "saveCanvas.\nUsage: saveCanvas file_name";
                    return false;
                }
                String path;
                if ((path = cg.saveCanvas(command[1])) == null) {
                    System.err.println("sava Canvas failed.");
                } else {
                    System.out.println("current Canvas saved at " + path);
                }
                break;
            }
            // setColor R G B
            case "setColor": {
                if (command.length != 4) {
                    errInfo = "setColor.\nUsage: setColor R G B";
                    return false;
                }
                try {
                    int r = Integer.parseInt(command[1]);
                    int g = Integer.parseInt(command[2]);
                    int b = Integer.parseInt(command[3]);
                    int color = ((0xFF << 24) | (r << 16) | (g << 8) | b);
                    lastColor = color;
                    cg.setColor(color);
                } catch (NumberFormatException e) {
                    errInfo = "drawPolygon.\nR, G and B values must be integers";
                    return false;
                }
                break;
            }
            /*
             * drawLine id x1 y1 x2 y2 algorithm[DDA|Bresenham] default = DDA
             */
            case "drawLine": {
                if (command.length != 7) {
                    errInfo = "drawLine.\nUsage: drawLine id x1 y1 x2 y2 [ DDA | Bresenham default = DDA]";
                    return false;
                }
                int id;
                try {
                    id = Integer.parseInt(command[1]);
                    cg.setCurrentId(id);
                    cg.drawLine(new Point((int) Double.parseDouble(command[2]), (int) Double.parseDouble(command[3])), // Point a
                            new Point((int) Double.parseDouble(command[4]), (int) Double.parseDouble(command[5])), // Point b
                            command[6]);// algorithm
                    cg.setCurrentId(0x7fffffff);
                    cg.showImage();
                } catch (NumberFormatException e) {
                    errInfo = "drawLine.\nid values must be integers and coordinate values must be float number";
                    return false;
                }
                if (!shapesPut(id)) {
                    errInfo = "drawLine.\nid:" + id + " already existed.";
                    return false;
                }
                break;
            }
            /*
             * drawPolygon id n algorithm x1 y1 x2 y2 ... xn yn
             */
            case "drawPolygon": {
                if (command.length <= 4) {
                    errInfo = "drawPolygon.\nUsage: drawPolygon id n [ DDA | Bresenham default = DDA] x1 y1 x2 y2 ... xn yn";
                    return false;
                }
                int id;
                try {
                    int n = Integer.parseInt(command[2]);
                    if (command.length != 4 + 2 * n) {
                        errInfo = "drawPolygon.\nUsage: drawPolygon id n [ DDA | Bresenham default = DDA] x1 y1 x2 y2 ... xn yn";
                        return false;
                    }
                    id = Integer.parseInt(command[1]);
                    Point[] points = new Point[n];
                    for (int i = 0; i < n; i++) {
                        points[i] = new Point((int) Double.parseDouble(command[4 + 2 * i]),
                                (int) Double.parseDouble(command[4 + 2 * i + 1]));
                    }
                    cg.setCurrentId(id);
                    cg.drawPloygon(points, command[3]);
                    cg.setCurrentId(0x7fffffff);
                    cg.showImage();
                } catch (NumberFormatException e) {
                    errInfo = "drawPolygon.\nid , n and coordinate values must be integers";
                    return false;
                }
                if (!shapesPut(id)) {
                    errInfo = "drawLine.\nid:" + id + " already existed.";
                    return false;
                }
                break;
            }
            // drawEllipse id x y rx ry
            case "drawEllipse": {
                if (command.length != 6)
                    return false;
                int id;
                try {
                    id = Integer.parseInt(command[1]);
                    cg.setCurrentId(id);
                    cg.drawEllipse(new Point((int) Double.parseDouble(command[2]), (int) Double.parseDouble(command[3])),
                            (int) Double.parseDouble(command[4]), (int) Double.parseDouble(command[5]));
                    cg.setCurrentId(0x7fffffff);
                    cg.showImage();
                } catch (NumberFormatException e) {
                    errInfo = "drawEllipse.\nid must be integer, x, y, rx and ry values must be float number";
                    return false;
                }
                if (!shapesPut(id)) {
                    errInfo = "drawEllipse.\nid:" + id + " already existed.";
                    return false;
                }
                break;
            }
            // drawCurve id n algorithm x1 y1 x2 y2  xn yn
            case "drawCurve": {
                if (command.length <= 4) {
                    errInfo = "drawCurve.\nUsage: drawCurve id n [ Bezier | B-spline default = Bezier] x1 y1 x2 y2 ... xn yn";
                    return false;
                }
                int id;
                try {
                    id = Integer.parseInt(command[1]);
                    int n = Integer.parseInt(command[2]);
                    String algorithm = command[3];
                    Point[] points = new Point[n];
                    for (int i = 0; i < n; i++) {
                        points[i] = new Point((int) Double.parseDouble(command[4 + i * 2]), (int) Double.parseDouble(command[4 + i * 2 + 1]));
                    }
                    cg.drawCurve(points, algorithm);
                    cg.showImage();
                } catch (NumberFormatException e) {
                    errInfo = "drawCurve.\nid must be integer, x, y, rx and ry values must be float number";
                    return false;
                }
                break;
            }
            // translate id dx dy
            case "translate": {
                if (command.length != 4)
                    return false;
                String[] tranCommand;
                int dx, dy;
                try {
                    int nn = Integer.parseInt(command[1]);
                    if (!shapes.containsKey(nn)) {
                        errInfo = "translate.\nid:" + nn + " does not exists.";
                        return false;
                    }
                    tranCommand = shapes.get(nn);
                    dx = (int) Double.parseDouble(command[2]);
                    dy = (int) Double.parseDouble(command[3]);
                } catch (NumberFormatException e) {
                    errInfo = "translate.\nid values must be integers and dx and dy values must be float number.";
                    return false;
                }
                switch (tranCommand[0]) {
                    case "drawLine": {
                        tranCommand[2] = Integer.toString(((int) Double.parseDouble(tranCommand[2]) + dx));
                        tranCommand[3] = Integer.toString(((int) Double.parseDouble(tranCommand[3]) + dy));
                        tranCommand[4] = Integer.toString(((int) Double.parseDouble(tranCommand[4]) + dx));
                        tranCommand[5] = Integer.toString(((int) Double.parseDouble(tranCommand[5]) + dy));
                        break;
                    }
                    case "drawPolygon": {
                        int n = Integer.parseInt(tranCommand[2]);
                        for (int i = 0; i < n; i++) {
                            tranCommand[4 + 2 * i] = Integer.toString((int) Double.parseDouble(tranCommand[4 + 2 * i]) + dx);
                            tranCommand[4 + 2 * i + 1] = Integer.toString((int) Double.parseDouble(tranCommand[4 + 2 * i + 1]) + dy);
                        }
                        break;
                    }
                    case "drawEllipse": {
                        // TODO:
                        break;
                    }
                    case "drawCurve": {
                        // TODO:
                        break;
                    }
                    default: {
                        errInfo = "translate.\n";
                        return false;
                    }
                }
                redraw();
                break;
            }
            // rotate id x y r
            case "rotate": {
                if (command.length != 5)
                    return false;
                int id;
                try {
                    id = Integer.parseInt(command[1]);
                    int x = (int) Double.parseDouble(command[2]);
                    int y = (int) Double.parseDouble(command[3]);
                    int r = Integer.parseInt(command[4]);
                    Matrix currentMatrix = rotateMatrix(x, y, r);
                    if (rotateMsg.containsKey(id)) {
                        Matrix matrix = rotateMsg.get(id);
                        rotateMsg.replace(id, matrix.times(currentMatrix));
                    } else {
                        rotateMsg.put(id, currentMatrix);
                    }
                    redraw();
                } catch (NumberFormatException e) {
                    errInfo = "rotate.\nid and r must be integer and x and y must be float number.";
                }
                break;
            }
            // scale id x y s
            case "scale": {
                if (command.length != 5)
                    return false;
                int id;
                try {
                    id = Integer.parseInt(command[1]);
                    int x = (int) Double.parseDouble(command[2]);
                    int y = (int) Double.parseDouble(command[3]);
                    double s = Double.parseDouble(command[4]);
                    Matrix currentMatrix = scaleMatrix(x, y, s, s);
                    if (scaleMsg.containsKey(id)) {
                        Matrix matrix = scaleMsg.get(id);
                        scaleMsg.replace(id, matrix.times(currentMatrix));
                    } else {
                        scaleMsg.put(id, currentMatrix);
                    }
                    redraw();
                } catch (NumberFormatException e) {
                    errInfo = "scale.\nid and s must be integer and x and y must be float number.";
                }
                break;
            }
            // clip id x1 y1 x2 y2 algorithm
            case "clip": {
                if (command.length != 7)
                    return false;
                int id;
                try {
                    id = Integer.parseInt(command[1]);
                    int x1 = (int) Double.parseDouble(command[2]);
                    int y1 = (int) Double.parseDouble(command[3]);
                    int x2 = (int) Double.parseDouble(command[4]);
                    int y2 = (int) Double.parseDouble(command[5]);
                    String algorithm = command[6];
                    clipMsg.put(id, new ClipWindow(x1, y1, x2, y2, algorithm));
                } catch (NumberFormatException e) {
                    return false;
                }
                this.redraw();
                break;
            }
            default: {
                return false;
            }
        }
        return true;
    }

    private Matrix rotateMatrix(int x, int y, int r) {
        double[][] a = {{1, 0, 0}, {0, 1, 0}, {(double) -x, (double) -y, 1}};
        double[][] b = {{Math.cos(r), Math.sin(r), 0}, {-Math.sin(r), Math.cos(r), 0}, {0, 0, 1}};
        double[][] c = {{1, 0, 0}, {0, 1, 0}, {(double) x, (double) y, 1}};
        return ((new Matrix(a)).times(new Matrix(b))).times(new Matrix(c));
    }

    private Matrix scaleMatrix(int x, int y, double sx, double sy) {
        double[][] a = {{1, 0, 0}, {0, 1, 0}, {(double) -x, (double) -y, 1}};
        double[][] b = {{sx, 0, 0}, {0, sy, 0}, {0, 0, 1}};
        double[][] c = {{1, 0, 0}, {0, 1, 0}, {(double) x, (double) y, 1}};
        return ((new Matrix(a)).times(new Matrix(b))).times(new Matrix(c));
    }
}

final class ClipWindow {
    private int xwmin;
    private int ywmin;
    private int xwmax;
    private int ywmax;
    private String algorithm;

    ClipWindow(int x1, int y1, int x2, int y2, String a) {
        this.xwmin = x1;
        this.ywmin = y1;
        this.xwmax = x2;
        this.ywmax = y2;
        this.algorithm = a;
    }

    public int getXwmin() {
        return xwmin;
    }

    public int getYwmin() {
        return ywmin;
    }

    public int getXwmax() {
        return xwmax;
    }

    public int getYwmax() {
        return ywmax;
    }

    public String getAlgorithm() {
        return algorithm;
    }
}
