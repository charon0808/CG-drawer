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
	private Cli cli;

	public CG(Frame f) throws IOException {
		frame = f;
		frame.setCG(this);
		frame.InitFrame();
	}

	public void setCli(Cli cc) {
		cli = cc;
	}

	public Cli getCli() {
		return cli;
	}

	public void resetCanvas(int w, int h) {
		width = w;
		height = h;
		image = new BufferedImage(width, height + 1, BufferedImage.TYPE_INT_RGB);
		setColor(0);
		clearCanvas();
	}

	public void clearCanvas() {
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height + 1; j++) {
				image.setRGB(i, j, 0xffffffff);
			}
		}
		// drawDashs();
		this.showImage();
	}

	public String saveCanvas(String name) {
		try {
			ImageIO.write(image, "bmp", new File(name + ".bmp"));
			return System.getProperty("user.dir") + "\\" + name + ".bmp";
		} catch (IOException e) {
			return null;
		}
	}

	public void setColor(int r, int g, int b) {
		color = (0xFF << 24) | (r << 16) | (g << 8) | b;
		frame.setSlider(r, g, b);
	}

	public void setColor(int c) {
		color = c;
		frame.setSlider((c & 0xff0000) >> 16, (c & 0xff00) >> 8, c & 0xff);
	}

	public void setImage(BufferedImage bufferedImage) {
		image = bufferedImage;
		width = image.getWidth();
		height = image.getHeight();
		showImage();
	}

	public int getColor() {
		return color;
	}

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}

	public BufferedImage getImage() {
		return image;
	}

	public void drawDashs() {
		for (int i = 0; i <= width; i += 100) {
			for (int j = 0; j <= height; j += 2) {
				drawPixel(new Point(i, j));
			}
		}
		for (int i = 0; i <= height; i += 100) {
			for (int j = 0; j <= width; j += 2) {
				drawPixel(new Point(j, i));
			}
		}
	}

	private void drawPixel(Point point) {
		// System.out.println("drawPixel, x= " + point.x + ", height - y=" + (height -
		// point.y));
		if (point.x < width && point.y < height) {
			image.setRGB(point.x, height - point.y, color);
		}
	}

	public void drawLine(Point a, Point b, String algorithm) {
		if (algorithm.equals("Bresenham")) {
			drawLineBresenham(a, b);
		} else {
			drawLineDDA(a, b);
		}
	}

	private void drawLineDDA(Point a, Point b) {
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
			double k = ((double) b.y - a.y) / (double) (b.x - a.x);
			if (Math.abs(k) <= 1) {
				if (a.x > b.x) {
					Point tmp = a;
					a = b;
					b = tmp;
				}
				for (int i = a.x; i < b.x; i++) {
					this.drawPixel(new Point(i, (int) (a.y + (i - a.x) * k)));
				}
			} else {
				if (a.y > b.y) {
					Point tmp = a;
					a = b;
					b = tmp;
				}
				for (int i = a.y; i < b.y; i++) {
					this.drawPixel(new Point((int) ((i - a.y) / k + a.x), i));
				}
			}
		}
	}

	private void drawLineBresenham(Point a, Point b) {
		if (Math.abs(b.y - a.y) < Math.abs(b.x - a.x)) {
			if (a.x > b.x) {
				Point tmp = a;
				a = b;
				b = tmp;
			}
			int dx = b.x - a.x;
			int dy = b.y - a.y;
			int yi = 1;
			if (dy < 0) {
				yi = -1;
				dy = -dy;
			}
			int D = 2 * dy - dx;
			int y = a.y;

			for (int x = a.x; y < b.x; x++) {
				this.drawPixel(new Point(x, y));
				if (D > 0) {
					y = y + yi;
					D = D - 2 * dx;
				}
				D = D + 2 * dy;
			}
		} else {
			if (a.y > b.y) {
				Point tmp = a;
				a = b;
				b = tmp;
			}
			int dx = b.x - a.x;
			int dy = b.y - a.y;
			int xi = 1;
			if (dx < 0) {
				xi = -1;
				dx = -dx;
			}
			int D = 2 * dx - dy;
			int x = a.x;

			for (int y = a.y; y < b.y; y++) {
				this.drawPixel(new Point(x, y));
				if (D > 0) {
					x = x + xi;
					D = D - 2 * dy;
				}
				D = D + 2 * dx;
			}
		}
	}

	public void drawPloygon(Point[] points, String algorithm) {
		for (int i = 0; i < points.length - 1; i++) {
			this.drawLine(points[i], points[i + 1], algorithm);
		}
		this.drawLine(points[0], points[points.length - 1], algorithm);
	}

	public void drawEllipse(Point center, int rx, int ry) {

	}

	public Point translate(Point a, int xx, int yy) {
		return new Point(a.x + xx, a.y + yy);
	}

	public void showImage() {
		frame.updateImage(image);
	}

}
