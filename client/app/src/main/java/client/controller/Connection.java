package client.controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import server.general.ArchemiiServer;
import server.general.SinglePlayerServer;
import shared.messages.Message;

public class Connection {

  private static final String hostName = "192.168.0.19";
  private static final int PORT = 26194;
  private static boolean isConnected = false;

  private ObjectInputStream input;
  private ObjectOutputStream output;

  public Connection(boolean singlePlayer) {
    if (isConnected) return;
    System.out.println("Connecting to server");
    if (singlePlayer) {
        ArchemiiServer.main(new String[]{"singleplayer"});
        input = ((SinglePlayerServer) ArchemiiServer.server).getInputStream();
        output = ((SinglePlayerServer) ArchemiiServer.server).getOutputStream();
        System.out.println("Connected to singleplayer server");
    } else {
      try {
        Socket clientSocket = new Socket(hostName, PORT);
        output = new ObjectOutputStream(clientSocket.getOutputStream());
        input = new ObjectInputStream(clientSocket.getInputStream());
        System.out.println("Connected to multiplayer server");
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    isConnected = true;
  }

  public ObjectInputStream getInputStream() {
    return input;
  }

  private ObjectOutputStream getOuputStream() {
    return output;
  }

  /**
   * Send a message to the server from the client.
   * @param msg message
   */
  public void sendMessage(Message msg) {
    System.out.println(msg);
    try {
      getOuputStream().writeObject(msg);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
