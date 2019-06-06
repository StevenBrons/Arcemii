package server.general;

/**
 * The main class of the Arcemii-server, contains the actual server instance as a singleton
 * @author Steven Bronsveld
 */
public abstract class ArcemiiServer {

	public static Console console;
	public static ServerGameHandler gameHandler;
	public static Server server;

	/**
	 * Starts Arcemii server, starts gamehandler and start console
	 * @param args no arguments are supported yet
	 * @author Steven Bronsveld
	 * @author Bram Pulles
	 */
	public static void main(String[] args) {
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
