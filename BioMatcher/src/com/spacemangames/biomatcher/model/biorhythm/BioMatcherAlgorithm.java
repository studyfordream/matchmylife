package com.spacemangames.biomatcher.model.biorhythm;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.spacemangames.biomatcher.R;

public enum BioMatcherAlgorithm {
    SUMMED_MAX, PHASE_DIFF;

    public static BioMatcherAlgorithm getFromPrefs(Context context) {
        String key = context.getResources().getString(R.string.pref_algorithm);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return BioMatcherAlgorithm.valueOf(prefs.getString(key, SUMMED_MAX.toString()));
    }
}
