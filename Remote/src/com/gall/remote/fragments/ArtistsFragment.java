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
import com.gall.remote.adapters.ArtistFragmentAdapter;
import com.gall.remote.network.Constants;

/**
 * Fragment that displays a list of artists to choose from
 * @author Matt Gall
 *
 */
public class ArtistsFragment extends ListFragment {

	private String searchType;
	private ArrayList<SongFile> allArtists;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.fragment_list, container, false);
		
		//Provides a way to search for specific artists to display.  No reason to implement yet for artists. 
		searchType = getArguments().getString(Constants.Keys.SEARCH_TYPE);
		
		if(searchType == Constants.SearchTypes.ARTISTS_ALL){
			SongDatabaseDAO artistDB = new SongDatabaseDAO(getActivity());
			allArtists = artistDB.fetchAllArtists();
		}

	
		//setup ListAdapter
		ArtistFragmentAdapter artistAdapter = new ArtistFragmentAdapter(inflater.getContext(), R.layout.row_layout, allArtists);
		setListAdapter(artistAdapter);

		return rootView;
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {

		super.onListItemClick(l, v, position, id);
		
		//Create new Songs Fragment and add arguments based on what album was selected
		AlbumsFragment albumsFragment = new AlbumsFragment();
		Bundle bundle = new Bundle();
		String artist = allArtists.get(position).getArtist();
		
		//populate bundle and set arguments
		bundle.putString(Constants.Keys.SEARCH_TYPE, Constants.SearchTypes.ALBUMS_BY_ARTIST);
		bundle.putString(Constants.Keys.ARTIST, artist);
		albumsFragment.setArguments(bundle);
		
		//Create fragment transaction and replace with new songs fragment
		FragmentTransaction trans = getActivity().getFragmentManager().beginTransaction();
		trans.addToBackStack(null);
		trans.remove(this);
		trans.add(R.id.fragmentContainer,  albumsFragment);

		trans.commit();

	}



}
