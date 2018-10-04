package xie.com.androidutils.UI;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class FlowLayout extends ViewGroup {
    public FlowLayout(Context context) {
        super(context,null);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs,0);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //获取设置的宽高的模式和具体的值
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int hightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int hightSize=MeasureSpec.getSize(heightMeasureSpec);

        //如果用户使用的至多模式，那么使用如下两个变量计算真实的宽高值。
        int width = 0;//整个的宽
        int height = 0;//整个的高

        //每一行的宽度
        int lineWidth = 0;//已经使用过的宽度
        int lineHeight = 0;

        int childCount = getChildCount();
        for(int i=0;i<childCount;i++){
            View childView = getChildAt(i);
            measureChild(childView,widthMeasureSpec,heightMeasureSpec);
            //获取子视图的宽高
            int childWidth = childView.getMeasuredWidth();
            int childHeight = childView.getMeasuredHeight();
            MarginLayoutParams mp = (MarginLayoutParams) childView.getLayoutParams();
            if(lineWidth+childWidth+mp.leftMargin+mp.rightMargin<widthSize){//不换行
                lineWidth+=childWidth+mp.leftMargin+mp.rightMargin;
                lineHeight = Math.max(lineHeight,childView.getHeight()+mp.bottomMargin+mp.bottomMargin);
            }else {
                width =Math.max(lineHeight,width) ;
                height +=lineHeight;

                lineWidth = childWidth+mp.leftMargin+mp.rightMargin;
                lineHeight = childHeight+mp.topMargin+mp.bottomMargin;

            }

        }

    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {
        
    }
}
