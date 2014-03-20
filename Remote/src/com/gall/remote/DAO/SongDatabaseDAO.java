package com.gall.remote.DAO;

import java.util.ArrayList;
import java.util.TreeSet;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
		// TODO Auto-generated constructor stub
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
		// TODO Auto-generated method stub

	}

	/**
	 * Find song in songs database by GUID (Track ID)
	 * @param id - GUID to search by
	 * @return SongFile - the Songfile that matches the input GUID
	 */
	//	public SongFile fetchSongByGuid(int id){
	//		SongFile sf = null;
	//
	//
	//		String sqlQuery = "SELECT " + columns + " FROM " + SONGS + " WHERE " + GUID + " = " + id;
	//
	//		Cursor cursor = getReadableDatabase().rawQuery(sqlQuery, null);
	//
	//		if(cursor.getCount() == 1){
	//
	//			cursor.moveToFirst();
	//			sf = new SongFile();
	//
	//			sf.setTrackID(cursor.getInt(0));
	//			sf.setName(cursor.getString(1));
	//			sf.setArtist(cursor.getString(2));
	//			sf.setAlbum(cursor.getString(3));
	//			sf.setTotalTime(cursor.getInt(4));
	//			sf.setTrackNumber(cursor.getInt(5));
	//
	//		}
	//
	//		return sf;
	//
	//	}

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
				allSongs.add(sf);

			}

		}

		cursor.close();
		return allSongs;
	}


	public ArrayList<SongFile> fetchAllSongs(){
		
		ArrayList<SongFile> allSongs = new ArrayList<SongFile>();
		
		getReadableDatabase().execSQL(schemaCheckExists);
		String sqlQuery = "SELECT " + columns + " FROM " + SONGS;
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
				allSongs.add(sf);

				cursor.moveToNext();

			}
		}
		cursor.close();
		return allSongs;

	}

	public TreeSet<SongFile> fetchAllArtists(){
		TreeSet<SongFile> allArtists = new TreeSet<SongFile>(SongFile.sortByArtist);
		getReadableDatabase().execSQL(schemaCheckExists);

		String sqlQuery = "SELECT " + columns + " FROM " + SONGS;
		Cursor cursor = getReadableDatabase().rawQuery(sqlQuery, null);

		if(cursor.getCount() > 0){

			cursor.moveToFirst();

			while(!cursor.isAfterLast()){

				SongFile sf = new SongFile();
				sf.setArtist(cursor.getString(1));
				sf.setName(cursor.getString(0));
				sf.setAlbum(cursor.getString(2));
				sf.setTotalTime(cursor.getInt(3));
				sf.setTrackNumber(cursor.getInt(4));
				allArtists.add(sf);

				cursor.moveToNext();
			}
		}
		
		cursor.close();
		return allArtists;
	}
	
	/*
	 * Returns a TreeSet of song files sorted by album
	 */
	public TreeSet<SongFile> fetchAllAlbums(){
		TreeSet<SongFile> allArtists = new TreeSet<SongFile>(SongFile.sortByAlbum);
		getReadableDatabase().execSQL(schemaCheckExists);

		String sqlQuery = "SELECT " + columns + " FROM " + SONGS;
		Cursor cursor = getReadableDatabase().rawQuery(sqlQuery, null);

		if(cursor.getCount() > 0){

			cursor.moveToFirst();

			while(!cursor.isAfterLast()){

				SongFile sf = new SongFile();
				sf.setArtist(cursor.getString(1));
				sf.setName(cursor.getString(0));
				sf.setAlbum(cursor.getString(2));
				sf.setTotalTime(cursor.getInt(3));
				sf.setTrackNumber(cursor.getInt(4));

				allArtists.add(sf);

				cursor.moveToNext();

			}
		}
		cursor.close();
		return allArtists;

	}

}
