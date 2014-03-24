package com.gall.remote.fragments;

import java.util.ArrayList;

import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.gall.remote.R;
import com.gall.remote.DAO.SongDatabaseDAO;
import com.gall.remote.DTO.SongFile;
import com.gall.remote.adapters.AlbumFragmentAdapter;
import com.gall.remote.network.Constants;

/**
 * Fragment to display a list of albums to choose from
 * @author Matt Gall
 *
 */
public class AlbumsFragment extends ListFragment {

	private String searchType;
	private ArrayList<SongFile> allAlbums;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

		searchType = getArguments().getString(Constants.Keys.SEARCH_TYPE);
		View rootView = inflater.inflate(R.layout.fragment_list, container, false);
		SongDatabaseDAO albumsDB = new SongDatabaseDAO(getActivity());

		//Determine which albums to display
		if(searchType == Constants.SearchTypes.ALBUMS_ALL){
			
			//Display All Albums
			allAlbums = albumsDB.fetchAllAlbums();
			
		}else if(searchType == Constants.SearchTypes.ALBUMS_BY_ARTIST){
			//Add Albums from a specific artist
			//TODO add fetchAlbumsByArtist method to db DAO
//			allAlbums = albumsDB.fetchAlbumsByArtist();
			
		}//end if

		//setup ListAdapter
		AlbumFragmentAdapter artistAdapter = new AlbumFragmentAdapter(inflater.getContext(), R.layout.row_layout, allAlbums);
		setListAdapter(artistAdapter);

		return rootView;
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {

		super.onListItemClick(l, v, position, id);
		
		//Create new Songs Fragment and add arguments based on what album was selected
		SongsFragment songsFragment = new SongsFragment();
		Bundle bundle = new Bundle();
		String album = allAlbums.get(position).getAlbum();
		String artist = allAlbums.get(position).getArtist();
		
		//populate bundle and set arguments
		bundle.putString(Constants.Keys.SEARCH_TYPE, Constants.SearchTypes.SONGS_BY_ALBUM);
		bundle.putString(Constants.Keys.ALBUM, album);
		bundle.putString(Constants.Keys.ARTIST, artist);
		songsFragment.setArguments(bundle);
		
		//Create fragment transaction and replace with new songs fragment
		FragmentTransaction trans = getActivity().getFragmentManager().beginTransaction();
		trans.addToBackStack(null);
		trans.remove(this);
		trans.add(R.id.fragmentContainer,  songsFragment);

		trans.commit();

	}
}
