package com.spacemangames.biomatcher.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.spacemangames.biomatcher.R;
import com.spacemangames.biomatcher.data.Profile;

public class NameCompound extends LinearLayout {

    private TextView name1;
    private TextView name2;

    public NameCompound(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public NameCompound(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public NameCompound(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.name_compound, this);

        name1 = (TextView) findViewById(R.id.TextViewName1);
        name2 = (TextView) findViewById(R.id.TextViewName2);
    }

    public void setNames(Profile profile1, Profile profile2) {
        name1.setText(profile1.getName());
        name2.setText(profile2.getName());
    }
}
