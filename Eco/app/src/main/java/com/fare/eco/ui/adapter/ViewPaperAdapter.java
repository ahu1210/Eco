package com.fare.eco.ui.adapter;

import java.util.List;

import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

public class ViewPaperAdapter extends PagerAdapter {
    private List<View> views = null;

    public ViewPaperAdapter(List<View> views) {
        this.views = views;
    }

    @Override
    public void destroyItem(View container, int position, Object object) {
    	((ViewPager)container).removeView(views.get(position));  
    }

    @Override
    public void finishUpdate(View arg0) {

    }

    @Override
    public int getCount() {
        return views.size();
    }

    @Override
    public Object instantiateItem(View container, int position) {
    	((ViewPager) container).addView(views.get(position), 0);
        return views.get(position);
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public void restoreState(Parcelable arg0, ClassLoader arg1) {

    }

    @Override
    public Parcelable saveState() {

        return null;
    }

    @Override
    public void startUpdate(View arg0) {

    }

}
