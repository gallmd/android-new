package com.gall.remote.network;

import java.net.Socket;

import com.gall.remote.network.ConnectRunnable.TaskRunnableConnectMethods;
import com.gall.remote.network.ReceiveRunnable.TaskRunnableReceiveMethods;

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

	NetworkTask(){
		mConnectRunnable = new ConnectRunnable(this);
		mReceiveRunnable = new ReceiveRunnable(this);
		mNetworkManager = NetworkManager.getInstance();
	}
	
	public void initializeConnectTask(String ip, String port){
		
		//Set IP address and port number to connect to
		this.ip = ip;
		this.port = port;
		
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

	//Handle state reported by connect runnable
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
		}
		
	}
	
	//passes new state updates to NetworkManager to handle
	private void handleState(int outstate) {
		mNetworkManager.handleState(this, outstate);
	}
	
	//Sets Connection Socket From ConnectRunnable
	@Override
	public void setConnectionSocket(Socket connectedSocket) {
		this.connectionSocket = connectedSocket;
	}
	
	//Gets Connection Socket From Receive Runnable
	@Override
	public Socket getConnectionSocket(){
		return this.connectionSocket;
	}


}
