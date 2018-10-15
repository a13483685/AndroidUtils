package xie.com.androidutils.ImageLoader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.os.StatFs;
import android.util.Log;
import android.util.LruCache;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import xie.com.androidutils.Utils.MyUtils;

public class ImageLoader {
    private static final int IO_BUFFER_SIZE = 1024*8;
    private final String TAG = "ImageLoader";
    private static final int DISK_CACHE_SIZE = 1024*1024*50;
    private boolean mIsDiskLruCacheCreated = false;
    private LruCache<String,Bitmap> mMemoryCache;
    private DiskLruCache mDisLruCache;
    private Context mContext;
    private int DISK_CACHE_INDEX = 0;

    private ImageLoader(Context context){
        mContext = context.getApplicationContext();
        int maxMemory = (int) (Runtime.getRuntime().maxMemory()/1024);
        int cacheSize = maxMemory/8;
        mMemoryCache = new LruCache<String, Bitmap>(cacheSize){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes()*value.getHeight()/1024;
            }
        };
        File diskCacheDir = getDiskCacheDir(mContext,"bitmap");
        Log.d(TAG,"diskCacheDir is :"+diskCacheDir);
        if(!diskCacheDir.exists()){
            diskCacheDir.mkdir();
        }

        if(getUsableSpace(diskCacheDir)>DISK_CACHE_SIZE){
            try {
                DiskLruCache.open(diskCacheDir,1,1,DISK_CACHE_SIZE);
                mIsDiskLruCacheCreated = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static ImageLoader build(Context context){
        return new ImageLoader(context);
    }

    private long getUsableSpace(File path) {
        if(Build.VERSION.SDK_INT> Build.VERSION_CODES.GINGERBREAD){
            return path.getUsableSpace();
        }
        StatFs stats = new StatFs(path.getPath());
        return (long) stats.getBlockSize()*(long) stats.getAvailableBlocks();
    }

    private File getDiskCacheDir(Context mContext, String uniqueName) {
        final String cachePath ;
        boolean externalStorageAvailable = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        if(externalStorageAvailable){
            cachePath = mContext.getExternalCacheDir().getPath();
        }else {
            cachePath = mContext.getCacheDir().getPath();
        }
        return new File(cachePath+File.pathSeparator + uniqueName);
    }

    private void addBitMapToMemoeryCache(String key,Bitmap bitmap){
        if(getBitmapFromMemCache(key)==null){
            mMemoryCache.put(key,bitmap);
        }
    }

    private Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }

    private Bitmap loadBitmapFromHttp(String url,int reqWidth,int reqHeight) throws IOException{
        if(Looper.myLooper() == Looper.getMainLooper()){
            throw new RuntimeException("can not visit network from UI Thread.");
        }
        if(mDisLruCache == null){
            return null;
        }
        String key = hashKeyFormUrl(url);
        //磁盘缓存的添加通过Editor来完成，其中的commit和abort来提交和撤销对文件系统的读写
        DiskLruCache.Editor edit = mDisLruCache.edit(key);
        if(edit!=null){
            OutputStream outputStream = edit.newOutputStream(DISK_CACHE_INDEX);
            if(downloadUrlToStream(url,outputStream)!=null){
                edit.commit();
            } else {
                edit.abort();
            }
            mDisLruCache.flush();
        }
        return null;
//        return loadBitmapFromDiskCache(url,reqWidth,reqHeight);
    }

    private boolean downloadUrlToStream(String url, OutputStream outputStream) {
        HttpURLConnection httpURLConnection = null;
        BufferedInputStream bufferedInputStream = null;
        BufferedOutputStream bufferedOutputStream = null;

        try {
            URL url1 = new URL(url);
            httpURLConnection = (HttpURLConnection) url1.openConnection();
            bufferedInputStream = new BufferedInputStream(httpURLConnection.getInputStream());
            bufferedOutputStream = new BufferedOutputStream(outputStream);
            int b;
            while ((b=bufferedInputStream.read())!=-1){
                bufferedOutputStream.write(b);
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(httpURLConnection !=null){
                httpURLConnection.disconnect();
            }
            MyUtils.close(bufferedInputStream);
            MyUtils.close(bufferedOutputStream);
        }
        return false;
    }

    private Bitmap downloadBitmapFromUrl(String url, OutputStream outputStream) {
        Bitmap bitmap = null;
        HttpURLConnection urlConnection = null;
        BufferedInputStream bufferedInputStream = null;

        try {
            final URL url1 = new URL(url);
            urlConnection = (HttpURLConnection) url1.openConnection();
            bufferedInputStream = new BufferedInputStream(urlConnection.getInputStream(),IO_BUFFER_SIZE);
            bitmap = BitmapFactory.decodeStream(bufferedInputStream);
        } catch (IOException e) {
            Log.e(TAG, "Error in downloadBitmap: " + e);
        }finally {
            if(urlConnection !=null){
                urlConnection.disconnect();
            }
            MyUtils.close(bufferedInputStream);
        }
        return bitmap;
    }

    private String hashKeyFormUrl(String url) {

    }


}
