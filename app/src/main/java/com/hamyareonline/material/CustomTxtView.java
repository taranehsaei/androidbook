package com.hamyareonline.material;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class CustomTxtView extends TextView {
    Typeface font = Typeface.createFromAsset(getResources().getAssets(), "IRANSansWeb.ttf");
    public CustomTxtView(Context context) {
        super(context);
        if(!isInEditMode()){
        this.setTypeface(font);}
    }

    public CustomTxtView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if(!isInEditMode()){
            this.setTypeface(font);}
    }
    public CustomTxtView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if(!isInEditMode()){
            this.setTypeface(font);}
    }

}
