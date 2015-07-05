package com.example.wlau.spotifystreamer_v2;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by wlau on 2015-06-26.
 */

public class ParcelableTrack implements Parcelable {
    String trackName;
    String albumName;
    String albumArtUrl;
    int albumArtWidth;
    int albumArtHeight;
    String previewUrl;

    public ParcelableTrack(String trackName,String albumName,String albumUrl,int albumArtWidth, int albumArtHeight,String previewUrl){
        this.trackName = trackName;
        this.albumName = albumName;
        this.albumArtUrl = albumUrl;
        this.albumArtWidth = albumArtWidth;
        this.albumArtHeight = albumArtHeight;
        this.previewUrl = previewUrl;
    }
    public ParcelableTrack(Parcel in){
        String[] fields = new String[4];
        int[] albumArtDims = new int[2];
        in.readStringArray(fields);
        in.readIntArray(albumArtDims);
        trackName = fields[0];
        albumName = fields[1];
        albumArtUrl = fields[2];
        previewUrl = fields[3];
        albumArtWidth = albumArtDims[0];
        albumArtHeight = albumArtDims[1];

    }
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStringArray(new String[]{trackName,albumName,albumArtUrl,previewUrl});
        parcel.writeIntArray(new int[]{albumArtWidth,albumArtHeight});
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public ParcelableTrack createFromParcel(Parcel in) {
            return new ParcelableTrack(in);
        }

        public ParcelableTrack[] newArray(int size) {
            return new ParcelableTrack[size];
        }
    };

}
