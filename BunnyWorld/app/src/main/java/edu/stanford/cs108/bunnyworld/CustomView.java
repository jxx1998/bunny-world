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
    Page currPage;

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

        //Creating a new page and adding shapes to it.
        currPage = new Page("initialExamplePage");
        Shape shape1 = new ShapeBuilder().name("shape1").coordinates(80f,250f,400f,450f).imageName("duck").buildShape();
        Shape shape2 = new ShapeBuilder().name("shape2").coordinates(1000f,250f,1300f,350f).imageName("carrot").buildShape();
        Shape shape3 = new ShapeBuilder().name("shape3").coordinates(500f,50f,1000f,550f).imageName("death").buildShape();
        currPage.addShape(shape1);
        currPage.addShape(shape2);
        currPage.addShape(shape3);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        canvas.drawLine(0.0f,0.0f,viewWidth,viewHeight,redOutlinePaint);
//        canvas.drawRect(50.0f,50.0f,150.0f,150.0f,blueFillPaint);
        currPage.draw(canvas);



    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewWidth = w;
        viewHeight = h;
    }
}
