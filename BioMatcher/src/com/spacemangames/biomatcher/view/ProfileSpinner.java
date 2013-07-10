package com.spacemangames.biomatcher.view;

import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;

import com.actionbarsherlock.internal.widget.IcsSpinner;
import com.spacemangames.biomatcher.controller.ProfileArrayAdapter;
import com.spacemangames.biomatcher.controller.ProfileManager;
import com.spacemangames.biomatcher.data.Profile;

public class ProfileSpinner extends IcsSpinner {

    private ProfileArrayAdapter adapter;
    private Context             context;

    public ProfileSpinner(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public ProfileSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        refreshProfiles();
    }

    public String getSelectedId() {
        if (getSelectedProfile() != null) {
            return getSelectedProfile().getId();
        }
        return null;
    }

    public void setSelectionById(String id) {
        Profile find = new Profile();
        find.setId(id);
        setSelection(adapter.getPosition(find));
    }

    public Profile getSelectedProfile() {
        return (Profile) getSelectedItem();
    }

    public ProfileArrayAdapter getProfileAdapter() {
        return adapter;
    }

    public void refreshProfiles() {
        adapter = new ProfileArrayAdapter(context, new ArrayList<Profile>(ProfileManager.getProfiles(context)));
        setAdapter(adapter);
    }
}
