package com.example.wlau.spotifystreamer_v2;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.Image;

/**
 * Created by wlau on 2015-06-19.
 */
public class ArtistList extends ArrayAdapter<ParcelableArtist> {
    private final Activity context;
    public List<ParcelableArtist> artists = new ArrayList<ParcelableArtist>();

    public ArtistList(Activity context){
        super(context,R.layout.list_items_image_text);
        this.context = context;


    }

    @Override
    public void add(ParcelableArtist object) {
        super.add(object);
        artists.add(object);
    }

    @Override
    public void clear() {
        super.clear();
        artists = new ArrayList<ParcelableArtist>();
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.list_items_image_text, null, true);
        ParcelableArtist artist = artists.get(position);
        String artistImage = artist.url;

        TextView txtTitle = (TextView) rowView.findViewById(R.id.TextView_listItemArtist);

        ImageView imageView = (ImageView) rowView.findViewById(R.id.ImageView_listItemArtist);

//        try {
            txtTitle.setText(artist.name.toString());
//        }catch(Exception ex){
//            Log.d("nigga days", "" + (artist.name == null) + (artists == null) + artists.size());
//        }


//        if (artistImages.size() > 0) {
//            Picasso.with(context)
//                    .load(artistImages.get(0).url)
//                    .resize(50, 50)
//                    .centerCrop()
//                    .into(imageView);
//        }

        if (artistImage != null){
            Picasso.with(context)
                    .load(artistImage)
                    .resize(50, 50)
                    .centerCrop()
                    .into(imageView);
        }

        return rowView;
    }
}