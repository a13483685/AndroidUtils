package xie.com.androidutils.IPC.BindTest;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class HouseManagerService extends Service {
    private static final String TAG ="BMS";
    private CopyOnWriteArrayList<House> mHouseList = new CopyOnWriteArrayList<House>();
    private Binder mBinder = new IHouseInterface.Stub() {
        @Override
        public List<House> getHouseList() throws RemoteException {
            return mHouseList;
        }

        @Override
        public void addHouse(House house) throws RemoteException {
            mHouseList.add(house);
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
        mHouseList.add(new House("wuhan","1000000","xiezheng"));
        mHouseList.add(new House("huangshi","500000","xiezheng"));
    }
}
