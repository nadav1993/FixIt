package com.hadarfem.fixit.cache;

import android.graphics.Bitmap;

/**
 * Created by Tzach & Nadav on 2/6/2018.
 */

public interface IPictureCache {
    void addToCache(String url, Bitmap bitmap);

    boolean isInCache(String url);

    Bitmap getFromCache(String url);
}
