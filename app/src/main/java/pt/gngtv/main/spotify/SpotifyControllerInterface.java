package pt.gngtv.main.spotify;

/**
 * Created by jcalado on 29/09/15.
 */
public interface SpotifyControllerInterface {
    void playSong(String uri);

    String getTopSongForArtist(String artistName);
}
