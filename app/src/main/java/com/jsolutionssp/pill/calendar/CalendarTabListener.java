package com.jsolutionssp.pill.calendar;

import android.app.ActionBar;
import android.app.ActionBar.TabListener;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.util.Log;

import com.jsolutionssp.pill.R;


public class CalendarTabListener<T extends Fragment> implements TabListener {

	private Fragment mFragment;
	private final Activity mActivity;
	private final String mTag;
	private final Class<T> mClass;

	/** Constructor used each time a new tab is created.
	 * @param contraceptivePill  The host Activity, used to instantiate the fragment
	 * @param tag  The identifier tag for the fragment
	 * @param clz  The fragment's Class, used to instantiate the fragment
	 */
	public CalendarTabListener(Activity contraceptivePill, String tag, Class<T> clz) {
		mActivity = contraceptivePill;
		mTag = tag;
		mClass = clz;
	}

	/* The following are each of the ActionBar.TabListener callbacks */
	@Override
	public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ignoredFt) {
		FragmentManager fragMgr = mActivity.getFragmentManager();
		FragmentTransaction ft = fragMgr.beginTransaction();

		// Check if the fragment is already initialized
		if (mFragment == null) {
			// If not, instantiate and add it to the activity
			mFragment = Fragment.instantiate(mActivity, mClass.getName());

			ft.add(R.id.mainLayout, mFragment, mTag);
		    ft.commit();
		} else {
			// If it exists, simply attach it in order to show it
			ft.attach(mFragment);
		    ft.commit();
		}
	}
	@Override
	public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
		if (ft == null) {
			FragmentManager fragMgr = mActivity.getFragmentManager();
			ft = fragMgr.beginTransaction();
		}
		if (mFragment != null) {
			// Detach the fragment, because another one is being attached
			ft.detach(mFragment);
		}
	}
	@Override
	public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
		// User selected the already selected tab. Usually do nothing.
	}
}