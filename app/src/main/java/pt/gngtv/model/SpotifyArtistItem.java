package pt.gngtv.model;

/**
 * Created by joseaguiar on 30/09/15.
 */
public class SpotifyArtistItem {

    private String id;
    private String name;
    private int populatiry;
    private String type;
    private String uri;

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

    public int getPopulatiry() {
        return populatiry;
    }

    public void setPopulatiry(int populatiry) {
        this.populatiry = populatiry;
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
