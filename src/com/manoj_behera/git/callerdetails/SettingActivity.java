package com.manoj_behera.git.callerdetails;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class SettingActivity extends PreferenceActivity implements
		OnSharedPreferenceChangeListener {

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		if (item.getItemId() == R.id.action_about) {
			Functions.showAboutDialog(this);
			return true;
		}

		return super.onMenuItemSelected(featureId, item);
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.addPreferencesFromResource(R.xml.preferences);
		Preferences.initSummaries(this, this.getPreferenceScreen());

		this.getPreferenceScreen().getSharedPreferences()
				.registerOnSharedPreferenceChangeListener(this);

	}

	@SuppressWarnings("deprecation")
	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {

		try {
			Preference pref = findPreference(key);
			Preferences.setSummary(this, pref);
		} catch (Exception e) {
			Log.d("main",
					" SettingActivity onSharedPreferenceChanged: "
							+ e.getMessage());
		}
	}

}
