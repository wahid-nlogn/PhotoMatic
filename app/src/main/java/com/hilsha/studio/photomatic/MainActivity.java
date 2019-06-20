package com.hilsha.studio.photomatic;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;

import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.codemybrainsout.ratingdialog.RatingDialog;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.hilsha.studio.photomatic.Utils.BitmapUtils;
import com.hilsha.studio.photomatic.Utils.DataController;
import com.hilsha.studio.photomatic.Utils.DeviceUtils;
import com.hilsha.studio.photomatic.analytics.AnalyticsConstants;
import com.hilsha.studio.photomatic.fragments.AppCommon;
import com.hilsha.studio.photomatic.fragments.FilterCategory;
import com.hilsha.studio.photomatic.fragments.FilterChooserFragment;
import com.hilsha.studio.photomatic.fragments.FilterFactory;
import com.hilsha.studio.photomatic.fragments.OverLayAdapter;
import com.hilsha.studio.photomatic.fragments.Overlay;
import com.hilsha.studio.photomatic.fragments.SaveFragment;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;

import org.greenrobot.eventbus.EventBus;
import org.wysaid.common.Common;
import org.wysaid.nativePort.CGENativeLibrary;
import org.wysaid.view.ImageGLSurfaceView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.GPUImageView;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageBrightnessFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageContrastFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageDissolveBlendFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageGammaFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageLookupFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageMixBlendFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageSaturationFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageSharpenFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageTwoInputFilter;
import jp.co.cyberagent.android.gpuimage.sample.GPUImageFilterTools;

public class MainActivity extends AppCompatActivity implements ImageEditFragment.ChangeBrightness,
        ImageEditFragment.ChangeContrast, ImageEditFragment.ChangeSatuarion, ImageEditFragment.ChangeSharpness,
        ImageEditFragment.ChangeGama, Overlay.ChangeOverlay {

    protected static final String BASIC_FILTER_CONFIG = "@adjust brightness 0 @adjust contrast 1 @adjust saturation 1 @adjust sharpen 0";
    private String mCurrentConfig;
    private GPUImageFilterTools.FilterAdjuster filterAdjuster = null;
    private static final String SAMPLE_CROPPED_IMAGE_NAME = "SampleCropImage";
    public Receiver receiver = new Receiver();
    GPUImageView mainImagview;
    @BindView(R.id.newProject)
    ImageView newProject;
    private Bitmap mBitmap;
    GPUImage gpuImage;

    @BindView(R.id.layoutimageedit)
    LinearLayout layoutimageedit;

    @BindView(R.id.layoutfilter)
    LinearLayout layoutfilter;

    @BindView(R.id.layoutoverlay)
    LinearLayout layoutoverlay;

    @BindView(R.id.btnsaveImage) Button btnsaveImage;
    @BindView(R.id.progressbar)
    ProgressBar progressBar;
    @BindView(R.id.ll_gpuimage)
    RelativeLayout ll_gpuimage;

    ImageEditFragment imageEditFragment;

    FilterChooserFragment filterChooserFragment = new FilterChooserFragment();
    Overlay overlayFragment=new Overlay();
    public static FiltersName filtersName = new FiltersName();

    private InterstitialAd mPublisherInterstitialAd;

    public static FilterAdapter fiteradapter;
    SaveFragment saveFragment=new SaveFragment();

    SharedPreferences sharedPref ;
    SharedPreferences.Editor editor;
    public static FilterAdapter getfiteradapter() {
        return fiteradapter;
    }

    // IMAGE eDITING TOOLS
    GPUImageBrightnessFilter gpuImageBrightnessFilter = new GPUImageBrightnessFilter(1.5f);
    GPUImageContrastFilter gpuImageContrastFilter = new GPUImageContrastFilter(2.0f);
    GPUImageSaturationFilter gpuImageSaturationFilter = new GPUImageSaturationFilter(1.0f);
    GPUImageSharpenFilter gpuImageSharpenFilter = new GPUImageSharpenFilter();
    GPUImageGammaFilter gpuImageGammaFilter = new GPUImageGammaFilter(2.0f);
    GPUImageDissolveBlendFilter gpuImageDissolveBlendFilter=new GPUImageDissolveBlendFilter();


    BrightnessAdjuster brightnessAdjuster;
    ContrastAdjuster contrastAdjuster;
    SaturationAdjuster saturationAdjuster;
    SharpnessAdjuster sharpnessAdjuster;
    GamaAdjuster gamaAdjuster;

    Activity mainAct;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
      //  EventBus.getDefault().register(this);
        ButterKnife.bind(this);
        mainAct=this;
        overlayFragment.setChangeOverlay(mainAct);
        //full screen only for release
        MobileAds.initialize(this, getResources().getString(R.string.admob_ap_id));

        initFullScreenAD();


       sharedPref=getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        if(DataController.sharedInstance.getLengthofdrawables()<15)
            loadImages();
        mainImagview = (GPUImageView) findViewById(R.id.mainImagview);
        mainImagview.setScaleType(GPUImage.ScaleType.CENTER_INSIDE);
        mainImagview.setBackgroundColor(217.0f / 255.0f, 217.0f / 255.0f, 217.0f / 255.0f);

        brightnessAdjuster = new BrightnessAdjuster(gpuImageBrightnessFilter);
        contrastAdjuster = new ContrastAdjuster(gpuImageContrastFilter);
        saturationAdjuster = new SaturationAdjuster(gpuImageSaturationFilter);
        sharpnessAdjuster = new SharpnessAdjuster(gpuImageSharpenFilter);
        gamaAdjuster = new GamaAdjuster(gpuImageGammaFilter);

        imageEditFragment = new ImageEditFragment();

        LandingFragment landingFragment = new LandingFragment();
        addfragment(landingFragment);
        loadImageEditFragment();
        //set the listener

        fiteradapter = new FilterAdapter(filtersName.getMapofFilters(), this);

        imageEditFragment.setContrastlistener(this);
        imageEditFragment.setChangeSatuarionlistener(this);
        imageEditFragment.setSharpnessListener(this);
        imageEditFragment.setgamaListener(this);


    }



    int displayIndex = 0;

