package com.spacemangames.biomatcher.controller;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.spacemangames.biomatcher.R;
import com.spacemangames.biomatcher.data.Profile;

public class ProfileArrayAdapter extends ArrayAdapter<Profile> {

    private final Context       context;
    private final List<Profile> profiles;

    public ProfileArrayAdapter(Context context, List<Profile> profiles) {
        super(context, R.layout.profile_list_item, profiles);
        this.context = context;
        this.profiles = profiles;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, parent);
    }

    private View getCustomView(int position, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.profile_list_item, parent, false);

        TextView nameView = (TextView) rowView.findViewById(R.id.nameTextView);
        TextView dateView = (TextView) rowView.findViewById(R.id.dateTextView);
        nameView.setText(profiles.get(position).getName());
        dateView.setText(profiles.get(position).dateString());

        return rowView;
    }
}
