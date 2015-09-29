package pt.gngtv.main;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import pt.gngtv.GNGTVApplication;
import pt.gngtv.Globals;
import pt.gngtv.R;
import pt.gngtv.main.controller.MainController;
import pt.gngtv.main.controller.MainControllerInterface;
import pt.gngtv.main.service.GNGWebService;
import pt.gngtv.main.service.WebService;
import pt.gngtv.model.GNGFirebaseModel;
import pt.gngtv.model.Model;
import pt.gngtv.model.Wishlist;

/**
 * Created by joseaguiar on 25/09/15.
 */
public class MainActivityGNG extends Activity implements MainControllerInterface {
    private WebService<GNGWebService> service;
    private MainController mController = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        service = new WebService<>(((GNGTVApplication) getApplication()).restAdapter.create(GNGWebService.class));

        if (mController == null) {
            mController = new MainController(this);
            mController.loadInfo(Globals.USER_ID, Globals.ACCESS_TOKEN);
            mController.w();
        }
    }


    public WebService getService(){
        return service;
    }

    @Override
    public void setContent(List<Wishlist> data) {
        if(data != null && data.size() > 0) {
            Log.e("First Wishlist Name:", data.get(0).getTitle());
            mController.loadWishlistModels(data.get(0).getId(), Globals.ACCESS_TOKEN);
        }
    }

    @Override
    public void setModelsContent(List<Model> data) {
        if(data != null && data.size() > 0) {
            Log.e("First Model Name:", data.get(0).getName());
        }
    }

    @Override
    public void setUserInfo(GNGFirebaseModel userInfo) {
        Toast.makeText(this, "user: " + userInfo.user_name, Toast.LENGTH_LONG).show();
    }
}
