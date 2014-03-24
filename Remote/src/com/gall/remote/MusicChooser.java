package com.gall.remote;

import java.lang.reflect.Field;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Toast;

import com.gall.remote.network.RemoteService;

/**
 * Main Activity:
 * Contains a ViewPager that displays various methods for navigating
 * library (by artist, by album, by song, by play list), as well as
 * a SlidingDrawer.  The DrawerHandle displays currently playing Artist
 * and Song.  The DrawerContent exposes more detailed play information as
 * well as player controls.
 * @author Matt Gall
 *
 */
public class MusicChooser extends FragmentActivity{

	private Intent mServiceIntent;

	/**
	 * **********************************************************************************************
	 * LifeCycle Management
	 * **********************************************************************************************
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//Initialization
		super.onCreate(savedInstanceState);
		setContentView(R.layout.music_chooser_layout);

		//Add Fragment
		Fragment viewPagerFragment = new viewPagerFragment();
		FragmentTransaction transaction = getFragmentManager().beginTransaction();

		transaction.add(R.id.fragmentContainer, viewPagerFragment);
		transaction.addToBackStack(null);
		transaction.commit();


		//Force overflow menu items to actionBar
		try {
			ViewConfiguration config = ViewConfiguration.get(this);
			Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
			if(menuKeyField != null) {
				menuKeyField.setAccessible(true);
				menuKeyField.setBoolean(config, false);
			}
		} catch (Exception ex) {
			// Ignore
		}

		SharedPreferences sharedPrefs = getPreferences(MODE_PRIVATE);
		String defaultIP = getResources().getString(R.string.default_ip_address);
		String prefIP = sharedPrefs.getString("pref_ip_address", defaultIP);
		Toast.makeText(getApplicationContext(), prefIP, Toast.LENGTH_SHORT).show();

		//Start Network Service
		mServiceIntent = new Intent(getApplicationContext(), RemoteService.class);
		mServiceIntent.putExtra("ip", getResources().getString(R.string.default_ip_address));
		mServiceIntent.putExtra("port", getResources().getString(R.string.default_port_number));
		getApplicationContext().startService(mServiceIntent);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_chooser, menu);

		return true;
	}

	/**
	 * **********************************************************************************************
	 * End LifeCycle Management
	 * **********************************************************************************************
	 */



	//Handle button presses
	public void onClick(View button){
		switch(button.getId()){

		case R.id.btnNext:
			//Send next track command
			break;

		case R.id.btnPlay:
			//Send Play command
			break;

		case R.id.btnPrev:
			//Send Previous command
			break;

		}
		

	}
	
	@Override
	public void onBackPressed(){
		
		android.app.FragmentManager fm = getFragmentManager();
		
		if(fm.getBackStackEntryCount() > 1){
			fm.popBackStack();
		}
		
	}


	//handle menu presses
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {

		switch(item.getItemId()){
		
		case R.id.action_settings:
			Intent settingsIntent = new Intent(this, UserPreferencesActivity.class);
			startActivity(settingsIntent);
			break;
		}
		
		return super.onMenuItemSelected(featureId, item);
	}

}