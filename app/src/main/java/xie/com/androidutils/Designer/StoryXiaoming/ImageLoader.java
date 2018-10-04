package xie.com.androidutils.Designer.StoryXiaoming;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.LruCache;
import android.widget.ImageView;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ImageLoader {
    ImageCache mImageCache = null;//设置图片缓存
    ExecutorService executorService;

    public ImageLoader() {
        executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    }

    public void displayImage(final String url, final ImageView imageView) {
        Bitmap bitmap = mImageCache.get(url);
        if (bitmap != null) {//如果图片在缓存中
            imageView.setImageBitmap(bitmap);
            return;
        }
        //如果不在缓存 那么下载
        imageView.setTag(url);
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                Bitmap bm = downLoadImage(url);
                if (bm == null) {
                    return;
                }
                if (imageView.getTag().equals(url)) {
                    imageView.setImageBitmap(bm);
                }
                mImageCache.put(url, bm);
            }
        });
    }

    private Bitmap downLoadImage(String url) {
        Bitmap bitmap = null;
        try {
            URL mUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) mUrl.getContent();
            bitmap = BitmapFactory.decodeStream(connection.getInputStream());
            connection.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
