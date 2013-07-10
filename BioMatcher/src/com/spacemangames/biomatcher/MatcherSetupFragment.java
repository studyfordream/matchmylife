package com.spacemangames.biomatcher;

import android.content.Intent;
import android.view.View;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.internal.widget.IcsAdapterView;
import com.actionbarsherlock.internal.widget.IcsAdapterView.OnItemSelectedListener;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.FragmentById;
import com.googlecode.androidannotations.annotations.ViewById;
import com.spacemangames.biomatcher.controller.ApplicationPrefs_; 
import com.spacemangames.biomatcher.controller.IProfileUpdateListener;
import com.spacemangames.biomatcher.controller.ProfileManager;
import com.spacemangames.biomatcher.data.Profile;
import com.spacemangames.biomatcher.view.ProfileSpinner;

@EFragment(R.layout.fragment_matcher_setup)
public class MatcherSetupFragment extends SherlockFragment implements IProfileUpdateListener {
    @FragmentById(R.id.resultFragment)
    protected BioMatchResultCombinedFragment resultFragment;

    @ViewById(R.id.spinner1)
    protected ProfileSpinner                 spinner1;

    @ViewById(R.id.spinner2)
    protected ProfileSpinner                 spinner2;

    protected final class OnProfileSelectedListener implements OnItemSelectedListener {
        private final IBioMatchResultPresenter fragment;

        private OnProfileSelectedListener(IBioMatchResultPresenter fragment) {
            this.fragment = fragment;
        }

        @Override
        public void onItemSelected(IcsAdapterView<?> parent, View view, int position, long id) {
            fragment.setProfile1(spinner1.getSelectedProfile());
            fragment.setProfile2(spinner2.getSelectedProfile());
        }

        @Override
        public void onNothingSelected(IcsAdapterView<?> parent) {
            fragment.setProfile1(null);
            fragment.setProfile2(null);

        }
    }

    public static final String PROFILE_ONE = "profile_one";
    public static final String PROFILE_TWO = "profile_two";

    private ApplicationPrefs_  applicationPrefs;

    @Click(R.id.buttonGo)
    protected void onMatchClicked() {
        Intent intent = new Intent(getActivity(), BioMatcherMatchResultActivity_.class);
        intent.putExtra(PROFILE_ONE, spinner1.getSelectedProfile());
        intent.putExtra(PROFILE_TWO, spinner2.getSelectedProfile());
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();

        loadPrefs();
        ProfileManager.addListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();

        ProfileManager.removeListener(this);

        applicationPrefs.profile1().put(spinner1.getSelectedId());
        applicationPrefs.profile2().put(spinner2.getSelectedId());
    }

    private void loadPrefs() {
        applicationPrefs = new ApplicationPrefs_(getActivity());

        if (applicationPrefs.profile1().exists()) {
            spinner1.setSelectionById(applicationPrefs.profile1().get());
        }
        if (applicationPrefs.profile2().exists()) {
            spinner2.setSelectionById(applicationPrefs.profile2().get());
        }

        if (resultFragment != null) {
            setResultFragment(resultFragment);
            resultFragment.setProfile1(spinner1.getSelectedProfile());
            resultFragment.setProfile2(spinner2.getSelectedProfile());
        }
    }

    @Override
    public void profileAdded(Profile profile) {
        spinner1.getProfileAdapter().add(profile);
        spinner2.getProfileAdapter().add(profile);
    }

    @Override
    public void profileRemoved(Profile profile) {
        spinner1.getProfileAdapter().remove(profile);
        spinner2.getProfileAdapter().remove(profile);
    }

    @Override
    public void profileEdited(Profile profile) {
        profileRemoved(profile);
        profileAdded(profile);
    }

    public void setResultFragment(final IBioMatchResultPresenter fragment) {
        spinner1.setOnItemSelectedListener(new OnProfileSelectedListener(fragment));
        spinner2.setOnItemSelectedListener(new OnProfileSelectedListener(fragment));
    }

    public void setVisible(boolean visible) {
        if (resultFragment != null) {
            resultFragment.setVisible(visible);
        }
    }
}
