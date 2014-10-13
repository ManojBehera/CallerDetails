package com.manoj_behera.git.callerdetails;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceGroup;
import android.preference.RingtonePreference;
import android.util.Log;

public class Preferences extends PreferenceActivity {

	/**
	 * Set the summaries of all preferences
	 */
	static void initSummaries(final Context context, PreferenceGroup pg) {
		try {
			for (int i = 0; i < pg.getPreferenceCount(); ++i) {
				Preference p = pg.getPreference(i);
				if (p instanceof PreferenceGroup)
					initSummaries(context, (PreferenceGroup) p); // recursion
				else
					setSummary(context, p);
			}
		} catch (Exception e) {
			Log.d("main", "initSummaries Preferences: " + e.getMessage());
		}
	}

	/**
	 * Set the summaries of the given preference
	 */
	static void setSummary(final Context context, Preference pref) {
		try {
			// react on type or key
			if (pref instanceof ListPreference) {
				ListPreference listPref = (ListPreference) pref;
				pref.setSummary(listPref.getEntry());
			}

			if (pref instanceof EditTextPreference) {
				EditTextPreference editPref = (EditTextPreference) pref;
				pref.setSummary(editPref.getText());
			}

			// if (pref instanceof ColorDialogPreference) {
			// ColorDialogPreference colorPref = (ColorDialogPreference) pref;
			// pref.setSummary(colorPref.getSharedPreferences().getString("prefcolor",
			// ""));
			// }

			if (pref instanceof RingtonePreference) {

				final RingtonePreference ringPref = (RingtonePreference) pref;
				ringPref.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
					@Override
					public boolean onPreferenceChange(Preference preference,
							Object newValue) {
						Ringtone ringtone = RingtoneManager.getRingtone(
								context, Uri.parse((String) newValue));
						ringPref.setSummary(ringtone.getTitle(context));
						return true;
					}
				});

				String ringtonePath = pref.getSharedPreferences().getString(
						pref.getKey(), "");
				Ringtone ringtone = RingtoneManager.getRingtone(context,
						Uri.parse((String) ringtonePath));
				ringPref.setSummary(ringtone.getTitle(context));
			}
		} catch (Exception e) {
			Log.d("main", "setSummary Preferences: " + e.getMessage());
		}
	}

	public static String getStringPreference(SharedPreferences preference,
			SharedPreferences user_preference, String key, String defaultValue) {
		try {
			return user_preference.getString("user_" + key,
					preference.getString(key, defaultValue));

		} catch (Exception e) {
			Log.d("main", "getStringPreference Preferences: " + e.getMessage());
		}

		return "";
	}

	public static Boolean getBooleanPreference(SharedPreferences preference,
			SharedPreferences user_preference, String key, boolean defaultValue) {
		try {
			return user_preference.getBoolean("user_" + key,
					preference.getBoolean(key, defaultValue));

		} catch (Exception e) {
			Log.d("main", "getStringPreference Preferences: " + e.getMessage());
		}

		return false;
	}

}
