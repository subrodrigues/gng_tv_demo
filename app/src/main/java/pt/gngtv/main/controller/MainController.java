package pt.gngtv.main.controller;

import android.util.Log;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.FirebaseException;
import com.firebase.client.ValueEventListener;

import java.util.List;
import java.util.Random;

import pt.gngtv.main.MainActivityGNG;
import pt.gngtv.main.service.GNGWebService;
import pt.gngtv.main.spotify.SpotifyControllerInterface;
import pt.gngtv.model.BaseModel;
import pt.gngtv.model.GNGFirebaseModel;
import pt.gngtv.model.Model;
import pt.gngtv.model.Wishlist;
import pt.gngtv.utils.Utils;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by filiperodrigues on 28/09/15.
 */
public class MainController {

    private MainActivityGNG mActivity;
    private MainControllerInterface mCallback;
    private SpotifyControllerInterface mSpotifyCallback;
    private Firebase gngFirebaseRoot;

    public MainController(MainActivityGNG activity) {
        this.mActivity = activity;
        this.mCallback = activity;
        this.mSpotifyCallback = activity;
        Firebase.setAndroidContext(activity.getApplicationContext());
        gngFirebaseRoot = new Firebase("https://gngdemo.firebaseio.com/");
    }

    // TODO: implement cache
    public void loadInfo(String userId, String accessToken) {

        if (accessToken != null) {
            ((GNGWebService) mActivity.getService().getService()).getWishlists(null, Utils.getLanguage(), userId, accessToken, "cover,sociable", new Callback<BaseModel<List<Wishlist>>>() {

                @Override
                public void success(BaseModel<List<Wishlist>> baseModels, Response response) {

                    if (mActivity != null && !mActivity.isFinishing() && mCallback != null) {
                        mCallback.setContent(baseModels.getData());
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    //   mCallback.processError(error);
                    // TODO: deal with it
                }
            });
        } else {
            // TODO: deal with it
        }
    }

    public void loadWishlistModels(String wishlistId, String accessToken) {

        if (accessToken != null) {
            ((GNGWebService) mActivity.getService().getService()).getWishlistModels(null, Utils.getLanguage(), wishlistId, accessToken, "cover", new Callback<BaseModel<List<Model>>>() {

                @Override
                public void success(BaseModel<List<Model>> baseModels, Response response) {

                    if (mActivity != null && !mActivity.isFinishing() && mCallback != null)
                        mCallback.setModelsContent(baseModels.getData());
                }

                @Override
                public void failure(RetrofitError error) {
                    // TODO: deal with it
                }
            });
        } else {
            // TODO: deal with it
        }
    }

    public void registerFirebaseListener() {
        Log.i("GNGTV", "Data changed");
        gngFirebaseRoot.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Log.i("GNGTV", "Data changed");
                try {
                    GNGFirebaseModel userInfo = snapshot.getValue(GNGFirebaseModel.class);
                    Log.i("GNGTV", "There is a user: " + Boolean.toString(userInfo != null));
                    if (userInfo != null) {
                        mCallback.setUserInfo(userInfo);
                        //TODO lets make the search
                        getSpotifyMusicURI(userInfo.artists_names, userInfo.favorite_genre);
                    }
                    //else
                    //mCallback.error???
                } catch (FirebaseException fe) {
                    Log.e("GNGTV", "Error while parsing firebase snapshot.", fe);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.i("GNGTV", "Error:" + firebaseError.getMessage());
            }
        });
    }

    private String getSpotifyMusicURI(String artists_names, String genre) {
        String[] artists = artists_names.split(";_;");

        if(artists.length > 0){
            Random r = new Random();
            int bandIdx = r.nextInt(artists.length);

            String artistSongURI = mSpotifyCallback.getTopSongForArtist(artists[bandIdx]);
            if(artistSongURI != null){
                return artistSongURI;
            }
        }
        //TODO search for genre
        return mSpotifyCallback.getTopSongForArtist(genre);

    }


}
