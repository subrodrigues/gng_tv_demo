package pt.gngtv.main.spotify;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;

import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Connectivity;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerNotificationCallback;
import com.spotify.sdk.android.player.PlayerState;
import com.spotify.sdk.android.player.Spotify;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import pt.gngtv.main.MainActivityGNG;
import pt.gngtv.main.controller.MainController;
import pt.gngtv.main.controller.SpotifyControllerInterface;
import pt.gngtv.main.controller.SpotifyPlayerInterface;
import pt.gngtv.model.SpotifyArtist;
import pt.gngtv.model.SpotifyTrack;
import pt.gngtv.model.SpotifyTracksModel;

/**
 * Created by jcalado on 29/09/15.
 */
public abstract class SpotifyBaseActivity extends Activity implements PlayerNotificationCallback, ConnectionStateCallback, SpotifyControllerInterface{

    @SuppressWarnings("SpellCheckingInspection")
    private static final String CLIENT_ID = "b98bea9caabf47d59dd2245e1ef764f6";
    // TODO: Replace with your redirect URI
    private static final String REDIRECT_URI = "pt-spotifydemo://callback";


    /**
     * Request code that will be passed together with authentication result to the onAuthenticationResult
     */
    private static final int REQUEST_CODE = 1337;
    private Player mPlayer;
    private BroadcastReceiver mNetworkStateReceiver;
    private PlayerState mCurrentPlayerState;
    private List<SpotifyTrack> spotifyTracks;
    private int currentTrack = -1;

    private SpotifyPlayerInterface mCallback;
    private MainActivityGNG mainActivity;

    protected void setActivityInterface(MainActivityGNG mainActivity, SpotifyPlayerInterface mCallback){
        this.mainActivity = mainActivity;
        this.mCallback = mCallback;
    }

