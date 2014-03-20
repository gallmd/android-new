package com.gall.remote.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;

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
	//TODO maked isConnected accurate in every case
	//TODO actually handle exceptions

	public Socket connectToServer(InetAddress serverAddress, int portNumber){
		
		try {
			
			clientSocket = new Socket(serverAddress, portNumber);
			input = new DataInputStream(clientSocket.getInputStream());
			output = new DataOutputStream(clientSocket.getOutputStream());
			connectionStatus = true;
		} catch (IOException e) {
			connectionStatus = false;
			e.printStackTrace();
			
		}
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
	


}
