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

			System.out.println("Type 'help' for a list of commands.");

			while (scanner.hasNextLine()) {
				switch (scanner.next()) {
					case "help":
						help();
						break;
					case "stop":
						stop();
						break;
					case "log":
						log = !log;
						break;
					default:
						help();
				}
			}
			}
		});
		thread.start();
	}

	public static void log(ConsoleTag tag, String message){
		if(log){
			System.out.println("server/" + tag + ": " + message);
		}
	}

	public static void log(ConsoleTag tag, String message, Player player) {
		if (log) {
			System.out.println(player.toString() + "/" + tag.toString() + ": " + message);
		}
	}

	private static void stop(){
		System.exit(0);
	}

	private static void help(){
		System.out.println("------ Available commands: ------");
		System.out.println("help");
		System.out.println("stop");
		System.out.println("log");
		//System.out.println("parties");
	}

}

enum ConsoleTag {
	CONNECTION
}