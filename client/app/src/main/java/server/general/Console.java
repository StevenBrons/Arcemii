package server.general;

import java.util.Scanner;

public class Console {

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
					default:
						help();
				}
				System.out.print("> ");
			}
			}
		});
		thread.start();
	}

	private void help(){
		System.out.println("------ Available commands: ------");
		System.out.println("help");
		System.out.println("parties");
	}

}