@OnClick(R.id.btnsaveImage)
    public void saveImage(){

    btnsaveImage.setVisibility(View.GONE);
    progressBar.setVisibility(View.VISIBLE);
    String screenShotUrl = null;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.US);
    Date now = new Date();
    String fileNameWithDate = formatter.format(now);

    Random num = new Random();
    int nu = num.nextInt(1000);
    String picId = String.valueOf(nu);
    String myfile =   fileNameWithDate+picId + ".jpeg";
   mainImagview.saveToPictures("PhotoMatic",myfile, new GPUImageView.OnPictureSavedListener() {
       @Override
       public void onPictureSaved(Uri uri) {
           DeviceUtils.setMyimageURL(uri);
           Toast.makeText(mainAct,"Picture saved at: "+uri,Toast.LENGTH_SHORT).show();
           showAD();
           saveFragment.show(getSupportFragmentManager(),saveFragment.getTag());
           btnsaveImage.setVisibility(View.VISIBLE);
           progressBar.setVisibility(View.GONE);

       }
   });




}



    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter(AppCommon.FILTER_SELECTED);
        this.registerReceiver(receiver, intentFilter);
        IntentFilter intentFilter1 = new IntentFilter(AppCommon.POSITION_SELECTED);
        this.registerReceiver(receiver, intentFilter1);

    }
    @Override
    protected void onStop() {
        super.onStop();

        unregisterReceiver(receiver);
    }

    @OnClick(R.id.layoutimageedit)
    public void loadImageEditFragment() {

        layoutimageedit.setBackgroundColor(getResources().getColor(R.color.toolbarcolorselected));
        layoutfilter.setBackgroundColor(getResources().getColor(R.color.toolbarcolor));
        layoutoverlay.setBackgroundColor(getResources().getColor(R.color.toolbarcolor));
        // layoutresize.setBackgroundColor(Color.parseColor("#35495e"));
        loadFrgament(imageEditFragment);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        EventBus.getDefault().unregister(this);
    }

    @OnClick(R.id.layoutfilter)
    public void loadFilterFragment() {
        loadfilterFrgament(filterChooserFragment);

        layoutimageedit.setBackgroundColor(getResources().getColor(R.color.toolbarcolor));
        layoutfilter.setBackgroundColor(getResources().getColor(R.color.toolbarcolorselected));
        layoutoverlay.setBackgroundColor(getResources().getColor(R.color.toolbarcolor));
        // layoutresize.setBackgroundColor(Color.parseColor("#35495e"));
    }
    @OnClick(R.id.layoutoverlay)
    public void loadOverlayFragment(){
        loadOverlayFragment(overlayFragment);
        layoutimageedit.setBackgroundColor(getResources().getColor(R.color.toolbarcolor));
        layoutfilter.setBackgroundColor(getResources().getColor(R.color.toolbarcolor));
        layoutoverlay.setBackgroundColor(getResources().getColor(R.color.toolbarcolorselected));
    }
    @OnClick(R.id.newProject)
    public void startnewProject() {
        DataController.sharedInstance.reset();
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }
// LOAD WHAT EVER FRAGMENT PASSES AS PARAMETTER
    private void loadFrgament(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragment);

        transaction.commit();

        findViewById(R.id.fragmentContainer).setVisibility(View.VISIBLE);
        findViewById(R.id.filtercontainer).setVisibility(View.GONE);
        findViewById(R.id.overlaycontainer).setVisibility(View.GONE);
    }
    private void loadfilterFrgament(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.filtercontainer, fragment);
        transaction.commit();
        findViewById(R.id.fragmentContainer).setVisibility(View.GONE);
        findViewById(R.id.filtercontainer).setVisibility(View.VISIBLE);
        findViewById(R.id.overlaycontainer).setVisibility(View.GONE);
    }
    private void loadOverlayFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.overlaycontainer, fragment);
        transaction.commit();
        findViewById(R.id.fragmentContainer).setVisibility(View.GONE);
        findViewById(R.id.filtercontainer).setVisibility(View.GONE);
        findViewById(R.id.overlaycontainer).setVisibility(View.VISIBLE);

    }
    private void addfragment(LandingFragment landingFragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

// Replace whatever is in the fragment_container view with this fragment,
// and add the transaction to the back stack
        transaction.add(R.id.containerofLAndingFragment, landingFragment);



// Commit the transaction
        transaction.commit();
        findViewById(R.id.containerofLAndingFragment).setVisibility(View.VISIBLE);
        int count=sharedPref.getInt(getResources().getString(R.string.seesion_count),0);
        boolean showorNot=sharedPref.getBoolean(getResources().getString(R.string.preference_file_key_2),true);
        if(count%7==0 && showorNot ){
        rateUS();
        }
    }

    @Override
    public void onPause() {
      //  Log.i(Common.LOG_TAG, "Filter Demo2 onPause...");
        super.onPause();


    }

    @Override
    public void onResume() {
     //   Log.i(Common.LOG_TAG, "Filter Demo2 onResume...");
        super.onResume();

    }

    @Override
    protected void onActivityResult(int requestCode, final int resultCode, Intent data) {

        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            // Get a list of picked images
            try {


                Image image = ImagePicker.getFirstImageOrNull(data);
                Uri uri = Uri.fromFile(new File(image.getPath()));
                startCropActivity(uri);


            } catch (Exception ex) {

            }
        }
        if (resultCode == RESULT_OK) {
            if (requestCode == UCrop.REQUEST_CROP) {
                findViewById(R.id.containerofLAndingFragment).setVisibility(View.GONE);
                handleCropResult(data);
            }
        }
        if (resultCode == UCrop.RESULT_ERROR) {
            handleCropError(data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void handleCropResult(Intent data) {
        final Uri resultUri = UCrop.getOutput(data);
        resetImageSelection(resultUri);
    }
    public static int imageViewHeight , imageViewWidth ;
    public static int actualHeight, actualWidth;
    private void resetImageSelection(Uri imageUri) {
        try {


            mBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
            mBitmap = BitmapUtils.rotateImageIfRequired(this, mBitmap, imageUri);
            imageEditFragment.setChangeBrightness(this, mBitmap);

            mainImagview.setImage(mBitmap);

            DataController.sharedInstance.setMyBitmap(mBitmap);
            ViewTreeObserver vto = ll_gpuimage.getViewTreeObserver();
            vto.addOnGlobalLayoutListener (new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    ll_gpuimage.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    imageViewWidth = mainImagview.getMeasuredWidth();
                    imageViewHeight  = mainImagview.getMeasuredHeight();
                    final int bitmapHeight = mBitmap.getHeight(), bitmapWidth = mBitmap.getWidth();
                   // Log.d("wahidttt",imageViewHeight+" "+imageViewWidth+" "+bitmapHeight+" "+bitmapWidth);
                    if (imageViewHeight * bitmapWidth <= imageViewWidth * bitmapHeight) {
                        actualWidth = bitmapWidth * imageViewHeight / bitmapHeight;
                        actualHeight = imageViewHeight;
                        RelativeLayout.LayoutParams layoutParams=new RelativeLayout.LayoutParams(actualWidth,actualHeight);
                        //layoutParams.gravity= Gravity.CENTER;
                        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT,RelativeLayout.TRUE);
                        ll_gpuimage.setGravity(Gravity.CENTER);
                        ll_gpuimage.setLayoutParams(layoutParams);
                    } else {
                        actualHeight = bitmapHeight * imageViewWidth / bitmapWidth;
                        actualWidth = imageViewWidth;
                        RelativeLayout.LayoutParams layoutParams=new RelativeLayout.LayoutParams(actualWidth,actualHeight);
                        //layoutParams.gravity= Gravity.CENTER;
                        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT,RelativeLayout.TRUE);
                        ll_gpuimage.setGravity(Gravity.CENTER);
                        ll_gpuimage.setLayoutParams(layoutParams);

                    }
            //        Log.d("wahidttt",actualHeight+" "+actualWidth+" "+" ");

                }
            });
        } catch (Exception e) {
          //  Log.d("wahidpp", e.toString());
        }
        showAD();
    }

    private void handleCropError(@NonNull Intent result) {
        final Throwable cropError = UCrop.getError(result);
        if (cropError != null) {
            //Log.e(TAG, "handleCropError: ", cropError);
            Toast.makeText(MainActivity.this, cropError.getMessage(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(MainActivity.this, "Please Try Again", Toast.LENGTH_SHORT).show();
        }
    }

    private void startCropActivity(@NonNull Uri uri) {

        String destinationFileName = SAMPLE_CROPPED_IMAGE_NAME;
        destinationFileName += ".png";

        UCrop uCrop = UCrop.of(uri, Uri.fromFile(new File(getCacheDir(), destinationFileName)));
        uCrop = basisConfig(uCrop);
        uCrop = advancedConfig(uCrop);
        uCrop.start(this);
    }

    private UCrop basisConfig(@NonNull UCrop uCrop) {

        uCrop = uCrop.useSourceImageAspectRatio();
        return uCrop;
    }

    private UCrop advancedConfig(@NonNull UCrop uCrop) {
        UCrop.Options options = new UCrop.Options();
        options.setToolbarColor(getResources().getColor(R.color.toolbarcolor));
        options.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        options.setActiveWidgetColor(getResources().getColor(R.color.toolbarcolor));

        options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
        options.setCompressionQuality(100);

        //If you want to configure how gestures work for all UCropActivity tabs
        options.setAllowedGestures(UCropActivity.SCALE, UCropActivity.ROTATE, UCropActivity.ALL);

        return uCrop.withOptions(options);
    }
    private boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {

        if (findViewById(R.id.containerofLAndingFragment).getVisibility() == View.VISIBLE) {

            finish();
        } else {


            if (!doubleBackToExitPressedOnce) {
                this.doubleBackToExitPressedOnce = true;
                Toast.makeText(getApplicationContext(),getResources().getString(R.string.string_message_exit), Toast.LENGTH_SHORT).show();

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        doubleBackToExitPressedOnce = false;
                    }
                }, 1500);

            } else {
                startnewProject();


            }

        }
    }

    @Override
    public void changeBrightness(int intensity) {
        brightnessAdjuster.adjust(intensity);


    }

    private Bitmap getLookupImageBitmapForSelectedFilter() {

        DataController.FilterSelection selectedFilter = DataController.sharedInstance.getSelectedFilter();
        if (selectedFilter == null) {
            return null;
        }

        int categoryIndex = selectedFilter.categoryIndex;
        int filterIndex = selectedFilter.filterIndex;
        FilterCategory selectedFilterCategory = FilterFactory.loadFilters(MainActivity.this).get(categoryIndex);

        int imageResourceId = AppCommonUtils.getResourceIdFromDrawable(selectedFilterCategory.getFilterFileNames().get(filterIndex), MainActivity.this);
        return BitmapUtils.decodeSampledBitmapFromResource(MainActivity.this.getResources(), imageResourceId, mainImagview.getWidth(), mainImagview.getHeight());
    }

    private void logAppliedFilterEvent(DataController.FilterSelection selectedFilter) {
        String appliedFilterName = FilterFactory.loadFilters(this).get(selectedFilter.categoryIndex).getTitle();
        HashMap<String, String> attrs = new HashMap<>();
        attrs.put(AnalyticsConstants.PACK, appliedFilterName);

        // AnalyticsManager.logEvent(AnalyticsConstants.FILTER_APPLIED, attrs);
    }

    private void applySelectedFilter() {
        Bitmap lookupBitmap = getLookupImageBitmapForSelectedFilter();

        GPUImageLookupFilter lookupFilter = new GPUImageLookupFilter();
        lookupFilter.setBitmap(lookupBitmap);

        mainImagview.setFilter(lookupFilter);

        DataController.FilterSelection selectedFilter = DataController.sharedInstance.getSelectedFilter();

        logAppliedFilterEvent(selectedFilter);
    }

    @Override
    public void changeContrast(int intensity) {

        contrastAdjuster.adjust(intensity);
        // mainImagview.setFilterIntensityForIndex(ralintesity, index, shouldProcess);
    }


    @Override
    public void changeSaturation(int intensity) {


        saturationAdjuster.adjust(intensity);
        //mainImagview.setFilterIntensityForIndex(ralintesity, index, shouldProcess);
    }

    @Override
    public void changeSharpeness(int intensity) {
        sharpnessAdjuster.adjust(intensity);

        //mainImagview.setFilterIntensityForIndex(ralintesity, index, shouldProcess);

    }

    public float range(int percentage, float start, float end) {
        return (end - start) * percentage / 100.0f + start;
    }

    @Override
    public void changegama(int intensity) {
        gamaAdjuster.adjust(intensity);
    }

    @Override
    public void changeOverlay(int pos) {
     //   Log.d("malysia",pos+" ");
        gpuImageMixBlendFilter.setMix(range(pos,0.2f,0.8f));
        mainImagview.setFilter(gpuImageMixBlendFilter);
    }


    private class BrightnessAdjuster {
        GPUImageBrightnessFilter filter;

        BrightnessAdjuster(GPUImageBrightnessFilter gpuImageBrightnessFilter) {
            filter = gpuImageBrightnessFilter;
        }

        public void adjust(int percentage) {

            filter.setBrightness(range(percentage, -1.0f, 1.0f));
            mainImagview.setFilter(filter);
        }
    }

    private class ContrastAdjuster {
        GPUImageContrastFilter filter;

        ContrastAdjuster(GPUImageContrastFilter gpuImageBrightnessFilter) {
            filter = gpuImageBrightnessFilter;
        }

        public void adjust(int percentage) {

            filter.setContrast(range(percentage, 0.0f, 2.0f));
            mainImagview.setFilter(filter);
        }
    }

    private class SaturationAdjuster {
        GPUImageSaturationFilter filter;

        SaturationAdjuster(GPUImageSaturationFilter gpuImageBrightnessFilter) {
            filter = gpuImageBrightnessFilter;
        }

        public void adjust(int percentage) {

            filter.setSaturation(range(percentage, 0.0f, 2.0f));
            mainImagview.setFilter(filter);
        }
    }

    private class SharpnessAdjuster {
        GPUImageSharpenFilter filter;

        SharpnessAdjuster(GPUImageSharpenFilter gpuImageBrightnessFilter) {
            filter = gpuImageBrightnessFilter;
        }

        public void adjust(int percentage) {

            filter.setSharpness(range(percentage, -4.0f, 4.0f));
            mainImagview.setFilter(filter);
        }
    }

    private class GamaAdjuster {
        GPUImageGammaFilter filter;

        GamaAdjuster(GPUImageGammaFilter gpuImageBrightnessFilter) {
            filter = gpuImageBrightnessFilter;
        }

        public void adjust(int percentage) {

            filter.setGamma(range(percentage, 0.0f, 3.0f));
            mainImagview.setFilter(filter);

        }
    }

    class Receiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context arg0, Intent intent) {
            if (intent.getAction().equals(AppCommon.FILTER_SELECTED)) {
              //  Log.d("malisha", "filter selected broadcast received");
                applySelectedFilter();

            }else if (intent.getAction().equals(AppCommon.POSITION_SELECTED)){
            //    Log.d("malisha","Position: "+DataController.sharedInstance.getPosition());
                 gpuImageMixBlendFilter =  createBlendFilter(DataController.sharedInstance.getPosition());
                gpuImageMixBlendFilter.setMix(0.35f);
                mainImagview.setFilter(gpuImageMixBlendFilter);
            }
        }
    }
    public GPUImageMixBlendFilter gpuImageMixBlendFilter;
    void initFullScreenAD() {

        try {
            mPublisherInterstitialAd = new InterstitialAd(this);
            mPublisherInterstitialAd.setAdUnitId(getResources().getString(R.string.full_screen_ap_id));
            mPublisherInterstitialAd.loadAd(new AdRequest.Builder().build());
            mPublisherInterstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    // Code to be executed when an ad finishes loading.
                   // Log.d("ADDDD", "onAdLoaded");
                }

                @Override
                public void onAdFailedToLoad(int errorCode) {
                    // Code to be executed when an ad request fails.
                   // Log.d("ADDDD", "onAdFailedToLoad "+errorCode);
                }

                @Override
                public void onAdClosed() {
                    mPublisherInterstitialAd.loadAd(new AdRequest.Builder().build());
                   // Log.d("ADDDD", "onAdClosed");

                    // Code to be executed when when the interstitial ad is closed.
                }
            });

        } catch (Exception e) {

        }
    }

    void showAD() {

      if (mPublisherInterstitialAd.isLoaded() ) {
            mPublisherInterstitialAd.show();
        }
    }

    public void rateUS(){
        String s="https://play.google.com/store/apps/details?id="+getPackageName();
        String packagename=getPackageName();
        final RatingDialog ratingDialog = new RatingDialog.Builder(this)
                .threshold(4)
                .title("How was your experience with us?")
                .titleTextColor(R.color.black)
                .positiveButtonText("Not Now")
                .negativeButtonText("Never")
                .positiveButtonTextColor(R.color.toolbarcolor)
                .negativeButtonTextColor(R.color.grey_500)
                .formTitle("Submit Feedback")
                .formHint("Tell us where we can improve")
                .formSubmitText("Submit")
                .formCancelText("Cancel")
                .ratingBarColor(R.color.toolbarcolor)
                .playstoreUrl(s)

                .onRatingBarFormSumbit(new RatingDialog.Builder.RatingDialogFormListener() {
                    @Override
                    public void onFormSubmitted(String feedback) {
                        editor.putInt(getString(R.string.seesion_count), 0);

                        editor.commit();
                        DeviceUtils.sendFeedback(mainAct,feedback);
                    }
                })
                .onThresholdCleared(new RatingDialog.Builder.RatingThresholdClearedListener() {
                    @Override
                    public void onThresholdCleared(RatingDialog ratingDialog, float rating, boolean thresholdCleared) {
                        //do something
                      //  Log.d("wahidpp","one");

                        editor.putBoolean(getString(R.string.preference_file_key_2),false );

                        editor.commit();
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packagename)));
                        }catch (Exception ex) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + packagename)));
                        }
                        ratingDialog.dismiss();
                    }
                })

                .onRatingChanged(new RatingDialog.Builder.RatingDialogListener() {
                    @Override
                    public void onRatingSelected(float rating, boolean thresholdCleared) {
                        //Log.d("wahidpp","three "+" "+rating);
                    }
                })
                .build();

        ratingDialog.show();
    }

    private void loadImages() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int j = 1; j < 17; j++) {
                    int imageResourceId = AppCommonUtils.getResId("o"+j, R.drawable.class);
                  //  Log.d("malisha",imageResourceId+" ");
                    Bitmap bitmapFromResource = BitmapUtils.decodeSampledBitmapFromResource(getResources(),
                            imageResourceId, (int)getResources().getDimension(R.dimen.filter_layout_size),
                            (int) getResources().getDimension(R.dimen.filter_image_size));
                    Bitmap bitmap=BitmapFactory.decodeResource(getResources(),imageResourceId);
                    bitmaplist.add(j-1,bitmap);
                    DataController.sharedInstance.setData(bitmapFromResource);


                }
            }
        }).start();

    }
    ArrayList<Bitmap> bitmaplist=new ArrayList<Bitmap>();

    public GPUImageMixBlendFilter createBlendFilter(int pos){



                gpuImageDissolveBlendFilter.setBitmap(bitmaplist.get(pos-1));




        return gpuImageDissolveBlendFilter;
    }

}
