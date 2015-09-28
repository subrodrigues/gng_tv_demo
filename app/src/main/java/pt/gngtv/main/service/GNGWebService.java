package pt.gngtv.main.service;

import java.util.List;

import pt.gngtv.model.BaseModel;
import pt.gngtv.model.Model;
import pt.gngtv.model.Wishlist;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by filiperodrigues on 28/09/15.
 */
public interface GNGWebService {

    @GET("/v1/users/{id}/wishlists")
    void getWishlists(@Query("scale") String scale, @Query("locale") String locale, @Path("id") String id, @Query("access_token") String accessToken, @Query("fields") String fields, Callback<BaseModel<List<Wishlist>>> cb);

    @GET("/v1/wishlists/{id}/models")
    void getWishlistModels(@Query("scale") String scale, @Query("locale") String locale, @Path("id") String id, @Query("access_token") String accessToken, @Query("fields") String fields, Callback<BaseModel<List<Model>>> cb);

}
