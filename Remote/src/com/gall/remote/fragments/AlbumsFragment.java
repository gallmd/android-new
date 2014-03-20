package com.gall.remote.fragments;

import java.util.ArrayList;
import java.util.TreeSet;

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
 * Fragment to display a list of albums to choose from
 * @author Matt Gall
 *
 */
public class AlbumsFragment extends android.support.v4.app.ListFragment {
	
    private ArrayList<RowItem> rowItems;

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_list, container, false);
        SongDatabaseDAO albumsDB = new SongDatabaseDAO(getActivity());
        
        TreeSet<SongFile> allAlbums = albumsDB.fetchAllAlbums();
        
		rowItems = new ArrayList<RowItem>();
        for (SongFile songFile : allAlbums) {
			RowItem item = new RowItem(R.drawable.ic_launcher, songFile.getAlbum());
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
