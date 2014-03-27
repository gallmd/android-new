package com.gall.remote.network;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/*
 * Runnable that performs all actions necessary to connect to host server
 * Reports connection status back to Network task as well as the socket that it connected to
 */
public class ConnectRunnable implements Runnable {

	//State Flags
	public static final int CONNECT_STATE_CONNECTED = 1;
	public static final int CONNECT_STATE_FAILED = 0;

	private String ipAddress;
	private String portString;

	final TaskRunnableConnectMethods mNetworkTask;
	private Socket clientSocket;

	interface TaskRunnableConnectMethods{

		//method to set the current thread
		void setConnectThread(Thread currentThread);

		//getter for IP address
		String getIP();

		//getter for Port
		String getPort();

		void handleConnectState(int state);
		
		//Return a copy of socket
		void setConnectionSocket(Socket connectedSocket);
	}


	public ConnectRunnable(TaskRunnableConnectMethods mNetworkTask) {
		this.mNetworkTask = mNetworkTask;
	}



	@Override
	public void run() {

		android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);

		mNetworkTask.setConnectThread(Thread.currentThread());

		ipAddress = mNetworkTask.getIP();
		portString = mNetworkTask.getPort();


		try {
			//create InetAddress from passed 'IP' String
			InetAddress serverAddress = InetAddress.getByName(ipAddress);

			//Create port number from passed 'port' string
			int port = Integer.parseInt(portString);

			clientSocket = new Socket(serverAddress, port);
			
			
			//Successful Connection, report back to Network Task
			mNetworkTask.handleConnectState(CONNECT_STATE_CONNECTED);
			mNetworkTask.setConnectionSocket(clientSocket);

		} catch (UnknownHostException e) {
			mNetworkTask.handleConnectState(CONNECT_STATE_FAILED);
			e.printStackTrace();
		} catch (IOException e) {
			mNetworkTask.handleConnectState(CONNECT_STATE_FAILED);
			e.printStackTrace();
		}		
	}

}
