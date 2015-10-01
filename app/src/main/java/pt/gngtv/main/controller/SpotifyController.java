package pt.gngtv.main.controller;

import android.util.Log;

import java.util.List;

import pt.gngtv.GNGTVApplication;
import pt.gngtv.main.service.SpotifyWebService;
import pt.gngtv.main.service.WebService;
import pt.gngtv.main.spotify.SpotifyBaseActivity;
import pt.gngtv.model.SpotifyArtist;
import pt.gngtv.model.SpotifyArtistsModel;
import pt.gngtv.model.SpotifyItemsModel;
import pt.gngtv.model.SpotifyTrack;
import pt.gngtv.model.SpotifyTracksModel;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by joseaguiar on 30/09/15.
 */
public class SpotifyController {

    private SpotifyBaseActivity mActivity;
    private SpotifyControllerInterface mCallback;
    private WebService<SpotifyWebService> spotifyService;

    public SpotifyController(SpotifyBaseActivity activity){
        this.mActivity = activity;
        this.mCallback = activity;
        this.spotifyService = new WebService<>(((GNGTVApplication) mActivity.getApplication()).spotifyAdapter.create(SpotifyWebService.class));
    }

    public void getTopSongForArtist(String artistName) {

        spotifyService.getService().searchArtist(artistName, "artist", new Callback<SpotifyArtistsModel<SpotifyItemsModel<List<SpotifyArtist>>>>() {

            @Override
            public void success(SpotifyArtistsModel<SpotifyItemsModel<List<SpotifyArtist>>> cb, Response response) {

                if (mActivity != null && !mActivity.isFinishing() && mCallback != null) {
                   // mCallback.setArtistList(spotifyArtistsModelSpotifyArtistsModel.getArtists().getItems());

                    if(cb != null && cb.getArtists() != null && cb.getArtists().getItems() != null && cb.getArtists().getItems().size() > 0){
                        getArtistTopTracks(cb.getArtists().getItems().get(0));
                    }
                    else{
                        Log.e("Spotify", "getTopSongForArtist error");
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("Spotify", "getTopSongForArtist RetrofitError: " + error.toString());
            }
        });
    }

    public void getArtistTopTracks(SpotifyArtist artist){

        spotifyService.getService().getArtistTopTracks(artist.getId(), "PT", new Callback<SpotifyTracksModel<List<SpotifyTrack>>>() {

            @Override
            public void success(SpotifyTracksModel<List<SpotifyTrack>> listSpotifyTracksModel, Response response) {
                int x = 1;
            }

            @Override
            public void failure(RetrofitError error) {
                int x = 1;
            }
        });
    }

}
