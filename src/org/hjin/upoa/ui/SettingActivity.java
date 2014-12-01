package org.hjin.upoa.ui;

import org.hjin.upoa.R;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.util.Log;

public class SettingActivity extends PreferenceActivity {
	
	private final String TAG = "SettingFragment";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		getActionBar().setTitle("…Ë÷√");
		addPreferencesFromResource(R.xml.setting);
		
		final ListPreference typePreference = (ListPreference)findPreference("setting_item_typedefault");
		final ListPreference positionPreference = (ListPreference)findPreference("setting_item_positiondefault");
		final CheckBoxPreference loginsaveautoPreference = (CheckBoxPreference)findPreference("setting_item_loginsaveauto");
		final CheckBoxPreference loginautoPreference = (CheckBoxPreference)findPreference("setting_item_loginauto");
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
				}else if(key.equals("setting_item_loginauto")){
					if(loginautoPreference.isChecked()){
						loginsaveautoPreference.setChecked(true);
					}
				}else if(key.equals("setting_item_loginsaveauto")){
					if(!loginsaveautoPreference.isChecked()){
						loginautoPreference.setChecked(false);
					}
				}
			}
		});
	}



}
