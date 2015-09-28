package pt.gngtv.main.controller;

import java.util.List;

import pt.gngtv.main.MainActivity;
import pt.gngtv.main.service.GNGWebService;
import pt.gngtv.model.BaseModel;
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
    public void loadInfo(String user_id, String access_token) {

        if (access_token != null) {
            ((GNGWebService) mActivity.getService().getService()).getWishlists(null, Utils.getLanguage(), user_id, access_token, "cover,sociable", new Callback<BaseModel<List<Wishlist>>>() {

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
}
