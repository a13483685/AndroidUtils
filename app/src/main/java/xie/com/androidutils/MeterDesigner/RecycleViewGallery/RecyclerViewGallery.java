package xie.com.androidutils.MeterDesigner.RecycleViewGallery;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.ButterKnife;
import butterknife.InjectView;
import xie.com.androidutils.R;

public class RecyclerViewGallery extends AppCompatActivity {

    @InjectView(R.id.rv_gallery)
    RecyclerView rvGallery;
    private ArrayList<Integer> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view_gallery);
        ButterKnife.inject(this);
        initData();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        rvGallery.setLayoutManager(linearLayoutManager);

        rvGallery.setAdapter(new GalleryAdapter(this,list));
    }

    private void initData() {
        list = new ArrayList<>(Arrays.asList(R.drawable.a, R.drawable.b, R.drawable.c, R.drawable.d, R.drawable.e, R.drawable.f));
    }
}
