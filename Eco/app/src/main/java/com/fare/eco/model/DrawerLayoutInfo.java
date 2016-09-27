package com.fare.eco.model;

import android.graphics.drawable.Drawable;

public class DrawerLayoutInfo {
    private String mText;
    private Drawable mDrawable;

    public DrawerLayoutInfo(String text, Drawable drawable) {
        mText = text;
        mDrawable = drawable;
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
    }

    public Drawable getDrawable() {
        return mDrawable;
    }

    public void setDrawable(Drawable drawable) {
        mDrawable = drawable;
    }
}
