package com.hadarfem.fixit.tasks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.hadarfem.fixit.cache.IPictureCache;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Tzach & Nadav on 2/6/2018.
 */

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    private ImageView imageView;
    private int scaledWidthPx;
    private int scaledHeightPx;
    private IPictureCache pictureCache;

    public DownloadImageTask(ImageView imageView, IPictureCache pictureCache, int scaledWidthPx, int scaledHeightPx) {
        this.imageView = imageView;
        this.pictureCache = pictureCache;
        this.scaledWidthPx = scaledWidthPx;
        this.scaledHeightPx = scaledHeightPx;
    }

    protected Bitmap doInBackground(String... urls) {
        if (pictureCache.isInCache(urls[0])) {
            return pictureCache.getFromCache(urls[0]);
        }

        Bitmap bitmap = getBitmapFromURL(urls[0]);

        pictureCache.addToCache(urls[0], bitmap);

        return bitmap;
    }

    protected void onPostExecute(Bitmap result) {
        if (imageView != null && result != null) {
            result = Bitmap.createScaledBitmap(result, scaledWidthPx, scaledHeightPx, false);

            imageView.setImageBitmap(result);
        }
    }

    private Bitmap getBitmapFromURL(String urlString) {
        HttpURLConnection connection = null;

        try {
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();

            Bitmap fetchedImage = BitmapFactory.decodeStream(input);

            return fetchedImage;
        } catch (Exception e) {
            Log.w("Image Fetch", e);
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}