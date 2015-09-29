package pt.gngtv.main;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
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
import pt.gngtv.utils.TypeFaceSpan;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by joseaguiar on 25/09/15.
 */
public class MainActivityGNG extends Activity implements MainControllerInterface {
    private WebService<GNGWebService> service;
    private MainController mController = null;

    @Bind(R.id.discountLabel) TextView txtProductDiscount;
    @Bind(R.id.priceLabel) TextView txtProductPrice;
    @Bind(R.id.productDescriptionLabel) TextView txtProductDescription;
    @Bind(R.id.productSwitcher) ImageSwitcher txtPrice;
    @Bind(R.id.first_name) TextSwitcher txtUsername;
    @Bind(R.id.whishlistLabel) TextView txtWhishlist;
    @Bind(R.id.albumCover) ImageView imgAlbumCover;
    @Bind(R.id.playingSongTitle) TextView txtPlayingSongTitle;
    @Bind(R.id.playingSongAlbumTitle) TextView txtPlayingSongAlbumTitle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        service = new WebService<>(((GNGTVApplication) getApplication()).restAdapter.create(GNGWebService.class));

        if (mController == null) {
            mController = new MainController(this);
            mController.loadInfo(Globals.USER_ID, Globals.ACCESS_TOKEN);
            mController.registerFirebaseListener();
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        ButterKnife.unbind(this);
    }

    public WebService getService(){
        return service;
    }

    @Override
    public void setContent(List<Wishlist> data) {
        if(data != null && data.size() > 0) {
            Log.e("First Wishlist Name:", data.get(0).getTitle());
            txtWhishlist.setText(formatWhishlistName(data.get(0).getTitle()));
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
        txtUsername.setText(formatUserName(userInfo.user_name));
    }

    private SpannableStringBuilder formatWhishlistName(String whilistName) {
        SpannableStringBuilder spanName = new SpannableStringBuilder(getString(R.string.whishlist_label, whilistName));

        Typeface light = Typeface.createFromAsset(getAssets(), getString(R.string.roobotoCondensedLight));
        Typeface regular = Typeface.createFromAsset(getAssets(), getString(R.string.roobotoCondensedRegular));
        int welcomeLegnth = getString(R.string.whishlist_label,"").trim().length();
        spanName.setSpan (new TypeFaceSpan("", light), 0, welcomeLegnth, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        spanName.setSpan(new RelativeSizeSpan(0.6f), 0, welcomeLegnth, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        spanName.setSpan(new TypeFaceSpan("", regular), welcomeLegnth + 1, spanName.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        spanName.setSpan(new RelativeSizeSpan(0.8f), welcomeLegnth + 1, spanName.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        return spanName;
    }

    private SpannableStringBuilder formatUserName(String userName) {
        SpannableStringBuilder spanName = new SpannableStringBuilder(getString(R.string.welcome, userName));

        Typeface light = Typeface.createFromAsset(getAssets(), getString(R.string.roobotoCondensedLight));
        Typeface regular = Typeface.createFromAsset(getAssets(), getString(R.string.roobotoCondensedRegular));
        int welcomeLegnth = getString(R.string.welcome,"").trim().length();
        spanName.setSpan (new TypeFaceSpan("", light), 0, welcomeLegnth, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        spanName.setSpan(new RelativeSizeSpan(1.0f), 0, welcomeLegnth, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        spanName.setSpan(new TypeFaceSpan("", regular), welcomeLegnth + 1, spanName.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        spanName.setSpan(new RelativeSizeSpan(1.4f), welcomeLegnth + 1, spanName.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        return spanName;
    }
}
