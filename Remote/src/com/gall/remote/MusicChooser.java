package com.gall.remote;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.SlidingDrawer;
import android.widget.Toast;

import com.gall.remote.service.NetworkManager;
import com.gall.remote.service.RemoteService;

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
@SuppressWarnings("deprecation")
public class MusicChooser extends FragmentActivity{

	private Intent mServiceIntent;
	private SlidingDrawer drawer;
	
	private static Messenger mService;
	private boolean mBound;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//Initialization
		super.onCreate(savedInstanceState);
		setContentView(R.layout.music_chooser_layout);
		drawer = (SlidingDrawer) findViewById(R.id.drwrPlayControls);

		//Add Fragment
		Fragment viewPagerFragment = new viewPagerFragment();
		FragmentTransaction transaction = getFragmentManager().beginTransaction();

		transaction.add(R.id.fragmentContainer, viewPagerFragment);
		transaction.addToBackStack("Remote");
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
		
		String defaultPort = getResources().getString(R.string.default_port_number);
		String prefPort = sharedPrefs.getString("pref_port_number", defaultPort);

		//Start Network Service
		mServiceIntent = new Intent(getApplicationContext(), RemoteService.class);
		mServiceIntent.putExtra(Constants.Keys.IP_ADDRESS, prefIP);
		mServiceIntent.putExtra(Constants.Keys.PORT, prefPort);
		startService(mServiceIntent);
		
		bindService(mServiceIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
	}
	
	@Override
	protected void onDestroy() {
		//Send message to service that application is begin destroyed and that it should start clean-up.
		Message msg = Message.obtain(null,Constants.ServiceMessages.APPLICATION_EXITING, 0, 0);
		try {
			
			mService.send(msg);
//			unbindService(mServiceConnection);
			Thread.sleep(500);

		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		super.onDestroy();
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_chooser, menu);

		return true;
	}


	//Handle button presses
	public void onClick(View button){
		if(!mBound) return;
		
		int msgContent = 0;
		
		switch(button.getId()){

		case R.id.btnNext:
			msgContent = Constants.ServiceMessages.NEXT_PRESSED;
			break;

		case R.id.btnPlay:
			msgContent = Constants.ServiceMessages.PLAY_PRESSED;
			break;

		case R.id.btnPrev:
			msgContent = Constants.ServiceMessages.PREVIOUS_PRESSED;
			break;

		}
		
		//Send message to service
		Message msg = Message.obtain(null,msgContent, 0, 0);
		try {
			mService.send(msg);
		} catch (RemoteException e) {
			e.printStackTrace();
		}

	}
	
	//Static method that can be called by fragments to obtain the mService Messenger
	public static Messenger getMessenger(){
		return mService;
	}

	@Override
	public void onBackPressed(){

		android.app.FragmentManager fm = getFragmentManager();
		
		//TODO Questionable navigation technique, try to do it without overriding system behavior
		if(!drawer.isOpened()){
			//Only pop back stack if drawer is closed
			int backStackCount = fm.getBackStackEntryCount();

			if(backStackCount > 1){
				//pop back stack if we have moved past the home fragment tabs
				String entryName = fm.getBackStackEntryAt(backStackCount-1).getName();
				fm.popBackStack();
				
				if(backStackCount > 2){
					//Set action bar to back stack name
					getActionBar().setTitle(entryName);
				}else{
					//if we popped back to the home fragment tabs, set the action bar title back to "Remote"
					getActionBar().setTitle("Remote");
				}
			}
		}else{
			//if the drawer is open, close it
			drawer.close();
		}

	}


	//handle menu presses
	@SuppressLint("SdCardPath")
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {

		switch(item.getItemId()){
		
		//Launch settings menu
		case R.id.action_settings:
//			Intent settingsIntent = new Intent(this, UserPreferencesActivity.class);
//			startActivity(settingsIntent);
			
			//Dumps database to sd card so it can be copied to a computer and viewed.
			File f=new File("/data/data/com.gall.remote/databases/remotesongdatabase.db");
			FileInputStream fis=null;
			FileOutputStream fos=null;

			try
			{
			  fis=new FileInputStream(f);
			  File root = Environment.getExternalStorageDirectory();
			  
			  File dir = new File(root.getAbsolutePath() + "/database");
			  dir.mkdirs();
			  
			  File newFile = new File(dir, "database_dump.db");
			  
			  
			  fos=new FileOutputStream(newFile);
			  while(true)
			  {
			    int i=fis.read();
			    if(i!=-1)
			    {fos.write(i);}
			    else
			    {break;}
			  }
			  fos.flush();
			  Toast.makeText(this, "DB dump OK", Toast.LENGTH_LONG).show();
			}
			catch(Exception e)
			{
			  e.printStackTrace();
			  Toast.makeText(this, "DB dump ERROR", Toast.LENGTH_LONG).show();
			}
			finally
			{
			  try
			  {
			    fos.close();
			    fis.close();
			  }
			  catch(IOException ioe)
			  {}
			}
			break;
		}

		return super.onMenuItemSelected(featureId, item);
	}
	
	
	
	private ServiceConnection mServiceConnection = new ServiceConnection(){

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {

			mService = new Messenger(service);
			mBound = true;
			
			//Provide Service with Handler to respond back to
			Message replyMessage = Message.obtain();
			replyMessage.replyTo = fromServiceMessenger;
			replyMessage.what = Constants.ServiceMessages.SET_REPLY_MESSENGER;
			
			try {
				mService.send(replyMessage);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {

			mService = null;
			mBound = false;
		}
		
	};
	
	//Create messenger to send to service
	Messenger fromServiceMessenger = new Messenger(new FromServiceHandler());
	
	//Create Handler for messages from service
	@SuppressLint("HandlerLeak")
	class FromServiceHandler extends Handler{
		
		@Override
		public void handleMessage(Message msg) {
			
			switch(msg.what){
			
			case NetworkManager.CONNECTED:
				Toast.makeText(getApplicationContext(), "Connected", Toast.LENGTH_SHORT).show();
				break;
				
			case NetworkManager.CONNECTION_LOST:
				Toast.makeText(getApplicationContext(), "Lost Connection", Toast.LENGTH_SHORT).show();
				break;
				
			case NetworkManager.LIBRARY_DELETED:
				//TODO add prompt to confirm library deletion, or initiate command on the client side
				Toast.makeText(getApplicationContext(), "Library Deleted!", Toast.LENGTH_SHORT).show();
				break;
				
			case NetworkManager.LIBRARY_UPDATE_IN_PROGRESS:
				Toast.makeText(getApplicationContext(), "Library Update In Progress", Toast.LENGTH_SHORT).show();
				break;
				
			case NetworkManager.LIBRARY_UPDATE_COMPLETED:
				Toast.makeText(getApplicationContext(), "Library Update Completed", Toast.LENGTH_SHORT).show();
				break;
				
			}
			super.handleMessage(msg);
		}
		
	}

}