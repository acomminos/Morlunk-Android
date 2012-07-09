package com.acomminos.morlunk.account.minecraft;

import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import com.acomminos.morlunk.R;
import com.acomminos.morlunk.account.MorlunkAccountManager;
import com.acomminos.morlunk.dummy.MinecraftContent;
import com.acomminos.morlunk.dummy.MinecraftContent.MinecraftItem;

public class MinecraftOptionListActivity extends FragmentActivity
        implements MinecraftOptionListFragment.Callbacks, OnNavigationListener {

    private boolean mTwoPane;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_minecraftoption_list);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        
        getActionBar().setDisplayShowTitleEnabled(false);
        getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        getActionBar().setListNavigationCallbacks(new ArrayAdapter<String>(
                getActionBar().getThemedContext(),
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                new String[]{
                        getString(R.string.title_minecraft)
                }),
        this);

        if (findViewById(R.id.minecraftoption_detail_container) != null) {
            mTwoPane = true;
            ((MinecraftOptionListFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.minecraftoption_list))
                    .setActivateOnItemClick(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.menu_logout:
            	MorlunkAccountManager.getInstance().logout();
            	finish();
            	return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(String id) {
    	
    	MinecraftItem item = MinecraftContent.ITEM_MAP.get(id);
    	
        if (mTwoPane) {
            Fragment fragment = null;
			try {
				fragment = item.fragmentClass.newInstance();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.minecraftoption_detail_container, fragment)
                    .commit();

        } else {
            Intent detailIntent = new Intent(this, MinecraftOptionDetailActivity.class);
            detailIntent.putExtra("fragment_class", item.fragmentClass);
            detailIntent.putExtra("item_id", item.id);
            startActivity(detailIntent);
        }
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_morlunk_account, menu);
        return true;
    }

	@Override
	public boolean onNavigationItemSelected(int arg0, long arg1) {
		return true;
	}
}
