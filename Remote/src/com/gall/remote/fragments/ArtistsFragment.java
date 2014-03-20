package com.gall.remote.fragments;

import java.util.ArrayList;
import java.util.TreeSet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.gall.remote.R;
import com.gall.remote.DAO.SongDatabaseDAO;
import com.gall.remote.DTO.SongFile;
import com.gall.remote.adapters.MusicChooserAdapter;
import com.gall.remote.adapters.RowItem;

/**
 * Fragment that displays a list of artists to choose from
 * @author Matt Gall
 *
 */
public class ArtistsFragment extends android.support.v4.app.ListFragment {
	
    private ArrayList<RowItem> rowItems;

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_list, container, false);
        
        SongDatabaseDAO artistDB = new SongDatabaseDAO(getActivity());
        TreeSet<SongFile> allArtists = artistDB.fetchAllArtists();

        
		//Create List of RowItems, currently dummy data.  Will populate list from
        //a database.  Database will be synched with server via JSON messages sent
        //over network		
        rowItems = new ArrayList<RowItem>();
        for (SongFile songFile : allArtists) {
			RowItem item = new RowItem(R.drawable.ic_launcher, songFile.getArtist());
			rowItems.add(item);
		}
        

		RowItem blank = new RowItem(R.drawable.black_square, "");
		rowItems.add(blank);
		
		//setup ListAdapter
		MusicChooserAdapter artistAdapter = new MusicChooserAdapter(inflater.getContext(), R.layout.row_layout, rowItems);
		setListAdapter(artistAdapter);
		
        return rootView;
    }
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		String toastString = (rowItems.get(position).getTitle());
		Toast toast = Toast.makeText(getActivity(), toastString, Toast.LENGTH_SHORT);
		toast.show();
		
	}
	
	

}
