package com.spacemangames.biomatcher;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class WelcomeFragment extends DialogFragment {
    private ProfileManagerFragment profileManagerFragment;

    public static WelcomeFragment newInstance(ProfileManagerFragment profileManagerFragment) {
        WelcomeFragment f = new WelcomeFragment();
        f.setProfileManagerFragment(profileManagerFragment);

        return f;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity()).setTitle(R.string.welcome_dialog_title).setMessage(R.string.welcome_dialog_content)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                        ProfileEditFragment profileEditFragment = new ProfileEditFragment();
                        profileEditFragment.setProfile(null, profileManagerFragment);
                        profileEditFragment.setCancelable(false);
                        profileEditFragment.show(getActivity().getSupportFragmentManager(), "profileEditor");
                    }
                }).create();
    }

    private void setProfileManagerFragment(ProfileManagerFragment profileManagerFragment) {
        this.profileManagerFragment = profileManagerFragment;
    }
}
