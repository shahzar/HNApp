package com.shzlabs.app.hnapp;

import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.shzlabs.app.hnapp.custom.CustomViewPager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String FIREBASE_URL = "https://hacker-news.firebaseio.com/v0";

    Context ctx;
    CustomViewPager viewPager;
    ScreenSlidePagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ctx = this;

        if(!PreferenceManager.getDefaultSharedPreferences(ctx).contains("INTRO_DONE")){
            // Play intro
            Intent intent = new Intent(this, SplashScreenActivity.class);
            startActivity(intent);
            finish();
            PreferenceManager.getDefaultSharedPreferences(ctx).edit().putString("INTRO_DONE", "true").commit();
        }

        // Link views
        viewPager = (CustomViewPager) findViewById(R.id.customViewPager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mPagerAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case R.id.action_about:
                Intent intent = new Intent(this, AboutMeActivity.class);
                startActivity(intent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /* Set actionbar subtitle */
    public void setSubTitle(String content){
        try {
            getSupportActionBar().setSubtitle(content);
        }catch(Exception e){e.printStackTrace();}
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        private List<Fragment> pages;

        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
            pages = new ArrayList<>();
            pages.add(MainListFragment.newInstance());
        }

        public void addPage(int eventID){
//            pages.add(EventDetailsFragment.newInstance(eventID));
            notifyDataSetChanged();
        }

        public void removePage(int position){
            pages.remove(position);
            notifyDataSetChanged();
        }

        @Override
        public Fragment getItem(int position) {
            return pages.get(position);
        }

        @Override
        public int getItemPosition(Object object) {
            if(object instanceof MainListFragment){
                return PagerAdapter.POSITION_UNCHANGED;
            }
            return PagerAdapter.POSITION_NONE;
        }

        @Override
        public int getCount() {
            return pages.size();
        }

    }
}
