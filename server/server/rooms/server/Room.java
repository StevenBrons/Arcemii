package server;

import java.util.ArrayList;

public abstract class Room {

	private ArrayList<Player> players = new ArrayList<>();
	private int ID;
	private boolean isPublic = false;

	public Room() {
		ID = (int) (Math.random() * 9999);
	}

	public void outputAll(Message message) {
		for (Player p : players) {
			output(p, message);
		}
	}

	public abstract void input(Player player, Message message);

	public void output(Player player, Message message) {
		player.send(message);
	}

	@Override
	public String toString() {
		return "Room: " + getName() + "\tID: " + getId();
	}

	public abstract String getName();

	public abstract void leave(Player player);

	public abstract void join(Player player);

	public void onLeave(Player player) {
		leave(player);
		players.remove(player);
	}

	public void onJoin(Player player) {
		players.add(player);
		join(player);
	}

	public String getRoomInfo() {
		StringBuilder sb = new StringBuilder();
		sb.append(this + "\n");
		for (Player p : players) {
			sb.append("\t- " + p + "\n");
		}
		if (players.size() == 0) {
			sb.append("\t- No active players\n");
		}
		return sb.toString();
	}

	public abstract void update();

	public Object getId() {
		return ID;
	}
}
