package xie.com.androidutils.IPC.BinderPool;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

import java.util.concurrent.CountDownLatch;

/**
 * Binder连接池的绝体操作：
 * 在他内部首先要去绑定远程服务，绑定成功之后，客户端就可以通过它的queryBinder方法
 * 去获取各自对应的binder,拿到所需要的binder之后就可以进行各种操作了
 */
public class BinderPool {
    /***
     * binder连接池的具体实现，方式每一个aidl都要写一个service，这样应用程序会显得非常冗余
     */
    private static final String TAG = "BinderPool";
    public static final int BINDER_NONE = -1;
    public static final int BINDER_COMPUTE = 0;
    public static final int BINDER_SECURITY_CENTER = 1;

    private Context mContext = null;
    private IBinderPool mBinderPool;
    private static volatile BinderPool sInstance;
    private CountDownLatch mConnectBinderPoolCountDownLatch;

    private BinderPool(Context context) {
        mContext = context.getApplicationContext();
        connectBinderPoolService();

    }

    private ServiceConnection mBinderPoolConnect = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBinderPool = IBinderPool.Stub.asInterface(service);
            try {
                mBinderPool.asBinder().linkToDeath(mBinderPoolDeathRecipient,0);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            mConnectBinderPoolCountDownLatch.countDown();//连接成功，connectBinderPoolService才可以往下走，这个用法比较经典
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };


    private IBinder.DeathRecipient mBinderPoolDeathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            mBinderPool.asBinder().unlinkToDeath(mBinderPoolDeathRecipient, 0);
            mBinderPool = null;
            connectBinderPoolService();//远程服务断线重连
        }
    };

    public static BinderPool getInstance(Context context) {
        if (sInstance == null) {
            synchronized (BinderPool.class) {
                if (sInstance == null) {//此处要加2个判断
                    sInstance = new BinderPool(context);
                }
            }
        }
        return sInstance;
    }

    private void connectBinderPoolService() {
        mConnectBinderPoolCountDownLatch = new CountDownLatch(1);//初始化为1
        Intent service = new Intent(mContext, BinderPoolService.class);
        mContext.bindService(service, mBinderPoolConnect, Context.BIND_AUTO_CREATE);
        try {
            mConnectBinderPoolCountDownLatch.await();//进入阻塞状态，mConnectBinderPoolCountDownLatch为0的时候结束阻塞
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public IBinder queryBinder(int bindCode){
        IBinder binder = null;
        if(mBinderPool!=null){
            try {
                binder = mBinderPool.queryBinder(bindCode);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return binder;
    }

    /**
     * 请求转发的实现方法，当Binder连接池连接上远程服务时，会根据不同的binderCode来返回Binder对象
     * 通过这个binder对象所执行的操作，全部发生在远程客户端
     */
    public static class BinderPoolImpl extends IBinderPool.Stub {
        public BinderPoolImpl() {
            super();
        }

        @Override
        public IBinder queryBinder(int binderCode) throws RemoteException {
            IBinder binder = null;
            switch (binderCode) {
                case BINDER_SECURITY_CENTER:
                    binder = new SecurityCenterImpl();
                    break;
                case BINDER_COMPUTE:
                    binder = new ComputeImpl();
                    break;
                default:
                    break;
            }
            return binder;
        }
    }
}
