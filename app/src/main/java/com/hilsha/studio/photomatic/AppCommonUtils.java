package com.hilsha.studio.photomatic;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;



import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentActivity;




public class AppCommonUtils {

    private static final String TAG = AppCommonUtils.class.getName();

    public static void goToInstagramPageKITE(FragmentActivity activity) {
        Uri uri = Uri.parse("http://instagram.com/kitegamesstudio/");
        Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

        likeIng.setPackage("com.instagram.android");

        try {
            activity.startActivity(likeIng);
        } catch (ActivityNotFoundException e) {
            activity.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://instagram.com/kitegamesstudio/")));
        }
    }

    public static void goToTermsConditionPageKITE(FragmentActivity activity) {
        Uri uri = Uri.parse("https://sites.google.com/view/kgspolicy/terms-conditions");
        Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

        //likeIng.setPackage("com.instagram.android");

        try {
            activity.startActivity(likeIng);
        } catch (ActivityNotFoundException e) {
            activity.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://instagram.com/kitegamesstudio/")));
        }
    }

    public static void rateUs(FragmentActivity activity) {
        final String appPackageName = activity.getPackageName(); // getPackageName() from Context or Activity object
        try {
            activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (ActivityNotFoundException anfe) {
            activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

    public static void goToPrivacyPageKITE(FragmentActivity activity) {
        Uri uri = Uri.parse("https://sites.google.com/view/kgspolicy/privacy-policy");
        Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

        //likeIng.setPackage("com.instagram.android");

        try {
            activity.startActivity(likeIng);
        } catch (ActivityNotFoundException e) {
            activity.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://instagram.com/kitegamesstudio/")));
        }
    }

    public static boolean isAppInstalled(Context context, String appPackageName){
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(appPackageName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
        }

        return false;
    }

    public static void launchAnApp(Context context, String packageName) {
        if (!isAppInstalled(context, packageName)) {
            return;
        }
        Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        if (launchIntent != null) {
            context.startActivity(launchIntent);//null pointer check in case package name was not found
        }
    }

    public static void openAppInPlaystore(Context context, String appPackageName) {
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (ActivityNotFoundException anfe) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

    public static int getStatusBarHeight(Activity activity){
        Rect rectangle = new Rect();
        Window window = activity.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(rectangle);
        int statusBarHeight = rectangle.top;
        return statusBarHeight;
    }

    public static int getNavigationBarHeight(Context context){
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    public static int getResourceIdFromDrawable(String fileName, Context context) {
        int identifier = context.getResources().getIdentifier(fileName, "drawable", context.getPackageName());
        return identifier;
    }

    public Drawable loadDrawableFromAssets(Context context, String path)
    {
        InputStream stream = null;
        try
        {
            stream = context.getAssets().open(path);
            return Drawable.createFromStream(stream, null);
        }
        catch (Exception ignored) {} finally
        {
            try
            {
                if(stream != null)
                {
                    stream.close();
                }
            } catch (Exception ignored) {}
        }
        return null;
    }

    public static int getResId(String resName, Class<?> c) {
        try {
            Field idField = c.getDeclaredField(resName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static int getDpToPixels(int dps, Context context) {
        final float scale = context.getResources().getDisplayMetrics().density;
        int pixels = (int) (dps * scale + 0.5f);
        return pixels;
    }

    public static ColorMatrix getColorMatrix(int colorValue) {
        int dr = Color.red(colorValue);
        int dg = Color.green(colorValue);
        int db = Color.blue(colorValue);
        float drf = dr / 255f;
        float dgf = dg / 255f;
        float dbf = db / 255f;
        ColorMatrix tintMatrix = new ColorMatrix(new float[]{
                drf, 0, 0, 0, 0, //
                0, dgf, 0, 0, 0, //
                0, 0, dbf, 0, 0, //
                0, 0, 0, 1, 0, //
        });

        return tintMatrix;
    }

    //THIS METHOD TAKES A SCREENSHOT AND SAVES IT AS .jpg
    public static String takeScreenshot(View view) {

        String screenShotUrl = null;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.US);
        Date now = new Date();
        String fileNameWithDate = formatter.format(now);

        Random num = new Random();
        int nu = num.nextInt(1000); //PRODUCING A RANDOM NUMBER FOR FILE NAME

        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache(true);
        Bitmap bmp = loadBitmapFromView(view);  //Bitmap.createBitmap(view.getDrawingCache()); //this crashes for large image
        view.setDrawingCacheEnabled(false); // clear drawing cache
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        byte[] bitmapdata = bos.toByteArray();
        ByteArrayInputStream fis = new ByteArrayInputStream(bitmapdata);

        String picId = String.valueOf(nu);
        String myfile = "Babysnaps" + fileNameWithDate+picId + ".jpeg";

        File imageDir = new File(Environment.getExternalStorageDirectory() + File.separator + "Babysnaps");
        imageDir.mkdirs();

        try {
            File tmpFile = new File(imageDir, myfile);
            FileOutputStream fos = new FileOutputStream(tmpFile);

            byte[] buf = new byte[1024];
            int len;
            while ((len = fis.read(buf)) > 0) {
                fos.write(buf, 0, len);
            }
            fis.close();
            fos.close();
/*            Toast.makeText(getApplicationContext(),
                    "The file is saved at :SD/Ultimate Entity Detector",Toast.LENGTH_LONG).show();*/
            Log.d(TAG, "image saved");

/*
            screenShotUrl = MediaStore.Images.Media.insertImage(view.getContext().getContentResolver(), BitmapFactory.decodeFile(tmpFile.getAbsolutePath()), myfile, "Babysnaps");

            if (tmpFile.delete()) {
                Log.d(TAG, "image deleted after inserting into MediaStore");
            }
*/
            screenShotUrl= tmpFile.getAbsolutePath();
            galleryAddPic(screenShotUrl,view);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return screenShotUrl;
    }

    private static void galleryAddPic(String mCurrentPhotoPath, View view) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        view.getContext().sendBroadcast(mediaScanIntent);
    }

    private static Bitmap loadBitmapFromView(View v) {
        Bitmap b = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);

        Canvas c = new Canvas(b);
        v.layout(0, 0, v.getLayoutParams().width, v.getLayoutParams().height);
        v.draw(c);
        return b;
    }

    public static int getScreenWidth(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int widthPixels = displayMetrics.widthPixels;
        Log.d(TAG, "screen width in pixels: " + widthPixels);
        return widthPixels;
    }

   /* public static String getDeviceInfoImagePath(){
        return new File(GetImageOutputDirectory(), "deviceinfo.jpg").getAbsolutePath();
    }*/



    public static boolean hasEnoughSpaceForSaving(Context context){
        long fileSize = 10 * 1000 * 1000;
        long avSpace = AppCommonUtils.getFreeSpace(context);
        Log.d("space: ",avSpace+"");
        //Log.d(TAG, "hasEnoughSpaceForSaving: " + avSpace + " " + fileSize);
        return  avSpace > (fileSize*2);
    }

    public static long getFreeSpace(Context context) {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return availableBlocks*blockSize;
    }

    public static int getDeviceHeight(FragmentActivity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        return height;
    }

    public static int getDeviceWidth(FragmentActivity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        return width;
    }

    public static void contactUs(FragmentActivity activity, String deviceInfoPath, boolean hasPermission) {

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setData(Uri.parse("mailto:"));
        String to[] = {"info.kite.an@gmail.com"};
        intent.putExtra(Intent.EXTRA_EMAIL, to);
        intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback for Baby Snaps");
        intent.setType("message/rfc822");

        if (hasPermission) {
            String authority = activity.getApplicationContext().getPackageName() + ".com.kitegames.babysnaps" + ".provider";
            Uri uri = FileProvider.getUriForFile(activity, authority, new File(deviceInfoPath));
            intent.putExtra(Intent.EXTRA_STREAM, uri);
        } else {
            intent.putExtra(Intent.EXTRA_TEXT, "Permission Denied");
        }

        final PackageManager pm = activity.getPackageManager();
        final List<ResolveInfo> matches = pm.queryIntentActivities(intent, 0);
        String className = null;
        for (final ResolveInfo info : matches) {
            if (info.activityInfo.packageName.equals("com.google.android.gm")) {
                className = info.activityInfo.name;

                if (className != null && !className.isEmpty()) {
                    break;
                }
            }
        }

        intent.setClassName("com.google.android.gm", className);
        activity.startActivity(intent);

    }

    public static void copyTextClipBoard(FragmentActivity activity, String text) {
        ClipboardManager clipboard = (ClipboardManager) activity.getSystemService(activity.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("label", text);
        clipboard.setPrimaryClip(clip);
    }



   /* public static void showPermissionSettingAlert(Context baseContext, int msg) {

        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(baseContext, com.kitegamesstudio.kgspicker.R.style.AlertDialogTheme));
        builder.setTitle("Permission Denied");
        builder.setMessage(msg);
        builder.setPositiveButton("Settings", (dialog, id) -> {
            gotoApplicationSetting(baseContext);
            dialog.dismiss();
        });
        builder.setNegativeButton("not now", (dialog, id) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private static void gotoApplicationSetting(Context baseContext) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.fromParts("package", baseContext.getPackageName(), null));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        baseContext.startActivity(intent);
    }*/

}
