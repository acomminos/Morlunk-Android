package com.acomminos.morlunk;

import android.widget.ViewSwitcher;

/**
 * A class to manage activities/fragments that use ViewSwitcher to show a loading screen.
 * @author andrew
 *
 */
public interface MorlunkLoadingHandler {
	
	public void showLoadingScreen();
	public void dismissLoadingScreen();
	
	public ViewSwitcher getViewSwitcher();

}
