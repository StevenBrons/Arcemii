package client.controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.debernardi.archemii.R;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import server.general.ArcemiiServer;
import shared.messages.Message;

import static android.content.Context.MODE_PRIVATE;

public class Connection {

	public static final String TAG = "CONNECTION";

	private String hostName = "10.0.2.2";
	private final int PORT = 26194;
	private boolean isConnected = false;
	private boolean isStarting = true;

	private Socket clientSocket;

	private ObjectInputStream input;
	private ObjectOutputStream output;

	private Context context;

	private final boolean singleplayer;

	public Connection(final boolean singleplayer, Context context) {
		this.singleplayer = singleplayer;
		this.context = context;

		new Thread(new Runnable() {
			@Override
			public void run() {
				if (singleplayer) {
					runLocalServer();
				}

				connectToServer();
			}
		}).start();
	}


	private void runLocalServer(){
		ArcemiiServer.main(new String[]{""});
		hostName = "localhost";

		// Wait until the server has been started.
		while(ArcemiiServer.server.isStarting()){
		}
	}

	/**
	 * Establish a connection with the mutliplayer server.
	 * @author Bram Pulles and Steven Bronsveld.
	 */
	private void connectToServer(){
		Log.d(TAG, "Connecting to server...");

		try {
			clientSocket = new Socket();
			clientSocket.connect(new InetSocketAddress(hostName, PORT), 100);
			output = new ObjectOutputStream(clientSocket.getOutputStream());
			input = new ObjectInputStream(clientSocket.getInputStream());

			Log.d(TAG, "Connected to server.");
			setIsConnected(true);
		} catch (IOException e) {
			Log.d(TAG, "Could not connect to the server.");
		}

		isStarting = false;
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
	public void sendMessage(final Message msg) {
		if (isConnected()){
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						getOutputStream().writeObject(msg);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}).start();
		}
	}

	/**
	 * Stop this connection.
	 * @author Bram Pulles
	 */
	public synchronized void stop(){
		setIsConnected(false);

		try{
			if(input != null)
				input.close();
			if(output != null)
				output.close();
			if(clientSocket != null)
				clientSocket.close();

			if(singleplayer)
				ArcemiiServer.stop();

			Log.d(TAG, "Succesfully closed the connection.");
		}catch(Exception e){
			Log.d(TAG, "Could not properly close the connection.");
		}
	}

	public boolean isConnected(){
		return isConnected;
	}

	public synchronized boolean isStarting(){
		return isStarting;
	}

}
