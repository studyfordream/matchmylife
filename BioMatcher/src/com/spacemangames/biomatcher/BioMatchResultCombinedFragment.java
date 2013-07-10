package com.spacemangames.biomatcher;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.widget.ShareActionProvider;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.ViewById;
import com.spacemangames.biomatcher.data.Profile;
import com.spacemangames.biomatcher.model.biorhythm.BioMatcherAlgorithm;
import com.spacemangames.biomatcher.model.biorhythm.BioRhythm;
import com.spacemangames.biomatcher.model.biorhythm.BioRhythmMatcher;
import com.spacemangames.biomatcher.model.biorhythm.BioType;
import com.spacemangames.biomatcher.view.BioMatchResultGraph;
import com.spacemangames.biomatcher.view.NameCompound;
import com.spacemangames.biomatcher.view.PercentageEmotionalCompound;
import com.spacemangames.biomatcher.view.PercentageIntellectualCompound;
import com.spacemangames.biomatcher.view.PercentageOverallCompound;
import com.spacemangames.biomatcher.view.PercentagePhysicalCompound;

@EFragment(R.layout.fragment_bio_match_result_combined)
public class BioMatchResultCombinedFragment extends SherlockFragment implements IBioMatchResultPresenter {

    private static final String              SCREENSHOT_FILENAME = "match.png";

    @ViewById
    protected BioMatchResultGraph            graph;

    @ViewById(R.id.NameCompound)
    protected NameCompound                   names;

    @ViewById
    protected PercentageOverallCompound      overallPercentage;

    @ViewById
    protected PercentagePhysicalCompound     physicalPercentage;

    @ViewById
    protected PercentageEmotionalCompound    emotionalPercentage;

    @ViewById
    protected PercentageIntellectualCompound intellectualPercentage;

    @ViewById(R.id.mainlayout)
    protected LinearLayout                   mainLayout;

    private FragmentActivity                 activity;

    private Intent                           shareIntent;

    private Profile                          profile1;

    private Profile                          profile2;

    @Override
    public void onStart() {
        super.onStart();
    }

    public void setVisible(boolean visible) {
        setHasOptionsMenu(visible);
    }

    @AfterViews
    protected void afterViews() {
        activity = getActivity();
        profile1 = (Profile) activity.getIntent().getSerializableExtra(MatcherSetupFragment.PROFILE_ONE);
        profile2 = (Profile) activity.getIntent().getSerializableExtra(MatcherSetupFragment.PROFILE_TWO);
        updateUI();
    }

    private void updateUI() {
        updateBarUI();
        updateGraphUI();
        getSherlockActivity().invalidateOptionsMenu();
    }

    private void updateBarUI() {
        if (profile1 != null && profile2 != null) {
            BioRhythm bio1 = new BioRhythm();
            BioRhythm bio2 = new BioRhythm();

            bio1.setDate(profile1.timestamp());
            bio2.setDate(profile2.timestamp());

            names.setNames(profile1, profile2);

            BioRhythmMatcher bioMatch = new BioRhythmMatcher(bio1, bio2, BioMatcherAlgorithm.getFromPrefs(activity));

            int overallMatch = bioMatch.calculateTotalMatch();
            int physicalMatch = bioMatch.calculateMatch(BioType.PHYSICAL);
            int emotionalMatch = bioMatch.calculateMatch(BioType.EMOTIONAL);
            int intellectualMatch = bioMatch.calculateMatch(BioType.INTELLECTUAL);

            overallPercentage.setValue(overallMatch);
            physicalPercentage.setValue(physicalMatch);
            emotionalPercentage.setValue(emotionalMatch);
            intellectualPercentage.setValue(intellectualMatch);
        }
    }

    private void updateGraphUI() {
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

    public void updateMatchUIFor(TextView textView, ProgressBar progressBar, int match) {
        textView.setText(String.format("%d%%", match));
        progressBar.setProgress(match);
    }

    @Override
    public void onResume() {
        super.onResume();
        afterViews();
    }

    @Override
    public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {
        inflater.inflate(R.menu.result_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);

        updateShareIntent(menu);
    }

    @UiThread
    protected void updateMenu(Menu menu, boolean addShareIntent) {
        if (addShareIntent) {
            ShareActionProvider shareActionProvider = (ShareActionProvider) menu.findItem(R.id.menu_share).getActionProvider();
            shareActionProvider.setShareIntent(shareIntent);
        } else {
            menu.removeItem(R.id.menu_share);
        }
    }

    @Background
    protected void updateShareIntent(Menu menu) {
        Uri imageUri = getImageUri();
        if (imageUri == null) {
            updateMenu(menu, false);
        }

        shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setType("image/*");
        shareIntent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.share_text));
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        updateMenu(menu, true);
    }

    @SuppressLint("WorldReadableFiles")
    private Uri getImageUri() {
        int width = mainLayout.getWidth();
        int height = mainLayout.getHeight();
        if (width <= 0 || height <= 0) {
            invalidateMenuAfterOneSecond();
            return null;
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmap.eraseColor(Color.BLACK);
        Canvas canvas = new Canvas(bitmap);
        mainLayout.draw(canvas);
        File file = null;
        try {
            file = File.createTempFile(SCREENSHOT_FILENAME, ".png", getActivity().getExternalCacheDir());
            FileOutputStream stream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (file != null) {
            return Uri.fromFile(file);
        } else {
            return null;
        }
    }

    @Background
    protected void invalidateMenuAfterOneSecond() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }
        invalidateMenuNow();
    }

    @UiThread
    protected void invalidateMenuNow() {
        getSherlockActivity().invalidateOptionsMenu();
    }

    @Override
    public void setProfile1(Profile profile1) {
        this.profile1 = profile1;
        updateUI();
    }

    @Override
    public void setProfile2(Profile profile2) {
        this.profile2 = profile2;
        updateUI();
    }
}