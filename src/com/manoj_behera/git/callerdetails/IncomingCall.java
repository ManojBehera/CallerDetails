package com.manoj_behera.git.callerdetails;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

public class IncomingCall extends BroadcastReceiver {

	Context context = null;

	public void onReceive(Context context, Intent intent) {

		this.context = context;

		try {
			// TELEPHONY MANAGER class object to register one listner
			TelephonyManager tmgr = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);

			// Create Listner
			MyPhoneStateListener PhoneListener = new MyPhoneStateListener();

			// Register listener for LISTEN_CALL_STATE
			tmgr.listen(PhoneListener, PhoneStateListener.LISTEN_CALL_STATE);

		} catch (Exception e) {
			Log.e("Phone Receive Error", " " + e);
		}

	}

	private class MyPhoneStateListener extends PhoneStateListener {
		@SuppressLint("ShowToast")
		public void onCallStateChanged(int state, String incomingNumber) {

			final SharedPreferences preference = PreferenceManager
					.getDefaultSharedPreferences(context);

			// see if app is enabled and if so start the sms monitor service
			boolean isEnabled = preference.getBoolean("prefEnabled", false);

			// do nothing if app isn't enabled
			if (!isEnabled) {
				return;
			}

			Log.d("MyPhoneListener", state + "   incoming no:" + incomingNumber);

			if (state == 1) {
				try {
					String info = "";
					String nl = "\n";

					String Nickname = Functions.getNickname(context,
							incomingNumber);
					String Email = Functions.getEmail(context, incomingNumber);
					String Address = Functions.getAddress(context,
							incomingNumber);
					// String IM = Functions.getIM(context, incomingNumber);
					String Note = Functions.getNote(context, incomingNumber);
					String Organization = Functions.getOrganization(context,
							incomingNumber);

					if (!Nickname.isEmpty()) {
						info += "Nickname: " + Nickname + nl;
					}

					if (!Organization.isEmpty()) {
						info += "Organization: " + Organization + nl;
					}

					if (!Note.isEmpty()) {
						info += "Note: " + Note + nl;
					}

					if (!Email.isEmpty()) {
						info += "Email: " + Email + nl;
					}

					// if (!IM.isEmpty()) {
					// info += "IM: " + IM + nl;
					// }

					if (!Address.isEmpty()) {
						info += "Address: " + Address + nl;
					}

					if (!info.isEmpty()) {
						Toast toast = Toast.makeText(context, info,
								Toast.LENGTH_SHORT);

						String position = preference.getString("prefposition",
								"Center");

						String duration = preference.getString("prefduration",
								"10");

						if (position.equals("Top")) {
							toast.setGravity(Gravity.TOP
									| Gravity.CENTER_HORIZONTAL, 0, 0);
						} else if (position.equals("Center")) {
							toast.setGravity(Gravity.CENTER, 0, 0);
						} else if (position.equals("Bottom")) {
							toast.setGravity(Gravity.BOTTOM
									| Gravity.CENTER_HORIZONTAL, 0, 0);
						}

						ToastExpander.showFor(toast,
								(Integer.parseInt(duration) * 1000));

					}
				} catch (Exception e) {
					Toast.makeText(context, "Error " + e.toString(),
							Toast.LENGTH_LONG).show();
				}

			} else {
				// close activity here...
			}
		}
	}

}
