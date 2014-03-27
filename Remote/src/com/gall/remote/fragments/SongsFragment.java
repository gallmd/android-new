package com.gall.remote.fragments;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.gall.remote.Constants;
import com.gall.remote.R;
import com.gall.remote.DAO.SongDatabaseDAO;
import com.gall.remote.DTO.SongFile;
import com.gall.remote.adapters.SongsFragmentAdapter;

/**
 * Fragment that displays a list of songs to choose from
 * @author Matt Gall
 *
 */
public class SongsFragment extends ListFragment {

	private String searchType;
	private ArrayList<SongFile> allSongs;
	private FragmentActivity myContext;
	private TextView totalTimeText;
	private TextView songText;
	private TextView artistText;
	private String artistFromArgs;
	private String albumFromArgs;

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

			artistFromArgs = getArguments().getString(Constants.Keys.ARTIST);
			albumFromArgs = getArguments().getString(Constants.Keys.ALBUM);
			
			getActivity().getActionBar().setTitle(albumFromArgs);


			//perform database query 
			allSongs = db.fetchSongsByAlbum(artistFromArgs, albumFromArgs);

		}else if(searchType == Constants.SearchTypes.SONGS_BY_ARTIST){
			//All songs by a specific artist, list is returned sorted by name
			//TODO add method to fetch songs by artist
		}


		//setup ListAdapter
		SongsFragmentAdapter artistAdapter = new SongsFragmentAdapter(inflater.getContext(), R.layout.row_layout, allSongs);
		setListAdapter(artistAdapter);

		artistText = (TextView) myContext.findViewById(R.id.txtArtist);
		songText = (TextView) myContext.findViewById(R.id.txtSong);
		totalTimeText = (TextView) myContext.findViewById(R.id.txtTimeTotal);

		return rootView;
	}

	@Override
	public void onAttach(Activity activity) {

		myContext = (FragmentActivity) activity;
		super.onAttach(activity);
	}

	@SuppressLint("DefaultLocale")
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {

		super.onListItemClick(l, v, position, id);
		
		//TODO send selected file to the intent service to be added to the play queue
		
		//Get selected song file
		SongFile selectedSong = allSongs.get(position);

		//Set Views based on search type
		if(searchType == Constants.SearchTypes.SONGS_ALL){
			
			artistText.setText(getResources().getString(R.string.lbl_artist) + " " + selectedSong.getArtist());
			songText.setText(getResources().getString(R.string.lbl_song) + " " + selectedSong.getName());
			
		}else if(searchType == Constants.SearchTypes.SONGS_BY_ALBUM){
			
			artistText.setText(getResources().getString(R.string.lbl_artist) + " " + artistFromArgs);
			songText.setText(getResources().getString(R.string.lbl_song) + " " + selectedSong.getName());
		}

		int time = selectedSong.getTotalTime();
		time = Math.abs(time);

		String timeString = String.format("%d:%d",
				TimeUnit.NANOSECONDS.toMinutes(time),
				TimeUnit.NANOSECONDS.toSeconds(time)-
				TimeUnit.MINUTES.toSeconds(TimeUnit.NANOSECONDS.toMinutes(time)));
		totalTimeText.setText(timeString);



	}

}
