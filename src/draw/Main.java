package draw;

import java.awt.Point;
import java.io.IOException;

public class Main {
	public static void main(String[] args) throws IOException {
		Frame frame = new Frame();
		CG cg = new CG(frame, 300, 400);
		Point a = new Point();
		Point b = new Point();
		a.x = a.y = 250;
		b.x = b.y = 300;
		Point[] ps = { new Point(50, 50), new Point(100, 100), new Point(100, 200), new Point(50, 200) };
		cg.drawPloygon(ps, 1);
		cg.showImage();
	}

}
