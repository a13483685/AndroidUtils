package xie.com.androidutils.IPC.BinderPool;

import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import xie.com.androidutils.R;

public class BindPoolActivity extends AppCompatActivity {

    @InjectView(R.id.bind_pool_work)
    Button bindPoolWork;
    private static final String TAG = "BindPoolActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_pool);
        ButterKnife.inject(this);
    }

    @OnClick(R.id.bind_pool_work)
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bind_pool_work:
                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        dowork();
                    }
                }.start();
                break;
        }
    }

    private void dowork() {
        BinderPool binderPool = BinderPool.getInstance(this);
        IBinder securityBinder = binderPool.queryBinder(BinderPool.BINDER_SECURITY_CENTER);
        ISecurityCenter iSecurityCenter =SecurityCenterImpl.asInterface(securityBinder);
        Log.d(TAG,"visit ISecurityCenter");
        String msg = "hello android";
        Log.d(TAG,"msg is :"+msg);
        try {
            String password = iSecurityCenter.decrypt(msg);
            Log.d(TAG,"password is :"+password);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        Log.d(TAG,"visit ICompute");
        IBinder computeBinder = binderPool.queryBinder(BinderPool.BINDER_COMPUTE);
        ICompute iCompute = ComputeImpl.asInterface(computeBinder);
        try {
            Log.d(TAG,"1+1 is :"+iCompute.add(1,1));
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }
}
