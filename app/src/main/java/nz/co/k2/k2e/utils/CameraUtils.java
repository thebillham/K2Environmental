package nz.co.k2.k2e.utils;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CameraUtils {

    private static final int DEFAULT_MIN_WIDTH_QUALITY = 400;        // min pixels
    private static final String TAG = "BenD";
    private static final String TEMP_IMAGE_NAME = "tempImage";

    public static int minWidthQuality = DEFAULT_MIN_WIDTH_QUALITY;

    public static String getFileName(String prefix) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return "K2_" + prefix + "_TIME_" + timeStamp + ".jpg";
    }

    public static Intent dispatchTakePictureIntent(Context context, String mCurrentPhotoPath) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(context.getPackageManager()) != null) {
            // Create an image file name
            File image = new File(mCurrentPhotoPath);
            Uri outputFileUri = Uri.fromFile(image);
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            return cameraIntent;
        }
        return null;
    }


    public static void galleryAddPic(Context context, String mCurrentPhotoPath) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        Log.d("BenD", contentUri.toString());
        mediaScanIntent.setData(contentUri);
        context.sendBroadcast(mediaScanIntent);
    }

    public static void setPic(ImageView mImageView, String mCurrentPhotoPath) {
        // Get the dimensions of the View
        int targetW = mImageView.getWidth();
        int targetH = mImageView.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);
        Log.d("BenD", photoW + " x " + photoH);
        Log.d("BenD", "Scale factor: " + String.valueOf(scaleFactor));
        Log.d("BenD", targetW + " x " + targetH);
        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        mImageView.setImageBitmap(bitmap);
    }
}