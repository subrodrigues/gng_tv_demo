package pt.gngtv.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by filiperodrigues on 28/09/15.
 */
public class ModelStyle implements Parcelable {

    private int id;

    private ModelStyleBrand brand;

    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ModelStyleBrand getBrand() {
        return brand;
    }

    public void setBrand(ModelStyleBrand brand) {
        this.brand = brand;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeParcelable(this.brand, flags);
        dest.writeString(this.name);
    }

    public ModelStyle() {
    }

    private ModelStyle(Parcel in) {
        this.id = in.readInt();
        this.brand = in.readParcelable(ModelStyleBrand.class.getClassLoader());
        this.name = in.readString();
    }

    public static final Parcelable.Creator<ModelStyle> CREATOR = new Parcelable.Creator<ModelStyle>() {
        public ModelStyle createFromParcel(Parcel source) {
            return new ModelStyle(source);
        }

        public ModelStyle[] newArray(int size) {
            return new ModelStyle[size];
        }
    };
}
