package pt.gngtv.main.controller;

import java.util.List;

import pt.gngtv.model.SpotifyArtistItem;

/**
 * Created by joseaguiar on 30/09/15.
 */
public interface SpotifyControllerInterface {

    void setArtistList(List<SpotifyArtistItem> artists);
}
