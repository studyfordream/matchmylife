package com.spacemangames.biomatcher;

import java.util.Date;
import java.util.Set;

import android.content.Intent;

import com.google.android.apps.dashclock.api.DashClockExtension;
import com.google.android.apps.dashclock.api.ExtensionData;
import com.googlecode.androidannotations.annotations.EService;
import com.googlecode.androidannotations.annotations.sharedpreferences.Pref;
import com.spacemangames.biomatcher.controller.ApplicationPrefs_;
import com.spacemangames.biomatcher.controller.ProfileManager;
import com.spacemangames.biomatcher.data.Profile;
import com.spacemangames.biomatcher.model.biorhythm.BioRhythm;
import com.spacemangames.biomatcher.model.biorhythm.BioType;

@EService
public class BioRhythmDashClockService extends DashClockExtension {

    private static final String INTELLECTUAL = " \uD83D\uDCA1";
    private static final String EMOTIONAL    = " \uD83D\uDC93";
    private static final String PHYSICAL     = "\uD83D\uDCAA";
    private static final String UP_ARROW     = "\u2191";
    private static final String DOWN_ARROW   = "\u2193";

    @Pref
    protected ApplicationPrefs_ applicationPrefs;
    private Profile             profile;
    private BioRhythm           biorhythm;
    private long                now;

    @Override
    protected void onUpdateData(int arg0) {
        if (applicationPrefs.dashClockProfile().exists()) {
            profile = getProfileById(applicationPrefs.dashClockProfile().get());
            biorhythm = new BioRhythm(profile.timestamp());
        }

        if (profile == null) {
            publishNoProfileUpdate();
        } else {
            setNow();
            publishRegularUpdate();
        }
    }

    private void setNow() {
        now = new Date().getTime();
    }

    private void publishRegularUpdate() {
        publishUpdate(new ExtensionData().visible(true).icon(R.drawable.dashclock_icon).status(getTitle())
                .expandedTitle(getExpandedTitle()).expandedBody(getExpandedBody()).clickIntent(getClickIntent()));

    }

    private Intent getClickIntent() {
        Intent intent = new Intent();
        intent = getPackageManager().getLaunchIntentForPackage("com.spacemangames.biomatcher");
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        return intent;
    }

    private String getExpandedBody() {
        return "";
    }

    private String getExpandedTitle() {
        StringBuilder sb = new StringBuilder();
        sb.append(PHYSICAL).append(biorhythm.getPercentageValue(BioType.PHYSICAL, now)).append(getDirectionChar(BioType.PHYSICAL, now));
        sb.append(EMOTIONAL).append(biorhythm.getPercentageValue(BioType.EMOTIONAL, now)).append(getDirectionChar(BioType.EMOTIONAL, now));
        sb.append(INTELLECTUAL).append(biorhythm.getPercentageValue(BioType.INTELLECTUAL, now))
                .append(getDirectionChar(BioType.INTELLECTUAL, now));
        return sb.toString();
    }

    private String getDirectionChar(BioType type, long time) {
        switch (biorhythm.getDirection(type, time)) {
        case DOWN:
            return DOWN_ARROW;
        case UP:
            return UP_ARROW;
        default:
            return "";
        }
    }

    private String getTitle() {
        StringBuilder sb = new StringBuilder();
        int average = biorhythm.getPercentageValue(BioType.PHYSICAL, now) + biorhythm.getPercentageValue(BioType.EMOTIONAL, now)
                + biorhythm.getPercentageValue(BioType.INTELLECTUAL, now);
        average /= 3;
        sb.append(average).append("%");
        return sb.toString();
    }

    private void publishNoProfileUpdate() {
        publishUpdate(new ExtensionData().visible(true).icon(R.drawable.dashclock_icon).expandedTitle("No profile selected"));
    }

    private Profile getProfileById(String id) {
        Set<Profile> profiles = ProfileManager.getProfiles(getApplicationContext());
        for (Profile profileIterator : profiles) {
            if (profileIterator.getId().equals(id)) {
                return profileIterator;
            }
        }
        return null;
    }
}
