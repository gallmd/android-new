package com.gall.remote.service;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.gall.remote.DTO.NetworkCommand;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

public class NetworkManager {

	//State flags
	public static final int CONNECTION_ERROR = -1;
	public static final int NOT_CONNECTED = 0;
	public static final int CONNECTED = 1;
	public static final int CONNECTION_LOST = 2;
	public static final int NO_WIFI = 3;
	
	//Command flags
	public static final int LIBRARY_UPDATE_IN_PROGRESS = 4;
	public static final int LIBRARY_UPDATE_COMPLETED = 5;
	public static final int LIBRARY_DELETED = 6;

	//Constants

	private static final int KEEP_ALIVE_TIME = 1;

	private static final TimeUnit KEEP_ALIVE_TIME_UNIT;

	private static final int CORE_POOL_SIZE = 8;

	private static final int MAX_POOL_SIZE = 8;

	@SuppressWarnings("unused")
	private static int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();

	//Queue containing network Runnables to be executed
	private final BlockingQueue<Runnable> mNetworkWorkQueue;

	//Queue containing ReceiveRunnabls
	private final BlockingQueue<Runnable> mNetworkReceiveQueue;

	//Network Tasks will be defined as connecting to the server and then
	//Receiving messages from server, in sequential order.
	private final BlockingQueue<NetworkTask> mNetworkTaskQueue;

	//Thread pool for network tasks
	private final ThreadPoolExecutor mNetworkConnectThreadPool;

	//Thread pool for receive tasks
	private final ThreadPoolExecutor mNetworkReceiveThreadPool;

	//Single Instance of NetworkManager
	private static NetworkManager sInstance = null;
	
	//Messenger to send data back to the UI.
	private static Messenger toUIMessenger;
	
	//Application Context that will be passed to classes that write to database.
	private static Context mContext;

	static{
		
		KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;

		sInstance = new NetworkManager();
	}

	//Constructor
	@SuppressLint("HandlerLeak")
	private NetworkManager() {

		//Initialize Queues
		mNetworkWorkQueue = new LinkedBlockingQueue<Runnable>();

		mNetworkReceiveQueue = new LinkedBlockingQueue<Runnable>();

		mNetworkTaskQueue = new LinkedBlockingQueue<NetworkTask>();

		//Initialize Thread Pools
		//Thread pool that contains ConnectRunnables 
		mNetworkConnectThreadPool = new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE_TIME, KEEP_ALIVE_TIME_UNIT, mNetworkWorkQueue);

		//Thread pool that contains ReceiveRunnables
		mNetworkReceiveThreadPool = new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE_TIME, KEEP_ALIVE_TIME_UNIT, mNetworkReceiveQueue);

	}//end Constructor
	


	//Will be the only way to get an instance of the Network Manager
	public static NetworkManager getInstance(){
		return sInstance;
	}

	//Handle Changes in Connection State
	//State changes will be received from instances of NetworkTask
	public void handleState(NetworkTask networkTask, int state){
		
		Message toUI = Message.obtain();

		switch(state){
		case CONNECTION_ERROR:
			//handle connection errors
			toUI.what = NetworkManager.CONNECTION_ERROR;
			break;

		case CONNECTED:
			mNetworkReceiveThreadPool.execute(networkTask.getReceiveRunnable());
			toUI.what = NetworkManager.CONNECTED;
			break;

		case CONNECTION_LOST:
			//Handle a lost connection
			toUI.what = NetworkManager.CONNECTION_LOST;
			break;

		case NO_WIFI:
			//Alert user to enable WIFI
			break;

		case NOT_CONNECTED:
			//Connect for first time, possibly not needed
			break;
			
		case LIBRARY_DELETED:
			toUI.what = NetworkManager.LIBRARY_DELETED;
			break;
			
		case LIBRARY_UPDATE_COMPLETED:
			toUI.what = NetworkManager.LIBRARY_UPDATE_COMPLETED;
			break;
			
		case LIBRARY_UPDATE_IN_PROGRESS:
			toUI.what = NetworkManager.LIBRARY_UPDATE_IN_PROGRESS;
			break;
		}
		
		try {
			toUIMessenger.send(toUI);
		} catch (RemoteException e) {
			e.printStackTrace();
		}


	}

	//Start a connection task
	public static NetworkTask startConnectTask(String ip, String port){

		NetworkTask connectTask = sInstance.mNetworkTaskQueue.poll();

		//Check if queue was empty
		if(connectTask == null){
			connectTask = new NetworkTask();
		}

		//Sends IP address and port number to NetworkTask
		connectTask.initializeConnectTask(ip, port, mContext);

		sInstance.mNetworkConnectThreadPool.execute(connectTask.getConnectRunnable());
		
		//Add Network Task to Queue
		sInstance.mNetworkTaskQueue.add(connectTask);

		return connectTask;

	}
	
	
	//Retrieves the current NetworkTask and calls its sendMessage method
	public static void sendMessageToServer(NetworkCommand networkCommand){
		
		NetworkTask sendTask = sInstance.mNetworkTaskQueue.peek();
		
		if(sendTask != null){
			sendTask.sendMessage(networkCommand);
		}
		
	}
	
	//Called by RemoteService to pass necessary data
	public static void initializeNetworkManager(Messenger messenger, Context context){
		toUIMessenger = messenger;
		mContext = context;
	}
	
	public void disconnectFromAll(){
		
		NetworkTask[] tasks = new NetworkTask[mNetworkTaskQueue.size()];
		sInstance.mNetworkTaskQueue.toArray(tasks);
		
		for (NetworkTask networkTask : tasks) {
			networkTask.closeConnection();
		}
	}

}
