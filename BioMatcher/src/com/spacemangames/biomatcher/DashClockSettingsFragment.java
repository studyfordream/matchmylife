package com.spacemangames.biomatcher;

import android.view.View;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.internal.widget.IcsAdapterView;
import com.actionbarsherlock.internal.widget.IcsAdapterView.OnItemSelectedListener;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.ViewById;
import com.spacemangames.biomatcher.controller.ApplicationPrefs_;
import com.spacemangames.biomatcher.controller.IProfileUpdateListener;
import com.spacemangames.biomatcher.controller.ProfileManager;
import com.spacemangames.biomatcher.data.Profile;
import com.spacemangames.biomatcher.view.ProfileSpinner;

@EFragment(R.layout.fragment_dashclock_settings)
public class DashClockSettingsFragment extends SherlockFragment implements IProfileUpdateListener {
    @ViewById(R.id.spinner1)
    protected ProfileSpinner spinner1;

    protected final class OnProfileSelectedListener implements OnItemSelectedListener {
        @Override
        public void onItemSelected(IcsAdapterView<?> parent, View view, int position, long id) {
            applicationPrefs.dashClockProfile().put(spinner1.getSelectedProfile().getId());
        }

        @Override
        public void onNothingSelected(IcsAdapterView<?> parent) {
        }
    }

    private ApplicationPrefs_ applicationPrefs;

    @Override
    public void onResume() {
        super.onResume();

        spinner1.setOnItemSelectedListener(new OnProfileSelectedListener());

        loadPrefs();
        ProfileManager.addListener(this);
    }

    public void resfreshProfiles() {
        if (spinner1 != null) {
            spinner1.refreshProfiles();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        ProfileManager.removeListener(this);

        applicationPrefs.dashClockProfile().put(spinner1.getSelectedId());
    }

    private void loadPrefs() {
        applicationPrefs = new ApplicationPrefs_(getActivity());

        if (applicationPrefs.dashClockProfile().exists()) {
            spinner1.setSelectionById(applicationPrefs.dashClockProfile().get());
        }
    }

    @Override
    public void profileAdded(Profile profile) {
        spinner1.getProfileAdapter().add(profile);
    }

    @Override
    public void profileRemoved(Profile profile) {
        spinner1.getProfileAdapter().remove(profile);
    }

    @Override
    public void profileEdited(Profile profile) {
        profileRemoved(profile);
        profileAdded(profile);
    }
}
