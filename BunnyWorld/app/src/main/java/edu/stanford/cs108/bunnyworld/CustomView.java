package edu.stanford.cs108.bunnyworld;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class CustomView extends View {
    protected static Page currPage;
    protected static Game currGame;
    protected static ArrayList<Page> gamePages;
    protected static int currPagePos, currShapePos;
    protected static float x1, x2, y1, y2, left, right, top, bottom;
    protected static String currDrawShapeName;
    protected static ArrayList<String> shapeNames;

    public CustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void setCurrPage(Page currPage) {
        this.currPage = currPage;
    }

    private void init(){
        //We create a game object and add an initial page (just to test out, this initial page will have three shapes in them)

        //currGame = new Game();

        //Creating a new page and adding shapes to it.

        gamePages = new ArrayList<Page>();
        shapeNames = new ArrayList<String>();
        Page firstPage = new Page("Page 1");
        //Shape shape1 = new ShapeBuilder().name("shape1").coordinates(80f,250f,400f,450f).imageName("duck").buildShape();
        //Shape shape2 = new ShapeBuilder().name("shape2").coordinates(1000f,250f,1300f,350f).imageName("carrot").buildShape();
        Shape shapeForFirst = new ShapeBuilder().name("shape3").coordinates(500f,50f,1000f,550f).imageName("death").buildShape();
        //currPage.addShape(shape1);
        //currPage.addShape(shape2);
        firstPage.addShape(shapeForFirst);


        Page secondPage = new Page("Page 2");
        //Shape shape1 = new ShapeBuilder().name("shape1").coordinates(80f,250f,400f,450f).imageName("duck").buildShape();
        //Shape shape2 = new ShapeBuilder().name("shape2").coordinates(1000f,250f,1300f,350f).imageName("carrot").buildShape();
        Shape shapeForSecond = new ShapeBuilder().name("shape3").coordinates(500f,50f,1000f,550f).imageName("fire").buildShape();
        //currPage.addShape(shape1);
        //currPage.addShape(shape2);
        secondPage.addShape(shapeForSecond);


        Page thirdPage = new Page("Page 3");
        //Shape shape1 = new ShapeBuilder().name("shape1").coordinates(80f,250f,400f,450f).imageName("duck").buildShape();
        //Shape shape2 = new ShapeBuilder().name("shape2").coordinates(1000f,250f,1300f,350f).imageName("carrot").buildShape();
        Shape shapeForThird = new ShapeBuilder().name("shape3").coordinates(500f,50f,1000f,550f).imageName("carrot").buildShape();
        //currPage.addShape(shape1);
        //currPage.addShape(shape2);
        thirdPage.addShape(shapeForThird);

        gamePages.add(firstPage);
        gamePages.add(secondPage);
        gamePages.add(thirdPage);
        currPagePos = 0;
        currPage = gamePages.get(currPagePos);

        shapeNames.add("carrot");
        shapeNames.add("carrot2");
        shapeNames.add("death");
        shapeNames.add("duck");
        shapeNames.add("fire");
        shapeNames.add("mystic");

        currShapePos = 1;
        currDrawShapeName = shapeNames.get(currShapePos);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint textPaint = new Paint();
        //canvas.drawPaint(textPaint);
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(50f);
        String pageName = currPage.name;
        canvas.drawText(pageName,50f,50f, textPaint);
        System.out.println("left, top, right, bottom:" + left + ", " + top + ", " + right + ", " + bottom);
        if (left > 0){
            Shape newShape = new ShapeBuilder().name("shape1").coordinates(left,top,right,bottom).imageName(currDrawShapeName).buildShape();
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
