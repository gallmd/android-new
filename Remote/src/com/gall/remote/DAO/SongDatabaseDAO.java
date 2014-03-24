package com.gall.remote.DAO;

import java.util.ArrayList;
import java.util.Collections;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;

import com.gall.remote.R;
import com.gall.remote.DTO.SongFile;

public class SongDatabaseDAO extends SQLiteOpenHelper {

	private static final String TRACKNUMBER = "TRACKNUMBER";
	private static final String TOTALTIME = "TOTALTIME";
	private static final String ALBUM = "ALBUM";
	private static final String ARTIST = "ARTIST";
	private static final String NAME = "NAME";
	private static final String _ID = "_ID";
	private static final String SONGS = "SONGS";
	private String columns = NAME + " , " + ARTIST + " , " + ALBUM + " , " + TOTALTIME + " , " + TRACKNUMBER;
	private String schemaCheckExists = "CREATE TABLE IF NOT EXISTS " + SONGS + " ( " + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +  NAME + " TEXT, " + ARTIST + " TEXT, " + ALBUM + " TEXT, " + TOTALTIME + " INTEGER, " + TRACKNUMBER + " INTEGER);";


	public SongDatabaseDAO(Context context) {
		super(context, "remotesongdatabase.db", null, 1);
	}


	//Inserts a SongFile into data base.  will be called from Recv Task
	public void insertSongs(SongFile songFile) throws SQLException{

			ContentValues cv = new ContentValues();
			cv.put(ALBUM, songFile.getAlbum());
			cv.put(ARTIST, songFile.getArtist());
			cv.put(NAME, songFile.getName());
			cv.put(TOTALTIME, songFile.getTotalTime());
			cv.put(TRACKNUMBER, songFile.getTrackNumber());

			//insert values into database
			getWritableDatabase().insert(SONGS, ALBUM, cv);

	}



	@Override
	public void onCreate(SQLiteDatabase db) {
		String schema = "CREATE TABLE " + SONGS + " ( " + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +  NAME + " TEXT, " + ARTIST + " TEXT, " + ALBUM + " TEXT, " + TOTALTIME + " INTEGER, " + TRACKNUMBER + " INTEGER);";
		db.execSQL(schema);
	}
	
