package com.acomminos.morlunk.account.minecraft;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

import com.acomminos.morlunk.R;
import com.acomminos.morlunk.dummy.MinecraftContent;
import com.acomminos.morlunk.dummy.MinecraftContent.MinecraftOption;

public class MinecraftOptionDetailActivity extends FragmentActivity {

    private MinecraftOption mItem;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minecraftoption_detail);
        
        getActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
        	@SuppressWarnings("unchecked")
			Class<? extends Fragment> fragmentClass = (Class<? extends Fragment>) getIntent().getExtras().get("fragment_class");
        	Fragment fragment = null;
			try {
				fragment = fragmentClass.newInstance();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			mItem = MinecraftContent.ITEM_MAP.get(getIntent().getExtras().getString("item_id"));
			setTitle(mItem.content);
			
			Bundle arguments = getIntent().getExtras().getBundle("arguments");
			fragment.setArguments(arguments);
			
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.minecraftoption_detail_container, fragment)
                    .commit();
        } else {
        	mItem = MinecraftContent.ITEM_MAP.get(savedInstanceState.getString("item_id"));
			setTitle(mItem.content);
		}
    }
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
    	super.onSaveInstanceState(outState);
    	outState.putString("item_id", mItem.id);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            NavUtils.navigateUpTo(this, new Intent(this, MinecraftOptionListActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
