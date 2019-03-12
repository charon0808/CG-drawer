package draw;

import java.awt.Point;
import java.util.ArrayList;

public class Cli {
	private String[] command;
	private String commandLine;
	private CG cg;

	private ArrayList<String> al;

	public Cli(CG c) {
		cg = c;
		al = new ArrayList<String>();
	}

	public void updateCli(String line) {
		commandLine = line;
		command = line.split(" ");
		if (!this.commandResolve()) {
			System.err.println("invalid command.");
		}
	}

	private boolean commandResolve() {
		switch (command[0]) {
		// resetCanvas width height
		case "resetCanvas": {
			if (command.length != 3)
				return false;
			try {
				int width = Integer.parseInt(command[1].toString());
				int height = Integer.parseInt(command[2].toString());
				cg.resetCanvas(width, height);
			} catch (NumberFormatException e) {
				return false;
			}
			break;
		}
		// saveCanvas name
		case "saveCanvas": {
			if (command.length != 2)
				return false;
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
			if (command.length != 4)
				return false;
			try {
				int r = Integer.parseInt(command[1]);
				int g = Integer.parseInt(command[2]);
				int b = Integer.parseInt(command[3]);
				int color = ((0xFF << 24) | (r << 16) | (g << 8) | b);
				cg.setColor(color);
			} catch (NumberFormatException e) {
				return false;
			}
			break;
		}
		// drawLine id x1 y1 x2 y2 algorithm
		case "drawLine": {
			if (command.length != 7)
				return false;
			try {
				int id = Integer.parseInt(command[1]);
				cg.drawLine(new Point(Integer.parseInt(command[2]), Integer.parseInt(command[3])), // Point a
						new Point(Integer.parseInt(command[4]), Integer.parseInt(command[5])), // Point b
						Integer.parseInt(command[6]));// algorithm
				cg.showImage();
			} catch (NumberFormatException e) {
				return false;
			}
			break;
		}
		// drawPolygon id n algorithm
		case "drawPloygon": {
			if (command.length != 4)
				return false;
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
		default:
			return false;
		}
		al.add(commandLine);
		return true;
	}
}
