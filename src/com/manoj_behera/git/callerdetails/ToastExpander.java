package com.manoj_behera.git.callerdetails;

import android.util.Log;
import android.widget.Toast;

/*
 * how to use:
 * 
 *  Toast aToast = Toast.makeText(this, "Hello World", Toast.LENGTH_SHORT);
 *	ToastExpander.showFor(aToast, 5000); // 5 seconds
 * 
 * */

public class ToastExpander {

	public static final String TAG = "ToastExpander";

	public static void showFor(final Toast aToast,
			final long durationInMilliseconds) {

		aToast.setDuration(Toast.LENGTH_SHORT);

		try {

			Thread t = new Thread() {
				long timeElapsed = 0l;

				public void run() {
					try {
						while (timeElapsed <= durationInMilliseconds) {
							long start = System.currentTimeMillis();
							aToast.show();
							sleep(1750);
							timeElapsed += System.currentTimeMillis() - start;
						}
					} catch (InterruptedException e) {
						Log.e(TAG, e.toString());
					}
				}
			};
			t.start();
		} catch (Exception e) {
			Log.e(TAG, e.toString());
		}
	}
}
