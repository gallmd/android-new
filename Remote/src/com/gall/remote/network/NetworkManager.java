package com.gall.remote.network;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;

public class NetworkManager {

	//State flags
	public static final int CONNECTION_ERROR = -1;
	public static final int NOT_CONNECTED = 0;
	public static final int CONNECTED = 1;
	public static final int CONNECTION_LOST = 2;
	public static final int NO_WIFI = 3;

	//Constants

	private static final int KEEP_ALIVE_TIME = 1;

	private static final TimeUnit KEEP_ALIVE_TIME_UNIT;

	private static final int CORE_POOL_SIZE = 8;

	private static final int MAX_POOL_SIZE = 8;

	private static int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();

	//Queue containing network runnables to be executed
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

	private static  Handler mHandler;

	private static NetworkManager sInstance = null;

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

		mHandler = new Handler(){
			@Override
			public void handleMessage(Message msg) {

				//Handle messages from server that will need to be displayed on the UI
				switch(msg.what){
				case NetworkManager.CONNECTED:
					//						Toast.makeText(getApplicationContext(), "Connected", Toast.LENGTH_SHORT).show();
					break;
				case NetworkManager.CONNECTION_ERROR:
					//						Toast.makeText(getApplicationContext(), "Connection Error", Toast.LENGTH_SHORT).show();
					break;
				case NetworkManager.CONNECTION_LOST:
					//						Toast.makeText(getApplicationContext(), "Connection Lost", Toast.LENGTH_SHORT).show();
					break;
				}
			}

		};
	}//end Constructor

	//Will be the only way to get an instance of the Network Manager
	public static NetworkManager getInstance(){
		return sInstance;
	}

	//Handle Changes in Connection State
	public void handleState(NetworkTask networkTask, int state){
		Message msg = mHandler.obtainMessage();

		switch(state){
		case CONNECTION_ERROR:
			//handle connection errors
			msg.what = NetworkManager.CONNECTION_ERROR;
			mHandler.dispatchMessage(msg);
			break;

		case CONNECTED:
			mNetworkReceiveThreadPool.execute(networkTask.getReceiveRunnable());
			msg.what = NetworkManager.CONNECTED;
			mHandler.dispatchMessage(msg);
			break;

		case CONNECTION_LOST:
			//Handle a lost connection
			msg.what = NetworkManager.CONNECTION_LOST;
			mHandler.dispatchMessage(msg);
			break;

		case NO_WIFI:
			//Alert user to enable WIFI
			break;

		case NOT_CONNECTED:
			//Connect for first time, possibly not needed
			break;
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
		connectTask.initializeConnectTask(ip, port);

		sInstance.mNetworkConnectThreadPool.execute(connectTask.getConnectRunnable());

		return connectTask;

	}

}
