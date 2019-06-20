package com.hilsha.studio.photomatic.Utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import androidx.annotation.Nullable;

public class BitmapUtils {
    public static PointF aspectFill(int imageWidth, int imageHeight, int expectedWindowSize) {

//        Log.d(Tag,"newBitmap mImageWidth  : "+mImageWidth+"  mImageHeight : "+mImageHeight);


        PointF aspectRatio = new PointF();

        float mW =  (expectedWindowSize / (float)imageWidth);
        float mH = (expectedWindowSize / (float)imageHeight);

        aspectRatio.x = expectedWindowSize;
        aspectRatio.y=expectedWindowSize;

        if( mH > mW ) {
            aspectRatio.x = mH * imageWidth;
        }
        else if( mW > mH ) {
            aspectRatio.y = (mW * imageHeight);
        }

        return aspectRatio;
    }
    public static Bitmap getAspectFillBitmap(Bitmap bitmap, int expectedWindowSize){

        PointF newResolution = aspectFill(bitmap.getWidth(),bitmap.getHeight(),expectedWindowSize);
        int newHeight = (int)newResolution.y;
        int newWidth = (int)newResolution.x;

        int offsetX = 0,offsetY = 0;

        Bitmap scaleBitmap,newBitmap = null;

        if(newHeight>expectedWindowSize){

            offsetY = newHeight/2-expectedWindowSize/2;

            scaleBitmap = Bitmap.createScaledBitmap(bitmap, newWidth,
                    newHeight, false);

            newBitmap = Bitmap.createBitmap(scaleBitmap, offsetX,offsetY,expectedWindowSize,expectedWindowSize );
        }else if(newWidth>expectedWindowSize){

            offsetX = newWidth/2-expectedWindowSize/2;

            scaleBitmap = Bitmap.createScaledBitmap(bitmap, newWidth,
                    newHeight, false);

            newBitmap = Bitmap.createBitmap(scaleBitmap, offsetX,offsetY,expectedWindowSize,expectedWindowSize );
        }else {
            scaleBitmap = Bitmap.createScaledBitmap(bitmap, newWidth,
                    newHeight, false);

            newBitmap = Bitmap.createBitmap(scaleBitmap, offsetX,offsetY,expectedWindowSize,expectedWindowSize );
        }

        bitmap.recycle();
        scaleBitmap.recycle();

        return newBitmap;
    }
    @Nullable
    public static Bitmap decodeSampledBitmapFromBitmap(Bitmap bitmap, int reqWidth, int reqHeight) {

        return Bitmap.createScaledBitmap(bitmap, reqWidth, reqHeight, false);
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inDither = true;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static Bitmap decodeSampledBitmapFromResource(String path, int reqWidth, int reqHeight) {

        // First decode from inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap from inSampleSize set
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        options.inDither = true;

        int rotateAngle = getPhotoOrientation(path);
        if(rotateAngle>0){
            Matrix mat = new Matrix();
            mat.postRotate(rotateAngle);
            Bitmap bitmap = BitmapFactory.decodeFile(path, options);
            return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), mat, true);
        }
        return BitmapFactory.decodeFile(path, options);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    private static int getPhotoOrientation(String imagePath){
        int rotate = 0;
        try {

            File imageFile = new File(imagePath);

            ExifInterface exif = new ExifInterface(imageFile.getAbsolutePath());
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
            }

            Log.i("RotateImage", "Exif orientation: " + orientation);
            Log.i("RotateImage", "Rotate value: " + rotate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rotate;
    }
    public static Bitmap rotateImageIfRequired(Context context, Bitmap img, Uri selectedImage) throws IOException {

        InputStream input = context.getContentResolver().openInputStream(selectedImage);
        ExifInterface ei;
        if (Build.VERSION.SDK_INT > 23)
            ei = new ExifInterface(input);
        else
            ei = new ExifInterface(selectedImage.getPath());

        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotateImage(img, 90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotateImage(img, 180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotateImage(img, 270);
            default:
                return img;
        }
    }

    private static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        img.recycle();
        return rotatedImg;
    }
}
