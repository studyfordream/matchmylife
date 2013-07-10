package com.spacemangames.biomatcher.controller;

import com.googlecode.androidannotations.annotations.sharedpreferences.DefaultString;
import com.googlecode.androidannotations.annotations.sharedpreferences.SharedPref;
import com.googlecode.androidannotations.annotations.sharedpreferences.SharedPref.Scope;

@SharedPref(value = Scope.UNIQUE)
public interface ApplicationPrefs {

    @DefaultString("")
    String profile1();

    @DefaultString("")
    String profile2();

    @DefaultString("")
    String dashClockProfile();
}
