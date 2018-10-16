package xie.com.androidutils;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import xie.com.androidutils.Activities.MeterDesignerActivity;
import xie.com.androidutils.ImageLoader.ImageLoaderActivity;
import xie.com.androidutils.Timer.TimerActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Button mTimerButton = null;
    private Button mMd = null;
    private Button mImageLoader = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTimerButton = findViewById(R.id.timer);
        mTimerButton.setOnClickListener(this);
        mMd = findViewById(R.id.md);
        mMd.setOnClickListener(this);
        mImageLoader = findViewById(R.id.imageloader);
        mImageLoader.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.timer:
                startActivity(new Intent(this, TimerActivity.class));
                finish();
                break;
            case R.id.md:
                startActivity(new Intent(this,MeterDesignerActivity.class));
                break;
            case R.id.imageloader:
                startActivity(new Intent(this,ImageLoaderActivity.class));
                break;
        }

    }
}
