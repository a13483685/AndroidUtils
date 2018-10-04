package xie.com.androidutils.Designer.StoryXiaoming;

import android.graphics.Bitmap;
import android.util.LruCache;

public class ImageCache {
    LruCache<String, Bitmap> mImageCache = null;//设置图片缓存

    public ImageCache() {
        int cacheSzie = (int) Runtime.getRuntime().maxMemory() / 1024;//取内存的四分之一为图片缓存
        mImageCache = new LruCache<String, Bitmap>(cacheSzie/4){
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getRowBytes()*bitmap.getHeight()/1024;
            }
        };
    }
    public void put(String url,Bitmap bitmap){
        mImageCache.put(url,bitmap);
    }
    public Bitmap get(String url){
        return mImageCache.get(url);
    }

}
