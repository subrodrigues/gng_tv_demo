package pt.gngtv.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by filiperodrigues on 28/09/15.
 */
public class Cover implements Parcelable {
    private int id;
    private int type;
    private String image;


    private String thumb;
    private String url;
    private String main_color;
    private String orientation;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.type);
        dest.writeString(this.image);
        dest.writeString(this.thumb);
        dest.writeString(this.url);
        dest.writeString(this.main_color);
        dest.writeString(this.orientation);

    }

    public Cover() {}
    private Cover(Parcel in) {
        this.id = in.readInt();
        this.type = in.readInt();
        this.image = in.readString();
        this.thumb = in.readString();
        this.url = in.readString();
        this.main_color = in.readString();
        this.orientation = in.readString();
    }

    public static final Parcelable.Creator<Cover> CREATOR = new Parcelable.Creator<Cover>() {
        public Cover createFromParcel(Parcel source) {
            return new Cover(source);
        }

        public Cover[] newArray(int size) {
            return new Cover[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMainColor() {
        return main_color;
    }

    public void setMainColor(String main_color) {
        this.main_color = main_color;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

}
