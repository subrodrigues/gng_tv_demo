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

}
