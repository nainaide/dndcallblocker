/*
 * DND Call Blocker
 * A simple Android application that automatically block unwanted incoming calls.
 * Copyright (c) 2010 Zoltan Meleg, android+dndcb@zoliweb.hu
 * 
 * This file is part of DND Call Blocker.
 * 
 * DND Call Blocker is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * DND Call Blocker is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with DND Call Blocker.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package hu.zoliweb.android.dndcallblocker;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceGroup;

public class DNDCallBlockerPreferenceActivity extends PreferenceActivity
		implements OnSharedPreferenceChangeListener {

	private DNDCallBlockerNotifier mNotifier;
	private SharedPreferences sharedPreferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// status bar notification
		mNotifier = new DNDCallBlockerNotifier(this);
		mNotifier.updateNotification();
		// get preferences
		sharedPreferences = getPreferenceManager().getSharedPreferences();
		sharedPreferences.registerOnSharedPreferenceChangeListener(this);
		addPreferencesFromResource(R.xml.preferences);

		// init summary of listpreferences
		for (int i = 0; i < getPreferenceScreen().getPreferenceCount(); i++) {
			Preference pref = getPreferenceScreen().getPreference(i);
			if (pref instanceof PreferenceGroup) {
				for (int j = 0; j < ((PreferenceGroup) pref)
						.getPreferenceCount(); j++) {
					initListPrefSummary(((PreferenceGroup) pref)
							.getPreference(j));
				}
			} else {
				initListPrefSummary(pref);
			}
		}
	}

	@Override
	public void setContentView(int layoutResID) {
		super.setContentView(R.layout.preferences);
	}

	@Override
	protected void onDestroy() {
		getPreferenceManager().getSharedPreferences()
				.unregisterOnSharedPreferenceChangeListener(this);
		super.onDestroy();
	}

	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		if (key.equals("enabled") || key.equals("stat_notify")) {
			mNotifier.updateNotification();
		} else {
			Preference pref = findPreference(key);
			initListPrefSummary(pref);
		}
	}

	private void initListPrefSummary(Preference pref) {
		if (pref instanceof ListPreference) {
			ListPreference listPref = (ListPreference) pref;
			pref.setSummary(listPref.getEntry());
		}
	}
}