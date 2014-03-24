package com.gall.remote.network;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.os.AsyncTask;
import android.widget.Toast;

import com.gall.remote.ParseJSON;
import com.gall.remote.DAO.SongDatabaseDAO;
import com.gall.remote.DTO.SongFile;

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
	
	//Async Network Tasks
		class NetworkConnectTask extends AsyncTask<AddressInfo, NetworkOperations, Void>{

			//		private NetworkRecvTask nrt;
			private Context mContextConnect;
			private SongDatabaseDAO db;
			private ParseJSON parse;

			//Flags
			private boolean bInCommand = false;
			private boolean bUpdateFlag = false;

			NetworkConnectTask(Context c){
				this.mContextConnect = c;
			}

			@Override
			protected Void doInBackground(AddressInfo... addressInfos) {
				Thread.currentThread().setName("Connect Async Task");

				//extract passed address info
				AddressInfo addressInfo = addressInfos[0];
				InetAddress ia = addressInfo.getIP();
				int portNumber = addressInfo.getPort();

				//create new NetworkOperations and run connect method
				NetworkOperations connectnetop = new NetworkOperations();
				try {
					connectnetop.connectToServer(ia, portNumber);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				publishProgress(connectnetop);

				//infinite loop
				while(connectnetop.isConnected()){

					String message;
					//receive  message
					try {

						message = connectnetop.recvMessage();

						//handle message
						//Check if currently in the middle of a multi-message command
						if(!bInCommand){
							//if not in a command, figure out which command to start

							//update library command
							if(message.equalsIgnoreCase("update library")){
								bUpdateFlag = true;
								bInCommand = true;
								parse = new ParseJSON();
								db = new SongDatabaseDAO(mContextConnect);
							}

							if(message.equalsIgnoreCase("delete library")){
								db = new SongDatabaseDAO(mContextConnect);
								db.dropTable();
							}

							//Add new commands here

						}else if (bInCommand){
							//if we got here, we are in the middle of a command

							//update library command
							if(bUpdateFlag){
								//check for end of command
								if(message.equalsIgnoreCase("end of update")){

									bUpdateFlag = false;
									bInCommand = false;

								}else{

									//carry out command
									SongFile sf = new SongFile();
									sf = parse.parse(message);

									if(sf!=null){

										try{
											db.insertSongs(sf);
										}catch(SQLException e){
											e.printStackTrace();
										}//end catch
									}
								}
							}//end update command

							//Add new commands here
						}

					} catch (SocketException e) {

						NetworkConnectTask reconnect = new NetworkConnectTask(mContextConnect);
						e.printStackTrace();

					} 
				}
				return null;
			}

			@Override
			protected void onProgressUpdate(NetworkOperations... connectnetop) {
				Toast nettoast = Toast.makeText(getApplicationContext(), "Connected", Toast.LENGTH_SHORT);
				nettoast.show();
			}


		}

	
}
