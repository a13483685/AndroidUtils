package xie.com.androidutils.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import xie.com.androidutils.MeterDesigner.RecycleViewGallery.RecyclerViewGallery;
import xie.com.androidutils.MeterDesigner.RecyclerViewTestActivity;
import xie.com.androidutils.R;

public class MeterDesignerActivity extends AppCompatActivity {

    @InjectView(R.id.cardViewTest)
    Button cardViewTest;
    @InjectView(R.id.RecyclerView)
    Button RecyclerView;
//    @InjectView(R.id.RecyclerViewGallerybt)
//    Button RecyclerViewGallerybt;
//    @InjectView(R.id.RecyclerView)
//    Button RecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meter_designer);
        ButterKnife.inject(this);
    }

    @OnClick({R.id.cardViewTest, R.id.RecyclerView})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cardViewTest:
                startActivity(new Intent(this, CardViewActitivy.class));
                break;
            case R.id.RecyclerView:
                startActivity(new Intent(this, RecyclerViewTestActivity.class));
                break;
//            case R.id.RecyclerViewGallerybt:
//                startActivity(new Intent(this, RecyclerViewGallery.class));
//                break;
        }
    }
}
