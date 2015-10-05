package pt.gngtv.main.controller;

import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.FirebaseException;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import pt.gngtv.R;
import pt.gngtv.main.MainActivityGNG;
import pt.gngtv.main.service.GNGWebService;
import pt.gngtv.model.BaseModel;
import pt.gngtv.model.GNGFirebaseModel;
import pt.gngtv.model.Model;
import pt.gngtv.model.SpotifyTrack;
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
    private SpotifyController mController;
    private Firebase gngFirebaseRoot;
    private GNGFirebaseModel userInfo;

    public MainController(MainActivityGNG activity) {
        this.mActivity = activity;
        this.mCallback = activity;
        this.mController = new SpotifyController(activity);
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
                    userInfo = snapshot.getValue(GNGFirebaseModel.class);
                    Log.i("GNGTV", "There is a user: " + Boolean.toString(userInfo != null));
                    if (userInfo != null) {
                        mCallback.setUserInfo(userInfo);
                        //TODO lets make the search

                        if(!TextUtils.isEmpty(userInfo.artists_names))
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

    private void getSpotifyMusicURI(String artists_names, String genre) {

        final String[] artists = artists_names.split(";_;");
        final List<Integer> selectedIndex = new ArrayList<>();

        SpotifyControllerInterface spotifyInterface = new SpotifyControllerInterface() {


            @Override
            public void setTracks(List<SpotifyTrack> tracks) {

            }

            @Override
            public void spotifyError() {

            }

            @Override
            public void searchNoResuls() {

                if(artists != null && selectedIndex.size() < artists.length) {

                    Random r = new Random();
                    int bandIdx = r.nextInt(artists.length);

                    while(selectedIndex.contains(bandIdx)) {
                        bandIdx = r.nextInt(artists.length);
                    }

                    selectedIndex.add(bandIdx);
                    mController.getTopSongForArtist(artists[bandIdx], this);
                }

                else{
                    if(mActivity != null && !mActivity.isFinishing())
                        Toast.makeText(mActivity, mActivity.getString(R.string.no_artist_found_for_spotify), Toast.LENGTH_SHORT).show();
                }
            }
        };


        if(artists.length > 0){
            Random r = new Random();
            int bandIdx = r.nextInt(artists.length);
            selectedIndex.add(bandIdx);
            Log.i("Spotify", "getSpotifyMusicURI: " + artists[bandIdx]);
            mController.getTopSongForArtist(artists[bandIdx], spotifyInterface);
        }
    }

    public void loadSpotifyData(){

        if(userInfo != null)
            getSpotifyMusicURI(userInfo.artists_names, userInfo.favorite_genre);
    }


}
