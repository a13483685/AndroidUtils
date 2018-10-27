package xie.com.androidutils.ActivityMode;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import xie.com.androidutils.R;
import xie.com.androidutils.Utils.ToastUtils;

public class singleTopActivity extends AppCompatActivity {

    @InjectView(R.id.singleTop_press)
    Button singleTopPress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_top);
        ButterKnife.inject(this);
    }

    @Override
    protected void onStart() {
        ToastUtils.showToast(this,"onStart");
        super.onStart();
    }

    @Override
    protected void onStop() {
        ToastUtils.showToast(this,"onStop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        ToastUtils.showToast(this,"onDestroy");
        super.onDestroy();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        ToastUtils.showToast(this,"onNewIntent");
        super.onNewIntent(intent);
    }

    @OnClick(R.id.singleTop_press)
    public void onClick() {
        startActivity(new Intent(this,singleTopActivity.class));
    }
}
