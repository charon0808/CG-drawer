package draw;

import java.awt.Point;

public class Main {
	public static void main(String[] args) {
		Frame frame=new Frame();
		CG cg=new CG(frame);
		Point a=new Point();
		Point b=new Point();
		a.x=b.y=150;
		b.x=a.y=200;
		cg.drawLine(a, b, 1);
		// frame.setPoint(200, 100);
		// frame.setPoint(100, 100);
	}

}
