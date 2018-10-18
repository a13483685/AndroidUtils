package xie.com.androidutils.IPC.Parcel;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Button;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import xie.com.androidutils.R;


public class ParcelTestActivity extends Activity {
    @InjectView(R.id.send_pacel_data)
    Button sendPacelData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_parcel);
        ButterKnife.inject(this);
    }

    @NonNull
    private User initData() {
        House house = new House();
        house.setUserName("小解");
        house.setPrice("$1,000,000");
        house.setLocation("北京");
        User user = new User();
        user.setmHouse(house);
        user.setUserAge(29);
        user.setUserName("小解");
        user.setUserSex("男");
        return user;
    }

    @OnClick(R.id.send_pacel_data)
    public void onClick() {
        User user = initData();
        Intent intent = new Intent();
        intent.putExtra("ParcelTest", user);
        intent.setClass(this, ParcelSecondActivity.class);
        startActivity(intent);
    }
}
