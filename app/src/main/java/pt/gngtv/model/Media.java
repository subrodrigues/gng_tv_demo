package pt.gngtv.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by filiperodrigues on 28/09/15.
 */
public class Media implements Parcelable {

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int id;
    private String modified;
    private String image;
    private String thumb;
    private Mime mime;
    private String main_color;
    private int type;
    private String orientation;

    private Sociable sociable = null;

    private String url = null;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;

    public int getFeatureLevel() {
        return feature_level;
    }

    public void setFeatureLevel(int feature_level) {
        this.feature_level = feature_level;
    }

    private int feature_level;

    public Media(){}

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public Mime getMime() {
        return mime;
    }

    public void setMime(Mime mime) {
        this.mime = mime;
    }

    public String getMainColor() {
        return main_color;
    }

    public void setMain_color(String main_color) {
        this.main_color = main_color;
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
        dest.writeString(this.modified);
        dest.writeString(this.image);
        dest.writeString(this.url);
        dest.writeString(this.thumb);
        dest.writeParcelable(this.mime, 0);
        dest.writeString(this.main_color);
    }

    private Media(Parcel in) {
        this.modified = in.readString();
        this.image = in.readString();
        this.url = in.readString();
        this.thumb = in.readString();
        this.mime = in.readParcelable(Mime.class.getClassLoader());
        this.main_color = in.readString();
    }

    public static final Parcelable.Creator<Media> CREATOR = new Parcelable.Creator<Media>() {
        public Media createFromParcel(Parcel source) {
            return new Media(source);
        }

        public Media[] newArray(int size) {
            return new Media[size];
        }
    };

    public boolean isAlreadyInAsymmetricGrid() {
        return alreadyInAsymmetricGrid;
    }

    public void setAlreadyInAsymmetricGrid(boolean alreadyInAsymmetricGrid) {
        this.alreadyInAsymmetricGrid = alreadyInAsymmetricGrid;
    }

    private boolean alreadyInAsymmetricGrid = false; // flag used with asymmetric grid algorithm

    public int getType() {
        return type;
    }
}
