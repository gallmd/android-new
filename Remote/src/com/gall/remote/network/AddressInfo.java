package com.gall.remote.network;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Class contains address information necessary for connecting to host (ip address and port number)
 * Passed to connect Async Task
 * @author Matt Gall
 *
 */

public class AddressInfo {
	
	/**
	 * 
	 */
	private InetAddress ipAddress;
	private int portNumber;
	
	
	//getters
	public InetAddress getIP(){
		return ipAddress;
	}
	
	public int getPort(){
		return portNumber;
	}

	//setters
	public void setIP(String ipString){
		InetAddress i;
		try {
			i = InetAddress.getByName(ipString);
			ipAddress = i;
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
	}
	
	public void setPortNumber(int p){
		portNumber = p;
	}
}
