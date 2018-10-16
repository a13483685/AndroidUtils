package xie.com.androidutils.ImageLoader;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.FileDescriptor;

public class ImageResizer {
    private static final String TAG = "ImageResizer";
    public ImageResizer(){

    }

    public Bitmap decodeSamepleBitmapFromResource(Resources res,int resId,int reqWidth,int regHight){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds=true;
        BitmapFactory.decodeResource(res,resId,options);
        options.inSampleSize = calculateInSample(options,reqWidth,regHight);
        options.inJustDecodeBounds=false;
        return BitmapFactory.decodeResource(res,resId,options);
    }

    public Bitmap decodeSampledBitmapFromFileDescriptor(FileDescriptor fd,int reqWidth,int reqHeight){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFileDescriptor(fd,null,options);
        options.inSampleSize = calculateInSample(options,reqWidth,reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFileDescriptor(fd,null,options);
    }

    private int calculateInSample(BitmapFactory.Options options, int reqWidth, int regHight) {
        if(reqWidth == 0 || regHight==0){
            return 1;
        }
        int width = options.outWidth;
        int height = options.outHeight;
        int samleSize = 1;
        if(width<reqWidth || height<regHight){
            int halfWidth = width / 2;
            int halfHigh = height / 2;
            while ((halfWidth/samleSize)>reqWidth && ((halfHigh/samleSize)>regHight)){
                samleSize*=2;
            }
        }
        Log.d(TAG,"sampleSize:"+samleSize);
        return samleSize;
    }
}
