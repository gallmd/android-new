package com.gall.remote.DTO;

import java.util.Comparator;
import java.util.Locale;

public class SongFile implements Comparable<SongFile> {
	
	private int trackID;
	private String artist;
	private String album;
	private String name;
	private int totalTime;
	private int trackNumber;
	private int imageID;
	
	//getters
	public int getTrackID() {
		return trackID;
	}
	public String getArtist() {
		return artist;
	}
	public String getAlbum() {
		return album;
	}
	public String getName() {
		return name;
	}
	public int getTotalTime() {
		return totalTime;
	}
	public int getTrackNumber() {
		return trackNumber;
	}
	public int getImageID(){
		return imageID;
	}
	
	//setters
	public void setTrackID(int trackID) {
		this.trackID = trackID;
	}
	public void setArtist(String artist) {
		this.artist = artist;
	}
	public void setAlbum(String album) {
		this.album = album;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setTotalTime(int totalTime) {
		this.totalTime = totalTime;
	}
	public void setTrackNumber(int trackNumber) {
		this.trackNumber = trackNumber;
	}
	public void setImageID(int imageID){
		this.imageID = imageID;
	}
	
	//comparators
	@Override
	public int compareTo(SongFile compareSong) {
		
		return 0;
	}
	//Sort by name Comparator
	public static Comparator<SongFile> sortByName = new Comparator<SongFile>(){

		@Override
		public int compare(SongFile song1, SongFile song2) {
			
			String songName1 = song1.getName().toUpperCase(Locale.US);
			String songName2 = song2.getName().toUpperCase(Locale.US);
			
			return songName1.compareTo(songName2);
		}
		
	};
	
	//Sort by name Comparator
	public static Comparator<SongFile> sortByArtist = new Comparator<SongFile>(){

		@Override
		public int compare(SongFile song1, SongFile song2) {
			
			String songName1 = song1.getArtist().toUpperCase(Locale.US);
			String songName2 = song2.getArtist().toUpperCase(Locale.US);
			
			return songName1.compareTo(songName2);
		}
		
	};
	
	//Sort by name Comparator
	public static Comparator<SongFile> sortByAlbum = new Comparator<SongFile>(){

		@Override
		public int compare(SongFile song1, SongFile song2) {
			
			String songName1 = song1.getAlbum().toUpperCase(Locale.US);
			String songName2 = song2.getAlbum().toUpperCase(Locale.US);
			
			return songName1.compareTo(songName2);
		}
		
	};
	
	//Sort by Track Number Comparator
	public static Comparator<SongFile> sortByTrackNumber = new Comparator<SongFile>(){

		@Override
		public int compare(SongFile song1, SongFile song2) {
			
			int trackNumber1 = song1.getTrackNumber();
			int trackNumber2 = song2.getTrackNumber();
			
			return trackNumber1 - trackNumber2;
		}
		
	};

}
