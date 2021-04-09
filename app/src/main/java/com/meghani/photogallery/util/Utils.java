package com.meghani.photogallery.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.meghani.photogallery.R;
import com.meghani.photogallery.databinding.SingleImageItemBinding;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by kunchok on 09/03/2021
 */
public class Utils {
    public static String API_KEY = "21054423-16861b903340d32f830e0cfcd";
    public static String BASE_URL = "https://pixabay.com/";

    public static ColorDrawable[] vibrantLightColorList =
            {
                    new ColorDrawable(Color.parseColor("#ffeead")),
                    new ColorDrawable(Color.parseColor("#93cfb3")),
                    new ColorDrawable(Color.parseColor("#fd7a7a")),
                    new ColorDrawable(Color.parseColor("#faca5f")),
                    new ColorDrawable(Color.parseColor("#1ba798")),
                    new ColorDrawable(Color.parseColor("#6aa9ae")),
                    new ColorDrawable(Color.parseColor("#ffbf27")),
                    new ColorDrawable(Color.parseColor("#d93947"))
            };

    public static ColorDrawable getRandomDrawableColor() {
        int idx = new Random().nextInt(vibrantLightColorList.length);
        return vibrantLightColorList[idx];
    }

    public static void downloadImage(Context context, String imageUrl, int imageId) {
        if (!ifImageExists(imageId, context)) {
            Glide.with(context)
                    .asBitmap()
                    .load(imageUrl)
                    .into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL,Target.SIZE_ORIGINAL) {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            saveImage(context, resource, imageId);
                        }

                    });
        }
    }



    private static String saveImage(Context context, Bitmap image, int imageId) {
        String savedImagePath = null;

        String imageFileName = "PhotoGallery_" + imageId + ".jpg";
        File storageDir = new File(            context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                + "/PhotoGallery");
        boolean success = true;
        if (!storageDir.exists()) {
            success = storageDir.mkdirs();
        }
        if (success) {
            File imageFile = new File(storageDir, imageFileName);
            savedImagePath = imageFile.getAbsolutePath();
            try {
                OutputStream fOut = new FileOutputStream(imageFile);
                image.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                fOut.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Add the image to the system gallery
            galleryAddPic(savedImagePath, context);
            Toast.makeText(context, "Image Saved", Toast.LENGTH_LONG).show();
        }
        return savedImagePath;
    }


    private static void galleryAddPic(String imagePath, Context context) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(imagePath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        context.sendBroadcast(mediaScanIntent);
    }

    public static boolean ifImageExists(int imageId, Context context) {

        String imageFileName = "PhotoGallery_" + imageId + ".jpg";
        File storageDir = new File(            context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                + "/PhotoGallery/"+imageFileName);
        return storageDir.exists();
    }
}
