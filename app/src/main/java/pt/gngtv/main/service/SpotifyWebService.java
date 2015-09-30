package pt.gngtv.main.service;

import java.util.List;

import pt.gngtv.model.SpotifyArtistItem;
import pt.gngtv.model.SpotifyArtistsModel;
import pt.gngtv.model.SpotifyItemsModel;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by joseaguiar on 30/09/15.
 */
public interface SpotifyWebService {

    @GET("/search")
    void searchArtist(@Query("q") String search, @Query("type") String type, Callback<SpotifyArtistsModel<SpotifyItemsModel<List<SpotifyArtistItem>>>> cb);

   /* @GET("/artists/{id}")
    void getArtistTopTracks(@Path("id") String id, @Query("country") String country)*/
}
