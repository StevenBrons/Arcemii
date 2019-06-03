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
import shared.messages.Message;

import static android.content.Context.MODE_PRIVATE;

public class Connection {

	private static final String TAG = "CONNECTION";

	private static final int TIMEOUT = 300000;

	// Jelmers server ip: 213.124.165.68
	private static final String hostName = "localhost";
	private static final int PORT = 26194;
	public  boolean isConnected = false;

	private Socket clientSocket;

	private static ObjectInputStream input;
	private static ObjectOutputStream output;

	private Context context;

	private final boolean singleplayer;

	public Connection(final boolean singleplayer, Context context) {
		this.singleplayer = singleplayer;
		this.context = context;

		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				if (singleplayer) {
					runLocalServer();
				}

				Log.d(TAG, "Connecting to server...");
				connectToServer();
			}
		});
		thread.start();
	}


	private void runLocalServer(){
		ArcemiiServer.main();
	}

	private void connectToSingleplayerServer() {
		connectToServer();

//		ArcemiiServer.main(new String[]{"singleplayer"});
//		input = ((SinglePlayerServer) ArcemiiServer.server).getInputStream();
//		output = ((SinglePlayerServer) ArcemiiServer.server).getOutputStream();
//		lastHeartbeat = System.currentTimeMillis();
//
//		Log.d(TAG, "Connected to singleplayer server.");
//		setIsConnected(true);
	}

	/**
	 * Establish a connection with the mutliplayer server.
	 * @author Bram Pulles and Steven Bronsveld.
	 */
	private void connectToServer(){
			try {
				clientSocket = new Socket(hostName, PORT);
				output = new ObjectOutputStream(clientSocket.getOutputStream());
				input = new ObjectInputStream(clientSocket.getInputStream());

				Log.d(TAG, "Connected to multiplayer server.");
				setIsConnected(true);
			} catch (IOException e) {
				Log.d(TAG, "Could not connect to the server.");
			}
	}

	/**
	 * Set the sharedpref and the normal variable to if the client is connected to the server yes or no.
	 * @param connected
	 * @author Bram Pulles
	 */
	private synchronized void setIsConnected(Boolean connected){
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
	public synchronized void sendMessage(final Message msg) {
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
	public synchronized void stopConnection(){
		setIsConnected(false);

		try{
			input.close();
			output.close();
			if(clientSocket != null)
				clientSocket.close();

			if(singleplayer)


			Log.d(TAG, "Succesfully closed the connection.");
		}catch(Exception e){
			Log.d(TAG, "Could not properly close the connection.");
		}
	}

}
