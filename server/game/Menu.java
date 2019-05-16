package server;

public class Menu extends Room{

	@Override
	public void input(Player player, Message message) {
	}

	@Override
	public String getName() {
		return "MENU";
	}

	@Override
	public void leave(Player player) {
	}

	@Override
	public void join(Player player) {
	}

	@Override
	public void update() {
	}
	
	@Override
	public Object getId() {
		return "MENU";
	}

}
