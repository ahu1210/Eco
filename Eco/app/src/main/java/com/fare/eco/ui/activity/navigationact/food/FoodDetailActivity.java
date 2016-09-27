package com.fare.eco.ui.activity.navigationact.food;

import android.os.Bundle;

import com.fare.eco.externalLibrary.pullzoomview.PullToZoomScrollViewEx;
import com.fare.eco.ui.BaseActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.fare.eco.ui.R;
import com.fare.eco.ui.util.ShowUtil;
import com.fare.eco.ui.view.TitleBarLayout;

/**
 * Created by Xiao_V on 2016/3/19.
 */
public class FoodDetailActivity extends BaseActivity implements PullToZoomScrollViewEx.ScrollViewListener {

    private static final String TAG = "FoodDetailActivity";

    private PullToZoomScrollViewEx mScrollView;

    private TitleBarLayout titleBar;

    private int imageHeight;

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);

        mScrollView = (PullToZoomScrollViewEx) findViewById(R.id.scrollView);
        titleBar = (TitleBarLayout) findViewById(R.id.titleBar);

        final String[] adapterData = new String[]{"1", "2",
                "3", "4", "5", "6", "7", "8", "9", "10", "11",
                "12", "13", "14", "15", "16","1", "2",
                "3", "4", "5", "6", "7", "8", "9", "10", "11",
                "12", "13", "14", "15", "16","1", "2",
                "3", "4", "5", "6", "7", "8", "9", "10", "11",
                "12", "13", "14", "15", "16"};

        /*mScrollView.setAdapter(new ArrayAdapter<String>(FoodDetailActivity.this, android.R.layout.simple_list_item_1, adapterData));
        mScrollView.getPullRootView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //判断headView的数量
                ShowUtil.showToast(FoodDetailActivity.this, adapterData[position] + "");
                //Toast.makeText(FoodDetailActivity.this, adapterData[position-1] + "", Toast.LENGTH_SHORT).show();
            }
        });*/

        DisplayMetrics localDisplayMetrics = new DisplayMetrics();
        // 获取屏幕的大小
        getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);
        int mScreenHeight = localDisplayMetrics.heightPixels;
        int mScreenWidth = localDisplayMetrics.widthPixels;
        LinearLayout.LayoutParams localObject = new LinearLayout.LayoutParams(mScreenWidth, (int) (11.0F * (mScreenHeight / 16.0F))); // 16 * 9
        mScrollView.setHeaderLayoutParams(localObject); // 设置头部的大小

        setHeader();
        setTitle();

    }

    private void setTitle() {

        View zooView = LayoutInflater.from(this).inflate(R.layout.food_detail_zoom_view, null);
        imageView = (ImageView) zooView.findViewById(R.id.iv_zoom);
        mScrollView.setZoomView(zooView);
        mScrollView.setZoomEnabled(true);
        mScrollView.setScrollContentView(LayoutInflater.from(this).inflate(R.layout.fragment_more, null, false));

        titleBar.setTitle("商品详情");
        titleBar.setTitleBarAlpha(150);

        ViewTreeObserver vto = imageView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                imageView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                imageHeight = imageView.getHeight();

                mScrollView.setScrollViewListener(FoodDetailActivity.this);
            }
        });
    }

    private void setHeader() {

        /*View headView = LayoutInflater.from(this).inflate(R.layout.food_detail_head_view, null);
        listView.setHeaderView(headView);
        listView.setHideHeader(false);*/
    }

    @Override
    public void scrollChanged(int x, int y, int oldx, int oldy) {
        Log.i(TAG, "y is " + y);
        if (y <= 0) {
            titleBar.setTitleBarAlpha(0);
        } else if (y > 0 && y <= imageHeight) {
            float scale = (float) y / imageHeight;
            float alpha = (255 * scale);
            titleBar.setTitleBarAlpha((int) alpha);
        } else {
            titleBar.setTitleBarAlpha(255);
        }
    }
}
