package draw;

import java.io.IOException;

public class Main {
	public static void main(String[] args) throws IOException {
		Frame frame = new Frame();
		CG cg = new CG(frame, 640, 360);

		Cli cli = new Cli(cg);
		CliNetwork cn = new CliNetwork(cli);
		cn.start();
	}

}
