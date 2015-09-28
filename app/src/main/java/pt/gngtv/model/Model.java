package pt.gngtv.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by filiperodrigues on 28/09/15.
 */
public class Model implements Parcelable {

    private String id;

    @SerializedName("is_active")
    private boolean isActive;

    private ModelCategory category;

    private int gender;

    private String name;

    private Cover cover;

    private float price;

    private String ref;

    private String cares;

    private String description;

    private String composition;

    private ModelStyle style;

    private List<Article> articles;

    @SerializedName("default_article")
    private Article defaultArticle;

    public Sociable sociable;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public ModelCategory getCategory() {
        return category;
    }

    public void setCategory(ModelCategory category) {
        this.category = category;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Cover getCover() {
        return cover;
    }

    public void setCover(Cover cover) {
        this.cover = cover;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public ModelStyle getStyle() {
        return style;
    }

    public void setStyle(ModelStyle style) {
        this.style = style;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }

    public Article getDefaultArticle() {
        return defaultArticle;
    }

    public void setDefaultArticle(Article defaultArticle) {
        this.defaultArticle = defaultArticle;
    }

    public String getCares() {
        return cares;
    }

    public void setCares(String cares) {
        this.cares = cares;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getComposition() {
        return composition;
    }

    public void setComposition(String composition) {
        this.composition = composition;
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
        dest.writeString(this.id);
        dest.writeByte(isActive ? (byte) 1 : (byte) 0);
        dest.writeParcelable(this.category, flags);
        dest.writeInt(this.gender);
        dest.writeString(this.name);
        dest.writeParcelable(this.cover, 0);
        dest.writeFloat(this.price);
        dest.writeString(this.ref);
        dest.writeString(this.cares);
        dest.writeString(this.description);
        dest.writeString(this.composition);
        dest.writeParcelable(this.style, flags);
        dest.writeTypedList(articles);
        dest.writeParcelable(this.defaultArticle, 0);
        dest.writeParcelable(this.sociable, flags);
    }

    public Model() {
    }

    private Model(Parcel in) {
        this.id = in.readString();
        this.isActive = in.readByte() != 0;
        this.category = in.readParcelable(ModelCategory.class.getClassLoader());
        this.gender = in.readInt();
        this.name = in.readString();
        this.cover = in.readParcelable(Cover.class.getClassLoader());
        this.price = in.readFloat();
        this.ref = in.readString();
        this.cares = in.readString();
        this.description = in.readString();
        this.composition = in.readString();
        this.style = in.readParcelable(ModelStyle.class.getClassLoader());
        in.readTypedList(articles, Article.CREATOR);
        this.defaultArticle = in.readParcelable(Article.class.getClassLoader());
        this.sociable = in.readParcelable(Sociable.class.getClassLoader());
    }

    public static final Parcelable.Creator<Model> CREATOR = new Creator<Model>() {
        public Model createFromParcel(Parcel source) {
            return new Model(source);
        }

        public Model[] newArray(int size) {
            return new Model[size];
        }
    };

}
