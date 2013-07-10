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
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.google.analytics.tracking.android.EasyTracker;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.ViewById;
import com.spacemangames.biomatcher.controller.MainMenuUtils;
import com.spacemangames.biomatcher.controller.ProfileManager;

@EActivity(R.layout.activity_match_my_life)
public class BioMatcherActivity extends SherlockFragmentActivity implements ActionBar.TabListener {

    private SectionsPagerAdapter sectionsPagerAdapter;

    @ViewById(R.id.pager)
    protected ViewPager          viewPager;

    private ActionBar            actionBar;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (bundle == null) {
            sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        } else {
            sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), bundle);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        sectionsPagerAdapter.onSaveInstanceState(outState);
    }

    @AfterViews
    protected void afterViews() {
        actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

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
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();
        EasyTracker.getInstance().activityStart(this);

        // switch to profilemanager if we need to create a profile
        if (ProfileManager.getProfiles(this).isEmpty()) {
            getSupportActionBar().setSelectedNavigationItem(2);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
        EasyTracker.getInstance().activityStop(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (MainMenuUtils.onOptionsItemSelected(this, item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        int position = tab.getPosition();
        viewPager.setCurrentItem(position);

        MatcherSetupFragment setupFragment = sectionsPagerAdapter.getFragment(MatcherSetupFragment.class);
        if (setupFragment != null && sectionsPagerAdapter.getItem(position) == setupFragment) {
            setupFragment.setVisible(true);
        } else if (setupFragment != null) {
            setupFragment.setVisible(false);
        }
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @SuppressLint("DefaultLocale")
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private final List<String>    titles    = new ArrayList<String>();
        private final List<Fragment>  fragments = new ArrayList<Fragment>();
        private final FragmentManager fm;

        public SectionsPagerAdapter(FragmentManager fm, Bundle inState) {
            super(fm);
            this.fm = fm;
            ProfileManagerFragment_ profileManagerFragment = (ProfileManagerFragment_) fm.getFragment(inState,
                    ProfileManagerFragment.class.getName());
            titles.add(0, getString(R.string.title_page_biorhythm).toUpperCase());
            titles.add(1, getString(R.string.title_page_match_setup).toUpperCase());
            titles.add(2, getString(R.string.title_page_profiles).toUpperCase());
            fragments.add(0, fm.getFragment(inState, BioRhythmFragment.class.getName()));
            fragments.add(1, fm.getFragment(inState, MatcherSetupFragment.class.getName()));
            fragments.add(2, profileManagerFragment);

        }

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
            this.fm = fm;
            ProfileManagerFragment_ profileManagerFragment = new ProfileManagerFragment_();
            titles.add(0, getString(R.string.title_page_biorhythm).toUpperCase());
            titles.add(1, getString(R.string.title_page_match_setup).toUpperCase());
            titles.add(2, getString(R.string.title_page_profiles).toUpperCase());
            fragments.add(0, new BioRhythmFragment_());
            fragments.add(1, new MatcherSetupFragment_());
            fragments.add(2, profileManagerFragment);
            profileManagerFragment.init(BioMatcherActivity.this);
        }

        public void onSaveInstanceState(Bundle outState) {
            fm.putFragment(outState, BioRhythmFragment.class.getName(), fragments.get(0));
            fm.putFragment(outState, MatcherSetupFragment.class.getName(), fragments.get(1));
            fm.putFragment(outState, ProfileManagerFragment.class.getName(), fragments.get(2));
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
