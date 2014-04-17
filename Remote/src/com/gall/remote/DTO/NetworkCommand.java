package com.gall.remote.DTO;

import org.json.JSONException;
import org.json.JSONObject;

public class NetworkCommand {
	
	//command type constants
	//To server
	public static final String ADD_TO_PLAY_QUEUE = "ADD TO PLAY QUEUE";
	public static final String PLAY_NOW = "PLAY NOW";
	public static final String DISCONNECT = "DISCONNECT";
	public static final String BUTTON_PRESSED = "BUTTON PRESSED";
	
	//From server
	public static final String UPDATE_LIBRARY = "UPDATE LIBRARY";
	public static final String LIBRARY_UPDATE_COMPLETE = "LIBRARY UPDATE COMPLETE";
	public static final String DELETE_LIBRARY = "DELETE LIBRARY";
	
	//Constants to fill "Extra" fields
	//Button Presses
	public static final String PLAY_PRESSED = "PLAY PRESSED";
	public static final String PREVIOUS_PRESSED = "PREVIOUS PRESSED";
	public static final String NEXT_PRESSED = "NEXT PRESSED";
	
	//selection sources
	public static final String SELECTED_FROM_ALBUM = "SELECTED FROM ALBUM";
	public static final String SELECTED_FROM_ARTIST = "SELECTED FROM ARTIST";
	public static final String SELECTED_FROM_SONGS = "SELECTED FROM SONGS";
	
	//JSON keys for parsing
	public static final String COMMAND_TYPE = "COMMAND TYPE";
	public static final String EXTRA1 = "EXTRA1";
	public static final String EXTRA2 = "EXTRA2";
	public static final String EXTRA3 = "EXTRA3";
	
	//String indicating that a value was not set.
	private static final String NOT_SET = "NOT SET";
	
	//Constants to write JSON
	private static final String JSON_START = "{";
	private static final String JSON_SEPARATOR = "\" , ";
	private static final String JSON_END = "\"}";
	
	private static final String JSON_COMMAND_TYPE = "\"COMMAND TYPE\":\"";
	private static final String JSON_EXTRA1 = "\"EXTRA1\":\"";
	private static final String JSON_EXTRA2 = "\"EXTRA2\":\"";
	private static final String JSON_EXTRA3 = "\"EXTRA3\":\"";
	
	//Fields in the command to be filled out.
	private String commandType;
	private String extra1;
	private String extra2;
	private String extra3;
	
	
	/**
	 *Default Constructor, set all arguments to NOT_SET
	 */
	public NetworkCommand() {
		this.setCommandType(NetworkCommand.NOT_SET);
		this.setExtra1(NetworkCommand.NOT_SET);
		this.setExtra2(NetworkCommand.NOT_SET);
		this.setExtra3(NetworkCommand.NOT_SET);
	}
	
	/**
	 * Create a network command with an input JSON String.
	 * @param JSONString - a JSON string that contains values to set the command type and extra fields.
	 */
	public NetworkCommand(String JSONString){
		
	JSONObject jsonObject;

	try {
		jsonObject = new JSONObject(JSONString);
		setCommandType(jsonObject.getString(COMMAND_TYPE));
		setExtra1(jsonObject.getString(EXTRA1));
		setExtra2(jsonObject.getString(EXTRA2));
		setExtra3(jsonObject.getString(EXTRA3));
	} catch (JSONException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
		

		
	}
	
	/**
	 * Create a NetworkCommand from arguments.  Pass a null
	 * value to mark a field as NOT_SET.
	 * @param commandType
	 * @param extra1
	 * @param extra2
	 * @param extra3
	 */
	public NetworkCommand(String commandType, String extra1,String extra2,String extra3){
		
		if(commandType != null)
			this.commandType = commandType;
		else
			this.commandType = NOT_SET;
		
		if(extra1 != null)
			this.extra1 = extra1;
		else
			this.extra1 = NOT_SET;
		
		if(extra2 != null)
			this.extra2 = extra2;
		else
			this.extra2 = NOT_SET;
		
		if(extra3 != null)
			this.extra3 = extra3;
		else
			this.extra3 = NOT_SET;
		
		
	}
	

	
	/**
	 * NetworkCommands come in the following JSON format:
	 * {"COMMAND TYPE":"value", "EXTRA1":"value", "Extra2":"value", "Extra3":"value"}
	 * The meaning of the extra fields changes based on the command type.
	 * @param JSONString
	 * @return
	 */
	public void parseNetworkCommand(String JSONString){
		
		
		try {
			
			JSONObject jsonObject = new JSONObject(JSONString);
			setCommandType(jsonObject.getString(COMMAND_TYPE));
			setExtra1(jsonObject.getString(EXTRA1));
			setExtra2(jsonObject.getString(EXTRA2));
			setExtra3(jsonObject.getString(EXTRA3));
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

				
	}
	
	/**
	 * Writes a command to send to the server formatted in JSON.
	 * @return JSON formatted command string.
	 */
	public String writeCommand(){
		
		StringBuffer sb = new StringBuffer();
		
		//Start of JSON string
		sb.append(NetworkCommand.JSON_START);
		
		//Write command type
		sb.append(NetworkCommand.JSON_COMMAND_TYPE);
		sb.append(this.commandType);
		
		//Write Extra1
		sb.append(NetworkCommand.JSON_SEPARATOR);
		sb.append(NetworkCommand.JSON_EXTRA1);
		sb.append(this.extra1);
		
		//Write Extra2
		sb.append(NetworkCommand.JSON_SEPARATOR);
		sb.append(NetworkCommand.JSON_EXTRA2);
		sb.append(this.extra2);
		
		//Write Extra3
		sb.append(NetworkCommand.JSON_SEPARATOR);
		sb.append(NetworkCommand.JSON_EXTRA3);
		sb.append(this.extra3);
		
		//Write end of JSON String
		sb.append(NetworkCommand.JSON_END);
		
		
		return sb.toString();
	}


	public String getCommandType() {
		return commandType;
	}



	public void setCommandType(String commandType) {
		this.commandType = commandType;
	}



	public String getExtra1() {
		return extra1;
	}



	public void setExtra1(String extra1) {
		this.extra1 = extra1;
	}



	public String getExtra2() {
		return extra2;
	}



	public void setExtra2(String extra2) {
		this.extra2 = extra2;
	}



	public String getExtra3() {
		return extra3;
	}



	public void setExtra3(String extra3) {
		this.extra3 = extra3;
	}

}
