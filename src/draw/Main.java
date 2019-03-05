package draw;

import java.awt.Point;
import java.io.IOException;

public class Main {
	public static void main(String[] args) throws IOException {
		Frame frame=new Frame();
		CG cg=new CG(frame);
		Point a=new Point();
		Point b=new Point();
		a.x=b.y=150;
		b.x=a.y=200;
		cg.showImage();
		//cg.drawLine(a, b, 1);
		// frame.setPoint(200, 100);
		// frame.setPoint(100, 100);
	}

}
