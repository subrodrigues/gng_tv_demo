package pt.gngtv.model;

/**
 * Created by filiperodrigues on 28/09/15.
 */
public class SpotifyTracksModel<T> {

    private T tracks;

    public T getTracks() {
        return tracks;
    }

    public void setTracks(T tracks) {
        this.tracks = tracks;
    }
}
