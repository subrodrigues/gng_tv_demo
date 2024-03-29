package pt.gngtv.main;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.nineoldandroids.animation.Animator;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ThreadLocalRandom;

import butterknife.Bind;
import butterknife.ButterKnife;
import pt.gngtv.Animations;
import pt.gngtv.GNGTVApplication;
import pt.gngtv.R;
import pt.gngtv.main.controller.MainController;
import pt.gngtv.main.controller.MainControllerInterface;
import pt.gngtv.main.controller.SpotifyPlayerInterface;
import pt.gngtv.main.service.GNGWebService;
import pt.gngtv.main.service.WebService;
import pt.gngtv.main.spotify.SpotifyBaseActivity;
import pt.gngtv.model.Cover;
import pt.gngtv.model.GNGFirebaseModel;
import pt.gngtv.model.Model;
import pt.gngtv.model.SpotifyTrack;
import pt.gngtv.model.Wishlist;
import pt.gngtv.utils.TypeFaceSpan;
import pt.gngtv.utils.Utils;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by joseaguiar on 25/09/15.
 */
public class MainActivityGNG extends SpotifyBaseActivity implements MainControllerInterface, SpotifyPlayerInterface {

    private WebService<GNGWebService> service;

    private MainController mController = null;
    private CountDownTimer timer;
    private String accessToken;
    private boolean hasUser = false;

    private Timer animationTimer;

    @Bind(R.id.discountLabel) TextView txtProductDiscount;
    @Bind(R.id.priceLabel) TextView txtProductPrice;
    @Bind(R.id.productDescriptionLabel) TextView txtProductDescription;
    @Bind(R.id.productSwitcher) ImageSwitcher imgProductPicture;
   // @Bind(R.id.first_name) TextSwitcher txtUsername;
   // @Bind(R.id.whishlistLabel) TextView txtWhishlist;
    @Bind(R.id.connected_store) TextView txtConnectedStore;
    @Bind(R.id.albumCover) ImageView imgAlbumCover;
    @Bind(R.id.playingSongTitle) TextView txtPlayingSongTitle;
    @Bind(R.id.playingSongAlbumTitle) TextView txtPlayingSongAlbumTitle;
    @Bind(R.id.songInfo) RelativeLayout layoutSongInfo;
   // @Bind(R.id.triangle) View shapeTriangle;;

    @Bind(R.id.priceContainer) RelativeLayout priceContainer;
    @Bind(R.id.playingIcon) ImageView songMusicIcon;
    @Bind(R.id.playingLabel) TextView songPlayingLabel;

    private Animation zoomIn;
    private Animation zoomOut;

    private Typeface gngTypeface;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        service = new WebService<>(((GNGTVApplication) getApplication()).restAdapter.create(GNGWebService.class));

        gngTypeface = Typeface.createFromAsset(getAssets(),"fonts/RobotoCondensed-Regular.ttf");

        if (mController == null) {
            mController = new MainController(this);
            mController.registerFirebaseListener();
        }

        setActivityInterface(this, this);

        showViews(false);
        setConnectedStoreText();

        zoomIn = AnimationUtils.loadAnimation(this, R.anim.zoom_in);
        zoomOut = AnimationUtils.loadAnimation(this, R.anim.zoom_out);

