package draw;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    private static Cli cli;

    public static void parseFile(Scanner scanner) throws InterruptedException {
        String line;
        System.out.print(">>> ");
        while (scanner.hasNext()) {
            line = scanner.nextLine();
            if (line.contains("drawPolygon") || line.contains("drawCurve")) {
                if (scanner.hasNextLine())
                    line += " " + scanner.nextLine();
                else {
                    // error
                }
                line = line.replace("\n", " ");
            }
            line = line.trim();
            if (line.equals("exit"))
                return;
            System.out.println(line);
            cli.updateCli(line);
            Thread.sleep(1);
            System.out.print(">>> ");
        }
        scanner.close();
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        Frame frame = new Frame();
        CG cg = new CG(frame);
        cg.resetCanvas(800, 600);
        cli = new Cli(cg);
        String line;
        Scanner scanner;
        if (args.length != 0)
            scanner = new Scanner(new File(args[0]));
        else
            scanner = new Scanner(System.in);
        parseFile(scanner);
    }

}
