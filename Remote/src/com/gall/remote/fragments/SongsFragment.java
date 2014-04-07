package com.gall.remote.fragments;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.gall.remote.Constants;
import com.gall.remote.MusicChooser;
import com.gall.remote.R;
import com.gall.remote.DAO.SongDatabaseDAO;
import com.gall.remote.DTO.NetworkCommand;
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
	private Messenger mMessenger;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_list, container, false);
		SongDatabaseDAO db = new SongDatabaseDAO(getActivity());

		//Determine what type of search to perform to display songs
		searchType = getArguments().getString(Constants.Keys.SEARCH_TYPE);

		if(searchType == Constants.SearchTypes.SONGS_ALL){
			
			//All songs, used for main list. list is returned sorted by song title.
			allSongs = db.fetchAllSongs();

		}else if(searchType == Constants.SearchTypes.SONGS_BY_ALBUM){
			
			//All Songs on a specific album, list is returned sorted by track number
			
			//Get artist and album from arguments passed to fragment.
			artistFromArgs = getArguments().getString(Constants.Keys.ARTIST);
			albumFromArgs = getArguments().getString(Constants.Keys.ALBUM);

			getActivity().getActionBar().setTitle(albumFromArgs);


			//perform database query 
			allSongs = db.fetchSongsByAlbum(artistFromArgs, albumFromArgs);

		}else if(searchType == Constants.SearchTypes.SONGS_BY_ARTIST){
			//All songs by a specific artist, list is returned sorted by name
			//TODO add method to fetch songs by artist
		}

		//If list is longer than the screen can display, a blank row is added
		//to the list so that the bottom list element is not obscured by the 
		//sliding drawer handle.
		if(allSongs.size() >= 6 | allSongs == null){
			//Add blank entry to end of list
			SongFile blank = new SongFile();
			blank.setImageID(R.drawable.black_square);
			allSongs.add(blank);
		}


		//setup ListAdapter
		SongsFragmentAdapter artistAdapter = new SongsFragmentAdapter(inflater.getContext(), R.layout.row_layout, allSongs);
		setListAdapter(artistAdapter);
		
		//Find Views that will be populated upon song selection.
		artistText = (TextView) myContext.findViewById(R.id.txtArtist);
		songText = (TextView) myContext.findViewById(R.id.txtSong);
		totalTimeText = (TextView) myContext.findViewById(R.id.txtTimeTotal);

		return rootView;
	}

	@Override
	public void onAttach(Activity activity) {
		//Get context and Messenger to service from the main activity.
		myContext = (FragmentActivity) activity;
		mMessenger = MusicChooser.getMessenger();
		super.onAttach(activity);
	}

	@SuppressLint("DefaultLocale")
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {

		super.onListItemClick(l, v, position, id);

		if(position < allSongs.size()){

			//Get selected song file
			SongFile selectedSong = allSongs.get(position);
			
			//Create a network command to be sent to server.
			NetworkCommand mNetworkCommand = new NetworkCommand();
			mNetworkCommand.setCommandType(NetworkCommand.PLAY_NOW);
			mNetworkCommand.setExtra1(selectedSong.getGUID()+"");

			//Set Views based on search type
			if(searchType == Constants.SearchTypes.SONGS_ALL){

				artistText.setText(getResources().getString(R.string.lbl_artist) + " " + selectedSong.getArtist());
				songText.setText(getResources().getString(R.string.lbl_song) + " " + selectedSong.getTitle());
				mNetworkCommand.setExtra2(NetworkCommand.SELECTED_FROM_SONGS);

			}else if(searchType == Constants.SearchTypes.SONGS_BY_ALBUM){

				artistText.setText(getResources().getString(R.string.lbl_artist) + " " + artistFromArgs);
				songText.setText(getResources().getString(R.string.lbl_song) + " " + selectedSong.getTitle());
				mNetworkCommand.setExtra2(NetworkCommand.SELECTED_FROM_ALBUM);

			}else if(searchType == Constants.SearchTypes.SONGS_BY_ARTIST)
				mNetworkCommand.setExtra2(NetworkCommand.SELECTED_FROM_ARTIST);

			//Convert Song length from nano seconds to minutes and seconds
			//and display the result in the totalTimeText TextView.
			String rawTime = selectedSong.getLength();
			long time = Long.parseLong(rawTime);

			long seconds = time / 1000000000;
			int minutes = (int) (seconds / 60);
			seconds = seconds - (minutes * 60);
			String timeString = (minutes + ":" + seconds);

			totalTimeText.setText(timeString);


			//Create and send message to service that a song was selected
			Message msg = Message.obtain();
			msg.what = Constants.ServiceMessages.SONG_SELECTED;
			msg.obj = mNetworkCommand;

			try {
				mMessenger.send(msg);
			} catch (RemoteException e) {
				e.printStackTrace();
			}//end catch block
			
		}//end if test
		
	}//end method
	
}
