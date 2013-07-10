package com.spacemangames.biomatcher.controller;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.actionbarsherlock.view.MenuItem;
import com.spacemangames.biomatcher.R;
import com.spacemangames.biomatcher.SettingsActivity_;

public class MainMenuUtils {

    private static final String APP_PACKAGE = "com.spacemangames.biomatcher";

    public static boolean onOptionsItemSelected(Context context, MenuItem item) {
        switch (item.getItemId()) {
        case R.id.menu_rate_and_review:
            goToMarket(context);
            return true;
        case R.id.menu_settings:
            goToSettings(context);
            return true;
        }
        return false;
    }

    private static void goToSettings(Context context) {
        Intent settingsIntent = new Intent(context, SettingsActivity_.class);
        context.startActivity(settingsIntent);
    }

    private static void goToMarket(Context context) {
        String appPackageName = APP_PACKAGE;
        try {
            Intent marketIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName));
            marketIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
            context.startActivity(marketIntent);
        } catch (ActivityNotFoundException e) {
            Intent marketIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id="
                    + appPackageName));
            marketIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
            context.startActivity(marketIntent);
        }
    }
}
