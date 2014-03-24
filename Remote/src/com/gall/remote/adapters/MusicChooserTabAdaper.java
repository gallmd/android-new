package com.gall.remote.adapters;

import android.app.Fragment;
import android.os.Bundle;

import com.gall.remote.Constants;
import com.gall.remote.fragments.AlbumsFragment;
import com.gall.remote.fragments.ArtistsFragment;
import com.gall.remote.fragments.SongsFragment;

/**
 * TabAdapter for MusicChooser ViewPager
 * @author Matt Gall
 *
 */
public class MusicChooserTabAdaper extends android.support.v13.app.FragmentPagerAdapter {
	
	private Bundle bundle;

	public MusicChooserTabAdaper(android.app.FragmentManager fragmentManager) {
		super(fragmentManager);
	}
	
	@Override
	public Fragment getItem(int index){

		
		switch(index){
		
		case 0: //Artists Fragment
			
			bundle = new Bundle();
			bundle.putString(Constants.Keys.SEARCH_TYPE, Constants.SearchTypes.ARTISTS_ALL);
			ArtistsFragment af = new ArtistsFragment();
			af.setArguments(bundle);
			return  af;
			
		case 1: //Albums Fragment
			
			bundle = new Bundle();
			bundle.putString(Constants.Keys.SEARCH_TYPE, Constants.SearchTypes.ALBUMS_ALL);
			AlbumsFragment abf = new AlbumsFragment();
			abf.setArguments(bundle);
			return abf;
			
		case 2: //Songs Fragment
			
			bundle = new Bundle();
			bundle.putString(Constants.Keys.SEARCH_TYPE, Constants.SearchTypes.SONGS_ALL);
			SongsFragment sf = new SongsFragment();
			sf.setArguments(bundle);
			return sf;
		
		}
		
		return null;
	}

	@Override
	public int getCount() {
		return 3;
	}

}
