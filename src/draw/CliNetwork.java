package draw;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class CliNetwork implements Runnable {
	private Thread t;
	private Cli cli;

	public CliNetwork(Cli c) {
		cli = c;
	}

	public void run() {
		int port = 28889;
		ServerSocket server;
		try {
			server = new ServerSocket(port);
			System.out.println("waiting for client to connect 127.0.0.1:28889 ...");
			Socket socket = server.accept();
			System.out.println("client connect success.");
			InputStream inputStream = socket.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				System.out.println("received data from client: " + line);
				cli.updateCli(line);
			}
			bufferedReader.close();
			inputStream.close();
			socket.close();
			server.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("server closed.");
	}

	public void start() {
		System.out.println("Starting");
		if (t == null) {
			t = new Thread(this);
			t.start();
		}
	}
}
