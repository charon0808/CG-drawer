package draw;

import java.awt.Point;
import java.util.HashMap;

public class Cli {
	private String[] command;
	private String commandLine;
	private CG cg;
	private String errInfo;

	private HashMap<Integer, String> shapes;

	public Cli(CG c) {
		cg = c;
		shapes = new HashMap<>();
	}

	public void updateCli(String line) {
		commandLine = line;
		command = line.split(" ");
		if (!this.commandResolve()) {
			if (errInfo == null)
				System.err.println("command cannot be resolved");
			else
				System.err.println("wrong parameters for command " + errInfo);
		}
	}

	private boolean commandResolve() {
		switch (command[0]) {
		// resetCanvas width height
		case "resetCanvas": {
			if (command.length != 3) {
				errInfo = "resetCanvas.\nUsage: resetCanvas canvas_width canvas_height";
				return false;
			}
			try {
				int width = Integer.parseInt(command[1].toString());
				int height = Integer.parseInt(command[2].toString());
				cg.resetCanvas(width, height);
			} catch (NumberFormatException e) {
				errInfo = "drawPolygon.\ncanvas_width and canvas_height values must be integers";
				return false;
			}
			break;
		}
		// saveCanvas name
		case "saveCanvas": {
			if (command.length != 2) {
				errInfo = "saveCanvas.\nUsage: saveCanvas file_name";
				return false;
			}
			String path;
			if ((path = cg.saveCanvas(command[1])) == null) {
				System.err.println("sava Canvas failed.");
			} else {
				System.out.println("current Canvas saved at " + path);
			}
			break;
		}
		// setColor R G B
		case "setColor": {
			if (command.length != 4) {
				errInfo = "setColor.\nUsage: setColor R G B";
				return false;
			}
			try {
				int r = Integer.parseInt(command[1]);
				int g = Integer.parseInt(command[2]);
				int b = Integer.parseInt(command[3]);
				int color = ((0xFF << 24) | (r << 16) | (g << 8) | b);
				cg.setColor(color);
			} catch (NumberFormatException e) {
				errInfo = "drawPolygon.\nR, G and B values must be integers";
				return false;
			}
			break;
		}
		/*
		 * drawLine id x1 y1 x2 y2 algorithm[DDA|Bresenham] default = DDA
		 */
		case "drawLine": {
			if (command.length != 7) {
				errInfo = "drawLine.\nUsage: drawLine id x1 y1 x2 y2 [ DDA | Bresenham default = DDA]";
				return false;
			}
			int id;
			try {
				id = Integer.parseInt(command[1]);
				cg.drawLine(new Point(Integer.parseInt(command[2]), Integer.parseInt(command[3])), // Point
																									// a
						new Point(Integer.parseInt(command[4]), Integer.parseInt(command[5])), // Point
																								// b
						command[6]);// algorithm
				cg.showImage();
			} catch (NumberFormatException e) {
				errInfo = "drawPolygon.\n id and coordinate values must be integers";
				return false;
			}
			shapes.put(id, commandLine);
			break;
		}
		/*
		 * drawPolygon id n algorithm x1 y1 x2 y2 ... xn yn
		 */
		case "drawPolygon": {
			if (command.length <= 4) {
				errInfo = "drawPolygon.\nUsage: drawLine id x1 y1 x2 y2 [ DDA | Bresenham default = DDA]";
				return false;
			}
			int id;
			try {
				int n = Integer.parseInt(command[2]);
				if (command.length != 4 + 2 * n) {
					errInfo = "drawPolygon.\nUsage: drawLine id x1 y1 x2 y2 [ DDA | Bresenham default = DDA]";
					return false;
				}
				id = Integer.parseInt(command[1]);
				Point[] points = new Point[n];
				for (int i = 0; i < n; i++) {
					points[i] = new Point(Integer.parseInt(command[4 + 2 * i]),
							Integer.parseInt(command[4 + 2 * i + 1]));
				}
				cg.drawPloygon(points, command[3]);
				cg.showImage();
			} catch (NumberFormatException e) {
				errInfo = "drawPolygon.\nid , n and coordinate values must be integers";
				return false;
			}
			shapes.put(id, commandLine);
			break;
		}
		// drawEllipse id x y rx ry
		case "drawEllipse": {
			if (command.length != 6)
				return false;
			break;
		}
		// drawCurve id n algorithm x1 y1 x2 y2 бн xn yn
		case "drawCurve": {
			// if(command.length!=3) return false;
			break;
		}
		// translate id dx dy
		case "translate": {
			if (command.length != 4)
				return false;
			break;
		}
		// rotate id x y r
		case "rotate": {
			if (command.length != 5)
				return false;
			break;
		}
		// scale id x y s
		case "scale": {
			if (command.length != 5)
				return false;
			break;
		}
		// clip id x1 y1 x2 y2 algorithm
		case "clip": {
			if (command.length != 7)
				return false;
			break;
		}
		default: {
			return false;
		}
		}
		return true;
	}
}
