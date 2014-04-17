package com.gall.remote.service;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import android.content.Context;

import com.gall.remote.DTO.NetworkCommand;
import com.gall.remote.service.ConnectRunnable.TaskRunnableConnectMethods;
import com.gall.remote.service.ReceiveRunnable.TaskRunnableReceiveMethods;

/**
 * This class manages the state of a single connection and the
 * thread it runs on.  It also has the ability to send a message
 * to the server.
 */
public class NetworkTask implements TaskRunnableReceiveMethods, TaskRunnableConnectMethods {
	
	private ConnectRunnable mConnectRunnable;
	private String ip;
	private String port;
	private ReceiveRunnable mReceiveRunnable;
	private NetworkManager mNetworkManager;
	@SuppressWarnings("unused")
	private Thread mCurrentThread;
	private Socket connectionSocket;
	private DataOutputStream output;
	private Context mContext;

	NetworkTask(){
		mConnectRunnable = new ConnectRunnable(this);
		mReceiveRunnable = new ReceiveRunnable(this);
		mNetworkManager = NetworkManager.getInstance();
	}
	
	/**
	 * Called when a connection is going to be made.  This
	 * method sets the IP address, and the port number to connect to.
	 * It also sets a reference to the application context to access the 
	 * application's database.
	 * @param ip - The IP Address of the server to connect to.
	 * @param port - The port number that the server is listening on.
	 * @param context - Application context used to access the database.
	 */
	public void initializeConnectTask(String ip, String port, Context context){
		
		//Set IP address and port number to connect to
		this.ip = ip;
		this.port = port;
		this.mContext = context;
		
	}

	
	/**
	 * Called from the RecieveRunnable that this instance of NetworkTask is managing.
	 * The ReceiveRunnable sets this field to the thread that it is currently
	 * running on.
	 * @param thread - The current thread for the ReceiveRunnable.
	 */
	public void setCurrentThread(Thread thread){
		synchronized (mNetworkManager) {
			mCurrentThread = thread;
		}
	}
	
	/**
	 * Called by the NetworkManager to obtain a reference to the ConnectRunnable
	 * that belongs to this NetworkTask. The NetworkManager will start the Runnable.
	 * @return The ConnectRunnable associated with this instance of NetworkTask.
	 */
	public Runnable getConnectRunnable(){
		return mConnectRunnable;
	}
	
	/**
	 * Called by the NetworkManager to obtain a reference to the ReceiveRunnable
	 * that belongs to this NetworkTask. The NetworkManager will start the Runnable.
	 * @return The ReceiveRunnable associated with this instance of NetworkTask.
	 */
	public Runnable getReceiveRunnable(){
		return mReceiveRunnable;
	}
	
	
	/**
	 * Called from the ConnectRunnable that this instance of NetworkTask is managing.
	 * The ConnectRunnable sets this field to the thread that it is currently
	 * running on.
	 * @param thread - The current thread for the ConnectRunnable.
	 */
	@Override
	public void setConnectThread(Thread currentThread) {
		setCurrentThread(currentThread);
		
	}
	
	/**
	 * Called from the ConnectRunnable to retrieve the IP
	 * Address to connect to.
	 * @return ip - IP address to connect to.
	 */
	@Override
	public String getIP() {
		return this.ip;
	}

	/**
	 * Called from the ConnectRunnable to retrieve the Port
	 * Number that the server is listening on.
	 * @return port - The port number that the server is listening on.
	 */	
	@Override
	public String getPort() {
		return this.port;
	}
	

	/**
	 * Called by connect runnable, changes in state are passed to 
	 *the NetworkManager to be handled.	
	 *@param ConnectRunnable Constant integer.
	 */
	@Override
	public void handleConnectState(int state) {
		
		int outstate;
		switch(state){
		
		case ConnectRunnable.CONNECT_STATE_CONNECTED:
			outstate = NetworkManager.CONNECTED;
			handleState(outstate);
			break;
			
		case ConnectRunnable.CONNECT_STATE_FAILED:
			outstate = NetworkManager.CONNECTION_ERROR;
			handleState(outstate);
			break;
			
		}
	}
	
	/**
	 * Called by ReceiveRunnable, changes in state are passed to 
	 *the NetworkManager to be handled.	
	 *@param ReceiveRunnable Constant integer.
	 */
	@Override
	public void handleReceiveState(int state) {
		int outstate;
		switch(state){
		
		case ReceiveRunnable.DISCONNECTED_FROM_SERVER:
			outstate = NetworkManager.NOT_CONNECTED;
			handleState(outstate);
			break;
			
		case ReceiveRunnable.CONNECTION_LOST:
			outstate = NetworkManager.CONNECTION_LOST;
			handleState(outstate);
			break;
			
		case ReceiveRunnable.SOCKET_READ_ERROR:
			outstate = NetworkManager.CONNECTION_LOST;
			handleState(outstate);
			break;
			
		case ReceiveRunnable.LIBRARY_DELETED:
			outstate = NetworkManager.LIBRARY_DELETED;
			handleState(outstate);
			break;
			
		case ReceiveRunnable.LIBRARY_UPDATE_IN_PROGRESS:
			outstate = NetworkManager.LIBRARY_UPDATE_IN_PROGRESS;
			handleState(outstate);
			break;
			
		case ReceiveRunnable.LIBRARY_UPDATE_COMPLETED:
			outstate = NetworkManager.LIBRARY_UPDATE_COMPLETED;
			handleState(outstate);
			NetworkCommand nc = new NetworkCommand(NetworkCommand.LIBRARY_UPDATE_COMPLETE, null, null, null);
			sendMessage(nc);
			break;
		}
		
	}
	
	/**
	 * Passes new state updates to NetworkManager to handle
	 * @param outstate
	 */
	private void handleState(int outstate) {
		mNetworkManager.handleState(this, outstate);
	}
	
	/**
	 * Sets Connection Socket From ConnectRunnable and creates DataStream to write to.
	 * @param connectedSocket - The socket that the ConnectRunnable used to connect to the server.
	 */
	@Override
	public void setConnectionSocket(Socket connectedSocket) {
		//Set connection socket to the socket that was created
		//in the ConnectRunnable.
		this.connectionSocket = connectedSocket;
		
		try {
			
			output = new DataOutputStream(connectionSocket.getOutputStream());
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	//Called to pass the connection socket to the ReceiveRunnable
	@Override
	public Socket getConnectionSocket(){
		return this.connectionSocket;
	}
	
	/**
	 * Send simple message over socket, small messages shouldn't need
	 * to be sent on a separate thread.

	 * @param networkCommand
	 */
	public void sendMessage(NetworkCommand networkCommand){
		
		if (connectionSocket.isConnected()){
			
			try {
				String message  = networkCommand.writeCommand();
				message = (message + "\n");
				output.writeBytes(message);
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public Context getContext() {
		return mContext;
	}
	
	/**
	 * Called from NetworkManager when the connection is to be closed.
	 */
	public void closeConnection(){
		
		NetworkCommand nc = new NetworkCommand();
		nc.setCommandType(NetworkCommand.DISCONNECT);
		sendMessage(nc);
		try {
			output.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