        zoomIn.setAnimationListener(translationDownAnimationListener);
        imgProductPicture.startAnimation(zoomIn);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(timer != null) timer.cancel();
        if(animationTimer != null) animationTimer.cancel();
        ButterKnife.unbind(this);
    }

    public WebService getService(){
        return service;
    }


    @Override
    public void setContent(List<Wishlist> data) {
        if(data != null && data.size() > 0) {
            Log.e("First Wishlist Name:", data.get(0).getTitle());
            mController.loadWishlistModels(data.get(0).getId(), accessToken);

        }
    }

    @Override
    public void setModelsContent(final List<Model> data) {
        if(data != null && data.size() > 0) {
            Log.e("First Model Name:", data.get(0).getName());

            if(timer != null) timer.cancel();
            timer = new CountDownTimer(60000 * 30, 6000) {

                int position = -1;

                public void onTick(long millisUntilFinished) {
                    position = position < data.size() - 1 ? ++position : 0; //Go ahead through all model positions. If it reaches the end then restart over again.
                    final Model product = data.get(position);

                    product.setPrice(ThreadLocalRandom.current().nextInt(55, 150)); //Setting random price for demonstration purposes

                    YoYo.with(new Animations.SlideOutLeftNoTransparencyAnimator())
                            .duration(700)
                            .withListener(new Animator.AnimatorListener() {
                                @Override
                                public void onAnimationStart(Animator animation) {

                                }

                                @Override
                                public void onAnimationEnd(Animator animation) {

                                }

                                @Override
                                public void onAnimationCancel(Animator animation) {

                                }

                                @Override
                                public void onAnimationRepeat(Animator animation) {

                                }
                            })
                            .playOn(txtProductDescription);

                    YoYo.with(new Animations.SlideOutLeftNoTransparencyAnimator())
                            .duration(1000)
                            .withListener(new Animator.AnimatorListener() {
                                @Override
                                public void onAnimationStart(Animator animation) {

                                }

                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    
                                    if(isFinishing())
                                        return;

                                    if(product.getCover().getImage() != null) {
                                        Glide.with(MainActivityGNG.this).load(product.getCover().getImage()).asBitmap().into(new SimpleTarget<Bitmap>(1080, 1080) {
                                            @Override
                                            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {

                                                if (isFinishing()) return;

                                                imgProductPicture.setImageDrawable(new BitmapDrawable(getResources(), resource));

                                                txtProductDescription.setText(product.getName());
                                                YoYo.with(new Animations.SlideInLeftNoTransparencyAnimator())
                                                        .duration(1000)
                                                        .playOn(txtProductDescription);

                                                txtProductPrice.setText(String.format("%s %s", Utils.formatCurrency(product.getPrice()), "€"));
                                                txtProductDiscount.setText(String.format("%s %s", Utils.formatCurrency((product.getPrice() + (product.getPrice() * 0.2))), "€"));
                                                        // txtProductPrice.setText(getString(R.string.price_label, String.valueOf(product.getPrice())));
                                                        //txtProductDiscount.setText(getString(R.string.price_label, String.valueOf(product.getPrice() - (product.getPrice() * 0.2)))); // 20% discount of the original price for demonstration purpose
                                                        YoYo.with(new Animations.SlideInLeftNoTransparencyAnimator())
                                                                .duration(1300)
                                                                .playOn(priceContainer);
                                            }
                                        });
                                    }else {
                                        Random rdm = new Random();
                                        imgProductPicture.setImageDrawable(new ColorDrawable(Color.rgb(rdm.nextInt(256), rdm.nextInt(256), rdm.nextInt(256))));

                                        YoYo.with(new Animations.SlideInLeftNoTransparencyAnimator())
                                                .duration(1000)
                                                .playOn(txtProductDescription);

                                        txtProductPrice.setText(getString(R.string.price_label, String.valueOf(product.getPrice())));
                                        txtProductDiscount.setText(getString(R.string.price_label, String.valueOf(product.getPrice() - (product.getPrice() * 0.2)))); // 20% discount of the original price for demonstration purpose

                                        YoYo.with(new Animations.SlideInLeftNoTransparencyAnimator())
                                                .duration(1300)
                                                .playOn(priceContainer);
                                    }

                                }

                                @Override
                                public void onAnimationCancel(Animator animation) {

                                }

                                @Override
                                public void onAnimationRepeat(Animator animation) {

                                }
                            })
                            .playOn(priceContainer);

                    if(data.size() == 1) cancel();
                }

                public void onFinish() { /** Do nothing. */ }
            }.start();
        }
    }

    private void showSnackBar(String message){
        SnackbarManager.show(
                Snackbar.with(MainActivityGNG.this)
                        .text(message)
                      //  .duration(Snackbar.SnackbarDuration.LENGTH_)
                        .textColor(Color.WHITE) // change the text color
                        .textTypeface(gngTypeface)
                        .animation(true)
                        .color(getResources().getColor(R.color.red_levis)));
    }

    @Override
    public void setUserInfo(GNGFirebaseModel userInfo) {
       // Toast.makeText(this, "user: " + userInfo.user_name, Toast.LENGTH_LONG).show();
        if(TextUtils.isEmpty(userInfo.access_token) || TextUtils.isEmpty(userInfo.artists_names) || TextUtils.isEmpty(userInfo.favorite_genre) || TextUtils.isEmpty(userInfo.profile_url)
                || TextUtils.isEmpty(userInfo.user_id) || TextUtils.isEmpty(userInfo.user_name)) {
            showViews(false);
        }else {
            setUserName(userInfo.user_name);
            accessToken = userInfo.access_token;
            mController.loadInfo(userInfo.user_id, accessToken);
            showViews(true);
        }
    }

    private void showViews(boolean show) {

        if(isFinishing())
            return;

        int visibility = show ? View.VISIBLE : View.GONE;
        hasUser = show;
       // shapeTriangle.setVisibility(visibility);
        RelativeLayout.LayoutParams txtProdDiscountParams = (RelativeLayout.LayoutParams) txtProductDiscount.getLayoutParams();
        txtProdDiscountParams.width = show ? getResources().getDimensionPixelOffset(R.dimen.label_discount_width) : 0;
        txtProductDiscount.setLayoutParams(txtProdDiscountParams);
       // layoutSongInfo.setVisibility(visibility);
        
        if(!show) {
            /** Show some mock data. */
            setUserName(null);
            List<Model> data = new ArrayList<>(3);
            Model model = new Model();
            model.setName("Commuter raglan tee");
            Cover cover = new Cover();
            cover.setImage("http://lsco.scene7.com/is/image/lsco/Levi/clothing/195330001-front-pdp.jpg?$1330x800main$");
            model.setCover(cover);
            model.setPrice(98.5f);
            data.add(model);
            Model model1 = new Model();
            model1.setName("Sawtooth Western Shirt");
            Cover cover1 = new Cover();
            cover1.setImage("http://lsco.scene7.com/is/image/lsco/Levi/clothing/658190071-front-pdp.jpg?$1330x800main$");
            model1.setCover(cover1);
            model1.setPrice(85f);
            data.add(model1);
            Model model2 = new Model();
            model2.setName("Stock workshirt");
            Cover cover2 = new Cover();
            cover2.setImage("http://lsco.scene7.com/is/image/lsco/Levi/clothing/658220091-2015-spring-front-pdp.jpg?$1330x800main$");
            model2.setCover(cover2);
            model2.setPrice(117.95f);
            data.add(model2);

            setModelsContent(data);
        }
    }

    public void setConnectedStoreText(){

        SpannableStringBuilder spanName = new SpannableStringBuilder(getString(R.string.connected_store, getString(R.string.connected_store_by_thing_pink)).toUpperCase(Locale.US));
        Typeface bold = Typeface.createFromAsset(getAssets(), getString(R.string.roobotoCondensedBold));
        int welcomeLegnth = getString(R.string.connected_store,"").trim().length();
        spanName.setSpan (new TypeFaceSpan("", bold), 0, spanName.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        spanName.setSpan(new RelativeSizeSpan(0.8f), welcomeLegnth + 1, spanName.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        txtConnectedStore.setText(spanName);
    }

    public void setUserName(String userName) {

        if(!TextUtils.isEmpty(userName))
            showSnackBar("Favoritos de " + userName);
    }

    private SpannableStringBuilder formatWhishlistName(String whilistName) {

        SpannableStringBuilder spanName = new SpannableStringBuilder(getString(R.string.whishlist_label, whilistName));

        Typeface light = Typeface.createFromAsset(getAssets(), getString(R.string.roobotoCondensedLight));
        Typeface regular = Typeface.createFromAsset(getAssets(), getString(R.string.roobotoCondensedRegular));
        int welcomeLegnth = getString(R.string.whishlist_label,"").trim().length();
        spanName.setSpan (new TypeFaceSpan("", light), 0, welcomeLegnth, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        spanName.setSpan(new RelativeSizeSpan(0.8f), 0, welcomeLegnth, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        spanName.setSpan(new TypeFaceSpan("", regular), welcomeLegnth + 1, spanName.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        spanName.setSpan(new RelativeSizeSpan(1.0f), welcomeLegnth + 1, spanName.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        return spanName;
    }

    private SpannableStringBuilder formatUserName(String userName) {
        SpannableStringBuilder spanName = new SpannableStringBuilder(userName);

        Typeface light = Typeface.createFromAsset(getAssets(), getString(R.string.roobotoCondensedLight));
        Typeface regular = Typeface.createFromAsset(getAssets(), getString(R.string.roobotoCondensedRegular));
        String[] splittedUser = userName.split("\n", 2);
        int welcomeLegnth = splittedUser[0].length();
        spanName.setSpan (new TypeFaceSpan("", light), 0, welcomeLegnth, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        spanName.setSpan(new RelativeSizeSpan(1.0f), 0, welcomeLegnth, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        if(userName.contains(getString(R.string.welcome_nouser, ""))) spanName.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.playing_label_text)), 0, welcomeLegnth, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        spanName.setSpan(new TypeFaceSpan("", regular), welcomeLegnth + 1, spanName.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        spanName.setSpan(new RelativeSizeSpan(1.2f), welcomeLegnth + 1, spanName.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        return spanName;
    }

    @Override
    public void setCurrentPlayingTrack(SpotifyTrack track) {

    }

    @Override
    public void stop() {
        layoutSongInfo.setVisibility(View.INVISIBLE);
    }

    @Override
    public void play(SpotifyTrack track) {

        YoYo.with(Techniques.FadeIn)
                .duration(700)
                .playOn(layoutSongInfo);

        if(animationTimer == null) {
            animationTimer = new Timer();
            animationTimer.schedule(animationTask, 700, 5000);
        }


        layoutSongInfo.setVisibility(View.VISIBLE);
        txtPlayingSongTitle.setText(track.getName());
        txtPlayingSongAlbumTitle.setText(track.getArtists().get(0).getName());

        if(track.getAlbum().getAlbumImage() != null){
            Glide.with(MainActivityGNG.this).load(track.getAlbum().getAlbumImage().getUrl()).centerCrop().into(imgAlbumCover);
        }
    }

    public boolean hasUser(){
        return hasUser;
    }

    @Override
    public void searchNoResuls() {

    }

    private TimerTask animationTask = new TimerTask() {
        @Override
        public void run() {

            runOnUiThread(new TimerTask() {
                @Override
                public void run() {

                    YoYo.with(Techniques.FlipInX)
                            .duration(1400)
                            .playOn(songMusicIcon);

                    YoYo.with(Techniques.FlipInX)
                            .duration(1400)
                            .playOn(songPlayingLabel);
                }
            });


        }
    };

    private Animation.AnimationListener translationDownAnimationListener = new Animation.AnimationListener() {

        @Override
        public void onAnimationStart(Animation animation) {
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            zoomOut.setAnimationListener(translationUpAnimationListener);
            imgProductPicture.clearAnimation();
            imgProductPicture.startAnimation(zoomOut);
        }
    };

    private Animation.AnimationListener translationUpAnimationListener = new Animation.AnimationListener() {

        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            zoomIn.setAnimationListener(translationDownAnimationListener);
            imgProductPicture.clearAnimation();
            imgProductPicture.startAnimation(zoomIn);
        }
    };
}
