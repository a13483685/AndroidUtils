package xie.com.androidutils.MeterDesigner;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import xie.com.androidutils.R;

public class RecyclerViewTestActivity extends AppCompatActivity {

    @InjectView(R.id.rv)
    RecyclerView rv;
    private ArrayList<String> mDatas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view_test);
        initData();
        ButterKnife.inject(this);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(new HomeAdapter());
        rv.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL_LIST));
    }

    class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHoder>{


        @NonNull
        @Override
        public MyViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
            View view = LayoutInflater.from(RecyclerViewTestActivity.this).inflate(R.layout.item_home, parent, false);
            MyViewHoder myViewHoder = new MyViewHoder(view);
            return myViewHoder;
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHoder hoder, int pos) {
            hoder.tv.setText(mDatas.get(pos));
        }

        @Override
        public int getItemCount() {
            return mDatas.size();
        }

        class MyViewHoder extends RecyclerView.ViewHolder{
            TextView tv ;
            public MyViewHoder(@NonNull View itemView) {
                super(itemView);
                tv = (TextView) itemView.findViewById(R.id.id_num);
            }
        }
    }



    private void initData() {
        mDatas = new ArrayList<String>();
        for (int i='A';i<'Z';i++){
            mDatas.add("" + (char) i);
        }
    }
}
