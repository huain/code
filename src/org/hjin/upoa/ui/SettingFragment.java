package org.hjin.upoa.ui;

import org.hjin.upoa.R;
import org.hjin.upoa.constants.AppConstants;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.util.Log;

public class SettingFragment extends PreferenceFragment {
	
	private final String TAG = "SettingFragment";
	
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.setting);
	}



	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		final ListPreference typePreference = (ListPreference)findPreference("setting_item_typedefault");
		final ListPreference positionPreference = (ListPreference)findPreference("setting_item_positiondefault");
//		final SwitchPreference nonepicPreference = (SwitchPreference)findPreference("setting_item_nonepic");
		
		SharedPreferences setting = getPreferenceManager().getSharedPreferences();
		
		typePreference.setSummary(setting.getString("setting_item_typedefault", getResources().getString(R.string.setting_item_typedefault_desc)));
		positionPreference.setSummary(setting.getString("setting_item_positiondefault", getResources().getString(R.string.setting_item_positiondefault_desc)));
		setting.registerOnSharedPreferenceChangeListener(new OnSharedPreferenceChangeListener() {
			@Override
			public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
					String key) {
				Log.d(TAG, "===key:"+key);
				if(key.equals("setting_item_typedefault")){
					typePreference.setSummary(sharedPreferences.getString(key, getResources().getString(R.string.setting_item_typedefault_desc)));
				}else if(key.equals("setting_item_positiondefault")){
					positionPreference.setSummary(sharedPreferences.getString(key, getResources().getString(R.string.setting_item_positiondefault_desc)));
				}
			}
		});
		
		
		
		
	}
	
	
	
	
	
	
	

}
