package pt.gngtv.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by filiperodrigues on 28/09/15.
 */
public class Article implements Parcelable {

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int id;
    private Color color;
    private String size;
    @SerializedName("leg_size")
    private String legSize;
    private long ean;
    private long sku;
    private String ref;
    private Model model;

    private List<Media> media;

    public List<Media> getMedia() {
        return media;
    }

    public void setMedia(List<Media> media) {
        this.media = media;
    }

    public Media getFirstMedia() {
        if(media != null && media.size() > 0)
            return media.get(0);

        return null;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getLegSize() {
        return legSize;
    }

    public void setLegSize(String legSize) {
        this.legSize = legSize;
    }

    public long getEan() {
        return ean;
    }

    public void setEan(long ean) {
        this.ean = ean;
    }

    public long getSku() {
        return sku;
    }

    public void setSku(long sku) {
        this.sku = sku;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeParcelable(this.color, 0);
        dest.writeString(this.size);
        dest.writeString(this.legSize);
        dest.writeLong(this.ean);
        dest.writeLong(this.sku);
        dest.writeString(this.ref);
        dest.writeParcelable(this.model, 0);
        dest.writeTypedList(media);
    }

    public Article() {
    }

    private Article(Parcel in) {
        this.id = in.readInt();
        this.color = in.readParcelable(Color.class.getClassLoader());
        this.size = in.readString();
        this.legSize = in.readString();
        this.ean = in.readLong();
        this.sku = in.readLong();
        this.ref = in.readString();
        this.model = in.readParcelable(Model.class.getClassLoader());
        in.readTypedList(media, Media.CREATOR);
    }

    public static final Parcelable.Creator<Article> CREATOR = new Creator<Article>() {
        public Article createFromParcel(Parcel source) {
            return new Article(source);
        }

        public Article[] newArray(int size) {
            return new Article[size];
        }
    };
}
