package pt.gngtv.main.service;

import java.util.List;

import pt.gngtv.model.SpotifyArtist;
import pt.gngtv.model.SpotifyArtistsModel;
import pt.gngtv.model.SpotifyItemsModel;
import pt.gngtv.model.SpotifyTrack;
import pt.gngtv.model.SpotifyTracksModel;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by joseaguiar on 30/09/15.
 */
public interface SpotifyWebService {

    @GET("/search")
    void searchArtist(@Query("q") String search, @Query("type") String type, Callback<SpotifyArtistsModel<SpotifyItemsModel<List<SpotifyArtist>>>> cb);

    @GET("/artists/{id}/top-tracks")
    void getArtistTopTracks(@Path("id") String id, @Query("country") String country, Callback<SpotifyTracksModel<List<SpotifyTrack>>> cb);
}
