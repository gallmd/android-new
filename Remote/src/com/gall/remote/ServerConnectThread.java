package com.gall.remote;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import com.gall.remote.network.AddressInfo;

public class ServerConnectThread implements Runnable {
	private InetAddress ipAddress;
	private int portNumber;
	@SuppressWarnings("unused")
	private boolean isRunning = true;
	@SuppressWarnings("unused")
	private boolean isConnected = false;
	private Socket clientSocket;
	private DataInputStream input;
	private DataOutputStream output;
	private String recvMessage;

	public ServerConnectThread(AddressInfo ai){
		this.ipAddress = ai.getIP();
		this.portNumber = ai.getPort();
	}

	
	@Override
	public void run() {
		
		connect();
		while(true){
			recvMessage();
		}	
	}
	
	public void connect(){
		
		try {
			clientSocket = new Socket(ipAddress, portNumber);
			input = new DataInputStream(clientSocket.getInputStream());
			output = new DataOutputStream(clientSocket.getOutputStream());
			isConnected = true;
		} catch (IOException e) {
			isConnected = false;
			e.printStackTrace();
		}
		
	}
	
	@SuppressWarnings("deprecation")
	public String recvMessage(){
		
		if(clientSocket.isConnected()){
			try {
				recvMessage = input.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return recvMessage;
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

}
