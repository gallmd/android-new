package com.gall.remote.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Class contains all functions for any network activity
 * Connect to Server, send message, recieve message, check if connected
 * @author Matt Gall
 *
 */

public class NetworkOperations implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Socket clientSocket;
	private DataInputStream input;
	private DataOutputStream output;
	private boolean connectionStatus;
	private String recvMessage;

	//TODO fix send/recv (no print line/readline)
	//TODO make isConnected accurate in every case
	//TODO actually handle exceptions

	public Socket connectToServer(InetAddress serverAddress, int portNumber) throws IOException{
		final ExecutorService clientProcessingPool = Executors.newFixedThreadPool(10);

		clientSocket = new Socket(serverAddress, portNumber);
		input = new DataInputStream(clientSocket.getInputStream());
		output = new DataOutputStream(clientSocket.getOutputStream());
		clientProcessingPool.submit(new RecvTask(clientSocket, input));
		connectionStatus = true;
		return clientSocket;
	}

	public void sendMessage(String message){
		if (clientSocket.isConnected()){
			try {
				message = (message + "\n");
				output.writeBytes(message);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	@SuppressWarnings("deprecation")
	public String recvMessage() throws SocketException{

		if(clientSocket.isConnected()){
			try {
				recvMessage = input.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		return recvMessage;
	}

	public boolean isConnected(){
		connectionStatus = clientSocket.isConnected();
		return connectionStatus;
	}

	/*
	 * Runnable that runs an infinite loop waiting for messages on socket
	 */
	private class RecvTask implements Runnable{

		private Socket socket;
		private DataInputStream recvInput;

		public RecvTask(Socket s, DataInputStream i){
			this.socket = s;
			this.recvInput = i;
		}

		@Override
		public void run() {

			while(socket.isConnected()){

				try {
					String message = recvInput.readLine();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}//end while
		}//end run
	}//end RecvTask
}
