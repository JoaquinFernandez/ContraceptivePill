
package com.jsolutionssp.pill;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.google.android.gms.ads.MobileAds;

import com.jsolutionssp.pill.calendar.CalendarFragment;
import com.jsolutionssp.pill.calendar.CalendarTabListener;
import com.jsolutionssp.pill.gui.AboutDialog;
import com.jsolutionssp.pill.preference.PreferencesFragment;
import com.jsolutionssp.pill.preference.PreferencesTabListener;
import com.jsolutionssp.pill.preference.TransferPreferences;

public class ContraceptivePill extends Activity {

	public static String PREFS_NAME = "com.jsolutionssp.pill";

	public static final int five_week_calendar = 35;

	public static final int six_week_calendar = 42;

	public static final int months = 11; //From 0 to 11

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//Transfer old preferences if they have not been transfered yet, else run normally
		SharedPreferences oldPrefs = getSharedPreferences("com.jsolutionssp.pill", 0);
		if (oldPrefs.getInt("pillTakingDays", 0) != 0)
			TransferPreferences.TransferOldPreferences(this);

		//Get a reference of the action bar
		android.app.ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		//Set the navigation mode to tabs
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		//Create tabs and add listeners
		android.app.ActionBar.Tab tab1 = actionBar.newTab().setText(R.string.calendar_tab_name);
		android.app.ActionBar.Tab tab2 = actionBar.newTab().setText(R.string.preferences_tab_name);

		tab1.setTabListener(new CalendarTabListener<CalendarFragment>(this, "CalendarFragment", CalendarFragment.class));
		tab2.setTabListener(new PreferencesTabListener<PreferencesFragment>(this, "PreferencesFragment", PreferencesFragment.class));

		//Add tabs to action bar
		actionBar.addTab(tab1);
		actionBar.addTab(tab2);
		if (isFirstPreferenceTime())
			tab2.select();
		else if (isFirstCalendarTime()) {
			String text = getResources().getString(R.string.calendar_instructions);
			new AlertDialog.Builder(this).setTitle(getResources().getString(R.string.calendar_info)).setMessage(text).setIcon(R.drawable.ic_menu_info_details).setNeutralButton("Close", null).show();
		}

		setContentView(R.layout.main);

		MobileAds.initialize(getApplicationContext(), "ca-app-pub-8654030018694792~3578537469");
	}

	private boolean isFirstCalendarTime() {
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
		if (settings.getBoolean("first_time_used_calendar", true)) {
			PreferenceManager.getDefaultSharedPreferences(this).edit()
			.putBoolean("first_time_used_calendar", false).commit();
			return true;
		}
		return false;
	}

	/**
	 * Checks if it is the first time the application (or the new version) is running
	 * @return whether it is or not the first time
	 */
	private boolean isFirstPreferenceTime() {
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
		return settings.getBoolean("first_time_used_preference", true);
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	@Override
	public void onPause() {
		super.onPause();
		finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.MnuOpt1:
			new AboutDialog(this);
			return true;
		case R.id.MnuOpt2:
			//The dialog that is going to be shown when the user clicks on the change button
			final Dialog changePillType = new Dialog(this, R.style.NoTitleDialog);
			changePillType.setContentView(R.layout.select_pill_type);
			Button button = (Button) changePillType.findViewById(R.id.select_pill_type_button);
			button.setText(getResources().getString(R.string.button_close));
			//Dismiss button
			button.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					changePillType.dismiss();
				}
			});
			changePillType.show();
			return true;
		case R.id.MnuOpt3:
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}