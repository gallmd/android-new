package com.gall.remote.DAO;

import java.util.ArrayList;

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

	private static final String TRACK = "TRACK";
	private static final String LENGTH = "LENGTH";
	private static final String ALBUM = "ALBUM";
	private static final String ARTIST = "ARTIST";
	private static final String TITLE = "TITLE";
	private static final String _ID = "_ID";
	private static final String SONGS = "SONGS";
	private static final String GUID = "GUID";
	@SuppressWarnings("unused")
	private static final String columns = GUID + " , " + TITLE + " , " + ARTIST + " , " + ALBUM + " , " + LENGTH + " , " + TRACK;
	private static final String schemaCheckExists = "CREATE TABLE IF NOT EXISTS " + SONGS + " ( " + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + GUID + " INTEGER , " +  TITLE + " TEXT, " + ARTIST + " TEXT, " + ALBUM + " TEXT, " + LENGTH + " TEXT, " + TRACK + " INTEGER);";
	private static final String schema = "CREATE TABLE " + SONGS + " ( " + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + GUID + " INTEGER, " + TITLE + " TEXT, " + ARTIST + " TEXT, " + ALBUM + " TEXT, " + LENGTH + " TEXT, " + TRACK + " INTEGER);";

	public SongDatabaseDAO(Context context) {
		super(context, "remotesongdatabase.db", null, 1);
	}


	//Inserts a SongFile into data base.  will be called from Recv Task
	public void insertSongs(SongFile songFile) throws SQLException{

//		getReadableDatabase().execSQL(schemaCheckExists);

		ContentValues cv = new ContentValues();
		cv.put(GUID, songFile.getGUID());
		cv.put(ALBUM, songFile.getAlbum());
		cv.put(ARTIST, songFile.getArtist());
		cv.put(TITLE, songFile.getTitle());
		cv.put(LENGTH, songFile.getLength());
		cv.put(TRACK, songFile.getTrack());

		//insert values into database
		getWritableDatabase().insert(SONGS, ALBUM, cv);

	}



	@Override
	public void onCreate(SQLiteDatabase db) {
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
		String sqlQuery = "SELECT * FROM " + SONGS + " WHERE " + TITLE + " LIKE '%" + songName + "%'";
		Cursor cursor = getReadableDatabase().rawQuery(sqlQuery, null);

		if(cursor.getCount() > 0){

			cursor.moveToFirst();
			while(!cursor.isAfterLast()){

				SongFile sf = new SongFile();
				sf.setTitle(cursor.getString(0));
				sf.setArtist(cursor.getString(1));
				sf.setAlbum(cursor.getString(2));
				sf.setLength(cursor.getString(3));
				sf.setTrack(cursor.getInt(4));
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
		String[] queryColumns = {GUID, TITLE, LENGTH, TRACK};
		String query = SQLiteQueryBuilder.buildQueryString(false, SONGS, queryColumns, ALBUM + "='" + album +"' AND " + ARTIST + "='" + artist + "'", null, null, TRACK +" ASC", null);
		Cursor cursor = getReadableDatabase().rawQuery(query, null);

		if(cursor.getCount() > 0){

			cursor.moveToFirst();

			while(!cursor.isAfterLast()){

				SongFile sf = new SongFile();
				sf.setGUID(cursor.getInt(0));
				sf.setTitle(cursor.getString(1));
				sf.setArtist(artist);
				sf.setAlbum(album);
				sf.setLength(cursor.getString(2));
				sf.setTrack(cursor.getInt(3));
				sf.setImageID(R.drawable.ic_song);

				songsByAlbum.add(sf);

				cursor.moveToNext();
			}
		}

		cursor.close();
		return songsByAlbum;
	}


	public ArrayList<SongFile> fetchAllSongs(){

		ArrayList<SongFile> allSongs = new ArrayList<SongFile>();

		getReadableDatabase().execSQL(schemaCheckExists);
		String[] queryColumns = {GUID, TITLE, ARTIST, ALBUM, LENGTH, TRACK};
		String query = SQLiteQueryBuilder.buildQueryString(true, SONGS, queryColumns, null, null, null, TITLE +" ASC", null);
		Cursor cursor = getReadableDatabase().rawQuery(query, null);

		if(cursor.getCount() > 0){

			cursor.moveToFirst();

			while(!cursor.isAfterLast()){

				SongFile sf = new SongFile();
				sf.setGUID(cursor.getInt(0));
				sf.setTitle(cursor.getString(1));
				sf.setArtist(cursor.getString(2));
				sf.setAlbum(cursor.getString(3));
				sf.setLength(cursor.getString(4));
				sf.setTrack(cursor.getInt(5));
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