	/*
	 * Deletes SONGS table
	 */
	public void dropTable(){
		getReadableDatabase().execSQL("DROP TABLE IF EXISTS SONGS");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	/**
	 * Searches database by song name
	 * @param songName
	 * @return ArrayList<SongFile>
	 */

	public ArrayList<SongFile> fetchSongsByName(String songName){
		
		ArrayList<SongFile> allSongs = new ArrayList<SongFile>();
		
		getReadableDatabase().execSQL(schemaCheckExists);
		String sqlQuery = "SELECT " + columns + " FROM " + SONGS + " WHERE " + NAME + " LIKE '%" + songName + "%'";
		Cursor cursor = getReadableDatabase().rawQuery(sqlQuery, null);

		if(cursor.getCount() > 0){

			cursor.moveToFirst();
			while(!cursor.isAfterLast()){
				
				SongFile sf = new SongFile();
				sf.setName(cursor.getString(0));
				sf.setArtist(cursor.getString(1));
				sf.setAlbum(cursor.getString(2));
				sf.setTotalTime(cursor.getInt(3));
				sf.setTrackNumber(cursor.getInt(4));
				sf.setImageID(R.drawable.ic_song);

				allSongs.add(sf);

			}

		}

		cursor.close();
		return allSongs;
	}
	
	public ArrayList<SongFile> fetchSongsByAlbum(String artist, String album){
		
		ArrayList<SongFile> songsByAlbum = new ArrayList<SongFile>();
		
		
		//Check for apostrophe
		album = album.replace("'", "''");
		
		getReadableDatabase().execSQL(schemaCheckExists);
		String[] queryColumns = {NAME, TOTALTIME, TRACKNUMBER};
		String query = SQLiteQueryBuilder.buildQueryString(false, SONGS, queryColumns, ALBUM + "='" + album +"' AND " + ARTIST + "='" + artist + "'", null, null, null, null);
		Cursor cursor = getReadableDatabase().rawQuery(query, null);
		
		if(cursor.getCount() > 0){
			
			cursor.moveToFirst();
			
			while(!cursor.isAfterLast()){
				
				SongFile sf = new SongFile();
				sf.setName(cursor.getString(0));
				sf.setArtist(artist);
				sf.setAlbum(album);
				sf.setTotalTime(cursor.getInt(1));
				sf.setTrackNumber(cursor.getInt(2));
				sf.setImageID(R.drawable.ic_song);

				songsByAlbum.add(sf);
				
				cursor.moveToNext();
			}
		}
		
		cursor.close();
		Collections.sort(songsByAlbum, SongFile.sortByTrackNumber);
		return songsByAlbum;
	}


	public ArrayList<SongFile> fetchAllSongs(){
		
		ArrayList<SongFile> allSongs = new ArrayList<SongFile>();
		
		getReadableDatabase().execSQL(schemaCheckExists);
		String[] queryColumns = {NAME, ARTIST, ALBUM, TOTALTIME, TRACKNUMBER};
		String query = SQLiteQueryBuilder.buildQueryString(true, SONGS, queryColumns, null, null, null, NAME +" ASC", null);
		Cursor cursor = getReadableDatabase().rawQuery(query, null);
		
		if(cursor.getCount() > 0){

			cursor.moveToFirst();

			while(!cursor.isAfterLast()){
				
				SongFile sf = new SongFile();
				sf.setName(cursor.getString(0));
				sf.setArtist(cursor.getString(1));
				sf.setAlbum(cursor.getString(2));
				sf.setTotalTime(cursor.getInt(3));
				sf.setTrackNumber(cursor.getInt(4));
				sf.setImageID(R.drawable.ic_song);
				allSongs.add(sf);

				cursor.moveToNext();

			}
		}
		cursor.close();
		return allSongs;

	}

	/**
	 * Returns an ArrayList of SongFiles, sorted in ascending order by artist, with only the artist and image id fields populated
	 * List will be displayed in the "All Artists" ArtistsFragment
	 * @return
	 */
	public ArrayList<SongFile> fetchAllArtists(){
		ArrayList<SongFile> allArtists = new ArrayList<SongFile>();
		
		//If SONGS table doesn't exist, create it
		getReadableDatabase().execSQL(schemaCheckExists);
		
		//Create query using query builder
		String[] queryColumns = {ARTIST};
		String query = SQLiteQueryBuilder.buildQueryString(true, SONGS, queryColumns, null, null, null, ARTIST + " ASC", null);
		Cursor cursor = getReadableDatabase().rawQuery(query, null);

		if(cursor.getCount() > 0){

			cursor.moveToFirst();

			while(!cursor.isAfterLast()){
				
				//For list of artists, all that is needed is the artist name and a picture
				SongFile sf = new SongFile();
				sf.setArtist(cursor.getString(0));
				sf.setImageID(R.drawable.ic_artist);
				allArtists.add(sf);

				cursor.moveToNext();
			}
		}
		
		cursor.close();
		
		return allArtists;
	}
	
	/*
	 * Returns an ArrayList of song files sorted by album
	 */
	public ArrayList<SongFile> fetchAllAlbums(){
		
		ArrayList<SongFile> allAlbums = new ArrayList<SongFile>();
		
		//If SONGS table doesn't exist, create it
		getReadableDatabase().execSQL(schemaCheckExists);

		//Create query using query builder
		String[] queryColumns = {ALBUM, ARTIST};
		String query = SQLiteQueryBuilder.buildQueryString(true, SONGS, queryColumns, null, null, null, ALBUM + " ASC", null);
		Cursor cursor = getReadableDatabase().rawQuery(query, null);

		if(cursor.getCount() > 0){

			cursor.moveToFirst();

			while(!cursor.isAfterLast()){

				SongFile sf = new SongFile();
				
				sf.setAlbum(cursor.getString(0));
				sf.setArtist(cursor.getString(1));
				sf.setImageID(R.drawable.ic_album);

				allAlbums.add(sf);

				cursor.moveToNext();

			}
		}
		
		cursor.close();
		
		return allAlbums;

	}
	
	/*
	 * Returns an ArrayList of song files sorted by album
	 */
	public ArrayList<SongFile> fetchAlbumsByArtist(String artist){
		
		ArrayList<SongFile> allAlbums = new ArrayList<SongFile>();
		
		//Check for apostrophe
		artist = artist.replace("'", "''");
		
		//If SONGS table doesn't exist, create it
		getReadableDatabase().execSQL(schemaCheckExists);

		//Create query using query builder
		String[] queryColumns = {ALBUM};
		String query = SQLiteQueryBuilder.buildQueryString(true, SONGS, queryColumns, ARTIST + "='" + artist +"'", null, null, ALBUM + " ASC", null);
		Cursor cursor = getReadableDatabase().rawQuery(query, null);

		if(cursor.getCount() > 0){

			cursor.moveToFirst();

			while(!cursor.isAfterLast()){

				SongFile sf = new SongFile();
				
				sf.setAlbum(cursor.getString(0));
				sf.setArtist(artist);
				sf.setImageID(R.drawable.ic_album);

				allAlbums.add(sf);

				cursor.moveToNext();

			}
		}
		
		cursor.close();
		
		return allAlbums;

	}
	

}
