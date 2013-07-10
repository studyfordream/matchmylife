package com.spacemangames.biomatcher;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;

import com.spacemangames.biomatcher.data.Profile;

public class DatePickerFragment extends DialogFragment implements OnDateSetListener {

    private Profile profile;
    private OnDateChangedListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int year = profile.getYear();
        int month = profile.getMonth();
        int day = profile.getDay();

        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        listener.onDateChanged(null, year, month, day);
    }

    public void setProfileAndOnDateChangedListener(Profile profile, OnDateChangedListener listener) {
        this.profile = profile;
        this.listener = listener;
    }
}
