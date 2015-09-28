package pt.gngtv.main.controller;

import java.util.List;

import pt.gngtv.main.MainActivity;
import pt.gngtv.main.service.GNGWebService;
import pt.gngtv.model.BaseModel;
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

    private MainActivity mActivity;
    private MainControllerInterface mCallback;

    public MainController(MainActivity activity) {
        this.mActivity = activity;
        this.mCallback = activity;
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
        }
        else {
            // TODO: deal with it
        }
    }
}
