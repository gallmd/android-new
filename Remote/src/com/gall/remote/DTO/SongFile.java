package com.gall.remote.DTO;

import java.io.IOException;

public class SongFile {

	private int GUID;
	private String title;
	private String album;
	private String artist;
	private String albumArtist;
	private String composer;
	private int track;
	private int disc;
	private int bpm;
	private int year;
	private String genre;
	private String comment;
	private String compilation;
	private int length;
	private int bitRate;
	private int sampleRate;
	private int directory;
	private String fileName;
	private int mTime;
	private int cTime;
	private int fileSize;
	private int sampler;
	private String artAutomatic;
	private String artManual;
	private int fileType;
	private int playCount;
	private int lastPlayed;
	private int rating;
	private int forcedCompilationOn;
	private int forcedCompilationOff;
	private int skipCount;
	private int score;
	private int beginning;
	private String cuePath;
	private int unavailable;
	private String effectiveAlbumArtist;
	private int imageID;
	


	/**
	 * Returns the Global Unique ID of the SongFile,
	 * column '#' in the database.
	 * @return GUID of SongFile in Database
	 */
	public int getGUID() {
		return GUID;
	}



	/**
	 * Sets the Global Unique ID of the SongFile,
	 * column '#' in the database.
	 * @param GUID of SongFile in database
	 */
	public void setGUID(int gUID) {
		GUID = gUID;
	}



	/**
	 * Returns the Title of the SongFile,
	 * column 'title' in the database.
	 * @return Title of SongFile
	 */
	public String getTitle() {
		return title;
	}

	

	/**
	 * Sets the Title of the SongFile,
	 * column 'title' in the database.
	 * @param Tile of the SongFile
	 */
	public void setTitle(String title) {
		this.title = title;
	}



	/**
	 * Returns the Album name of the SongFile,
	 * column 'album' in the database.
	 * @return Album that the SongFile appears on.
	 */
	public String getAlbum() {
		return album;
	}



	/**
	 * Sets the Album name of the SongFile,
	 * column 'album' in the database.
	 * @param Album that the SongFile appears on.
	 */
	public void setAlbum(String album) {
		this.album = album;
	}



	/**
	 * Returns the Artist name of the SongFile,
	 * column 'artist' in the database.
	 * @return Artist that created the SongFile.
	 */
	public String getArtist() {
		return artist;
	}




	/**
	 * Sets the Artist name of the SongFile,
	 * column 'artist' in the database.
	 * @param Artist that created the SongFile.
	 */
	public void setArtist(String artist) {
		this.artist = artist;
	}




	/**
	 * Returns the Album Artist of the SongFile,
	 * column 'albumartist' in the database.
	 * @return Album Artist of  the SongFile.
	 */
	public String getAlbumArtist() {
		return albumArtist;
	}




	/**
	 * Sets the Album Artist of the SongFile,
	 * column 'albumartist' in the database.
	 * @param Album Artist of  the SongFile.
	 */
	public void setAlbumArtist(String albumArtist) {
		this.albumArtist = albumArtist;
	}



	/**
	 * Returns the Composer of the SongFile,
	 * column 'composer' in the database.
	 * @return Composer of  the SongFile.
	 */
	public String getComposer() {
		return composer;
	}



	/**
	 * Sets the Composer of the SongFile,
	 * column 'composer' in the database.
	 * @param Composer of  the SongFile.
	 */
	public void setComposer(String composer) {
		this.composer = composer;
	}



	/**
	 * Returns the Track Number of the SongFile,
	 * column 'track' in the database.
	 * @return Track Number of  the SongFile.
	 */
	public int getTrack() {
		return track;
	}



	/**
	 * Sets the Track Number of the SongFile,
	 * column 'track' in the database.
	 * @param Track Number of the SongFile.
	 */
	public void setTrack(int track) {
		this.track = track;
	}



	/**
	 * Returns the Disk Number of the SongFile,
	 * column 'disc' in the database.
	 * @return Disk Number of  the SongFile.
	 */
	public int getDisc() {
		return disc;
	}



	/**
	 * Sets the Disk Number of the SongFile,
	 * column 'disc' in the database.
	 * @param Disk Number of  the SongFile.
	 */
	public void setDisc(int disc) {
		this.disc = disc;
	}



	/**
	 * Returns the Beats per minute of the SongFile,
	 * column 'bpm' in the database.
	 * @return Beats per minute of  the SongFile.
	 */
	public int getBpm() {
		return bpm;
	}



	/**
	 * Sets the Beats per minute of the SongFile,
	 * column 'bpm' in the database.
	 * @param Beats per minute of  the SongFile.
	 */
	public void setBpm(int bpm) {
		this.bpm = bpm;
	}



	/**
	 * Returns the Year that the SongFile was created,
	 * column 'year' in the database.
	 * @return Year that the SongFile was created.
	 */
	public int getYear() {
		return year;
	}



	/**
	 * Sets the Year that the SongFile was created,
	 * column 'year' in the database.
	 * @param Year that the SongFile was created.
	 */
	public void setYear(int year) {
		this.year = year;
	}



