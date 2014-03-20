package com.gall.remote.fragments;

import java.util.ArrayList;
import java.util.Collections;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gall.remote.R;
import com.gall.remote.DAO.SongDatabaseDAO;
import com.gall.remote.DTO.SongFile;
import com.gall.remote.adapters.MusicChooserAdapter;
import com.gall.remote.adapters.RowItem;

/**
 * Fragment that displays a list of songs to choose from
 * @author Matt Gall
 *
 */
public class SongsFragment extends android.support.v4.app.ListFragment {
	
    private ArrayList<RowItem> rowItems;

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_list, container, false);
        
        SongDatabaseDAO db = new SongDatabaseDAO(getActivity());
        ArrayList<SongFile> allSongs = db.fetchAllSongs();
        Collections.sort(allSongs, SongFile.sortByName);
        
		//Create List of RowItems, currently dummy data.  Will populate list from
        //a database.  Database will be synched with server via JSON messages sent
        //over network
        rowItems = new ArrayList<RowItem>();
		for (int i = 0; i < allSongs.size(); i++) {
			RowItem item = new RowItem(R.drawable.ic_launcher, allSongs.get(i).getName());
			rowItems.add(item);
		}
		
		RowItem blank = new RowItem(R.drawable.black_square, "");
		rowItems.add(blank);
		
		//setup ListAdapter
		MusicChooserAdapter artistAdapter = new MusicChooserAdapter(inflater.getContext(), R.layout.row_layout, rowItems);
		setListAdapter(artistAdapter);
         
        return rootView;
    }
}
