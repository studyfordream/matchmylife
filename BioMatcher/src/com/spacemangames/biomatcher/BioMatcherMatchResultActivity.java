package com.spacemangames.biomatcher;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.google.analytics.tracking.android.EasyTracker;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.ViewById;
import com.spacemangames.biomatcher.controller.MainMenuUtils;

@EActivity(R.layout.activity_bio_match_result)
public class BioMatcherMatchResultActivity extends SherlockFragmentActivity implements ActionBar.TabListener {

    ResultSectionsPagerAdapter sectionsPagerAdapter;

    @ViewById(R.id.pager)
    ViewPager                  viewPager;

    private ActionBar          actionBar;

    @AfterViews
    protected void afterViews() {
        sectionsPagerAdapter = new ResultSectionsPagerAdapter(getSupportFragmentManager());

        actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayHomeAsUpEnabled(true);

        for (int i = 0; i < sectionsPagerAdapter.getCount(); i++) {
            actionBar.addTab(actionBar.newTab().setText(sectionsPagerAdapter.getPageTitle(i)).setTabListener(this));
        }

        viewPager.setAdapter(sectionsPagerAdapter);

        viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        EasyTracker.getInstance().activityStart(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EasyTracker.getInstance().activityStop(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (MainMenuUtils.onOptionsItemSelected(this, item)) {
            return true;
        }

        switch (item.getItemId()) {
        case android.R.id.home:
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @SuppressLint("DefaultLocale")
    public class ResultSectionsPagerAdapter extends FragmentPagerAdapter {

        private final List<String>   titles    = new ArrayList<String>();
        private final List<Fragment> fragments = new ArrayList<Fragment>();

        public ResultSectionsPagerAdapter(FragmentManager fm) {
            super(fm);
            titles.add(0, getString(R.string.title_result_page_overview).toUpperCase());
            titles.add(1, getString(R.string.title_result_page_graph).toUpperCase());
            fragments.add(0, new BioMatchResultFragment_());
            fragments.add(1, new BioMatchResultGraphFragment_());
        }

        public IBioMatchResultPresenter getBioMatchResultFragment() {
            return (IBioMatchResultPresenter) fragments.get(0);
        }

        @Override
        public Fragment getItem(int i) {
            return fragments.get(i);
        }

        @Override
        public int getCount() {
            return titles.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }
}
