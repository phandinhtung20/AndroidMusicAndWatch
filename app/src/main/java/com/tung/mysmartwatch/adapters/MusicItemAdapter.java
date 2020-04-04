package com.tung.mysmartwatch.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tung.mysmartwatch.R;
import com.tung.mysmartwatch.models.MusicItem;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MusicItemAdapter extends ArrayAdapter<MusicItem> {
    private Context context;
    private List<MusicItem> musicItems;
    private int resource;

    public MusicItemAdapter(@NonNull Context context, int resource, @NonNull List<MusicItem> objects) {
        super(context, resource, objects);
        this.context = context;
        this.musicItems = objects;
        this.resource = resource;
    }

    @Override
    public int getCount() {
        return this.musicItems.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View row = inflater.inflate(this.resource,null);

        TextView txtTitle = row.findViewById(R.id.tv_name);
        TextView txtAuthor = row.findViewById(R.id.tv_singer);
        TextView txtDuration= row.findViewById(R.id.tv_duration);

        txtTitle.setText(musicItems.get(position).getName());
        txtAuthor.setText(musicItems.get(position).getSinger());
        txtDuration.setText(musicItems.get(position).getDurationString());
        return row;
    }
}
