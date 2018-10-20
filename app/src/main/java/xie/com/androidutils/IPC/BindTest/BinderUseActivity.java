package xie.com.androidutils.IPC.BindTest;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import xie.com.androidutils.R;
import xie.com.androidutils.Utils.ToastUtils;

/**
 * 客戶端代碼
 */
public class BinderUseActivity extends AppCompatActivity {
    private static final String TAG = "BinderUseActivity";
    private static final int MESSAGE_NEW_ARRIVED = 1;

    @InjectView(R.id.add)
    Button add;
    private IHouseInterface iHouseManager;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_NEW_ARRIVED:
                    Log.d(TAG, "recevied new house" + msg.obj);
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    };


    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            iHouseManager = IHouseInterface.Stub.asInterface(service);
            try {
                List<House> houseList = iHouseManager.getHouseList();
                Log.i(TAG, "query book list,list type :" + houseList.getClass().getCanonicalName());
                Log.i(TAG, "query book list,list type :" + houseList.toString());
                ToastUtils.showToast(BinderUseActivity.this, "query book list,list type :" + houseList.getClass().getCanonicalName());
                ToastUtils.showToast(BinderUseActivity.this, "query book list,list type :" + houseList.toString());
                iHouseManager.registerListener(mOnNewHouseArrivedListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            iHouseManager = null;
            Log.i(TAG, "onServiceDisconnected:binder died");
        }
    };

    private IOnNewHouseArrivedListener mOnNewHouseArrivedListener = new IOnNewHouseArrivedListener.Stub() {
        @Override
        public void onNewHouseArrived(House newHouse) throws RemoteException {
                  mHandler.obtainMessage(MESSAGE_NEW_ARRIVED,newHouse).sendToTarget();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binder_use);
        ButterKnife.inject(this);
        Intent intent = new Intent(this, HouseManagerService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        if(iHouseManager!=null&&iHouseManager.asBinder().isBinderAlive()){
            try {
                Log.d(TAG, "unregister listener:"+mOnNewHouseArrivedListener);
                iHouseManager.unregisterListener(mOnNewHouseArrivedListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        unbindService(mConnection);
        super.onDestroy();
    }

    @OnClick(R.id.add)
    public void onClick() {
        House house = new House("武汉", "3000000", "解铮");
        try {
            iHouseManager.addHouse(house);
            List<House> houseList = iHouseManager.getHouseList();
            Log.i(TAG, "content is :" + houseList.toString());

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
