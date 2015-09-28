package pt.gngtv.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by filiperodrigues on 28/09/15.
 */
public class Color implements Parcelable {

    private int id;

    private String hex;

    private String name;

    private String code;

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

    public String getHex() {
        return hex;
    }

    public void setHex(String hex) {
        this.hex = hex;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return id == ((Color) o).id;
    }

    @Override
    public int hashCode() {
        return id;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.hex);
        dest.writeString(this.name);
        dest.writeString(this.code);
    }

    public Color() {
    }

    private Color(Parcel in) {
        this.id = in.readInt();
        this.hex = in.readString();
        this.name = in.readString();
        this.code = in.readString();
    }

    public static final Parcelable.Creator<Color> CREATOR = new Parcelable.Creator<Color>() {
        public Color createFromParcel(Parcel source) {
            return new Color(source);
        }

        public Color[] newArray(int size) {
            return new Color[size];
        }
    };
}
