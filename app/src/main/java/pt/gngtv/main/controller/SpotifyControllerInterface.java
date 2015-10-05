package pt.gngtv.main.controller;

import java.util.List;

import pt.gngtv.model.SpotifyArtist;
import pt.gngtv.model.SpotifyTrack;
import pt.gngtv.model.SpotifyTracksModel;

/**
 * Created by joseaguiar on 30/09/15.
 */
public interface SpotifyControllerInterface {

    void setTracks(List<SpotifyTrack> tracks);

    void spotifyError();

    void searchNoResuls();
}
