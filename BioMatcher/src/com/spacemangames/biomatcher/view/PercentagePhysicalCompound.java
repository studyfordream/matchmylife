package com.spacemangames.biomatcher.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.googlecode.androidannotations.annotations.EViewGroup;
import com.googlecode.androidannotations.annotations.ViewById;
import com.spacemangames.biomatcher.R;

@EViewGroup(R.layout.percentage_physical)
public class PercentagePhysicalCompound extends LinearLayout implements IPercentage {

    @ViewById(R.id.progress)
    protected ProgressBar progress;

    @ViewById(R.id.text)
    protected TextView    text;

    public PercentagePhysicalCompound(Context context) {
        super(context);
    }

    public PercentagePhysicalCompound(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setValue(int value) {
        text.setText(value + "%");
        progress.setProgress(value);
    }
}
