package server.general;

public abstract class ArcemiiServer {

    public static Console console = new Console();
    public static ServerGameHandler gameHandler = new ServerGameHandler();
    public static ArcemiiServer server;

    public static void main(String args[]) {
        if (args.length > 0 && args[0].equals("singleplayer")) {
            server = new SinglePlayerServer();
        } else {
            server = new MultiplayerServer();
        }
    }

}
