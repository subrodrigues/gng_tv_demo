package pt.gngtv.main.controller;

import java.util.List;

import pt.gngtv.GNGTVApplication;
import pt.gngtv.main.service.SpotifyWebService;
import pt.gngtv.main.service.WebService;
import pt.gngtv.main.spotify.SpotifyBaseActivity;
import pt.gngtv.model.SpotifyArtistItem;
import pt.gngtv.model.SpotifyArtistsModel;
import pt.gngtv.model.SpotifyItemsModel;
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

        spotifyService.getService().searchArtist(artistName, "artist", new Callback<SpotifyArtistsModel<SpotifyItemsModel<List<SpotifyArtistItem>>>>() {

            @Override
            public void success(SpotifyArtistsModel<SpotifyItemsModel<List<SpotifyArtistItem>>> spotifyArtistsModelSpotifyArtistsModel, Response response) {

                if (mActivity != null && !mActivity.isFinishing() && mCallback != null) {
                   // mCallback.setArtistList(spotifyArtistsModelSpotifyArtistsModel.getArtists().getItems());
                }
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });

    }

}
