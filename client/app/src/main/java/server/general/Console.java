package server.general;

import java.util.Scanner;

import shared.entities.Player;

public class Console {

	private static boolean log = false;

	/**
	 * Handles console input using the default input stream. A new thread is stared that handles all input.
	 * @author Steven Bronsveld
	 * @author Bram Pulles
	 */
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

	/**
	 * Log a server message with the specified tag. Is only displayed if the logging mode is true
	 * @param tag The tag under which tag is the message logged
	 * @param message The content of the message
	 */
	public static void log(ConsoleTag tag, Object message){
		if(log){
			System.out.println("server/" + tag + ": " + message);
		}
	}

	/**
	 * Log a server message with the specified tag and from the specified player
	 * @param tag The tag
	 * @param message The content of the message
	 * @param player The player of which the message is applicable to
	 * @author Steven Bronsveld
	 */
	public static void log(ConsoleTag tag, Object message, Player player) {
		if (log) {
			System.out.println(player.toString() + "/" + tag.toString() + ": " + message);
		}
	}
	private static void stop(){
		System.exit(0);
	}

	/**
	 * Print all possible console command
	 * @author Steven Bronsveld
	 */
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