package com.example.wlau.spotifystreamer_v2;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by wlau on 2015-06-26.
 */
public class ParcelableArtist implements Parcelable{
    String id;
    String name;
    String url;
    public ParcelableArtist(String id,String name,String url){
        this.id = id;
        this.name = name;
        this.url = url;
    }
    public ParcelableArtist(Parcel in){
        String[] fields = new String[3];
        in.readStringArray(fields);
        id = fields[0];
        name = fields[1];
        url = fields[2];
    }
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStringArray(new String[]{id,name,url});
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public ParcelableArtist createFromParcel(Parcel in) {
            return new ParcelableArtist(in);
        }

        public ParcelableArtist[] newArray(int size) {
            return new ParcelableArtist[size];
        }
    };

}
