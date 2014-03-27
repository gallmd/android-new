package com.gall.remote.network;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class ConnectThread implements Runnable {
	
	private NetworkOperations netop;
	private String ipAddress;
	private String portString;
	
	ConnectThread(NetworkOperations netop, String ipAddress, String port) {
		this.netop = netop;
		this.ipAddress = ipAddress;
		this.portString = port;

	}

	@Override
	public void run() {
		
		try {
			//create InetAddress from passed 'IP' String
			InetAddress address = InetAddress.getByName(ipAddress);

			//Create port number from passed 'port' string
			int port = Integer.parseInt(portString);

			netop = new NetworkOperations();
			netop.connectToServer(address, port);

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}

}
