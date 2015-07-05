package com.example.wlau.spotifystreamer_v2;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import kaaes.spotify.webapi.android.models.Image;

/**
 * Created by wlau on 2015-06-19.
 */
public class ListOfArtistsFragment extends Fragment{

    ArtistList mArtistsAdapter;
    boolean restarted = false; //this variable is used to determine whether this activity has been restarted
    final static String logTag = "good days";
    final static String ARTIST_LISTS = "ARTIST_LISTS";

    public ListOfArtistsFragment(){

    }

    @Override
    public void onSaveInstanceState(Bundle outState) { //overriding this method allows u to save stuff which are currently showing on the screen
        try {
            List<ParcelableArtist> artists = mArtistsAdapter.artists;
            ParcelableArtist[] artistsArray = artists.toArray(new ParcelableArtist[artists.size()]);
            outState.putParcelableArray(ARTIST_LISTS,artistsArray);
        }catch (Exception ex){
            Log.d("goodbye days",ex.toString());
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) { //restoring saved state
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            restarted = true;
            Parcelable[] artistsArray = savedInstanceState.getParcelableArray(ARTIST_LISTS);
            mArtistsAdapter.clear();
            for (int i = 0; i < artistsArray.length; i++) {
                mArtistsAdapter.add((ParcelableArtist) artistsArray[i]);
            }
        }
        Log.d(logTag,"onActivityCreated");

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        final EditText EditText_Artists = (EditText)getActivity().findViewById(R.id.EditText_Artists);

        EditText_Artists.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String artist = EditText_Artists.getText().toString();
//                if (artist.length() > 0) {
//                if (savedInstanceState == null) { //if it is not recreated from orientation change
                    Log.d(logTag,"afterTextChanged");
                if (!restarted){
                    FetchArtistsTask task = new FetchArtistsTask();
                    task.execute(artist);
                }else{
                    restarted = false;
                }
//                }


//                }else{
//                    mArtistsAdapter.clear();
//                }



            }
        });


        mArtistsAdapter = new ArtistList(getActivity());
        ListView ListView_Artists = (ListView)rootView.findViewById(R.id.ListView_Artists);
        ListView_Artists.setAdapter(mArtistsAdapter);




        ListView_Artists.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(),Top10TracksActivity.class);
                String artistId = mArtistsAdapter.artists.get(position).id;
                intent.putExtra(Intent.ACTION_SEND,artistId);
                startActivity(intent);

            }
        });


        return rootView;
    }

    public class FetchArtistsTask extends AsyncTask<String, Void, ArtistsPager> {
        /** The system calls this to perform work in a worker thread and
         * delivers it the parameters given to AsyncTask.execute() */
        protected ArtistsPager doInBackground(String... params) {// run in the background. After everything is done, it will call onPostExecute to update the data
            SpotifyApi api = new SpotifyApi();
            SpotifyService spotify = api.getService();

            if (params[0].length() > 0) {
                try {
                    ArtistsPager artistsPager = spotify.searchArtists(params[0]);

                    return artistsPager;
                } catch (Exception ex) {
                    Log.d(logTag, ex.toString());


                    return null;
                }
            }else{
                return null;
            }



        }

        @Override
        protected void onPostExecute(ArtistsPager artistsPager) {//update the UI in the main thread
            super.onPostExecute(artistsPager);
            //Log.d(logTag,artistsPager.toString());



            mArtistsAdapter.clear();
            //Log.d(logTag,artist.name.toString());
            Context context = getActivity();
            CharSequence text = "nothing found";
            Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);

            try {
                List<Artist> artists = artistsPager.artists.items;
                if (artists.size() > 0) {
                    for (Artist artist : artists) {
                        //Log.d(logTag,artist.name.toString());
                        String imgUrl = null;
                        if (artist.images.size() > 0){
                            imgUrl = artist.images.get(0).url;
                        }
                        ParcelableArtist pArtist = new ParcelableArtist(artist.id,artist.name,imgUrl);
                        try {
                            mArtistsAdapter.add(pArtist);
                        } catch (Exception ex) {
                            Log.d(logTag, ex.toString());
                        }
                    }
                }else{
                    toast.show();
                }
            }catch (Exception ex){
                //add toast here
                toast.show();
                Log.d(logTag,"latta");
            }






            //mForecastAdapter.notifyDataSetChanged();

        }
    }
}


