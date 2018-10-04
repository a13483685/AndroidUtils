package xie.com.androidutils.MeterDesigner.RecycleViewGallery;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import xie.com.androidutils.R;
import xie.com.androidutils.Utils.ToastUtils;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.MyviewHolder> {
    private LayoutInflater mInflater = null;
    private List<Integer> mDatas = null;
    public GalleryAdapter(Context context, List<Integer> datats){
        this.mInflater = LayoutInflater.from(context);
        this.mDatas = datats;
        for (Integer i:datats) {
            System.out.print("demo is ："+i);
        }
    }
    @NonNull
    @Override
    public GalleryAdapter.MyviewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.recycleview_item_gallery, viewGroup, false);
        MyviewHolder holder = new MyviewHolder(view);
        holder.image = view.findViewById(R.id.id_index_gallery_item_image);
        holder.tv = view.findViewById(R.id.index_gallery_item_tv);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyviewHolder myviewHolder, int i) {
//        System.out.print("demo is :"+i);
        myviewHolder.image.setImageResource(mDatas.get(i));
    }


    public static class MyviewHolder extends RecyclerView.ViewHolder{

        public MyviewHolder(@NonNull View itemView) {
            super(itemView);
        }
        ImageView image;
        TextView tv;

    }

    @Override
    public int getItemCount() {
        return this.mDatas.size();
    }
}
