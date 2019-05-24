package client.controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.debernardi.archemii.R;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import server.general.ArcemiiServer;
import server.general.SinglePlayerServer;
import shared.messages.Message;

import static android.content.Context.MODE_PRIVATE;

public class Connection {

	private static final String tag = "CONNECTION";

	private static final String hostName = "10.0.2.2";
	private static final int PORT = 26194;
	public static boolean isConnected = false;

	private Socket clientSocket;

	private static ObjectInputStream input;
	private static ObjectOutputStream output;

	private Context context;

	private boolean singleplayer;

	public Connection(final boolean singleplayer, Context context) {
		this.singleplayer = singleplayer;
		this.context = context;

		if (isConnected)
			return;

		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				Log.d(tag, "Connecting to server...");

				if (singleplayer) {
					ArcemiiServer.main(new String[]{"singleplayer"});
					input = ((SinglePlayerServer) ArcemiiServer.server).getInputStream();
					output = ((SinglePlayerServer) ArcemiiServer.server).getOutputStream();

					Log.d(tag, "Connected to singleplayer server.");
					setIsConnected(true);
				} else {
					while(!isConnected) {
						try {
							clientSocket = new Socket(hostName, PORT);
							output = new ObjectOutputStream(clientSocket.getOutputStream());
							input = new ObjectInputStream(clientSocket.getInputStream());

							Log.d(tag, "Connected to multiplayer server.");
							setIsConnected(true);
						} catch (IOException e) {
							Log.d(tag, "Could not connect to the server.");
						}
					}
				}
			}
		});
		thread.start();
	}

	/**
	 * Set the sharedpref and the normal variable to if the client is connected to the server yes or no.
	 * @param connected
	 * @author Bram Pulles
	 */
	private void setIsConnected(Boolean connected){
		isConnected = connected;

		SharedPreferences prefs = context.getSharedPreferences(context.getString(R.string.sharedpref_connectioninfo), MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putBoolean(context.getString(R.string.sharedpref_connection), connected);
		editor.apply();
	}

	public ObjectInputStream getInputStream() {
		return input;
	}

	private ObjectOutputStream getOutputStream() {
		return output;
	}

	/**
	 * Send a message to the server from the client.
	 * @param msg message
	 */
	public void sendMessage(final Message msg) {
		if (isConnected){
			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						getOutputStream().writeObject(msg);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});
			t.start();
		}
	}

	/**
	 * Stop this connection.
	 * @author Bram Pulles
	 */
	public void stopConnection(){
		setIsConnected(false);

		try{
			input.close();
			output.close();
			if(clientSocket != null)
				clientSocket.close();

			if(singleplayer)
				((SinglePlayerServer)ArcemiiServer.server).stopServer();

			Log.d(tag, "Succesfully closed the connection.");
		}catch(Exception e){
			Log.d(tag, "Could not properly close the connection.");
		}
	}
}
