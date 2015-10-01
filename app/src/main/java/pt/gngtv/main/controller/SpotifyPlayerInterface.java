package pt.gngtv.main.controller;

import pt.gngtv.model.SpotifyTrack;

/**
 * Created by joseaguiar on 01/10/15.
 */
public interface SpotifyPlayerInterface {

    void setCurrentPlayingTrack(SpotifyTrack track);

    void stop();

    void play(SpotifyTrack track);
}