	/**
	 * Returns the Genre of the SongFile,
	 * column 'genre' in the database.
	 * @return Genre of the SongFile.
	 */
	public String getGenre() {
		return genre;
	}



	/**
	 * Sets the Genre of the SongFile,
	 * column 'genre' in the database.
	 * @param Genre of the SongFile.
	 */
	public void setGenre(String genre) {
		this.genre = genre;
	}



	/**
	 * Returns any comments associated with the SongFile,
	 * column 'comment' in the database.
	 * @return Any comments associated with the SongFile.
	 */
	public String getComment() {
		return comment;
	}



	/**
	 * Sets any comments associated with the SongFile,
	 * column 'comment' in the database.
	 * @param Any comments associated with the SongFile.
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}



	/**
	 * column 'compilation' in the database.
	 */
	public String getCompilation() {
		return compilation;
	}



	/**
	 * column 'compilation' in the database.
	 */
	public void setCompilation(String compilation) {
		this.compilation = compilation;
	}



	/**
	 * Returns length of the SongFile in nano seconds.
	 * column 'length' in the database.
	 */
	public int getLength() {
		return length;
	}



	/**
	 * Returns length of the SongFile in nano seconds.
	 * column 'length' in the database.
	 */
	public void setLength(int length) {
		this.length = length;
	}



	/**
	 * column 'bitrate' in the database.
	 */
	public int getBitRate() {
		return bitRate;
	}



	/**
	 * column 'bitrate' in the database.
	 */
	public void setBitRate(int bitRate) {
		this.bitRate = bitRate;
	}



	/**
	 * column 'samplerate' in the database.
	 */
	public int getSampleRate() {
		return sampleRate;
	}



	/**
	 * column 'samplerate' in the database.
	 */
	public void setSampleRate(int sampleRate) {
		this.sampleRate = sampleRate;
	}



	/**
	 * column 'directory' in the database.
	 */
	public int getDirectory() {
		return directory;
	}



	/**
	 * column 'directory' in the database.
	 */
	public void setDirectory(int directory) {
		this.directory = directory;
	}



	/**
	 * column 'filename' in the database.
	 */
	public String getFileName() {
		return fileName;
	}



	/**
	 * column 'filename' in the database.
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}



	/**
	 * Returns the time since the epoch when the file
	 * was last modified.
	 * column 'mtime' in the database.
	 */
	public int getmTime() {
		return mTime;
	}



	/**
	 * Sets the time since the epoch when the file
	 * was last modified.
	 * column 'mtime' in the database.
	 */
	public void setmTime(int mTime) {
		this.mTime = mTime;
	}



	/**
	 * Returns the time since the epoch when the file
	 * was created.
	 * column 'ctime' in the database.
	 */
	public int getcTime() {
		return cTime;
	}



	/**
	 * Sets the time since the epoch when the file
	 * was created.
	 * column 'ctime' in the database.
	 */
	public void setcTime(int cTime) {
		this.cTime = cTime;
	}



	/**
	 * column 'filesize' in the database.
	 */
	public int getFileSize() {
		return fileSize;
	}



	/**
	 * column 'filesize' in the database.
	 */
	public void setFileSize(int fileSize) {
		this.fileSize = fileSize;
	}



	/**
	 * column 'sampler' in the database.
	 */
	public int getSampler() {
		return sampler;
	}



	/**
	 * column 'sampler' in the database.
	 */
	public void setSampler(int sampler) {
		this.sampler = sampler;
	}



	/**
	 * column 'art_automatic' in the database.
	 */
	public String getArtAutomatic() {
		return artAutomatic;
	}



	/**
	 * column 'art_automatic' in the database.
	 */
	public void setArtAutomatic(String artAutomatic) {
		this.artAutomatic = artAutomatic;
	}



	/**
	 * column 'art_manual' in the database.
	 */
	public String getArtManual() {
		return artManual;
	}



	/**
	 * column 'art_manual' in the database.
	 */
	public void setArtManual(String artManual) {
		this.artManual = artManual;
	}



	/**
	 * column 'filetype' in the database.
	 */
	public int getFileType() {
		return fileType;
	}



	/**
	 * column 'filetype' in the database.
	 */
	public void setFileType(int fileType) {
		this.fileType = fileType;
	}



	/**
	 * column 'playcount' in the database.
	 */
	public int getPlayCount() {
		return playCount;
	}



	/**
	 * column 'playcount' in the database.
	 */
	public void setPlayCount(int playCount) {
		this.playCount = playCount;
	}



	/**
	 * column 'lastplayed' in the database.
	 */
	public int getLastPlayed() {
		return lastPlayed;
	}


	
	/**
	 * column 'lastplayed' in the database.
	 */
	public void setLastPlayed(int lastPlayed) {
		this.lastPlayed = lastPlayed;
	}



	/**
	 * column 'rating' in the database.
	 */
	public int getRating() {
		return rating;
	}



	/**
	 * column 'rating' in the database.
	 */
	public void setRating(int rating) {
		this.rating = rating;
	}



