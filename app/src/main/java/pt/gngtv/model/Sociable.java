package pt.gngtv.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by filiperodrigues on 28/09/15.
 */
public class Sociable implements Parcelable {

    private List<Media> media;

    @SerializedName("share_text")
    private String shareText;

    @SerializedName("share_url")
    private String shareUrl;

    @SerializedName("share_title")
    private String shareTitle;

    @SerializedName("share_caption")
    private String shareCaption;

    public List<Media> getMedia() {
        return media;
    }

    public void setMedia(List<Media> media) {
        this.media = media;
    }

    public String getShareText() {
        return shareText;
    }

    public void setShareText(String shareText) {
        this.shareText = shareText;
    }

    public String getShareTitle() {
        return shareTitle;
    }

    public void setShareTitle(String shareTitle) {
        this.shareTitle = shareTitle;
    }

    public String getShareCaption() {
        return shareCaption;
    }

    public void setShareCaption(String shareCaption) {
        this.shareCaption = shareCaption;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(media);
        dest.writeString(this.shareText);
        dest.writeString(this.shareUrl);
        dest.writeString(this.shareTitle);
        dest.writeString(this.shareCaption);
    }

    public Sociable() {
    }

    private Sociable(Parcel in) {
        in.readTypedList(media, Media.CREATOR);
        this.shareText = in.readString();
        this.shareUrl = in.readString();
        this.shareTitle = in.readString();
        this.shareCaption = in.readString();
    }

    public static final Parcelable.Creator<Sociable> CREATOR = new Parcelable.Creator<Sociable>() {
        public Sociable createFromParcel(Parcel source) {
            return new Sociable(source);
        }

        public Sociable[] newArray(int size) {
            return new Sociable[size];
        }
    };
}
