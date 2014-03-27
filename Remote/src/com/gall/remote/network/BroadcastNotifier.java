package com.gall.remote.network;

import android.content.Context;
import android.support.v4.content.LocalBroadcastManager;

public class BroadcastNotifier {
	
	private LocalBroadcastManager mBroadcaster;
	
	public BroadcastNotifier(Context context){
		mBroadcaster = LocalBroadcastManager.getInstance(context);
	}

}
