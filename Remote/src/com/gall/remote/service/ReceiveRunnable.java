package com.gall.remote.service;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

import android.content.Context;
import android.database.SQLException;

import com.gall.remote.JSONParser;
import com.gall.remote.DAO.SongDatabaseDAO;
import com.gall.remote.DTO.NetworkCommand;
import com.gall.remote.DTO.SongFile;

/**
 * Runnable that runs an infinite loop to check for messages from server and then process them.
 * @author Matt
 *
 */
public class ReceiveRunnable implements Runnable{

	private boolean bUpdateFlag;
	private boolean bInCommand;
	private Context mContext;
	private JSONParser parser;
	private SongDatabaseDAO db;

	final TaskRunnableReceiveMethods mNetworkTask;
	private DataInputStream input;
	private Socket clientSocket;

	//Receive state flags
	public static final int DISCONNECTED_FROM_SERVER = 100;
	public static final int CONNECTION_LOST = 101;
	public static final int SOCKET_READ_ERROR = 102;
	public static final int LIBRARY_DELETED = 103;
	public static final int LIBRARY_UPDATE_IN_PROGRESS = 104;
	public static final int LIBRARY_UPDATE_COMPLETED = 105;

	//Interface implemented by NetworkTask.  The NetworkTask must have these methods.
	interface TaskRunnableReceiveMethods{

		Socket getConnectionSocket();

		void handleReceiveState(int state);

		Context getContext();
	}

	//RecieveRunnable obtains a reference to its network task in this constructor.
	public ReceiveRunnable(TaskRunnableReceiveMethods mNetworkTask) {

		this.mNetworkTask = mNetworkTask;
	}

	@Override
	public void run() {

		android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
		
		//Get client socket that was set in the NetworkTask by it's ConnectRunnable.
		clientSocket = mNetworkTask.getConnectionSocket();

		try {
			
			//Create DataInputStream to read from and get context for database access.
			input = new DataInputStream(clientSocket.getInputStream());
			mContext = mNetworkTask.getContext();

			//infinite loop
			while(clientSocket.isConnected()){
				
				//String message received from server.
				String message;
				
				//receive  message
				message = input.readLine();
				
				NetworkCommand command = new NetworkCommand(message);
				String commandType = command.getCommandType();
				
				//handle message
				//Check if currently in the middle of a multi-message command
				if(!bInCommand){
					//if not in a command, figure out which command to start

					//update library command
					if(commandType.equals(NetworkCommand.UPDATE_LIBRARY)){
						
						bUpdateFlag = true;
						bInCommand = true;
						parser = new JSONParser();
						db = new SongDatabaseDAO(mContext);
						mNetworkTask.handleReceiveState(LIBRARY_UPDATE_IN_PROGRESS);
					}

					//delete library command
					if(commandType.equals(NetworkCommand.DELETE_LIBRARY)){

						db = new SongDatabaseDAO(mContext);
						db.dropTable();
						mNetworkTask.handleReceiveState(LIBRARY_DELETED);

					}
					
					//Disconnect command, server is shutting down.
					if(commandType.equals(NetworkCommand.DISCONNECT)){
						//Break from while loop
						break;

					}
					
					//Add new commands here

				}else if (bInCommand){
					//if we got here, we are in the middle of a command

					//update library command
					if(bUpdateFlag){
						
						if(commandType == null)
							commandType = "";
						
						//check for end of command
						if(commandType.equals(NetworkCommand.LIBRARY_UPDATE_COMPLETE)){

							bUpdateFlag = false;
							bInCommand = false;
							db.close();
							db = null;
							mNetworkTask.handleReceiveState(LIBRARY_UPDATE_COMPLETED);

						}else{

							//carry out command
							SongFile sf = new SongFile();
							sf = parser.parseForDBInsertion(message);

							if(sf!=null){

								try{
									db.insertSongs(sf);
								}catch(SQLException e){
									e.printStackTrace();
								}//end catch
								
							}//end if test checking for null SongFile
							
						}//end Command if test
						
					}//end update command

					//Add new commands here
				}

			}//End While Loop

			//Exiting the while loop means that we disconnected from the server
			clientSocket.close();
			mNetworkTask.handleReceiveState(DISCONNECTED_FROM_SERVER);
			input.close();
			Thread.currentThread().interrupt();

		} catch (SocketException e) {
			mNetworkTask.handleReceiveState(CONNECTION_LOST);
			e.printStackTrace();


		} catch (IOException e1) {
			mNetworkTask.handleReceiveState(SOCKET_READ_ERROR);
			e1.printStackTrace();
		}//End catch 

	}//End run()

}
