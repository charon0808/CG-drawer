package draw;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Frame extends JFrame implements MouseListener {

	public JPanel p;
	private JButton b1, b2, b3;
	private Point point = new Point();
	public Graphics g;
	boolean flag;

	public Frame() {
		p = new JPanel();
		b1 = new JButton("test1");
		b2 = new JButton("test2");
		b3 = new JButton("test3");
		p.add(b1);
		p.add(b2);
		p.add(b3);
		p.setBackground(Color.white);
		flag = true;

		// this.add(p);
		this.add(p, BorderLayout.CENTER);

		this.setSize(400, 300);
		this.setTitle("CG-drawer");
		this.setLocation(700, 500);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setVisible(true);
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	/*
	 * @Override public void paint(Graphics graphics) { // 必须先调用父类的paint方法 if (flag)
	 * super.paint(graphics); flag = false; g = graphics; // graphics.drawLine(100,
	 * 100, 200, 200); graphics.drawLine(point.x, point.y, point.x + 100, point.y +
	 * 100); }
	 */

	public void setPoint(int x, int y) {
		point.x = x;
		point.y = y;
	}

}
