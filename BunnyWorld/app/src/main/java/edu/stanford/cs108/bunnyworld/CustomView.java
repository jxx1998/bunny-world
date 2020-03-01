package edu.stanford.cs108.bunnyworld;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class CustomView extends View {
    Paint redOutlinePaint;
    Paint blueFillPaint;
    int viewWidth;
    int viewHeight;

    public CustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        redOutlinePaint = new Paint();
        redOutlinePaint.setColor(Color.RED);
        redOutlinePaint.setStyle(Paint.Style.STROKE);
        redOutlinePaint.setStrokeWidth(5.0f);
        blueFillPaint = new Paint();
        blueFillPaint.setColor(Color.BLUE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawLine(0.0f,0.0f,viewWidth,viewHeight,redOutlinePaint);
        canvas.drawRect(50.0f,50.0f,150.0f,150.0f,blueFillPaint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewWidth = w;
        viewHeight = h;
    }
}
