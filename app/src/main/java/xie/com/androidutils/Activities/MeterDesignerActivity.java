package xie.com.androidutils.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import xie.com.androidutils.R;

public class MeterDesignerActivity extends AppCompatActivity {

    @InjectView(R.id.cardViewTest)
    Button cardViewTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meter_designer);
        ButterKnife.inject(this);
    }

    @OnClick(R.id.cardViewTest)
    public void onClick() {
        startActivity(new Intent(this,CardViewActitivy.class));
    }
}
