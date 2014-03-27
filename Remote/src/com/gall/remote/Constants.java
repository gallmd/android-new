package com.gall.remote;

public class Constants {

	//Keys for key-value pairs
	public class Keys{
		
		//Search Type
		public static final String SEARCH_TYPE = "searchType";
		
		//Keys for providing search parameters
		public static final String ALBUM = "album";
		public static final String SONG = "song";
		public static final String ARTIST = "artist";
		
		//Keys for network functions
		public static final String PORT = "port";
		public static final String IP_ADDRESS = "ipAddress";
		
	}
	
	//Values for search types
	public class SearchTypes{
		
		//Song search types
		public static final String SONGS_BY_ALBUM = "songsByAlbum";
		public static final String SONGS_BY_ARTIST = "songsByArtist";
		public static final String SONGS_ALL = "songsAll";
		
		//Album search types
		public static final String ALBUMS_BY_ARTIST = "albumsByArtist";
		public static final String ALBUMS_ALL = "albumsAll";
		
		//Artist search types
		public static final String ARTISTS_ALL = "artistsAll";
				
	}
	
	public class Intents{
		
		public static final String BROADCAST_ACTION = "com.gall.remote.Broadcast";
		
		public static final String EXTENDED_DATA_STATUS = "com.gall.remote.Status";
		
	}
	

	public class ServiceMessages{
		
		//Messages sent to the background service
		
		//Play Controls
		public static final int PLAY_PRESSED = 1;
		public static final int NEXT_PRESSED = 2;
		public static final int PREVIOUS_PRESSED = 3;
	}
	
	public class NetworkMessages{
		public static final int CONNECT = 1;
		public static final int SEND_MESSAGE = 2;
	}
}
