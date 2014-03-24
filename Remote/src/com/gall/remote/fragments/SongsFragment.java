package com.gall.remote.fragments;

import java.util.ArrayList;
import java.util.Collections;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gall.remote.R;
import com.gall.remote.DAO.SongDatabaseDAO;
import com.gall.remote.DTO.SongFile;
import com.gall.remote.adapters.RowItem;
import com.gall.remote.adapters.SongsFragmentAdapter;
import com.gall.remote.network.Constants;

/**
 * Fragment that displays a list of songs to choose from
 * @author Matt Gall
 *
 */
public class SongsFragment extends ListFragment {

	private ArrayList<RowItem> rowItems;
	private String searchType;
	private ArrayList<SongFile> allSongs;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_list, container, false);
		SongDatabaseDAO db = new SongDatabaseDAO(getActivity());

		//Determine what type of search to perform to display songs
		searchType = getArguments().getString(Constants.Keys.SEARCH_TYPE);
		
		if(searchType == Constants.SearchTypes.SONGS_ALL){
			//All songs, used for main list
			
			allSongs = db.fetchAllSongs();
			Collections.sort(allSongs, SongFile.sortByName);
			
		}else if(searchType == Constants.SearchTypes.SONGS_BY_ALBUM){
			//All Songs on a specific album, list is returned sorted by track number
			
			//Get artist and album arguments from calling fragment
			String artist = getArguments().getString(Constants.Keys.ARTIST);
			String album = getArguments().getString(Constants.Keys.ALBUM);
			
			//perform database query 
			allSongs = db.fetchSongsByAlbum(artist, album);
			
		}else if(searchType == Constants.SearchTypes.SONGS_BY_ARTIST){
			//All songs by a specific artist, list is returned sorted by name
			//TODO add method to fetch songs by artist
		}
		
		
		rowItems = new ArrayList<RowItem>();
		for (int i = 0; i < allSongs.size(); i++) {
			RowItem item = new RowItem(R.drawable.ic_launcher, allSongs.get(i).getName());
			rowItems.add(item);
		}

		RowItem blank = new RowItem(R.drawable.black_square, "");
		rowItems.add(blank);

		//setup ListAdapter
		SongsFragmentAdapter artistAdapter = new SongsFragmentAdapter(inflater.getContext(), R.layout.row_layout, allSongs);
		setListAdapter(artistAdapter);

		return rootView;
	}

}
