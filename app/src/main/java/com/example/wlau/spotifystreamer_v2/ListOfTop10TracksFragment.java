package com.example.wlau.spotifystreamer_v2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;

/**
 * Created by wlau on 2015-06-19.
 */
public class ListOfTop10TracksFragment extends  Fragment{
    TracksList mTracksAdapter;
    final String TRACK_LIST = "TRACK_LIST";
    public ListOfTop10TracksFragment() {
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        try {
            List<ParcelableTrack> tracks = mTracksAdapter.tracks;
            ParcelableTrack[] tracksArray = tracks.toArray(new ParcelableTrack[tracks.size()]);
            outState.putParcelableArray(TRACK_LIST,tracksArray);
        }catch (Exception ex){
            Log.d("op days",ex.toString());
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            Parcelable[] tracksArray = savedInstanceState.getParcelableArray(TRACK_LIST);
            mTracksAdapter.clear();
            for (int i = 0; i < tracksArray.length; i++) {
                mTracksAdapter.add((ParcelableTrack) tracksArray[i]);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_top10_tracks, container, false);

        Intent intent = getActivity().getIntent();
        String artistId = intent.getStringExtra(Intent.ACTION_SEND);

        mTracksAdapter = new TracksList(getActivity());
        ListView ListView_Top10Tracks = (ListView) rootView.findViewById(R.id.ListView_Top10Tracks);
        ListView_Top10Tracks.setAdapter(mTracksAdapter);

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String country = sharedPrefs.getString(
                getString(R.string.pref_country_key),
                getString(R.string.pref_country_default));
        if (savedInstanceState == null) { // if it is not created due to orientation change
            FetchTracksTask task = new FetchTracksTask();
            task.execute(artistId, country.toUpperCase());
        }

        ListView_Top10Tracks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
        return rootView;
    }

    private class FetchTracksTask extends AsyncTask<String, Void, Tracks> {
        /** The system calls this to perform work in a worker thread and
         * delivers it the parameters given to AsyncTask.execute() */
        protected Tracks doInBackground(String... params) {// run in the background. After everything is done, it will call onPostExecute to update the data
            SpotifyApi api = new SpotifyApi();
            SpotifyService spotify = api.getService();
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("country",params[1]);
            Tracks tracks = null;

            try {
                tracks = spotify.getArtistTopTrack(params[0], map);
                Log.d("its just another great day", "success boys");
                return tracks;
            }catch (Exception ex){
                Log.d("its just another great day",ex.toString());
            }


            return null;



        }

        @Override
        protected void onPostExecute(Tracks tracks) {//update the UI in the main thread
            super.onPostExecute(tracks);
            mTracksAdapter.clear();
            //Log.d(logTag,artistsPager.toString());
            try{
                for (Track track : tracks.tracks){
                    try{
                        String imgUrl = null;
                        int[] dims = new int[2];
                        if (track.album.images.size() > 0){
                            imgUrl = track.album.images.get(0).url;
                            dims[0] = track.album.images.get(0).width;
                            dims[1] = track.album.images.get(0).height;
                        }
                        mTracksAdapter.add(new ParcelableTrack(track.name,track.album.name,imgUrl,dims[0],dims[1],track.preview_url));

                    }catch (Exception ex){
                        Log.d("its just another great day",ex.toString());
                    }
                }
            }catch (Exception ex){
                Log.d("its just another great day","lata");
            }



            //mForecastAdapter.notifyDataSetChanged();

        }
    }
}
