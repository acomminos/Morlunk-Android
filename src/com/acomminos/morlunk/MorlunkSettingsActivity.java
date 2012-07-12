package com.acomminos.morlunk;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.v4.app.NavUtils;

import com.acomminos.morlunk.account.minecraft.MinecraftOptionListActivity;
import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.actionbarsherlock.view.MenuItem;

public class MorlunkSettingsActivity extends SherlockPreferenceActivity {
	
	// TODO port to compat lib
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO make use of support library instead
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_preferences);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getFragmentManager().beginTransaction()
			.replace(R.id.container, new MorlunkSettingsFragment())
			.commit();
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            NavUtils.navigateUpTo(this, new Intent(this, MinecraftOptionListActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
	
	public static class MorlunkSettingsFragment extends PreferenceFragment {
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.preferences);
		}
	}

}
