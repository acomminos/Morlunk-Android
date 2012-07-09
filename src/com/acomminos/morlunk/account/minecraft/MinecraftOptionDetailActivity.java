package com.acomminos.morlunk.account.minecraft;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

import com.acomminos.morlunk.R;

public class MinecraftOptionDetailActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minecraftoption_detail);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            arguments.putString(MinecraftOptionDetailFragment.ARG_ITEM_ID,
                    getIntent().getStringExtra(MinecraftOptionDetailFragment.ARG_ITEM_ID));
            MinecraftOptionDetailFragment fragment = new MinecraftOptionDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.minecraftoption_detail_container, fragment)
                    .commit();
        }
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
