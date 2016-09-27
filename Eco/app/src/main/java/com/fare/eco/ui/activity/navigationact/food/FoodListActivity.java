package com.fare.eco.ui.activity.navigationact.food;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.fare.eco.externalLibrary.pullto.PullToRefreshListView;
import com.fare.eco.ui.BaseActivity;
import com.fare.eco.ui.R;
import com.fare.eco.ui.view.TitleBarLayout;

public class FoodListActivity extends BaseActivity {

    private TitleBarLayout titleBar;
    private PullToRefreshListView footList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);

        initView();
    }

    private void initView() {
        titleBar = (TitleBarLayout) findViewById(R.id.titleBar);
        View searchView = View.inflate(this, R.layout.search_view_layout, null);
        titleBar.setMidCustomView(searchView);
        footList = (PullToRefreshListView) findViewById(R.id.lv_foodlist);
    }


    class MyFoodListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return null;
        }
    }
}
