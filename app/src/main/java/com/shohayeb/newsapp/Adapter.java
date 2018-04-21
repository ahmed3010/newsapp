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
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.story_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();

        }
        holder.title.setText(getItem(position).getTitle());
        holder.section.setText(getItem(position).getSection());
        holder.date.setText(getItem(position).getDate());
        holder.author.setText(getItem(position).getAuthor());
        return convertView;
    }

    class ViewHolder {
        private TextView title;
        private TextView section;
        private TextView date;
        private TextView author;

        public ViewHolder(View view) {
            this.title = view.findViewById(R.id.title_text_view);
            this.section = view.findViewById(R.id.section_text_view);
            this.date = view.findViewById(R.id.date_text_view);
            this.author = view.findViewById(R.id.author_text_view);

        }
    }
}