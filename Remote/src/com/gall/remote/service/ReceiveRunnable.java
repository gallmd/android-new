package com.gall.remote.service;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

import android.content.Context;
import android.database.SQLException;

import com.gall.remote.ParseJSON;
import com.gall.remote.DAO.SongDatabaseDAO;
import com.gall.remote.DTO.SongFile;

/**
 * Runnable that runs an infinite loop to check for messages from server and then process them.
 * @author Matt
 *
 */
public class ReceiveRunnable implements Runnable{

	private boolean bUpdateFlag;
	private boolean bInCommand;
	private Context mContextConnect;
	private ParseJSON parse;
	private SongDatabaseDAO db;
	
	final TaskRunnableReceiveMethods mNetworkTask;
	private DataInputStream input;
	private DataOutputStream output;
	private Socket clientSocket;
	
	//Receive state flags
	public static final int DISCONNECTED_FROM_SERVER = 0;
	public static final int CONNECTION_LOST = 1;
	public static final int SOCKET_READ_ERROR = 2;
	public static final int LIBRARY_DELETED = 3;
	public static final int LIBRARY_UPDATE_IN_PROGRESS = 4;
	public static final int LIBRARY_UPDATE_COMPLETED = 5;
	
	interface TaskRunnableReceiveMethods{
		
		Socket getConnectionSocket();
		
		void handleReceiveState(int state);
		
		Context getContext();
	}
	
	public ReceiveRunnable(TaskRunnableReceiveMethods mNetworkTask) {
		this.mNetworkTask = mNetworkTask;
	}

	@Override
	public void run() {
		
		android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
		
		clientSocket = mNetworkTask.getConnectionSocket();
		
		try {
			input = new DataInputStream(clientSocket.getInputStream());
			output = new DataOutputStream(clientSocket.getOutputStream());
			mContextConnect = mNetworkTask.getContext();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		
		//infinite loop
		while(clientSocket.isConnected()){

			String message;
			//receive  message
			try {
				
				message = input.readLine();
				
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
						mNetworkTask.handleReceiveState(LIBRARY_UPDATE_IN_PROGRESS);
					}

					if(message.equalsIgnoreCase("delete library")){
						
						db = new SongDatabaseDAO(mContextConnect);
						
						db.dropTable();
						mNetworkTask.handleReceiveState(LIBRARY_DELETED);
						
					}

					if(message.equals("DISCONNECT")){
						
						break;
						
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
							db.close();
							db = null;
							mNetworkTask.handleReceiveState(LIBRARY_UPDATE_COMPLETED);

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
				
				//Exiting the while loop means that we disconnected from the server
				clientSocket.close();
				mNetworkTask.handleReceiveState(DISCONNECTED_FROM_SERVER);
				input.close();

			} catch (SocketException e) {
				mNetworkTask.handleReceiveState(CONNECTION_LOST);
				e.printStackTrace();
				

			} catch (IOException e1) {
				mNetworkTask.handleReceiveState(SOCKET_READ_ERROR);
				e1.printStackTrace();
			} 
		}
	}

}
