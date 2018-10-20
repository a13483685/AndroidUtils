package xie.com.androidutils.IPC.BindTest;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 服务端代码，采用观察者模式，如果服务端有新的数据产生，那么会通知观察者数据发生了变化
 */
public class HouseManagerService extends Service {
    private static final String TAG = "BMS";
    private CopyOnWriteArrayList<House> mHouseList = new CopyOnWriteArrayList<House>();
    private CopyOnWriteArrayList<IOnNewHouseArrivedListener> mListenerList = new CopyOnWriteArrayList<IOnNewHouseArrivedListener>();
    private AtomicBoolean mIsServiceDestoryed = new AtomicBoolean(false);

    private Binder mBinder = new IHouseInterface.Stub() {
        @Override
        public List<House> getHouseList() throws RemoteException {
            return mHouseList;
        }

        @Override
        public void addHouse(House house) throws RemoteException {
            mHouseList.add(house);
        }

        @Override
        public void registerListener(IOnNewHouseArrivedListener listener) throws RemoteException {
            if (!mListenerList.contains(listener)) {
                mListenerList.add(listener);
            } else {
                Log.d(TAG, "already exits");
            }
            Log.d(TAG, "registerListener,size:" + mListenerList.size());

        }

        @Override
        public void unregisterListener(IOnNewHouseArrivedListener listener) throws RemoteException {
            if (mListenerList.contains(listener)) {
                mListenerList.remove(listener);
                Log.d(TAG, "unregister success.");
            } else {
                Log.d(TAG, "not found ,can not register.");
            }
            Log.d(TAG, "not found ,can not register.");
        }
    };

    public HouseManagerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mHouseList.add(new House("wuhan", "1000000", "xiezheng"));
        mHouseList.add(new House("huangshi", "500000", "xiezheng"));
        new Thread(new ServiceWorker()).start();
    }

    @Override
    public void onDestroy() {
        mIsServiceDestoryed.set(true);
        super.onDestroy();
    }

    /**
     * 在后台运行，没5S创建一个对象，然后通知观察者有新的数据到
     */
    private class ServiceWorker implements Runnable{

        @Override
        public void run() {
            while (!mIsServiceDestoryed.get()){
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int houseId = mHouseList.size()+1;
                House newHouse = new House("beijing","10000000","xiezheng");
                try {
                    onNewBookArrived(newHouse);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 通知每一个客户端有新的house加入
     * @param newHouse
     * @throws RemoteException
     */
    private void onNewBookArrived(House newHouse) throws RemoteException{
        mHouseList.add(newHouse);
        for (int i=0;i<mListenerList.size();i++){
            IOnNewHouseArrivedListener listener = mListenerList.get(i);
            Log.d(TAG,"OnNewBookArried,notify listener:"+listener);
            listener.onNewHouseArrived(newHouse);
        }
    }
}
