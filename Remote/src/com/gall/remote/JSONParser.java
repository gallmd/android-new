package com.gall.remote;

import org.json.JSONException;
import org.json.JSONObject;

import com.gall.remote.DTO.SongFile;

public class JSONParser {

private int guid;
private String title;
private String artist;
private String album;
private int track;
private String length;
private SongFile sf;

//TODO Remove this class and add parsing functionality to SongFile,
//JSON string will come from a NetworkCommand "Extra" field

/**
 * Recv's JSON data for song info one song at a time.
 * Adds the received data to the songs database.
 * 
 * @param jsonString
 */
public SongFile parseForDBInsertion(String jsonString){
	
	try {
		
		//Set JSON object from input string
		JSONObject jsonData = new JSONObject(jsonString);
		
		
		//get data from JSON object
		guid = jsonData.getInt("GUID");
		title = jsonData.getString("Title");
		artist = jsonData.getString("Artist");
		album = jsonData.getString("Album");
		track = jsonData.getInt("Track");
		length = jsonData.getString("Length");
		
		sf = new SongFile();
		sf.setGUID(guid);
		sf.setTitle(title);
		sf.setArtist(artist);
		sf.setAlbum(album);
		sf.setTrack(track);
		sf.setLength(length);
		
		
	} catch (JSONException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		sf = null;
	}
	
	return sf;
	
}


}
