package com.gall.remote.fragments;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.gall.remote.MusicChooserPageChangeListener;
import com.gall.remote.R;
import com.gall.remote.R.id;
import com.gall.remote.R.layout;
import com.gall.remote.adapters.MusicChooserTabAdaper;


public class viewPagerFragment extends Fragment implements ActionBar.TabListener {

	private ViewPager pager;
	private ActionBar actionBar;
	private MusicChooserTabAdaper scAdapter;
	private String[] tabNames = {"Artists", "Albums", "Songs"};
	private FragmentActivity myContext;


	public android.view.View onCreateView(android.view.LayoutInflater inflater, android.view.ViewGroup container, android.os.Bundle savedInstanceState) {
		
		View v = inflater.inflate(R.layout.view_pager_fragment, container, false);
		
		//Set up ViewPager
		pager = (ViewPager) v.findViewById(R.id.pager);
		actionBar = getActivity().getActionBar();
		scAdapter = new MusicChooserTabAdaper(myContext.getFragmentManager());

		pager.setAdapter(scAdapter);
		actionBar.setHomeButtonEnabled(true);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS); 

		//Add Tabs
		for(String tabName : tabNames){
			actionBar.addTab(actionBar.newTab().setText(tabName).setTabListener(this));
		}
		
		//set Page Change Listener
		pager.setOnPageChangeListener(new MusicChooserPageChangeListener(actionBar));

		return v;
	}
	
	
	@Override
	public void onAttach(Activity activity) {
		
		myContext = (FragmentActivity) activity;
		super.onAttach(activity);
	}
	
	//Tab change methods
		@Override
		public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			// TODO Auto-generated method stub
			pager.setCurrentItem(tab.getPosition());
		}

		@Override
		public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
			// TODO Auto-generated method stub

		}

}
