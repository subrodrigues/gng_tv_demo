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

import java.util.List;
import java.util.Locale;

import pt.gngtv.main.controller.SpotifyControllerInterface;
import pt.gngtv.model.SpotifyArtistItem;

/**
 * Created by jcalado on 29/09/15.
 */
public abstract class SpotifyBaseActivity extends Activity implements PlayerNotificationCallback, ConnectionStateCallback, SpotifyControllerInterface{

    @SuppressWarnings("SpellCheckingInspection")
    private static final String CLIENT_ID = "b98bea9caabf47d59dd2245e1ef764f6";
    // TODO: Replace with your redirect URI
    private static final String REDIRECT_URI = "pt-spotifydemo://callback";
    @SuppressWarnings("SpellCheckingInspection")
    private static final String TEST_SONG_URI = "spotify:track:6JEK0CvvjDjjMUBFoXShNZ";

    /**
     * Request code that will be passed together with authentication result to the onAuthenticationResult
     */
    private static final int REQUEST_CODE = 1337;
    private Player mPlayer;
    private BroadcastReceiver mNetworkStateReceiver;
    private PlayerState mCurrentPlayerState;

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
                    updateButtons();
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

    private void updateButtons() {
        boolean loggedIn = isLoggedIn();
        /*
        // Login button should be the inverse of the logged in state
        Button loginButton = (Button) findViewById(R.id.login_button);
        loginButton.setText(loggedIn ? R.string.logout_button_label : R.string.login_button_label);

        // Set enabled for all widgets which depend on initialized state
        for (int id : REQUIRES_INITIALIZED_STATE) {
            findViewById(id).setEnabled(loggedIn);
        }

        // Same goes for the playing state
        boolean playing = loggedIn && mCurrentPlayerState.playing;
        for (int id : REQUIRES_PLAYING_STATE) {
            findViewById(id).setEnabled(playing);
        }
        */
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
        updateButtons();
    }

    @Override
    public void onLoggedOut() {
        logStatus("Logout complete");
        updateButtons();
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
        updateButtons();
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


    @Override
    public void setArtistList(List<SpotifyArtistItem> artists) {

        if(artists != null && artists.size() > 0){
            Log.e("Spotify", artists.toString());

            SpotifyArtistItem item = artists.get(0);

        }
        else{
            Log.e("Spotify", "Pesquisa sem resultados");
        }
    }

}
