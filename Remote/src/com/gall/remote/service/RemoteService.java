package com.gall.remote.service;

import java.io.IOException;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;

import com.gall.remote.Constants;
import com.gall.remote.DTO.SongFile;

public class RemoteService extends Service {

	private String portString;
	private String ipAddress;
	
	//Create new messenger with the "FromUIHandler" Class
	final Messenger mMessenger = new Messenger(new FromUIHandler());
	
	//Messenger to send back to UI
	Messenger toUIMessenger;
	private NetworkManager mNetworkManager;

	//Handles messages sent from bound components
	@SuppressLint("HandlerLeak")
	class FromUIHandler extends Handler{
		@Override
		public void handleMessage(Message msg) {

			switch(msg.what){

			//Play Button Pressed
			case Constants.ServiceMessages.PLAY_PRESSED:
				NetworkManager.sendMessageToServer("Play Pressed");
				break;

				//Next Button Pressed
			case Constants.ServiceMessages.NEXT_PRESSED:
				NetworkManager.sendMessageToServer("Next Pressed");
				break;

				//Previous Button Pressed
			case Constants.ServiceMessages.PREVIOUS_PRESSED:
				NetworkManager.sendMessageToServer("Previous Pressed");
				break;
			
				//Sets Messenger in UI to reply to 
			case Constants.ServiceMessages.SET_REPLY_MESSENGER:
				toUIMessenger = msg.replyTo;
				NetworkManager.initializeNetworkManager(toUIMessenger, getApplicationContext());
				NetworkManager.startConnectTask(ipAddress, portString);
				break;
				
				//Song selected from a SongsFragment
			case Constants.ServiceMessages.SONG_SELECTED:
				SongFile sf = (SongFile) msg.obj;
				try {
					NetworkManager.sendMessageToServer(sf.writeSimpleJSON());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
				
			case Constants.ServiceMessages.APPLICATION_EXITING:
				mNetworkManager.disconnectFromAll();
				
				

			default:
				super.handleMessage(msg);

			}
		}
	}


	//Called when service is started
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		//get port and IP address passed by intent
		portString = intent.getStringExtra(Constants.Keys.PORT);
		ipAddress = intent.getStringExtra(Constants.Keys.IP_ADDRESS);
		mNetworkManager = NetworkManager.getInstance();
	
		return START_STICKY;
	}


	//Return binder to activity that bound to service
	@Override
	public IBinder onBind(Intent intent) {
		return mMessenger.getBinder();
	}
	
	@Override
	public boolean onUnbind(Intent intent) {
		stopSelf();
		return super.onUnbind(intent);
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		//Add methods to cancel all ongoing threads
		super.onDestroy();
	}
	
	

}
