package com.gall.remote;

import android.app.ActionBar;
import android.support.v4.view.ViewPager;



public class MusicChooserPageChangeListener implements ViewPager.OnPageChangeListener {
	
	private ActionBar ab;
	
	public MusicChooserPageChangeListener(ActionBar ab){
		this.ab = ab;
	}
	
	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}
	
	//new page selected
	@Override
	public void onPageSelected(int position) {
		// TODO Auto-generated method stub
		ab.setSelectedNavigationItem(position);

		
	}

}
