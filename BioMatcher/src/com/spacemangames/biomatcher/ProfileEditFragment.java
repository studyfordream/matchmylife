package com.spacemangames.biomatcher;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;

import com.spacemangames.biomatcher.controller.ProfileManager;
import com.spacemangames.biomatcher.data.Profile;

public class ProfileEditFragment extends DialogFragment implements OnDateChangedListener {
    private final class PositiveClickListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int whichButton) {
            if (nameView.getText().toString().isEmpty()) {
                profile.setName(getActivity().getResources().getString(R.string.name));
            } else {
                profile.setName(nameView.getText().toString());
            }
            if (editmode) {
                ProfileManager.editProfile(getActivity(), profile);
                profileManagerFragment.edit(profile);
            } else {
                ProfileManager.addProfile(getActivity(), profile);
                profileManagerFragment.add(profile);
            }
        }
    }

    private Profile                profile;
    private Button                 dateButton;
    private EditText               nameView;
    private ProfileManagerFragment profileManagerFragment;
    private boolean                editmode = false;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_edit_profile_dialog, null);
        initView(view);

        Builder builder = new AlertDialog.Builder(getActivity()).setView(view).setTitle(R.string.edit_profile_dialog_title)
                .setPositiveButton(android.R.string.ok, new PositiveClickListener());
        if (isCancelable()) {
            builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int whichButton) {
                }
            });
        }

        return builder.create();
    }

    private void initView(View view) {
        final OnDateChangedListener dateChangedListener = this;
        dateButton = (Button) view.findViewById(R.id.buttonDate);
        dateButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment datePickerFragment = new DatePickerFragment();
                datePickerFragment.setProfileAndOnDateChangedListener(profile, dateChangedListener);
                datePickerFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
            }
        });
        updateButtonText();

        nameView = (EditText) view.findViewById(R.id.editTextName);
        if (profile.getName() != null) {
            nameView.setText(profile.getName());
        }
    }

    private void updateButtonText() {
        dateButton.setText(profile.dateString());
    }

    public void setProfile(Profile profile, ProfileManagerFragment profileManagerFragment) {
        if (profile != null) {
            editmode = true;
            this.profile = profile;
        } else {
            this.profile = new Profile();
        }
        this.profileManagerFragment = profileManagerFragment;
    }

    @Override
    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        profile.setYear(year);
        profile.setMonth(monthOfYear);
        profile.setDay(dayOfMonth);
        updateButtonText();
    }

}
