package com.acomminos.morlunk.account.minecraft;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

import com.acomminos.morlunk.R;

public class MinecraftOptionListActivity extends FragmentActivity
        implements MinecraftOptionListFragment.Callbacks {

    private boolean mTwoPane;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minecraftoption_list);
        getActionBar().setDisplayHomeAsUpEnabled(true);

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
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(String id) {
        if (mTwoPane) {
            Bundle arguments = new Bundle();
            arguments.putString(MinecraftOptionDetailFragment.ARG_ITEM_ID, id);
            MinecraftOptionDetailFragment fragment = new MinecraftOptionDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.minecraftoption_detail_container, fragment)
                    .commit();

        } else {
            Intent detailIntent = new Intent(this, MinecraftOptionDetailActivity.class);
            detailIntent.putExtra(MinecraftOptionDetailFragment.ARG_ITEM_ID, id);
            startActivity(detailIntent);
        }
    }
}
