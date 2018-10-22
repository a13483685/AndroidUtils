package xie.com.androidutils.IPC.Parcel;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import xie.com.androidutils.R;
import xie.com.androidutils.Utils.ToastUtils;

public class ParcelSecondActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_empty);
        Intent intent = getIntent();
        User user = intent.getParcelableExtra("ParcelTest");
        ToastUtils.showToast(this,""+user.toString());
    }
}
