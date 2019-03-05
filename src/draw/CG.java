package draw;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class CG {
	Graphics g;
	Frame frame;
	BufferedImage image;

	public CG(Frame f) throws IOException {
		frame = f;
		image = ImageIO.read(new File("C:\\Users\\Xiao Li\\Desktop\\New folder\\1.png"));
	}

	private void drawPixel(Point point) {

	}

	public void drawLine(Point a, Point b, int algorithm) {
		g.drawLine(a.x, a.y, b.x, b.y);
		// frame.update(g);
		// frame.setPoint(200, 100);
		// frame.setPoint(100, 100);
	}

	public void showImage() {
		frame.updateImage(image);
	}
}
