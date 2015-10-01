package pt.gngtv.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by joseaguiar on 01/10/15.
 */
public class SpotifyAlbum {

    @SerializedName("album_type")
    private String albumType;
    private String href;
    private String id;
    private List<SpotifyImage> images;
    private String name;
    private String type;
    private String uri;

    public String getAlbumType() {
        return albumType;
    }

    public void setAlbumType(String albumType) {
        this.albumType = albumType;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<SpotifyImage> getImages() {
        return images;
    }

    public void setImages(List<SpotifyImage> images) {
        this.images = images;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public SpotifyImage getAlbumImage(){

       if(images.size() > 0)
           return images.get(0);

        return null;
    }
}
