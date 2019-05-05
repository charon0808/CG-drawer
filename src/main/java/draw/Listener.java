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

    public Listener(CG c, Frame f) {
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
        if (actionCommand.equals("open")) {
            JFileChooser jFileChooser = new JFileChooser(System.getProperty("user.dir"));
            if (JFileChooser.APPROVE_OPTION == jFileChooser.showOpenDialog(frame)) {
                File openFile = jFileChooser.getSelectedFile();
                try {
                    cg.setImage(ImageIO.read(openFile));
                } catch (IOException e1) {
                    // TODO:
                }
            }
        } else if (actionCommand.equals("save")) {
            JFileChooser jFileChooser = new JFileChooser(System.getProperty("user.dir"));
            if (JFileChooser.APPROVE_OPTION == jFileChooser.showSaveDialog(frame)) {
                File saveFile = jFileChooser.getSelectedFile();
                try {
                    ImageIO.write(cg.getImage(), "bmp", saveFile);
                } catch (IOException e1) {
                    // TODO:
                }
            }
        } else if (actionCommand.equals("clear")) {
            cg.resetCanvas(cg.getWidth(), cg.getHeight());
        } else
            currentShape = actionCommand;
        // System.err.println("shape=" + currentShape);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        Point tmpPoint = e.getPoint();
        tmpPoint.y = cg.getHeight() - tmpPoint.y;
        arrayList.add(tmpPoint);
        // System.err.println("start point x=" + e.getX() + ", y=" + (cg.getHeight() -
        // e.getY()));
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        Point tmpPoint = e.getPoint();
        tmpPoint.y = cg.getHeight() - tmpPoint.y;
        arrayList.add(tmpPoint);
        // System.err.println("end point x=" + e.getX() + ", y=" + (cg.getHeight() -
        // e.getY()));
        if (checkArrayList()) {
            if (currentShape.equals("line")) {
                String command = String.format("drawLine %d %d %d %d %d %s", drawID--, arrayList.get(0).x,
                        arrayList.get(0).y, arrayList.get(1).x, arrayList.get(1).y, "navie");
                cg.getCli().updateCli(command);
            } else if (currentShape.equals("rectangle")) { // drawPolygon id n algorithm x1 y1 x2 y2 ... xn yn
                Point aPoint = arrayList.get(0);
                Point bPoint = arrayList.get(1);
                String command = String.format("drawPolygon %d %d %s %d %d %d %d %d %d %d %d", drawID--, 4, "naive",
                        aPoint.x, aPoint.y, aPoint.x, bPoint.y, bPoint.x, bPoint.y, bPoint.x, aPoint.y);
                cg.getCli().updateCli(command);
            }
        }
        arrayList.clear();
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
