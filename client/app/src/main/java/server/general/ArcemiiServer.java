package server.general;

public abstract class ArcemiiServer {

	public static Console console;
	public static ServerGameHandler gameHandler;
	public static Server server;

	public static void main() {
		console = new Console();
		gameHandler = new ServerGameHandler();
		server = new Server(gameHandler);
	}

	/**
	 * Stop the server and the game handler.
	 * @author Bram Pulles
	 */
	public static void stop(){
		server.stop();
		gameHandler.stop();
	}

}
