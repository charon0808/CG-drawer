package draw;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;

public class CG {
	Graphics g;
	Frame frame;

	public CG(Frame f) {
		frame = f;
		g = (Graphics2D) frame.p.getGraphics();
		g.setColor(Color.black);
	}

	private void drawPixel(Point point) {

	}

	public void drawLine(Point a, Point b, int algorithm) {
		g.drawLine(a.x, a.y, b.x, b.y);
		// frame.update(g);
		// frame.setPoint(200, 100);
		// frame.setPoint(100, 100);
	}
}
