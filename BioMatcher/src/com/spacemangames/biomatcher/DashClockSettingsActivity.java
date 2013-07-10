package com.spacemangames.biomatcher;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.google.analytics.tracking.android.EasyTracker;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.ViewById;
import com.spacemangames.biomatcher.controller.ProfileManager;

@EActivity(R.layout.activity_dash_clock_settings)
public class DashClockSettingsActivity extends SherlockFragmentActivity implements ActionBar.TabListener {
    private SectionsPagerAdapter sectionsPagerAdapter;

    @ViewById(R.id.pager)
    protected ViewPager          viewPager;

    private ActionBar            actionBar;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
    }

    @AfterViews
    protected void afterViews() {
        actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setIcon(R.drawable.dashclock_icon);

        for (int i = 0; i < sectionsPagerAdapter.getCount(); i++) {
            actionBar.addTab(actionBar.newTab().setText(sectionsPagerAdapter.getPageTitle(i)).setTabListener(this));
        }

        viewPager.setAdapter(sectionsPagerAdapter);
        viewPager.setOffscreenPageLimit(sectionsPagerAdapter.getCount());

        viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        ProfileManagerFragment profileManagerFragment = sectionsPagerAdapter.getFragment(ProfileManagerFragment.class);
        if (profileManagerFragment != null) {
            profileManagerFragment.init(DashClockSettingsActivity.this);
            profileManagerFragment.resetAdapter();
        }
        DashClockSettingsFragment settingsFragment = sectionsPagerAdapter.getFragment(DashClockSettingsFragment.class);
        if (settingsFragment != null) {
            settingsFragment.resfreshProfiles();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();
        EasyTracker.getInstance().activityStart(this);

        // switch to profilemanager if we need to create a profile
        if (ProfileManager.getProfiles(this).isEmpty()) {
            getSupportActionBar().setSelectedNavigationItem(1);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        EasyTracker.getInstance().activityStop(this);
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        int position = tab.getPosition();
        viewPager.setCurrentItem(position);
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @SuppressLint("DefaultLocale")
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private final List<String>   titles    = new ArrayList<String>();
        private final List<Fragment> fragments = new ArrayList<Fragment>();

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
            titles.add(0, getString(R.string.title_page_dashclock_settings).toUpperCase());
            titles.add(1, getString(R.string.title_page_profiles).toUpperCase());
            fragments.add(0, new DashClockSettingsFragment_());
            fragments.add(1, new ProfileManagerFragment_());
        }

        @SuppressWarnings("unchecked")
        public <T extends Fragment> T getFragment(Class<T> clazz) {
            for (Fragment fragment : fragments) {
                if (clazz.isAssignableFrom(fragment.getClass())) {
                    return (T) fragment;
                }
            }
            return null;
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
