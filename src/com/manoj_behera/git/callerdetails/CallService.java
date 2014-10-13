package com.manoj_behera.git.callerdetails;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

public class CallService extends Service {

	IncomingCall incomingCall;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		try {
			Log.i("main", "Call Service started");
			IntentFilter intentFilter = new IntentFilter();
			registerReceiver(new IncomingCall(), intentFilter);
		} catch (Exception e) {

		}

		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		try {
			Log.i("main", "call service destroyed");
			this.unregisterReceiver(incomingCall);
		} catch (Exception e) {
		}

		super.onDestroy();
	}

}
