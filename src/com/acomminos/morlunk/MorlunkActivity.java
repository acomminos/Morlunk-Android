package com.acomminos.morlunk;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

public class MorlunkActivity extends FragmentActivity implements ActionBar.OnNavigationListener {

    private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_morlunk);
        
        // Set up the action bar.
        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

        // Set up the dropdown list navigation in the action bar.
        actionBar.setListNavigationCallbacks(
                // Specify a SpinnerAdapter to populate the dropdown list.
                new ArrayAdapter<String>(
                        actionBar.getThemedContext(),
                        android.R.layout.simple_list_item_1,
                        android.R.id.text1,
                        new String[]{
                                getString(R.string.title_blog),
                                getString(R.string.title_minecraft),
                                getString(R.string.title_android),
                        }),
                this);
        
        // TODO remove
        // Sample notification
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
        notificationBuilder.setSmallIcon(R.drawable.ic_launcher);
        notificationBuilder.setContentTitle("Minecraft Server Downtime");
        notificationBuilder.setContentText("Expected return: 5:50pm");
        Notification notification = notificationBuilder.getNotification();
        ((NotificationManager)getSystemService(NOTIFICATION_SERVICE)).notify(0, notification);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
            getActionBar().setSelectedNavigationItem(
                    savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(STATE_SELECTED_NAVIGATION_ITEM,
                getActionBar().getSelectedNavigationIndex());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_morlunk, menu);
        return true;
    }

    
    public boolean onNavigationItemSelected(int position, long id) {
    	Fragment replacementFragment = null;
    	switch(position) {
    	case 0:
    	{
    		// Load blog fragment
			MorlunkBlogFragment blogFragment = new MorlunkBlogFragment();
			replacementFragment = blogFragment;
        	break;
    	}
    	case 1:
    	{
    		// Load Minecraft page
    		MorlunkPageFragment pageFragment = new MorlunkPageFragment();
    		Bundle arguments = new Bundle();
    		arguments.putString("pageName", "minecraft");
    		pageFragment.setArguments(arguments);
    		replacementFragment = pageFragment;
        	break;
    	}
    	case 2:
    	{
    		// Load Android page
    		MorlunkPageFragment pageFragment = new MorlunkPageFragment();
    		Bundle arguments = new Bundle();
    		arguments.putString("pageName", "android");
    		pageFragment.setArguments(arguments);
    		replacementFragment = pageFragment;
        	break;
    	}
    	}
    	// Replace fragment
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.container, replacementFragment).commit();
        return true;
    }
    
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
    	switch (item.getItemId()) {
		case R.id.menu_account:
			// Start account activity
			break;

		case R.id.menu_settings:
		{
			// Start settings activity
			Intent intent = new Intent(this, MorlunkSettingsActivity.class);
			startActivity(intent);
		}
			break;
		case R.id.menu_about:
		{
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("About");
			builder.setMessage("Morlunk Co. for Android v1.0\n\n" +
					"This app was developed by and is currently maintained by Andrew Comminos.\n\n" +
					"This app's source is licensed under the LGPL. Visit http://www.github.com/Morlunk/Morlunk-Android/ to view the source.");
			builder.setNegativeButton("Close", null);
			builder.create().show();
		}
		break;
		}
    	return true;
    };
}
