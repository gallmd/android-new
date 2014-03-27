package com.gall.remote.network;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.gall.remote.Constants;

public class RecvHandler extends Handler {
	
	private String ipAddress;
	private String portString;
	private NetworkOperations netop;
	
	public RecvHandler(Looper looper,String ipAddress, String port) {
		super(looper);
		this.ipAddress  = ipAddress;
		this.portString = port;
	}
	
	@Override
	public void handleMessage(Message msg) {
		switch(msg.what){
		case Constants.NetworkMessages.CONNECT:
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
			break;
			
		case Constants.NetworkMessages.SEND_MESSAGE:
			netop.sendMessage(msg.arg1+"");
			break;
			
		default:
			super.handleMessage(msg);

		}
	}

}
