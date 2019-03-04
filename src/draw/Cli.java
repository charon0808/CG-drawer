package draw;

public class Cli {
	private String[] command;
	
	private boolean commandResolve() {
		switch(command[0]) {
		// resetCanvas width height
		case "resetCanvas":{
			if(command.length!=3) return false;
			double width=Double.valueOf(command[1].toString()); // test if command[1-2] is not a double
			double height=Double.valueOf(command[2].toString());
			break;
		}
		// saveCanvas name
		case "saveCanvas":{
			if(command.length!=2) return false;
			break;
		}
		// setColor R G B
		case "setColor":{
			if(command.length!=4) return false;
			break;
		}
		// drawLine id x1 y1 x2 y2 algorithm
		case "drawLine":{
			if(command.length!=7) return false;
			break;
		}
		// drawPolygon id n algorithm
		case "drawPloygon":{
			if(command.length!=4) return false;
			break;
		}
		// drawEllipse id x y rx ry
		case "drawEllipse":{
			if(command.length!=6) return false;
			break;
		}
		// drawCurve id n algorithm x1 y1 x2 y2 бн xn yn
		case "drawCurve":{
			//if(command.length!=3) return false;
			break;
		}
		// translate id dx dy
		case "translate":{
			if(command.length!=4) return false;
			break;
		}
		// rotate id x y r
		case "rotate":{
			if(command.length!=5) return false;
			break;
		}
		// scale id x y s
		case "scale":{
			if(command.length!=5) return false;
			break;
		}
		// clip id x1 y1 x2 y2 algorithm
		case "clip":{
			if(command.length!=7) return false;
			break;
		}
		default:
			return false;
		}
		return true;
	}
	
	public Cli(String line) {
		command=line.split(" ");
	}
}
