package com.hilsha.studio.photomatic.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;

import java.io.File;
import java.io.IOException;
import java.util.List;

import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentActivity;

public class DeviceUtils {
    public static int getHightofDevice(Context context){
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int height = displayMetrics.heightPixels;

        return  height;
    }
    public static int getWidthofDevice(Context context){
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        if(displayMetrics.densityDpi==DisplayMetrics.DENSITY_HIGH){
            Log.d("devicedens","DisplayMetrics.DENSITY_HIGH");
        }else if(displayMetrics.densityDpi==DisplayMetrics.DENSITY_LOW){
            Log.d("devicedens","DisplayMetrics.DENSITY_LOW");

        }else if(displayMetrics.densityDpi==DisplayMetrics.DENSITY_MEDIUM){
            Log.d("devicedens","DisplayMetrics.DENSITY_MEDIUM");

        }else if(displayMetrics.densityDpi==DisplayMetrics.DENSITY_260){
            Log.d("devicedens","DisplayMetrics.DENSITY_260");

        }else if(displayMetrics.densityDpi==DisplayMetrics.DENSITY_280){
            Log.d("devicedens","DisplayMetrics.DENSITY_280");

        }else if(displayMetrics.densityDpi==DisplayMetrics.DENSITY_300){
            Log.d("devicedens","DisplayMetrics.DENSITY_300");

        }else if(displayMetrics.densityDpi==DisplayMetrics.DENSITY_340){
            Log.d("devicedens","DisplayMetrics.DENSITY_340");

        }else if(displayMetrics.densityDpi==DisplayMetrics.DENSITY_360){
            Log.d("devicedens","DisplayMetrics.DENSITY_360");

        }else if(displayMetrics.densityDpi==DisplayMetrics.DENSITY_400){
            Log.d("devicedens","DisplayMetrics.DENSITY_400");

        }else if(displayMetrics.densityDpi==DisplayMetrics.DENSITY_420){
            Log.d("devicedens","DisplayMetrics.DENSITY_420");

        }else if(displayMetrics.densityDpi==DisplayMetrics.DENSITY_560){
            Log.d("devicedens","DisplayMetrics.DENSITY_560");

        }else{
            Log.d("devicedens","DisplayMetrics.DENSITY_DEFAULT");

        }
        int width = displayMetrics.widthPixels;

        return  width;
    }

    public static Uri getMyimageURL() {
        return myimageURL;
    }

    public static void setMyimageURL(Uri myimageURL) {
        DeviceUtils.myimageURL = myimageURL;
    }
    public static void shareImageInstagram(Uri imageURL, FragmentActivity activity) {

        if(!isPackageInstalled(activity,"com.instagram.android")){

            Toast.makeText(activity,"This app is not installed",Toast.LENGTH_SHORT).show();
            return;
        }

        //File file = new File(imageURL);
        Uri uri = imageURL;
       // Uri uri = FileProvider.getUriForFile(activity, activity.getApplicationContext().getPackageName() + ".com.kitegames.babysnaps" + ".provider", file);
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setPackage("com.instagram.android");
        share.putExtra(Intent.EXTRA_STREAM, uri);
        share.setType("image/*");
        share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        //activity.startActivity(Intent.createChooser(share, "Share image File"));
        activity.startActivity(share);
    }public static void sharetwitter(Uri imageURL, FragmentActivity activity) {

        if(!isPackageInstalled(activity,"com.twitter.android")){

            Toast.makeText(activity,"This app is not installed",Toast.LENGTH_SHORT).show();
            return;
        }

        //File file = new File(imageURL);
        Uri uri = imageURL;
        // Uri uri = FileProvider.getUriForFile(activity, activity.getApplicationContext().getPackageName() + ".com.kitegames.babysnaps" + ".provider", file);
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setPackage("com.twitter.android");
        share.putExtra(Intent.EXTRA_STREAM, uri);
        share.setType("image/*");
        share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        //activity.startActivity(Intent.createChooser(share, "Share image File"));
        activity.startActivity(share);
    }
    public static Uri myimageURL=null;
    public static void shareImageViaFacebook(Context context, Uri imageUrl) {

       if(!isPackageInstalled(context,"com.facebook.katana")){

            Toast.makeText(context,"Facebook is not installed",Toast.LENGTH_SHORT).show();
            return;
        }

       // File file = new File(imageUrl);
        Uri imageUri = imageUrl;
        try {

            Bitmap image = MediaStore.Images.Media.getBitmap(context.getContentResolver(), imageUri);

            SharePhoto photo = new SharePhoto.Builder()
                    .setBitmap(image)
                    .build();
            SharePhotoContent content = new SharePhotoContent.Builder()
                    .addPhoto(photo)
                    .build();

            ShareDialog.show((Activity) context, content);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static boolean isPackageInstalled(Context context, String packageName) {
        final PackageManager packageManager = context.getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage(packageName);
        if (intent == null) {
            return false;
        }
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return !list.isEmpty();
    }

    public static void sendFeedback(Activity activity,String feedback){
        String to[] = {"hilsastudio@gmail.com"};
        Intent email = new Intent(Intent.ACTION_SEND);
        email.setData(Uri.parse("mailto:"));
        email.putExtra(Intent.EXTRA_EMAIL, to);
        email.putExtra(Intent.EXTRA_SUBJECT, "Feed back for PhotoMatic");
        email.putExtra(Intent.EXTRA_TEXT, feedback);

//need this to prompts email client only
        email.setType("message/rfc822");

        activity.startActivity(Intent.createChooser(email, "Choose an Email client :"));
    }
}
