package com.spacemangames.biomatcher;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.googlecode.androidannotations.annotations.EFragment;
import com.spacemangames.biomatcher.controller.ProfileArrayAdapter;
import com.spacemangames.biomatcher.controller.ProfileManager;
import com.spacemangames.biomatcher.data.Profile;

@EFragment
public class ProfileManagerFragment extends SherlockListFragment {
    private ProfileArrayAdapter adapter;

    public void init(Context context) {
        ArrayList<Profile> profiles = new ArrayList<Profile>(ProfileManager.getProfiles(context));
        adapter = new ProfileArrayAdapter(context, profiles);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        getListView().setSelection(position);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public void resetAdapter() {
        setListAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();

        setHasOptionsMenu(true);

        setListAdapter(adapter);

        registerForContextMenu(getListView());

        if (adapter.getCount() == 0) {
            WelcomeFragment welcomeFragment = WelcomeFragment.newInstance(this);
            welcomeFragment.show(getFragmentManager(), "dialog");
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        android.view.MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.profile_context_menu, menu);

        // you can't delete the last item, there must always be at least 1
        // profile
        if (adapter.getCount() <= 1) {
            menu.removeItem(R.id.menu_delete_profile);
        }
    }

    @Override
    public boolean onContextItemSelected(android.view.MenuItem item) {
        AdapterContextMenuInfo info;
        Profile profile;
        switch (item.getItemId()) {
        case R.id.menu_edit_profile:
            info = (AdapterContextMenuInfo) item.getMenuInfo();
            profile = adapter.getItem(info.position);
            ProfileEditFragment profileEditFragment = new ProfileEditFragment();
            profileEditFragment.setProfile(profile, this);
            profileEditFragment.show(getActivity().getSupportFragmentManager(), "profileEditor");
            return true;
        case R.id.menu_delete_profile:
            info = (AdapterContextMenuInfo) item.getMenuInfo();
            profile = adapter.getItem(info.position);
            ProfileManager.removeProfile(getActivity(), profile);
            adapter.remove(profile);
            return true;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {
        inflater.inflate(R.menu.profile_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.menu_add_profile:
            ProfileEditFragment profileEditFragment = new ProfileEditFragment();
            profileEditFragment.setProfile(null, this);
            profileEditFragment.show(getActivity().getSupportFragmentManager(), "profileEditor");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void add(Profile profile) {
        adapter.add(profile);
    }

    public void edit(Profile profile) {
        adapter.remove(profile);
        adapter.add(profile);
    }
}
