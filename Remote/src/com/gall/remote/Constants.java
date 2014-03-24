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
}
