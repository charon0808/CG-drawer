package draw;

import java.io.IOException;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) throws IOException {
		Frame frame = new Frame();
		CG cg = new CG(frame);
		cg.resetCanvas(600, 400);
		cg.drawDashs();

		Cli cli = new Cli(cg);
		// CliNetwork cn = new CliNetwork(cli);
		// cn.start();
		String line;
		Scanner scanner = new Scanner(System.in);
		System.out.print(">>> ");
		while (scanner.hasNext()) {
			line = scanner.nextLine();
			cli.updateCli(line);
			System.out.print(">>> ");
		}
	}

}
