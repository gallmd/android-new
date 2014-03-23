package com.gall.remote.network;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import android.app.IntentService;
import android.content.Intent;

public class RemoteService extends IntentService {
	
//	private BroadcastNotifier mBroadcast = new BroadcastNotifier();
	


	public RemoteService() {
		super("Remote Service");
	}

	@Override
	protected void onHandleIntent(Intent workIntent) {
		
		try {
			//create InetAddress from passed 'ip' String
			String ipAddress = workIntent.getStringExtra("ip");
			InetAddress address = InetAddress.getByName(ipAddress);
			
			//Create port number from passed 'port' string
			String portNumber = workIntent.getStringExtra("port");
			int port = Integer.parseInt(portNumber);
			
			//Create new network operations and connect to server
			NetworkOperations netop = new NetworkOperations();
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
