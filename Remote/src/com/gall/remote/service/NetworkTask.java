package com.gall.remote.service;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import android.content.Context;

import com.gall.remote.service.ConnectRunnable.TaskRunnableConnectMethods;
import com.gall.remote.service.ReceiveRunnable.TaskRunnableReceiveMethods;

/*
 * This class manages the state of connections
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
	
	public void initializeConnectTask(String ip, String port, Context context){
		
		//Set IP address and port number to connect to
		this.ip = ip;
		this.port = port;
		this.mContext = context;
		
	}

	
	//Set thread that runnable is currently running on
	public void setCurrentThread(Thread thread){
		synchronized (mNetworkManager) {
			mCurrentThread = thread;
		}
	}
	
	public Runnable getConnectRunnable(){
		return mConnectRunnable;
	}
	
	public Runnable getReceiveRunnable(){
		return mReceiveRunnable;
	}
	
	//Implements ConnectRunnable.setConnectThread calls setCurrentThread
	@Override
	public void setConnectThread(Thread currentThread) {
		setCurrentThread(currentThread);
		
	}
	
	//Allows connect runnable to retrieve IP address
	@Override
	public String getIP() {
		return this.ip;
	}

	//Allows connect runnable to retrieve port
	@Override
	public String getPort() {
		return this.port;
	}
	

	//Called by connect runnable, changes in state are passed to 
	//the NetworkManager to be handled.	
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
			sendMessage("Library Update Complete");
			break;
		}
		
	}
	
	//passes new state updates to NetworkManager to handle
	private void handleState(int outstate) {
		mNetworkManager.handleState(this, outstate);
	}
	
	//Sets Connection Socket From ConnectRunnable and set DataStream to write to
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
	
	//Send simple message over socket, small messages shouldn't need
	//to be sent on a separate thread.
	public void sendMessage(String message){
		
		if (connectionSocket.isConnected()){
			
			try {
				
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
	
	public void closeConnection(){
		sendMessage("DISCONNECT");
		try {
			output.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}
