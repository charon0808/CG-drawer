package draw;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class CG {
	private Frame frame;
	private BufferedImage image;

	private int width, height;
	private int color;

	public CG(Frame f) throws IOException {
		frame = f;
	}

	public void resetCanvas(int w, int h) {
		width = w;
		height = h;
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		color = 0;
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				image.setRGB(i, j, 0xffffffff);
			}
		}
		this.showImage();
	}

	public String saveCanvas(String name) {
		try {
			ImageIO.write(image, "bmp", new File("name.bmp"));
			return System.getProperty("user.dir") + "name.bmp";
		} catch (IOException e) {
			return null;
		}
	}

	public void setColor(int c) {
		color = c;
	}

	private void drawPixel(Point point) {
		if (point.x < width && point.y < height)
			image.setRGB(point.x, point.y, color);
	}

	public void drawLine(Point a, Point b, int algorithm) {
		if (a.x == b.x) {
			if (a.y > b.y) {
				Point tmp = a;
				a = b;
				b = tmp;
			}
			for (int i = a.y; i < b.y; i++) {
				this.drawPixel(new Point(a.x, i));
			}
		} else {
			if (a.x > b.x) {
				Point tmp = a;
				a = b;
				b = tmp;
			}
			int k = (b.y - a.y) / (b.x - a.x);
			for (int i = a.x; i < b.x; i++) {
				this.drawPixel(new Point(i, a.y + (i - a.x) * k));
			}
		}
	}

	public void drawPloygon(Point[] points, int algorithm) {
		for (int i = 0; i < points.length - 1; i++) {
			this.drawLine(points[i], points[i + 1], algorithm);
		}
		this.drawLine(points[0], points[points.length - 1], algorithm);
	}

	public void showImage() {
		frame.updateImage(image);
	}

}
