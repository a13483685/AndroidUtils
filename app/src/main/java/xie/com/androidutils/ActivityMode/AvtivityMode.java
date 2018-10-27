package xie.com.androidutils.ActivityMode;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import xie.com.androidutils.R;

public class AvtivityMode extends AppCompatActivity {

    @InjectView(R.id.singleTop)
    Button singleTop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avtivity_mode);
        ButterKnife.inject(this);
    }

    @OnClick(R.id.singleTop)
    public void onClick() {
        startActivity(new Intent(this,singleTopActivity.class));
    }
}
