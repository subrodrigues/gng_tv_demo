package pt.gngtv.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by filiperodrigues on 28/09/15.
 */
public class ModelStyleBrand implements Parcelable {

    private int id;

    private String name;

    private String slug;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.slug);
    }

    public ModelStyleBrand() {
    }

    private ModelStyleBrand(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.slug = in.readString();
    }

    public static final Parcelable.Creator<ModelStyleBrand> CREATOR = new Parcelable.Creator<ModelStyleBrand>() {
        public ModelStyleBrand createFromParcel(Parcel source) {
            return new ModelStyleBrand(source);
        }

        public ModelStyleBrand[] newArray(int size) {
            return new ModelStyleBrand[size];
        }
    };
}
