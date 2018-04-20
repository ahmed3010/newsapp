package com.shohayeb.newsapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class Adapter extends ArrayAdapter<News> {

    Adapter(@NonNull Context context, @NonNull List<News> newsList) {
        super(context, 0, newsList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.story_item, parent, false);
        }
        TextView title = convertView.findViewById(R.id.title_text_view);
        TextView section = convertView.findViewById(R.id.section_text_view);
        TextView date = convertView.findViewById(R.id.date_text_view);
        title.setText(getItem(position).getTitle());
        section.setText(getItem(position).getSection());
        date.setText(getItem(position).getDate());
        return convertView;
    }
}
