package com.spacemangames.biomatcher;

import android.support.v4.app.FragmentActivity;

import com.actionbarsherlock.app.SherlockFragment;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.ViewById;
import com.spacemangames.biomatcher.data.Profile;
import com.spacemangames.biomatcher.model.biorhythm.BioMatcherAlgorithm;
import com.spacemangames.biomatcher.model.biorhythm.BioRhythm;
import com.spacemangames.biomatcher.model.biorhythm.BioRhythmMatcher;
import com.spacemangames.biomatcher.view.BioMatchResultGraph;
import com.spacemangames.biomatcher.view.NameCompound;

@EFragment(R.layout.fragment_bio_match_result_graph)
public class BioMatchResultGraphFragment extends SherlockFragment implements IBioMatchResultPresenter {

    private FragmentActivity      activity;

    @ViewById(R.id.graph)
    protected BioMatchResultGraph graph;

    @ViewById(R.id.NameCompound)
    protected NameCompound        names;

    private Profile               profile1;

    private Profile               profile2;

    @AfterViews
    protected void afterViews() {
        activity = getActivity();
        profile1 = (Profile) activity.getIntent().getSerializableExtra(MatcherSetupFragment.PROFILE_ONE);
        profile2 = (Profile) activity.getIntent().getSerializableExtra(MatcherSetupFragment.PROFILE_TWO);

        updateUI();
    }

    private void updateUI() {
        if (profile1 != null && profile2 != null) {
            BioRhythm bio1 = new BioRhythm();
            BioRhythm bio2 = new BioRhythm();
            bio1.setDate(profile1.timestamp());
            bio2.setDate(profile2.timestamp());

            BioRhythmMatcher bioMatch = new BioRhythmMatcher(bio1, bio2, BioMatcherAlgorithm.getFromPrefs(activity));

            graph.setBioMatch(bioMatch);

            names.setNames(profile1, profile2);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        afterViews();
    }

    @Override
    public void setProfile1(Profile profile) {
        profile1 = profile;
        updateUI();
    }

    @Override
    public void setProfile2(Profile profile) {
        profile2 = profile;
        updateUI();
    }
}
