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

	/**
	 * Start a new connection.
	 * If singleplayer then also run a local server.
	 * @param singleplayer
	 * @param context
	 * @author Bram Pulles
	 */
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


	/**
	 * Start a local server in order to play offline.
	 * @author Bram Pulles
	 */
	private void runLocalServer(){
		ArcemiiServer.main(new String[]{""});
		hostName = "localhost";

		// Wait until the server has been started.
		while(ArcemiiServer.server.isStarting()){
		}
	}

	/**
	 * Establish a connection with the server.
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


	private synchronized  void write(final Message msg) throws IOException{
		output.writeObject(msg);
		output.flush();
		output.reset();
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
						write(msg);
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

	/**
	 * @return if this connection is connected to a server.
	 * @author Bram Pulles
	 */
	public boolean isConnected(){
		return isConnected;
	}

	/**
	 * @return if this connection is still starting (booting up).
	 * @author Bram Pulles
	 */
	public synchronized boolean isStarting(){
		return isStarting;
	}

}
