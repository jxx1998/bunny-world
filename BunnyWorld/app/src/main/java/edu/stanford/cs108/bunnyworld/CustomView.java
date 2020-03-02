package edu.stanford.cs108.bunnyworld;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

public class CustomView extends View {
    protected static Page currPage;
    protected static float x1, x2, y1, y2, left, right, top, bottom;

    public CustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void setCurrPage(Page currPage) {
        this.currPage = currPage;
    }

    private void init(){
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
        System.out.println("left, top, right, bottom:" + left + ", " + top + ", " + right + ", " + bottom);
        if (left > 0){
            Shape newShape = new ShapeBuilder().name("shape1").coordinates(left,top,right,bottom).imageName("carrot2").buildShape();
            currPage.addShape(newShape);
        }
        currPage.draw(canvas);



    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                y1 = event.getY();
                break;

            case MotionEvent.ACTION_UP:
                x2 = event.getX();
                y2 = event.getY();

                if (x2 > x1) {
                    right = x2;
                    left = x1;
                } else {
                    right = x1;
                    left = x2;
                }

                if (y2 > y1) {
                    bottom = y2;
                    top = y1;
                } else {
                    bottom = y1;
                    top = y2;
                }

                invalidate();
                break;

        }

        return true;
    }
}
