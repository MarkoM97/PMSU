package com.example.marko.app1.utils;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.media.ExifInterface;
import android.util.DisplayMetrics;
import android.util.Size;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class ImageUtils {
    public static class imageUtils {
        private static final int REQUEST_EXTERNAL_STORAGE = 1;
        private static String[] PERMISSION_STORAGE = {Manifest.permission.READ_EXTERNAL_STORAGE};

        public static String realImagePath;
        private static int rotInDeg;
        private static int bitmapWidth;
        private static int bitmapHeight;

        public static void clearBitmap(){
            realImagePath = "";
            rotInDeg = 0;
            bitmapWidth = 0;
            bitmapHeight = 0;
        }

        public static int fixRotation(Context context, Uri uri) {

            try {

                String path = getRealPathFromUri(context,uri);
                realImagePath = path;
                ExifInterface exifInterface = new ExifInterface(path);
                int currentRotation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                int rotationInDegrees = exifToDegrees(currentRotation);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getScaledProportions(context);
                }
                rotInDeg = rotationInDegrees;
                return rotationInDegrees;
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.d("Error 202", "Image util error");
            return 0;
        }

        private static int exifToDegrees(int currentRotation) {
            if(currentRotation == ExifInterface.ORIENTATION_ROTATE_90) {
                Log.d("Rotation", "90");
                return 90;
            } else if (currentRotation == ExifInterface.ORIENTATION_ROTATE_180) {
                Log.d("Rotation", "180");
                return 180;
            }else if( currentRotation == ExifInterface.ORIENTATION_ROTATE_270) {
                Log.d("Rotation", "270");
                return 270;
            } else{
                return 0;
            }
        }

        private static String getRealPathFromUri(Context context, Uri uri) {
            Cursor cursor = null;
            try {
                String[] proj = {MediaStore.Images.Media.DATA};
                cursor = context.getContentResolver().query(uri, proj, null, null, null);
                int columnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                return cursor.getString(columnIndex);
            }catch (Exception e ) {e.printStackTrace();}
            return null;
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        public static void getScaledProportions(Context context)  {
            ExifInterface exifInterface = null;
            try {
                exifInterface = new ExifInterface(realImagePath);
                int currentWidth = exifInterface.getAttributeInt(ExifInterface.TAG_IMAGE_WIDTH, 500);
                int currentHeight = exifInterface.getAttributeInt(ExifInterface.TAG_IMAGE_LENGTH, 450);
                if(currentHeight == 0 && currentWidth == 0) {
                    currentWidth = 500;
                    currentHeight = 900;
                    Size currentImageSize = new Size(currentWidth, currentHeight);
                    Size newSize = scaleImageProportionaly(currentImageSize, currentImageSize);
                } else {
                    Size currentImageSize = new Size(currentWidth, currentHeight);
                    Size boundaries = new Size(500, 900);
                    Size newSize = scaleImageProportionaly(currentImageSize, boundaries);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            //return null;

        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        public static Size scaleImageProportionaly(Size imageDimension, Size boundaryDimension) {

            int originalWidth = imageDimension.getWidth();
            int originalHeight = imageDimension.getHeight();

            int boundaryWidth = boundaryDimension.getWidth();
            int boundaryHeight = boundaryDimension.getHeight();

            int newWidth = originalWidth;
            int newHeight = originalHeight;

            if(originalWidth > boundaryWidth) {
                newWidth = boundaryWidth;
                newHeight = (newWidth * originalHeight) / originalWidth;
            }

            if(originalHeight > boundaryHeight) {
                newHeight = boundaryHeight;
                newWidth = (newHeight * originalWidth) / originalHeight;
            }

            bitmapWidth = newWidth;
            bitmapHeight = newHeight;

            return new Size(newWidth, newHeight);

        }

        public static Bitmap getBitmap() {
            Bitmap bitmap = BitmapFactory.decodeFile(realImagePath);
            if(bitmap == null) {
                return null;
            }

            if(bitmap.getWidth() == 0 && bitmap.getHeight() == 0) {
                bitmapWidth = 500;
                bitmapHeight = 900;
            }

            Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, bitmapWidth, bitmapHeight, false);

            Matrix matrix = new Matrix();
            matrix.postRotate(rotInDeg);
            Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0 , scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
            return rotatedBitmap;
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        public static Size getBitmapSize() {
            return new Size(bitmapWidth, bitmapHeight);
        }

    }
}
