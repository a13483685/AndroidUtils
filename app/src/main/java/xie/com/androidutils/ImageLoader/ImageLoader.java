package xie.com.androidutils.ImageLoader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.media.audiofx.AutomaticGainControl;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.StatFs;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.LruCache;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import xie.com.androidutils.R;
import xie.com.androidutils.Utils.MyUtils;

public class ImageLoader {
    private static final int IO_BUFFER_SIZE = 1024*8;
    private final String TAG = "ImageLoader";
    private static final int DISK_CACHE_SIZE = 1024*1024*50;
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int CORE_POOL_SIZE =CPU_COUNT+1;//核心线程的个数
    private static final int MAXIMUM_POOL_SIZE =2*CPU_COUNT+1;//最大线程池的数量
    private static final Long KEEP_ALEVE =10L;
    public static final int MESSAGE_POST_RESULT = 1;

    private boolean mIsDiskLruCacheCreated = false;
    private LruCache<String,Bitmap> mMemoryCache;
    private DiskLruCache mDisLruCache;
    private Context mContext;
    private int DISK_CACHE_INDEX = 0;
    private ImageResizer mImageResizer = new ImageResizer();
    private static final int TAG_KEY_URI = R.id.imageloader_uri;

    private static final ThreadFactory sThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);
        @Override
        public Thread newThread(@NonNull Runnable r) {
            return new Thread(r,"ImageLoader#"+mCount);
        }
    };
    public static final Executor THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(CORE_POOL_SIZE,MAXIMUM_POOL_SIZE,
            KEEP_ALEVE, TimeUnit.SECONDS,new LinkedBlockingDeque<Runnable>(),sThreadFactory);

    private Handler mMainHandle = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            LoaderResult result = (LoaderResult) msg.obj;
            ImageView imageView = result.imageView;
            String uri = (String) imageView.getTag(TAG_KEY_URI);
            if(uri.equals(result.uri)){
                imageView.setImageBitmap(result.bitmap);
            }else {
                Log.w(TAG, "set image bitmap,but url has changed, ignored!");
            }
        }
    };

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
                mDisLruCache = DiskLruCache.open(diskCacheDir,1,1,DISK_CACHE_SIZE);
                mIsDiskLruCacheCreated = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static ImageLoader build(Context context){
        return new ImageLoader(context);
    }

    /**
     * 同步加载接口，先冲缓存中加载图片，然后再在磁盘中加载图片，最后在网络中加载图片
     * @param uri
     * @param reqWidth
     * @param reqHeigh
     * @return
     */
    public Bitmap loadBitmap(String uri,int reqWidth,int reqHeigh){
        Bitmap bitmap = null;
        bitmap = loadBitmapFromMemCache(uri);
        if(bitmap !=null){
            Log.d(TAG, "loadBitmapFromMemCache,url:" + uri);
            return bitmap;
        }
        try {
            bitmap= loadBitmapFromDiskCache(uri, reqWidth, reqHeigh);
            if (bitmap != null) {
                Log.d(TAG, "loadBitmapFromDisk,url:" + uri);
                return bitmap;
            }
            bitmap = loadBitmapFromHttp(uri, reqWidth, reqHeigh);
            Log.d(TAG, "loadBitmapFromHttp,url:" + uri);

        } catch (IOException e) {
            e.printStackTrace();
        }
        if(bitmap==null&&mIsDiskLruCacheCreated){
            Log.w(TAG, "encounter error, DiskLruCache is not created.");
            bitmap = downloadBitmapFromUrl(uri);
        }

        return bitmap;
    }

    /**
     * 从内存、缓存、网络中获取bitmap ,将bitmap绑定给imageView
     * @param uri
     * @param imageView
     */
    public void bindBitmap(final String uri,final ImageView imageView){
        bindBitmap(uri, imageView,0,0);
    }

    public void bindBitmap(final String uri, final ImageView imageView,
               final int reqWidth, final int reqHeight){
        imageView.setTag(TAG_KEY_URI, uri);
        Bitmap bitmap = loadBitmapFromMemCache(uri);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
            return;
        }
        Runnable loadBitmapTask = new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = loadBitmap(uri, reqWidth, reqHeight);
                if (bitmap != null) {
                    LoaderResult result = new LoaderResult(imageView, uri, bitmap);
                    mMainHandle.obtainMessage(MESSAGE_POST_RESULT, result).sendToTarget();
                }
            }
        };
        THREAD_POOL_EXECUTOR.execute(loadBitmapTask);
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

    /***
     * 从内存缓存中读取
     * @param url
     * @return
     */
    private Bitmap loadBitmapFromMemCache(String url){
        String key = hashKeyFormUrl(url);//先将url进行MD5加密，为了防止连接之中有中文
        Bitmap bitmap = null;
        if(key==null){
            return null;
        }
        bitmap = getBitmapFromMemCache(key);
        if(bitmap==null){
            return null;
        }
        return bitmap;
    }

    /**
     * 从网络获取
     * @param url
     * @param reqWidth
     * @param reqHeight
     * @return
     * @throws IOException
     */
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
            if(downloadUrlToStream(url,outputStream)){
                edit.commit();
            } else {
                edit.abort();
            }
            mDisLruCache.flush();
        }
        return loadBitmapFromDiskCache(url,reqWidth,reqHeight);
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

    private Bitmap downloadBitmapFromUrl(String url) {
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
    //从磁盘的缓存中读取图片
    private Bitmap loadBitmapFromDiskCache(String url,int reqWidth,int reqHeight) throws IOException{
        if(Looper.myLooper()==Looper.getMainLooper()){
            throw new RuntimeException("can not visit network from UI Thread.");
        }
        if(mDisLruCache==null){
            return null;
        }
        Bitmap bitmap = null;
        String key = hashKeyFormUrl(url);
        DiskLruCache.Snapshot snapshot = mDisLruCache.get(key);
        if(snapshot!=null){
            FileInputStream fileInputStream = (FileInputStream) snapshot.getInputStream(DISK_CACHE_INDEX);
            FileDescriptor fileDescriptor = fileInputStream.getFD();//用来压缩图片
            bitmap = mImageResizer.decodeSampledBitmapFromFileDescriptor(fileDescriptor,reqWidth,reqHeight);
            if(bitmap!=null){
                addBitMapToMemoeryCache(key,bitmap);
            }
        }
        return bitmap;
    }

//将网络连接的网址通过MD5来加密，避免链接中出现中文
    private String hashKeyFormUrl(String url) {
        String cacheKey;
        try {
            MessageDigest  messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(url.getBytes());
            cacheKey = bytesToHexString(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(url.hashCode());
            e.printStackTrace();
        }
        return cacheKey;
    }
//byte[]转String
    private String bytesToHexString(byte[] digest) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i =0;i<digest.length;i++){
            String s = Integer.toHexString(0xFF & digest[i]);
            if(s.length()==1){
                stringBuffer.append(s);
            }
            stringBuffer.append(s);
        }
        return stringBuffer.toString();
    }

    private static class LoaderResult{
        public ImageView imageView;
        public String uri;
        public Bitmap bitmap;

        public LoaderResult(ImageView imageView, String uri, Bitmap bitmap) {
            this.imageView = imageView;
            this.uri = uri;
            this.bitmap = bitmap;
        }
    }
}