    private void openLoginWindow() {
        final AuthenticationRequest request = new AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI)
                .setScopes(new String[]{"streaming"})
                .build();

        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        openLoginWindow();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);
            switch (response.getType()) {
                // Response was successful and contains auth token
                case TOKEN:
                    onAuthenticationComplete(response);
                    break;

                // Auth flow returned an error
                case ERROR:
                    logStatus("Auth error: " + response.getError());
                    break;

                // Most likely auth flow was cancelled
                default:
                    logStatus("Auth result: " + response.getType());
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Set up the broadcast receiver for network events. Note that we also unregister
        // this receiver again in onPause().
        mNetworkStateReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (mPlayer != null) {
                    Connectivity connectivity = getNetworkConnectivity(getBaseContext());
                    logStatus("Network state changed: " + connectivity.toString());
                    mPlayer.setConnectivityStatus(connectivity);
                }
            }
        };

        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mNetworkStateReceiver, filter);

        if (mPlayer != null) {
            mPlayer.addPlayerNotificationCallback(SpotifyBaseActivity.this);
            mPlayer.addConnectionStateCallback(SpotifyBaseActivity.this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mNetworkStateReceiver);

        // Note that calling Spotify.destroyPlayer() will also remove any callbacks on whatever
        // instance was passed as the refcounted owner. So in the case of this particular example,
        // it's not strictly necessary to call these methods, however it is generally good practice
        // and also will prevent your application from doing extra work in the background when
        // paused.
        if (mPlayer != null) {
            mPlayer.removePlayerNotificationCallback(SpotifyBaseActivity.this);
            mPlayer.removeConnectionStateCallback(SpotifyBaseActivity.this);
        }
    }

    @Override
    protected void onDestroy() {
        // *** ULTRA-IMPORTANT ***
        // ALWAYS call this in your onDestroy() method, otherwise you will leak native resources!
        // This is an unfortunate necessity due to the different memory management models of
        // Java's garbage collector and C++ RAII.
        // For more information, see the documentation on Spotify.destroyPlayer().
        Spotify.destroyPlayer(this);
        super.onDestroy();
    }

    private void onAuthenticationComplete(AuthenticationResponse authResponse) {
        // Once we have obtained an authorization token, we can proceed with creating a Player.
        logStatus("Got authentication token");
        if (mPlayer == null) {
            Config playerConfig = new Config(getApplicationContext(), authResponse.getAccessToken(), CLIENT_ID);
            // Since the Player is a static singleton owned by the Spotify class, we pass "this" as
            // the second argument in order to refcount it properly. Note that the method
            // Spotify.destroyPlayer() also takes an Object argument, which must be the same as the
            // one passed in here. If you pass different instances to Spotify.getPlayer() and
            // Spotify.destroyPlayer(), that will definitely result in resource leaks.
            mPlayer = Spotify.getPlayer(playerConfig, this, new Player.InitializationObserver() {
                @Override
                public void onInitialized(Player player) {
                    logStatus("-- Player initialized --");
                    mPlayer.setConnectivityStatus(getNetworkConnectivity(SpotifyBaseActivity.this));
                    mPlayer.addPlayerNotificationCallback(SpotifyBaseActivity.this);
                    mPlayer.addConnectionStateCallback(SpotifyBaseActivity.this);
                    // Trigger UI refresh
                    requestUserData();
                 //   updateButtons();
                }

                @Override
                public void onError(Throwable error) {
                    logStatus("Error in initialization: " + error.getMessage());
                }
            });
        } else {
            mPlayer.login(authResponse.getAccessToken());
        }
    }

    private Connectivity getNetworkConnectivity(Context context) {
        ConnectivityManager connectivityManager;
        connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected()) {
            return Connectivity.fromNetworkType(activeNetwork.getType());
        } else {
            return Connectivity.OFFLINE;
        }
    }

    private void requestUserData(){

        if(spotifyTracks != null){
            playSongs(spotifyTracks);
        }
    }

    private void updateButtons(EventType eventType) {

        Log.i("Spotify", "updateButtons: " + eventType.toString());

        if(this.mCallback != null) {

            if(eventType == EventType.BECAME_ACTIVE){
                currentTrack = -1;
            }
            else if (eventType == EventType.TRACK_START) {

                if(currentTrack != -1 && spotifyTracks != null && (currentTrack + 1 <= spotifyTracks.size()))
                    this.mCallback.play(spotifyTracks.get(currentTrack));

            }
            else if (eventType == EventType.TRACK_END || eventType == EventType.PAUSE) {
                this.mCallback.stop();

            }
            else if (eventType == EventType.TRACK_CHANGED) {

                if(mainActivity != null && mainActivity.hasUser())
                    currentTrack++;

                else if(mPlayer != null) {
                    mPlayer.clearQueue();
                    mPlayer.pause();
                }

            }
        }
    }

    private boolean isLoggedIn() {
        return mPlayer != null && mPlayer.isLoggedIn();
    }

    /*
    ##################
    SPOTIFY INTERFACES
    ##################
     */

    @Override
    public void onLoggedIn() {
        logStatus("Login complete");
        updateButtons(EventType.BECAME_ACTIVE);
        requestUserData();
    }

    @Override
    public void onLoggedOut() {
        logStatus("Logout complete");
        updateButtons(EventType.END_OF_CONTEXT);
    }

    @Override
    public void onLoginFailed(Throwable error) {
        logStatus("Login error");
    }

    @Override
    public void onTemporaryError() {
        logStatus("Temporary error occurred");
    }

    @Override
    public void onConnectionMessage(final String message) {
        logStatus("Incoming connection message: " + message);
    }

    @Override
    public void onPlaybackEvent(final EventType eventType, final PlayerState playerState) {
        // Remember kids, always use the English locale when changing case for non-UI strings!
        // Otherwise you'll end up with mysterious errors when running in the Turkish locale.
        // See: http://java.sys-con.com/node/46241
        String eventName = eventType.name().toLowerCase(Locale.ENGLISH).replaceAll("_", " ");
        logStatus("Player event: " + eventName);
        mCurrentPlayerState = playerState;
        updateButtons(eventType);
    }

    @Override
    public void onPlaybackError(ErrorType errorType, String errorDetails) {
        String errorName = errorType.name().toLowerCase(Locale.ENGLISH).replaceAll("_", " ");
        logStatus("Player error: " + errorName);
    }

    private void logStatus(String status) {
        Log.i("Spotify", status);
    }

    public void playSong(String uri) {

        if(mPlayer != null && mPlayer.isInitialized() && mPlayer.isLoggedIn()){
            logStatus("Play song with URI: " + uri);
            mPlayer.play(uri);
        } else {
            if(mPlayer == null){
                logStatus("Player not ready to play a song. Player is null");

            } else {
                logStatus("Player not ready to play a song." + "Initialized:" + mPlayer.isInitialized() + "Initialized:" + mPlayer.isLoggedIn());

            }
        }
    }

    public void playSongs(List<SpotifyTrack> tracks) {

        if(tracks.size() > 5)
            tracks = tracks.subList(0, 4);

        Collections.shuffle(tracks);
        this.spotifyTracks = tracks;

        if(mPlayer != null && mPlayer.isInitialized() && mPlayer.isLoggedIn()){

            List<String> tracksUris = new ArrayList<>();
            for(SpotifyTrack track : tracks)
                tracksUris.add(track.getUri());

            currentTrack = -1;
            mPlayer.play(tracksUris);

            Log.i("Spotify", "playSongs: " + tracks.toString());

           // logStatus("Play songs with URIs: " + tracksUris.toString());
        } else {

            if(mPlayer == null){
                logStatus("Player not ready to play a song. Player is null");

            } else {
                logStatus("Player not ready to play a song." + " Initialized:" + mPlayer.isInitialized() + " LoggedIn:" + mPlayer.isLoggedIn());

            }
        }
    }

    @Override
    public void setTracks(List<SpotifyTrack> tracks) {

        if(tracks != null && tracks.size() > 0)
            playSongs(tracks);
    }

    @Override
    public void spotifyError() {

    }

}
