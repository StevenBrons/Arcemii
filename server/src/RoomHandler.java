package server;

import java.util.ArrayList;

public class RoomHandler {

	public ArrayList<Room> rooms = new ArrayList<>();

	Thread roomLoop;
	
	public RoomHandler() {
		rooms.add(new Menu());
		roomLoop = new Thread(new Runnable() {
			@Override
			public void run() {
				updateRooms();
			}
		});
	}
	
	public void updateRooms() {
		for (Room r: rooms) {
			r.update();
		}
	}
	
	void joinRoom(Player p, Room room) {
		if (roomLoop.isAlive()) {
			roomLoop.start();
		}
		room.join(p);
	}
	
	void joinRoom(Player p, String roomId) {
		for (Room r : rooms) {
			if (r.getId().equals(roomId)) {
				joinRoom(p, r);
				return;
			}
		}	
	}

	public void listRooms() {
		for (Room r: rooms) {
			System.out.println(r.getRoomInfo());
		}
	}
	
}
