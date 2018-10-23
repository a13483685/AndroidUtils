package xie.com.androidutils.IPC;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import xie.com.androidutils.IPC.BindTest.BinderUseActivity;
import xie.com.androidutils.IPC.ContentProviderTest.ProviderActivity;
import xie.com.androidutils.IPC.Parcel.ParcelTestActivity;
import xie.com.androidutils.R;

/**
 * IPC相关的使用
 */
public class IpcActivity extends Activity {
    @InjectView(R.id.pacelable_test)
    Button pacelableTest;
    @InjectView(R.id.bind_use)
    Button bindUse;
    @InjectView(R.id.contentProvider)
    Button contentProvider;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ipc_activity);
        ButterKnife.inject(this);
    }

    @OnClick({R.id.pacelable_test, R.id.bind_use,R.id.contentProvider})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pacelable_test:
                //Parcel的使用
                startActivity(new Intent(this, ParcelTestActivity.class));
                break;
            case R.id.bind_use:
                //binder的使用
                startActivity(new Intent(this, BinderUseActivity.class));
                break;

            case R.id.contentProvider:
                startActivity(new Intent(this,ProviderActivity.class));
                break;

        }

    }

}
