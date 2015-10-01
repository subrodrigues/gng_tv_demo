package pt.gngtv.model;

/**
 * Created by joseaguiar on 30/09/15.
 */
public class SpotifyArtist {

    private String id;
    private String name;
    private int popularity;
    private String type;
    private String uri;
    private String href;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPopularity() {
        return popularity;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
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

    @Override
    public String toString() {
        return "SpotifyArtistItem{" +
                "name='" + name + '\'' +
                '}';
    }
}
