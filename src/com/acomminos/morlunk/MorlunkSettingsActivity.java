package com.acomminos.morlunk;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceFragment;

public class MorlunkSettingsActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO make use of support library instead
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_preferences);
		getFragmentManager().beginTransaction()
			.replace(R.id.container, new MorlunkSettingsFragment())
			.commit();
	}
	
	public static class MorlunkSettingsFragment extends PreferenceFragment {
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.preferences);
		}
	}

}
