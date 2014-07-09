package org.hjin.upoa.ui;

import org.hjin.upoa.R;
import org.hjin.upoa.util.Utility;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

public class NewsInfoActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar actionBar = this.getActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(true);
		Intent intent = getIntent();
		String id = intent.getStringExtra("id");
		if(!Utility.isBlank(id)){
			NewsInfoFragment newsFragment = NewsInfoFragment.newInstance(id);
	        FragmentTransaction ft = getFragmentManager().beginTransaction();
	        ft.add(R.id.baseLayout, newsFragment);
	        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
	        ft.commit();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
		return super.onOptionsItemSelected(item);
	}
	
	
	
	

}
