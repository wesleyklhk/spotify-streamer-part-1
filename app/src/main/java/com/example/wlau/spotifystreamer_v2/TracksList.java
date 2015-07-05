package com.example.wlau.spotifystreamer_v2;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.models.Image;
import kaaes.spotify.webapi.android.models.Track;

/**
 * Created by wlau on 2015-06-19.
 */
public class TracksList extends ArrayAdapter<ParcelableTrack> {
    private final Activity context;
    public List<ParcelableTrack> tracks = new ArrayList<ParcelableTrack>();

    public TracksList(Activity context){
        super(context,R.layout.list_items_image_text);
        this.context = context;


    }

    @Override
    public void add(ParcelableTrack object) {
        super.add(object);
        tracks.add(object);
    }

    @Override
    public void clear() {
        super.clear();
        tracks = new ArrayList<ParcelableTrack>();
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.list_items_image_text, null, true);
        ParcelableTrack track = tracks.get(position);
        String trackImage = track.albumArtUrl;

        TextView txtTitle = (TextView) rowView.findViewById(R.id.TextView_listItemArtist);

        ImageView imageView = (ImageView) rowView.findViewById(R.id.ImageView_listItemArtist);
        //Log.d("move bitch", artist.name.toString());
        txtTitle.setText(track.trackName.toString());
//
//        //imageView.setImageResource(imageId[position]);
        if (trackImage != null) {
            Picasso.with(context)
                    .load(trackImage)
                    .resize(50, 50)
                    .centerCrop()
                    .into(imageView);
        }

        return rowView;
    }
}