package xie.com.androidutils.MeterDesigner;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import xie.com.androidutils.R;
import xie.com.androidutils.Utils.ToastUtils;

public class RecyclerViewTestActivity extends AppCompatActivity {

    @InjectView(R.id.rv)
    RecyclerView rv;
    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    private ArrayList<String> mDatas;
    private HomeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view_test);
        initData();
        ButterKnife.inject(this);
        adapter = new HomeAdapter();
        toolbar.inflateMenu(R.menu.toolbar_menu);//关联目录
        rv.setItemAnimator(new DefaultItemAnimator());
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                int id = menuItem.getItemId();
                switch (id){
                    case R.id.search:
                        ToastUtils.showToast(RecyclerViewTestActivity.this,"查找");
                        break;
                    case R.id.share:
                        ToastUtils.showToast(RecyclerViewTestActivity.this,"分享");
                        break;
                    case R.id.demo_one:
                        ToastUtils.showToast(RecyclerViewTestActivity.this,"样式一");
                        rv.setLayoutManager(new LinearLayoutManager(RecyclerViewTestActivity.this));// 线性管理器，支持横向、纵向。
//                        rv.addItemDecoration(new DividerItemDecoration(RecyclerViewTestActivity.this, DividerItemDecoration.VERTICAL_LIST));
                        break;
                    case R.id.demo_two:
                        ToastUtils.showToast(RecyclerViewTestActivity.this,"样式二");
                        rv.setLayoutManager(new GridLayoutManager(RecyclerViewTestActivity.this, 4));// 网格布局管理器
//                        rv.addItemDecoration(new DividerGridItemDecoration(RecyclerViewTestActivity.this));
                        break;
                    case R.id.demo_three:
                        rv.setLayoutManager(new StaggeredGridLayoutManager(4,
                                StaggeredGridLayoutManager.HORIZONTAL));// 瀑布流水平
                        break;
                    case R.id.demo_four:
                        rv.setLayoutManager(new StaggeredGridLayoutManager(4,
                                StaggeredGridLayoutManager.VERTICAL));// 瀑布流垂直
                        break;

                    case R.id.add:
                        adapter.addData(1);
                        break;
                    case R.id.remove:
                        adapter.removeData(1);
                        break;
                }
                rv.setAdapter(new HomeAdapter());
                return false;
            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showToast(RecyclerViewTestActivity.this,"导航图标被点击");
            }
        });
        rv.setLayoutManager(new LinearLayoutManager(this));// 线性管理器，支持横向、纵向。
        rv.setAdapter(adapter);
        rv.addItemDecoration(new DividerGridItemDecoration(RecyclerViewTestActivity.this));

    }

    class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHoder> {
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

        public void addData(int pos){
            mDatas.add(pos,"add One");
            notifyItemInserted(pos);
        }

        public void removeData(int pos){
            mDatas.remove(pos);
            notifyItemRemoved(pos);
        }

        class MyViewHoder extends RecyclerView.ViewHolder {
            TextView tv;

            public MyViewHoder(@NonNull View itemView) {
                super(itemView);
                tv = (TextView) itemView.findViewById(R.id.id_num);
            }
        }
    }


    private void initData() {
        mDatas = new ArrayList<String>();
        for(int j = 0;j<5;j++){
            for (int i = 'A'; i < 'Z'; i++) {
                mDatas.add("" + (char) i);
            }
        }
    }
}
