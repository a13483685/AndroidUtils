package xie.com.androidutils.IPC.Parcel;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import xie.com.androidutils.R;

/**
 * IPC相关的使用
 */
public class IpcActivity extends Activity {
    @InjectView(R.id.pacelable_test)
    Button pacelableTest;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ipc_activity);
        ButterKnife.inject(this);
    }

    @OnClick(R.id.pacelable_test)
    public void onClick() {
        startActivity(new Intent(this,ParcelTestActivity.class));
    }
}
