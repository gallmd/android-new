package com.gall.remote.adapters;

import com.gall.remote.fragments.AlbumsFragment;
import com.gall.remote.fragments.ArtistsFragment;
import com.gall.remote.fragments.SongsFragment;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * TabAdapter for MusicChooser ViewPager
 * @author Matt Gall
 *
 */
public class MusicChooserTabAdaper extends FragmentPagerAdapter {
	
	public MusicChooserTabAdaper(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public android.support.v4.app.Fragment getItem(int index){
		switch(index){
		case 0:
			return new ArtistsFragment();
		case 1:
			return new AlbumsFragment();
		case 2:
			return new SongsFragment();
		
		}
		return null;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 3;
	}

}
