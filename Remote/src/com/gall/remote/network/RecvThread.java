package com.gall.remote.network;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

import com.gall.remote.Constants;

public class RecvThread extends HandlerThread{

	public RecvThread(String name) {
		super(name);
	}


	private Handler mHandler;
	private String ipAddress;
	private String portString;
	private NetworkOperations netop;


	@Override
	public void run() {

		Looper.prepare();

		mHandler = new RecvHandler();
		
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
		
		Looper.loop();

		super.run();
	}


	public class RecvHandler extends Handler{
		
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case Constants.NetworkMessages.SEND_MESSAGE:
				netop.sendMessage((String)msg.obj);
			}
			super.handleMessage(msg);


		}
	}

}
