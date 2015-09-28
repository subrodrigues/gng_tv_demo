package pt.gngtv.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by filiperodrigues on 28/09/15.
 */
public class Mime implements Parcelable {

    private String id;
    private String identifier;

    public Mime() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.identifier);
    }

    private Mime(Parcel in) {
        this.id = in.readString();
        this.identifier = in.readString();
    }

    public static final Parcelable.Creator<Mime> CREATOR = new Parcelable.Creator<Mime>() {
        public Mime createFromParcel(Parcel source) {
            return new Mime(source);
        }

        public Mime[] newArray(int size) {
            return new Mime[size];
        }
    };
}
