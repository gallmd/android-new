package com.gall.remote.network;

import java.net.InetAddress;

import android.os.AsyncTask;
/**
 * Not used at the moment
 * @author Matt Gall
 *
 */
public class NetworkConnectTask extends AsyncTask<AddressInfo, Integer, NetworkOperations>{
	
	@Override
	protected NetworkOperations doInBackground(AddressInfo... addressInfos) {
		AddressInfo addressInfo = addressInfos[0];
		InetAddress ia = addressInfo.getIP();
		int portNumber = addressInfo.getPort();
		
		NetworkOperations connectnetop = new NetworkOperations();
		connectnetop.connectToServer(ia, portNumber);
		return connectnetop;
	}
}
