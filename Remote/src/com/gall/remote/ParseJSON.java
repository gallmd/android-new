package com.gall.remote;

import org.json.JSONException;
import org.json.JSONObject;

import com.gall.remote.DTO.SongFile;

public class ParseJSON {

private String name;
private String artist;
private String album;
private String trackNumber;
private String totalTime;
private String trackID;
private SongFile sf;


/**
 * Recv's JSON data for song info one song at a time.
 * Adds the recieved data to the songs database.
 * 
 * @param jsonString
 */
public SongFile parse(String jsonString){
	
	try {
		
		//Set JSON object from input string
		JSONObject jsonData = new JSONObject(jsonString);
		
		//get data from json object
		name = jsonData.getString("Name");
		artist = jsonData.getString("Artist");
		album = jsonData.getString("Album");
		trackNumber = jsonData.getString("Track Number");
		totalTime = jsonData.getString("Total Time");
		
		sf = new SongFile();
		sf.setName(name);
		sf.setArtist(artist);
		sf.setAlbum(album);
		sf.setTrackNumber(Integer.parseInt(trackNumber));
		sf.setTotalTime(Integer.parseInt(totalTime));
		
		
	} catch (JSONException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		sf = null;
	}
	
	return sf;
	
}


}
