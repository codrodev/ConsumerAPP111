package com.yo4gis.consumersurvey.utilities;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Base64;


import com.google.android.gms.maps.model.LatLng;
import com.yo4gis.consumersurvey.model.AssignedTask;
import com.yo4gis.consumersurvey.model.GeoJsonData;
import com.yo4gis.consumersurvey.model.LocationInfo;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class Global {


    private static String AccessToken;
    private static String Username;
    private static boolean isMapView;
    private static List<AssignedTask> listAssignedTask;
    private static AssignedTask selectedAssignedTask;
    public static String pollForMarkerTitle;
    public static List<LatLng> arrayPolygonPoints;
    public static List<GeoJsonData> geoJsonData;

    public static List<GeoJsonData> getGeoJsonData() {
        return geoJsonData;
    }
    public static List<LocationInfo> lstPoleLatLngs = new ArrayList<>();

    public static void setGeoJsonData(List<GeoJsonData> geoJsonData) {
        Global.geoJsonData = geoJsonData;
    }

    public static boolean isIsMapView() {
        return isMapView;
    }

    public static void setIsMapView(boolean isSatelliteView) {
        Global.isMapView = isSatelliteView;
    }



    public static String getAccessToken() {
        return AccessToken;
    }

    public static void setAccessToken(String accessToken) {
        AccessToken = accessToken;
    }

    public static String getUsername() {
        return Username;
    }

    public static void setUsername(String username) {
        Username = username;
    }

    public static boolean isConnected(Context context) {

        NetworkInfo activeNetworkInfo = null;

        if (context != null) {
            ConnectivityManager connectivityManager
                    = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        }
        if (activeNetworkInfo == null) return false;
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();

    }

    public static boolean isOreoSupported() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return true;
        } else {
            return false;
        }
    }

    public static String getStringSharedRef(Activity activity, String tag) {
        String value = activity.getApplicationContext().getSharedPreferences(AppUtils.MY_PREF, 0).getString(tag, AppUtils.EMPTY);
        return value;
    }

    public static boolean getBooleanSharedRef(Activity activity, String tag) {
        boolean value = activity.getApplicationContext().getSharedPreferences(AppUtils.MY_PREF, 0).getBoolean(tag, false);
        return value;
    }

    public static int getIntegerSharedRef(Activity activity, String tag) {
        int value = activity.getApplicationContext().getSharedPreferences(AppUtils.MY_PREF, 0).getInt(tag, -1);
        return value;
    }

    public static double getDoubleSharedRef(Activity activity, String tag) {
        double value = Double.parseDouble(activity.getApplicationContext().getSharedPreferences(AppUtils.MY_PREF, 0).getString(tag,
                AppUtils.EMPTY));
        return value;
    }

    public static String getCurrentDateAndTime(){
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar obj = Calendar.getInstance();
        String str = formatter.format(obj.getTime());
        return str;
    }
    public static List<AssignedTask> getLstAssignedTask() {
        return listAssignedTask;
    }
    public static void setLstAssignedTask(List<AssignedTask> lstAssignedTask) {
        listAssignedTask = lstAssignedTask;
    }

    public static AssignedTask getSelectedAssignedTask() {
        return selectedAssignedTask;
    }

    public static void setSelectedAssignedTask(AssignedTask selectedAssignedTask) {
        Global.selectedAssignedTask = selectedAssignedTask;
    }

    public static List<LatLng> getArrayPolygonPoints() {
        return arrayPolygonPoints;
    }

    public static void setArrayPolygonPoints(List<LatLng> arrayPolygonPoints) {
        Global.arrayPolygonPoints = arrayPolygonPoints;
    }

    public static Bitmap compressImage(Uri imageUri, String imagenameName, Activity activity) {

        String filePath = getRealPathFromURI(imageUri.toString(), activity);
        String fileName = imagenameName;
        Bitmap scaledBitmap = null;
        Bitmap bmp = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        bmp = BitmapFactory.decodeFile(filePath, options);
        /*int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;*/
        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;
        //max Height and width values of the compressed image is taken as 816x612
        if (actualHeight == 0 || actualWidth == 0) {
            try {
                return BitmapFactory.decodeStream(getSourceStream(imageUri, activity));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        float maxHeight = 816.0f;
        float maxWidth = 612.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;
        //width and height values are set maintaining the aspect ratio of the image
        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;
            }
        }

        //setting inSampleSize value allows to load a scaled down version of the original image
        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

        //inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;
        //this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];
        try {
            //load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }
        try {
            /*scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight,
                    Bitmap.Config.ARGB_8888);*/
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight,
                    Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;
        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);
        if (scaledBitmap != null) {
            Canvas canvas = new Canvas(scaledBitmap);
            canvas.setMatrix(scaleMatrix);
            canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));
            //check the rotation of the image and display it properly
        }
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);
            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
            } else if (orientation == 3) {
                matrix.postRotate(180);
            } else if (orientation == 8) {
                matrix.postRotate(270);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(),
                    scaledBitmap.getHeight(), matrix, true);
        } catch (IOException e) {

            e.printStackTrace();
        }

        return scaledBitmap;
    }

    private static String getRealPathFromURI(String contentURI, Activity activity) {

        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = activity.getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }
    private static FileInputStream getSourceStream(Uri u, Activity activity) throws FileNotFoundException {
        FileInputStream out = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            ParcelFileDescriptor parcelFileDescriptor =
                    activity.getBaseContext().getContentResolver().openFileDescriptor(u, "r");
            FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
            out = new FileInputStream(fileDescriptor);
        } else {
            out = (FileInputStream) activity.getBaseContext().getContentResolver().openInputStream(u);
        }
        return out;
    }
    public static String encodeImage(Bitmap bm, String extension) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if (extension.contains("png")){
            bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        } else {
            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        }
        byte[] b = baos.toByteArray();
        // Base64.Encoder mimeEncode = Base64.getMimeEncoder();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);
        return encImage;
    }
}
