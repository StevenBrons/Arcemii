package server;

import java.util.Scanner;

public class Console {

	public Console() {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Type 'help' for a list of commands");
		while (scanner.hasNextLine()) {
			switch (scanner.next()) {
			case "help":
				System.out.println("------ Available commands: ------");
				System.out.println("help");
				System.out.println("rooms");
				break;
			case "rooms":
				ServerMain.server.roomHandeler.listRooms();
			}
		}
	}

}
