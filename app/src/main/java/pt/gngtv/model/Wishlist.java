package pt.gngtv.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by filiperodrigues on 28/09/15.
 */
public class Wishlist implements Parcelable {

    private String title;
    private String id;
    private Media cover;
    private String description;
    private String models_count;
    private Sociable sociable;

    private boolean isSelected = false;

    public Wishlist(){}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Media getCover() {
        return cover;
    }

    public void setCover(Media cover) {
        this.cover = cover;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public String getModels_count() {
        return models_count;
    }

    public void setModels_count(String models_count) {
        this.models_count = models_count;
    }

    public Sociable getSociable() {
        return sociable;
    }

    public void setSociable(Sociable sociable) {
        this.sociable = sociable;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.id);
        dest.writeParcelable(this.cover, 0);
        dest.writeString(this.description);
        dest.writeString(this.models_count);
        dest.writeParcelable(this.sociable, 0);
        dest.writeByte(isSelected ? (byte) 1 : (byte) 0);
    }

    protected Wishlist(Parcel in) {
        this.title = in.readString();
        this.id = in.readString();
        this.cover = in.readParcelable(Media.class.getClassLoader());
        this.description = in.readString();
        this.models_count = in.readString();
        this.sociable = in.readParcelable(Sociable.class.getClassLoader());
        this.isSelected = in.readByte() != 0;
    }

    public static final Parcelable.Creator<Wishlist> CREATOR = new Parcelable.Creator<Wishlist>() {
        public Wishlist createFromParcel(Parcel source) {
            return new Wishlist(source);
        }

        public Wishlist[] newArray(int size) {
            return new Wishlist[size];
        }
    };
}
