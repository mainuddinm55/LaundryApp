package com.kcirqueapps.laundryapp.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class CustomTextView extends View {
    public CustomTextView(Context context) {
        super(context);
    }

    public CustomTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }



    @Override
    protected void onDraw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrikeThruText(true);
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);
        super.onDraw(canvas);
        float width = getWidth();
        float heigh = getHeight();
        canvas.drawLine(width / 10, heigh / 10, (width - width / 10), (heigh - heigh / 10), paint);
    }
}
