package com.gall.remote.network;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.widget.Toast;

import com.gall.remote.Constants;

public class RemoteService extends Service {

	private String portString;
	private String ipAddress;
	private RecvHandler mRecvHandler;

	//Handles messages sent from bound components
	@SuppressLint("HandlerLeak")
	class FromUIHandler extends Handler{
		@Override
		public void handleMessage(Message msg) {
			Message sendMessage = mRecvHandler.obtainMessage();
			sendMessage.what = Constants.NetworkMessages.SEND_MESSAGE;

			switch(msg.what){

			//Play Button Pressed
			case Constants.ServiceMessages.PLAY_PRESSED:
				Toast.makeText(getApplicationContext(), "Play Pressed", Toast.LENGTH_SHORT).show();
//				netop.sendMessage("Play");
				sendMessage.arg1 = Constants.ServiceMessages.PLAY_PRESSED;
				break;

				//Next Button Pressed
			case Constants.ServiceMessages.NEXT_PRESSED:
				Toast.makeText(getApplicationContext(), "Next Pressed", Toast.LENGTH_SHORT).show();
				sendMessage.arg1 = Constants.ServiceMessages.NEXT_PRESSED;
				break;

				//Previous Button Pressed
			case Constants.ServiceMessages.PREVIOUS_PRESSED:
				Toast.makeText(getApplicationContext(), "Previous Pressed", Toast.LENGTH_SHORT).show();
				sendMessage.arg1 = Constants.ServiceMessages.PREVIOUS_PRESSED;
				break;

			default:
				super.handleMessage(msg);

			}
			mRecvHandler.sendMessage(sendMessage);
		}
	}

	//Create new messenger with the "FromUIHandler" Class
	final Messenger mMessenger = new Messenger(new FromUIHandler());
	private NetworkOperations netop;



	//Called when service is started
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		//get port and IP address passed by intent
		portString = intent.getStringExtra(Constants.Keys.PORT);
		ipAddress = intent.getStringExtra(Constants.Keys.IP_ADDRESS);

		HandlerThread thread = new HandlerThread("RecvThread");
		thread.start();

		Looper mLooper = thread.getLooper();
		mRecvHandler = new RecvHandler(mLooper, ipAddress, portString);
		Message msg = mRecvHandler.obtainMessage();
		msg.what = Constants.NetworkMessages.CONNECT;
		mRecvHandler.sendMessage(msg);
		
//		netop = new NetworkOperations();
//		new Thread(new ConnectThread(netop, ipAddress, portString)).start();

		return START_STICKY;
	}


	//Return binder to activity that bound to service
	@Override
	public IBinder onBind(Intent intent) {
		return mMessenger.getBinder();
	}


}
