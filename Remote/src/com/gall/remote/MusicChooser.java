package com.gall.remote;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.SocketException;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Toast;

import com.gall.remote.DAO.SongDatabaseDAO;
import com.gall.remote.DTO.SongFile;
import com.gall.remote.adapters.MusicChooserTabAdaper;
import com.gall.remote.network.AddressInfo;
import com.gall.remote.network.NetworkOperations;
import com.gall.remote.network.RemoteService;

/**
 * Main Activity:
 * Contains a ViewPager that displays various methods for navigating
 * library (by artist, by album, by song, by playlist), as well as
 * a SlidingDrawer.  The DrawerHandle displays currently playing Artist
 * and Song.  The DrawerContent exposes more detailed play information as
 * well as player controls.
 * @author Matt Gall
 *
 */
public class MusicChooser extends FragmentActivity implements ActionBar.TabListener{

	private NetworkOperations netop;
	private ViewPager pager;
	private ActionBar actionBar;
	private MusicChooserTabAdaper scAdapter;
	private String[] tabNames = {"Artists", "Albums", "Songs", "Play Lists"};
	private NetworkConnectTask nct;
	private AddressInfo ai;
	private Intent mServiceIntent;

	/**
	 * **********************************************************************************************
	 * LifeCycle Mangagement
	 * **********************************************************************************************
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//Initialization
		super.onCreate(savedInstanceState);
		setContentView(R.layout.music_chooser_layout);
		
		//Set up ViewPager
		pager = (ViewPager) findViewById(R.id.pager);
		actionBar = getActionBar();
		scAdapter = new MusicChooserTabAdaper(getSupportFragmentManager());

		pager.setAdapter(scAdapter);
		actionBar.setHomeButtonEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS); 

		//Add Tabs
		for(String tabName : tabNames){
			actionBar.addTab(actionBar.newTab().setText(tabName).setTabListener(this));
		}

		pager.setOnPageChangeListener(new MusicChooserPageChangeListener(actionBar));

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
		
		connectToServer();

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}


	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}



	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		nct.cancel(true);

	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_chooser, menu);

		return true;
	}

	/**
	 * **********************************************************************************************
	 * End LifeCycle Mangagement
	 * **********************************************************************************************
	 */

	public void connectToServer(){
//		ai = new AddressInfo();
//		ai.setIP(getResources().getString(R.string.default_ip_address));
//		ai.setPortNumber(Integer.parseInt("3490"));
//
//		netop = new NetworkOperations();
//		nct = new NetworkConnectTask(getApplicationContext());
//		nct.execute(ai);
		
		//Start Network Service
		mServiceIntent = new Intent(getApplicationContext(), RemoteService.class);
		mServiceIntent.putExtra("ip", getResources().getString(R.string.default_ip_address));
		mServiceIntent.putExtra("port", getResources().getString(R.string.default_port_number));
		getApplicationContext().startService(mServiceIntent);
		

	}


	//Handle button presses
	public void onClick(View button){
		if(netop.isConnected()){
			switch(button.getId()){

			case R.id.btnNext:
				//Send next track command
				netop.sendMessage("sn");
				break;

			case R.id.btnPlay:
				//Send Play command
				netop.sendMessage("sp");
				break;

			case R.id.btnPrev:
				//Send Previous command
				netop.sendMessage("spr");
				break;

			}

		}else{

			connectToServer();

		}

	}

	//handle menu presses
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// TODO Auto-generated method stub
		
		switch(item.getItemId()){
		case R.id.action_settings:
			Intent settingsIntent = new Intent(this, UserPreferencesActivity.class);
			startActivity(settingsIntent);
		}
		return super.onMenuItemSelected(featureId, item);
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

	//Async Network Tasks
	class NetworkConnectTask extends AsyncTask<AddressInfo, NetworkOperations, Void>{

		//		private NetworkRecvTask nrt;
		private Context mContextConnect;
		private SongDatabaseDAO db;
		private ParseJSON parse;

		//Flags
		private boolean bInCommand = false;
		private boolean bUpdateFlag = false;

		NetworkConnectTask(Context c){
			this.mContextConnect = c;
		}

		@Override
		protected Void doInBackground(AddressInfo... addressInfos) {
			Thread.currentThread().setName("Connect Async Task");

			//extract passed address info
			AddressInfo addressInfo = addressInfos[0];
			InetAddress ia = addressInfo.getIP();
			int portNumber = addressInfo.getPort();

			//create new NetworkOperations and run connect method
			NetworkOperations connectnetop = new NetworkOperations();
			try {
				connectnetop.connectToServer(ia, portNumber);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			publishProgress(connectnetop);

			//infinite loop
			while(connectnetop.isConnected()){

				String message;
				//recv message
				try {

					message = connectnetop.recvMessage();

					//handle message
					//Check if currently in the middle of a multi-message command
					if(!bInCommand){
						//if not in a command, figure out which command to start

						//update library command
						if(message.equalsIgnoreCase("update library")){
							bUpdateFlag = true;
							bInCommand = true;
							parse = new ParseJSON();
							db = new SongDatabaseDAO(mContextConnect);
						}
						
						if(message.equalsIgnoreCase("delete library")){
							db = new SongDatabaseDAO(mContextConnect);
							db.dropTable();
						}

						//Add new commands here

					}else if (bInCommand){
						//if we got here, we are in the middle of a command

						//update library command
						if(bUpdateFlag){
							//check for end of command
							if(message.equalsIgnoreCase("end of update")){

								bUpdateFlag = false;
								bInCommand = false;

							}else{

								//carry out command
								SongFile sf = new SongFile();
								sf = parse.parse(message);

								if(sf!=null){

									try{
										db.insertSongs(sf);
									}catch(SQLException e){
										e.printStackTrace();
									}//end catch
								}
							}
						}//end update command

						//Add new commands here
					}

				} catch (SocketException e) {

					NetworkConnectTask reconnect = new NetworkConnectTask(mContextConnect);
					reconnect.execute(ai);
					e.printStackTrace();

				} 
			}
			return null;
		}

		@Override
		protected void onProgressUpdate(NetworkOperations... connectnetop) {
			netop = connectnetop[0];
			Toast nettoast = Toast.makeText(getApplicationContext(), "Connected", Toast.LENGTH_SHORT);
			nettoast.show();
		}


	}

}


