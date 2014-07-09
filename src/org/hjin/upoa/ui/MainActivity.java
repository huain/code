package org.hjin.upoa.ui;

import java.util.Timer;
import java.util.TimerTask;

import org.hjin.upoa.R;
import org.hjin.upoa.constants.AppConstants;
import org.hjin.upoa.service.OnLineService;
import org.hjin.upoa.ui.view.DrawerAdapter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends BaseActivity {
	
	private final String TAG = "MainActivity";

	private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private LinearLayout mDrawerLinear;
    private ActionBarDrawerToggle mDrawerToggle;
    private int[] mDrawerIcons;
    private TextView mTv;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] mPlanetTitles;
   

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent();
        intent.setClass(getApplicationContext(), OnLineService.class);
        startService(intent);

        mTitle = mDrawerTitle = getTitle();
        mPlanetTitles = getResources().getStringArray(R.array.planets_array);
        mDrawerIcons = new int[]{R.drawable.home,R.drawable.notice,R.drawable.news,R.drawable.samllnote,R.drawable.newdaily,
        		R.drawable.daily,R.drawable.info,R.drawable.setting,R.drawable.about};
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLinear = (LinearLayout)findViewById(R.id.drawer_linear);
        mDrawerLayout.setBackgroundColor(getResources().getColor(R.color.txt_white));
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        
        mTv = (TextView)mDrawerLayout.findViewById(R.id.drawer_text);
        mTv.setText("当前在线人数："+AppConstants.onlinesum);
        
        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener
        mDrawerList.setAdapter(new DrawerAdapter<String>(this,
                R.layout.drawer_list_item, mPlanetTitles, mDrawerIcons));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        // enable ActionBar app icon to behave as action to toggle nav drawer
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
                ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                mTv.setText("当前在线人数："+AppConstants.onlinesum);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            selectItem(0);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.activity_main, menu);
        return true;
//        return super.onCreateOptionsMenu(menu);
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        //boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        //menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         // The action bar home/up action should open or close the drawer.
         // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action buttons
        switch(item.getItemId()) {
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    /* The click listner for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {
    	FragmentManager fragmentManager = getFragmentManager();
    	FragmentTransaction ft = fragmentManager.beginTransaction();
    	ft.setCustomAnimations(R.animator.slide_in_left,R.animator.slide_out_right); 
    	switch(position){
    	case 0:{
    		Log.d(TAG, "首页");
    		Fragment fragment = new IndexFragment();
    		ft.replace(R.id.content_frame, fragment).commit(); 
           
    	}break;
    	case 1:{
    		Log.d(TAG, "公告");
    		Fragment fragment = new NewsListFragment("公告","2");
    	   ft.replace(R.id.content_frame, fragment).commit();
           Log.v(TAG, "===item index:"+position);
    	}break;
    	case 2:{
    		Log.d(TAG, "新闻");
    		Fragment fragment = new NewsListFragment("新闻","1");
//          Bundle args = new Bundle();
//          args.putInt(PlanetFragment.ARG_PLANET_NUMBER, position);
//          fragment.setArguments(args);

    		ft.replace(R.id.content_frame, fragment).commit();
         	Log.v(TAG, "===item index:"+position);

     	}break;
    	case 3:{
    		Log.d(TAG, "小字报");
    		Fragment fragment = new SmallNoteListFragment();
    		ft.replace(R.id.content_frame, fragment).commit();
            Log.v(TAG, "===item index:"+position);
      	   
      	}break;
    	case 4:{
    		Log.d(TAG, "填写日报");
    		Fragment fragment = new DailyFragment();
    		ft.replace(R.id.content_frame, fragment).commit();
            Log.v(TAG, "===item index:"+position);
     	}break;
    	case 5:{
    		Log.d(TAG, "历史日报");
    		Fragment fragment = new DailyListFragment();
    		ft.replace(R.id.content_frame, fragment).commit();
            Log.v(TAG, "===item index:"+position);
     	}break;
     	case 6:{
     		Log.d(TAG, "通讯录");
     		Fragment fragment = new InfoListFragment();
     		ft.replace(R.id.content_frame, fragment).commit();
            Log.v(TAG, "===item index:"+position);
       	}break;
     	case 7:{
     		Log.d(TAG, "设置");
     		Fragment fragment = new SettingFragment();
     		ft.replace(R.id.content_frame, fragment).commit();
     	}break;
     	case 8:{
     		Log.d(TAG, "关于");
     		Fragment fragment = new AboutFragment();
     		ft.replace(R.id.content_frame, fragment).commit();
      	}break;
    	default:break;
    	}
    	
    	mDrawerList.setItemChecked(position, true);
        setTitle(mPlanetTitles[position]);
        mDrawerLayout.closeDrawer(mDrawerLinear);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    /**
     * Fragment that appears in the "content_frame", shows a planet
     */
    public static class PlanetFragment extends Fragment {
        public static final String ARG_PLANET_NUMBER = "planet_number";

        public PlanetFragment() {
            // Empty constructor required for fragment subclasses
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_planet, container, false);
            ImageView im = (ImageView)rootView.findViewById(R.id.image);
            int i = getArguments().getInt(ARG_PLANET_NUMBER);
            String planet = getResources().getStringArray(R.array.planets_array)[i];

            Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.animator.loading);
            LinearInterpolator lin = new LinearInterpolator();
            hyperspaceJumpAnimation.setInterpolator(lin);
            im.startAnimation(hyperspaceJumpAnimation);
            
            getActivity().setTitle(planet);
            return rootView;
        }
    }
    
    
    @Override  
	public boolean onKeyDown(int keyCode, KeyEvent event) {  
	    if(keyCode == KeyEvent.KEYCODE_BACK){    
	    	exitBy2Click();      //调用双击退出函数  
	    }  
	    return false;  
	}
    
    /** 
	 * 双击退出函数 
	 */  
	private static Boolean isExit = false;  
	  
	private void exitBy2Click() {  
	    Timer tExit = null;  
	    if (isExit == false) {  
	        isExit = true; // 准备退出  
	        Toast.makeText(this, "再按一次退出到桌面", Toast.LENGTH_SHORT).show();  
	        tExit = new Timer();  
	        tExit.schedule(new TimerTask() {  
	            @Override  
	            public void run() {  
	                isExit = false; // 取消退出  
	            }  
	        }, 1000); // 如果1秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务  
	  
	    } else {  
//	        finish();  
//	        System.exit(0);  
	    	Intent home = new Intent(Intent.ACTION_MAIN);  
	        home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  
	        home.addCategory(Intent.CATEGORY_HOME);  
	        startActivity(home);
	    }  
	}

	@Override
	protected void onDestroy() {
		Intent intent = new Intent();
        intent.setClass(getApplicationContext(), OnLineService.class);
        stopService(intent);
		super.onDestroy();
	}
	
	
	
	
	
	

}
