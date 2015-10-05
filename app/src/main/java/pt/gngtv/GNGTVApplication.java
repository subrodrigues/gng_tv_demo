package pt.gngtv;

import android.app.Application;
import android.content.Context;

import com.facebook.stetho.okhttp.StethoInterceptor;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;

import java.net.CookieHandler;
import java.util.concurrent.TimeUnit;

import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by filiperodrigues on 28/09/15.
 */
public class GNGTVApplication extends Application {

    public RestAdapter restAdapter;
    public RestAdapter spotifyAdapter;

    @Override
    public void onCreate() {
        super.onCreate();
        OkHttpClient okHttpClient = new OkHttpClient();

        okHttpClient.setCookieHandler(CookieHandler.getDefault());
        okHttpClient.setConnectTimeout(Globals.CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS);
        okHttpClient.setReadTimeout(Globals.SOCKET_TIMEOUT, TimeUnit.MILLISECONDS);

        if (BuildConfig.DEBUG)
            okHttpClient.networkInterceptors().add(new StethoInterceptor());

        Gson gson = new GsonBuilder()
                .setExclusionStrategies(new ExclusionStrategy() {
                    @Override
                    public boolean shouldSkipField(FieldAttributes f) {
//                        return f.getDeclaringClass().equals(RealmObject.class);
                        return false;
                    }

                    @Override
                    public boolean shouldSkipClass(Class<?> clazz) {
                        return false;
                    }
                })
                .create();

        restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setClient(new OkClient(okHttpClient))
                .setConverter(new GsonConverter(gson))
                .setEndpoint(Globals.HOST_NAME)
                .build();

        spotifyAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setClient(new OkClient(okHttpClient))
                .setConverter(new GsonConverter(gson))
                .setEndpoint(Globals.SPOTIFY_HOST_NAME)
                .build();

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/Roboto-RobotoRegular.ttf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

}
