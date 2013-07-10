package com.spacemangames.biomatcher;

import android.view.View;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.internal.widget.IcsAdapterView;
import com.actionbarsherlock.internal.widget.IcsAdapterView.OnItemSelectedListener;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.ViewById;
import com.spacemangames.biomatcher.controller.IProfileUpdateListener;
import com.spacemangames.biomatcher.controller.ProfileManager;
import com.spacemangames.biomatcher.data.Profile;
import com.spacemangames.biomatcher.model.biorhythm.BioRhythm;
import com.spacemangames.biomatcher.view.BioRhythmGraph;
import com.spacemangames.biomatcher.view.ProfileSpinner;

@EFragment(R.layout.fragment_biorhythm)
public class BioRhythmFragment extends SherlockFragment implements IProfileUpdateListener {
    @ViewById
    protected ProfileSpinner profileSpinner;

    @ViewById
    protected BioRhythmGraph graph;

    @AfterViews
    protected void afterViews() {
        profileSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(IcsAdapterView<?> parent, View view, int position, long id) {
                BioRhythm bioRhythm = new BioRhythm(profileSpinner.getSelectedProfile().timestamp());
                graph.setBioRhythm(bioRhythm);
            }

            @Override
            public void onNothingSelected(IcsAdapterView<?> parent) {
            }
        });
        ProfileManager.addListener(this);
    }

    @Override
    public void profileAdded(Profile profile) {
        profileSpinner.getProfileAdapter().add(profile);
    }

    @Override
    public void profileRemoved(Profile profile) {
        profileSpinner.getProfileAdapter().remove(profile);
    }

    @Override
    public void profileEdited(Profile profile) {
        profileRemoved(profile);
        profileAdded(profile);
    }
}
