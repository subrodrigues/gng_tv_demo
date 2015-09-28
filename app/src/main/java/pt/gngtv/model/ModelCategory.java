package pt.gngtv.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by filiperodrigues on 28/09/15.
 */
public class ModelCategory implements Parcelable {

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

    public ModelCategory() {
    }

    private ModelCategory(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.slug = in.readString();
    }

    public static final Parcelable.Creator<ModelCategory> CREATOR = new Parcelable.Creator<ModelCategory>() {
        public ModelCategory createFromParcel(Parcel source) {
            return new ModelCategory(source);
        }

        public ModelCategory[] newArray(int size) {
            return new ModelCategory[size];
        }
    };
}
