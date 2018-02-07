package com.hadarfem.fixit.cache;

import android.graphics.Bitmap;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tzach & Nadav on 2/6/2018.
 */

public class MemoryPictureCache implements IPictureCache {
    private Map<String, Bitmap> pictureMap;

    public MemoryPictureCache() {
        pictureMap = new HashMap<>();
    }

    @Override
    public void addToCache(String url, Bitmap bitmap) {
        if (!isInCache(url)) {
            pictureMap.put(url, bitmap);
        }
    }

    @Override
    public boolean isInCache(String url) {
        return pictureMap.containsKey(url);
    }

    @Override
    public Bitmap getFromCache(String url) {
        return pictureMap.get(url);
    }
}
