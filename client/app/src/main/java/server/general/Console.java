package server.general;

import java.util.Scanner;

import shared.entities.Player;

public class Console {

	private static boolean log = false;

	public Console() {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
			Scanner scanner = new Scanner(System.in);
			System.out.print("Type 'help' for a list of commands.\n> ");

			while (scanner.hasNextLine()) {
				switch (scanner.next()) {
					case "help":
						help();
						break;
					case "stop":
						stop();
						break;
					case "log":
						log = true;
						break;
					default:
						help();
				}
			}
			}
		});
		thread.start();
	}

	public static void log(String tag,String message, Player player) {
		if (log) {
			System.out.println(message);
		}
	}

	private void stop(){
		System.exit(0);
	}

	private void help(){
		System.out.println("------ Available commands: ------");
		System.out.println("help");
		System.out.println("stop");
		System.out.println("log");
		//System.out.println("parties");
	}

}
