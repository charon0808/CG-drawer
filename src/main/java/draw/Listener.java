package draw;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Listener extends MouseAdapter implements ActionListener {
    private String currentShape;
    private ArrayList<Point> arrayList;
    private CG cg;
    private int drawID;
    private Frame frame;
    private int mousePressedId;
    private Point lastTimerPoint;
    private Point timerPoint;
    private Point polygonStartPoint;
    private Point polygonLastPoint;

    Listener(CG c, Frame f) {
        // TODO Auto-generated constructor stub
        currentShape = "line";
        arrayList = new ArrayList<>();
        drawID = -1;
        cg = c;
        frame = f;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        String actionCommand = ((JButton) e.getSource()).getActionCommand();
        switch (actionCommand) {
            case "open": {
                JFileChooser jFileChooser = new JFileChooser(System.getProperty("user.dir"));
                if (JFileChooser.APPROVE_OPTION == jFileChooser.showOpenDialog(frame)) {
                    File openFile = jFileChooser.getSelectedFile();
                    try {
                        cg.setImage(ImageIO.read(openFile));
                    } catch (IOException e1) {
                        // TODO:
                    }
                }
                break;
            }
            case "save": {
                JFileChooser jFileChooser = new JFileChooser(System.getProperty("user.dir"));
                if (JFileChooser.APPROVE_OPTION == jFileChooser.showSaveDialog(frame)) {
                    File saveFile = jFileChooser.getSelectedFile();
                    try {
                        ImageIO.write(cg.getImage(), "bmp", saveFile);
                    } catch (IOException e1) {
                        // TODO:
                    }
                }
                break;
            }
            case "clear": {
                cg.resetCanvas(cg.getWidth(), cg.getHeight());
                break;
            }
            default:
                currentShape = actionCommand;
                break;
        }
        // System.err.println("shape=" + currentShape);
    }

    private void updateLoc() {
        if (mousePressedId == 0x7fffffff)
            return;
        String[] tranCommand = cg.getCli().shapes.get(mousePressedId);
        int dx = -lastTimerPoint.x + timerPoint.x;
        int dy = lastTimerPoint.y - timerPoint.y;
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
                tranCommand[2] = Integer.toString(((int) Double.parseDouble(tranCommand[2]) + dx));
                tranCommand[3] = Integer.toString(((int) Double.parseDouble(tranCommand[3]) + dy));
                break;
            }
            case "drawCurve": {
                // TODO:
                break;
            }
        }
        cg.getCli().redraw();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        Point tmpPoint = e.getPoint();
        lastTimerPoint = (Point) tmpPoint.clone();
        timerPoint = (Point) tmpPoint.clone();
        tmpPoint.y = cg.getHeight() - tmpPoint.y;
        if (currentShape.equals("drag") || currentShape.equals("rotate")) {
            mousePressedId = cg.findNearShape(tmpPoint);
        } else if (currentShape.equals("polygon")) {
            if (polygonStartPoint == null) {
                polygonStartPoint = (Point) tmpPoint.clone();
                polygonLastPoint = (Point) tmpPoint.clone();
            }
        }
        arrayList.add(tmpPoint);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        Point tmpPoint = e.getPoint();
        tmpPoint.y = cg.getHeight() - tmpPoint.y;
        arrayList.add(tmpPoint);
        if (checkArrayList()) {
            switch (currentShape) {
                case "line": {
                    String command = String.format("drawLine %d %d %d %d %d %s", drawID--, arrayList.get(0).x,
                            arrayList.get(0).y, arrayList.get(1).x, arrayList.get(1).y, "navie");
                    cg.getCli().updateCli(command);
                    break;
                }
                case "rectangle": { // drawPolygon id n algorithm x1 y1 x2 y2 ... xn yn
                    Point aPoint = arrayList.get(0);
                    Point bPoint = arrayList.get(1);
                    String command = String.format("drawPolygon %d %d %s %d %d %d %d %d %d %d %d", drawID--, 4, "naive",
                            aPoint.x, aPoint.y, aPoint.x, bPoint.y, bPoint.x, bPoint.y, bPoint.x, aPoint.y);
                    cg.getCli().updateCli(command);
                    break;
                }
                case "ellipse": {
                    Point aPoint = arrayList.get(0);
                    Point bPoint = arrayList.get(1);
                    // drawEllipse id x y rx ry
                    String command = String.format("drawEllipse %d %d %d %d %d", drawID--, (aPoint.x + bPoint.x) / 2, (aPoint.y + bPoint.y) / 2, Math.abs(aPoint.x - bPoint.x) / 2, Math.abs(aPoint.y - bPoint.y) / 2);
                    cg.getCli().updateCli(command);
                    break;
                }
                case "clip": {
                    Point aPoint = arrayList.get(0);
                    Point bPoint = arrayList.get(1);
                    break;
                }
                case "drag": {
                    break;
                }
                case "polygon": {
                    boolean tmpFlag = false;
                    if (Math.abs(arrayList.get(1).x - polygonStartPoint.x) <= 10 && Math.abs(arrayList.get(1).y - polygonStartPoint.y) <= 10) {
                        arrayList.remove(1);
                        arrayList.add(polygonStartPoint);
                        tmpFlag = true;
                    }
                    String command = String.format("drawLine %d %d %d %d %d %s", drawID--, polygonLastPoint.x,
                            polygonLastPoint.y, arrayList.get(1).x, arrayList.get(1).y, "navie");
                    cg.getCli().updateCli(command);
                    polygonLastPoint = arrayList.get(1);
                    if (tmpFlag) {
                        polygonStartPoint = null;
                        polygonLastPoint = null;
                    }
                    break;
                }
                case "rotate": {
                    System.out.println(mousePressedId);
                    String inputValue;
                    if (mousePressedId != 0x7fffffff) {
                        try {
                            String pointInput = JOptionPane.showInputDialog("input the rotate center x and y coordinate, separated by commas");
                            String[] xy = pointInput.split(",");
                            String angleInput = JOptionPane.showInputDialog("input rotate angle");
                            int angle = (int) Double.parseDouble(angleInput);
                            int x = (int) Double.parseDouble(xy[0]);
                            int y = (int) Double.parseDouble(xy[1]);
                            String command = String.format("rotate %d %d %d %d", mousePressedId, x, y, angle);
                            System.out.println(command);
                            cg.getCli().updateCli(command);
                        } catch (Exception ee) {
                            JOptionPane.showMessageDialog(null, "Invalid input!");
                        }
                    }
                    break;
                }
            }

        }
        arrayList.clear();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (currentShape.equals("drag")) {
            lastTimerPoint = timerPoint;
            timerPoint = new Point(e.getX(), e.getY());
            updateLoc();
        }
    }

    private boolean checkArrayList() {
        for (Point a : arrayList) {
            if (a.x > cg.getWidth() || a.y > cg.getHeight() || a.x < 0 || a.y < 0) {
                return false;
            }
        }
        return true;
    }

}