	/**
	 * column 'forced_compilation_on' in the database.
	 */
	public int getForcedCompilationOn() {
		return forcedCompilationOn;
	}



	/**
	 * column 'forced_compilation_on' in the database.
	 */
	public void setForcedCompilationOn(int forcedCompilationOn) {
		this.forcedCompilationOn = forcedCompilationOn;
	}



	/**
	 * column 'forced_compilation_off' in the database.
	 */
	public int getForcedCompilationOff() {
		return forcedCompilationOff;
	}



	/**
	 * column 'forced_compilation_off' in the database.
	 */
	public void setForcedCompilationOff(int forcedCompilationOff) {
		this.forcedCompilationOff = forcedCompilationOff;
	}



	/**
	 * column 'skipcount' in the database.
	 */
	public int getSkipCount() {
		return skipCount;
	}



	/**
	 * column 'skipcount' in the database.
	 */
	public void setSkipCount(int skipCount) {
		this.skipCount = skipCount;
	}



	/**
	 * column 'score' in the database.
	 */
	public int getScore() {
		return score;
	}



	/**
	 * column 'score' in the database.
	 */
	public void setScore(int score) {
		this.score = score;
	}



	/**
	 * column 'beginning' in the database.
	 */
	public int getBeginning() {
		return beginning;
	}



	/**
	 * column 'beginning' in the database.
	 */
	public void setBeginning(int beginning) {
		this.beginning = beginning;
	}



	/**
	 * column 'cue_path' in the database.
	 */
	public String getCuePath() {
		return cuePath;
	}



	/**
	 * column 'cue_path' in the database.
	 */
	public void setCuePath(String cuePath) {
		this.cuePath = cuePath;
	}



	/**
	 * column 'unavailable' in the database.
	 */
	public int getUnavailable() {
		return unavailable;
	}



	/**
	 * column 'unavailable' in the database.
	 */
	public void setUnavailable(int unavailable) {
		this.unavailable = unavailable;
	}



	/**
	 * column 'effective_albumartist' in the database.
	 */
	public String getEffectiveAlbumArtist() {
		return effectiveAlbumArtist;
	}



	/**
	 * column 'effective_albumartist' in the database.
	 */
	public void setEffectiveAlbumArtist(String effectiveAlbumArtist) {
		this.effectiveAlbumArtist = effectiveAlbumArtist;
	}



	public int getImageID() {
		return imageID;
	}



	public void setImageID(int imageID) {
		this.imageID = imageID;
	}



	/**
	 * Method writes a JSON string to send to mobile clients.
	 * Not all field are written since the mobile application only needs
	 * a limited number of fields to operate.
	 * @return JSON string to be sent to a mobile client.
	 * @throws IOException
	 */
	public String writeSimpleJSON() throws IOException{
		StringBuffer sb = new StringBuffer();

		sb.append("{\"GUID\":\"");
		sb.append(GUID);
		sb.append("\", \"Title\":\"");
		sb.append(title);
		sb.append("\", \"Artist\":\"");
		sb.append(artist);
		sb.append("\", \"Album\":\"");
		sb.append(album);
		sb.append("\", \"Length\":\"");
		sb.append(length);
		sb.append("\", \"Track\":\"");
		sb.append(track);
		
		

		sb.append("\"}\n");

		return sb.toString();

	}

//	//comparators
//	@Override
//	public int compareTo(SongFile compareSong) {
//
//		return 0;
//		
//	}
//	//Sort by name Comparator
//	public static Comparator<SongFile> sortByName = new Comparator<SongFile>(){
//
//		@Override
//		public int compare(SongFile song1, SongFile song2) {
//
//			String songName1 = song1.getName().toUpperCase(Locale.US);
//			String songName2 = song2.getName().toUpperCase(Locale.US);
//
//			return songName1.compareTo(songName2);
//		}
//
//	};
//
//	//Sort by name Comparator
//	public static Comparator<SongFile> sortByArtist = new Comparator<SongFile>(){
//
//		@Override
//		public int compare(SongFile song1, SongFile song2) {
//
//			String songName1 = song1.getArtist().toUpperCase(Locale.US);
//			String songName2 = song2.getArtist().toUpperCase(Locale.US);
//
//			return songName1.compareTo(songName2);
//		}
//
//	};
//
//	//Sort by name Comparator
//	public static Comparator<SongFile> sortByAlbum = new Comparator<SongFile>(){
//
//		@Override
//		public int compare(SongFile song1, SongFile song2) {
//
//			String songName1 = song1.getAlbum().toUpperCase(Locale.US);
//			String songName2 = song2.getAlbum().toUpperCase(Locale.US);
//
//			return songName1.compareTo(songName2);
//		}
//
//	};
//
//	//Sort by Track Number Comparator
//	public static Comparator<SongFile> sortByTrackNumber = new Comparator<SongFile>(){
//
//		@Override
//		public int compare(SongFile song1, SongFile song2) {
//
//			int trackNumber1 = song1.getTrackNumber();
//			int trackNumber2 = song2.getTrackNumber();
//
//			return trackNumber1 - trackNumber2;
//		}
//
//	};

}
