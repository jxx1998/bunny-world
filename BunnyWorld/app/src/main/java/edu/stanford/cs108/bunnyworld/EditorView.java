package edu.stanford.cs108.bunnyworld;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;


public class EditorView extends View {
    protected static Page currPage;
    protected static String currGameName;
    protected static ArrayList<Page> gamePages;
    protected static int currPagePos, currShapePos;
    protected static float x1, x2, y1, y2, left, right, top, bottom,xSelect, ySelect;
    protected static float selectedLeft,selectedRight,selectedTop,selectedBot;
    protected static String currDrawShapeName;
    protected static ArrayList<String> shapeNames;
    protected static Shape selectedShape, mostRecentAddedShape;
    protected static boolean isNew;

    //FOR DRAGGABLE SHAPE
    final float SQUARE_SIZE = 100.0f;
    final float START_X = 200.0f;
    final float START_Y = 200.0f;
    protected static float selectedX, selectedY;
    Paint myPaint;
    protected static boolean isAShapeSelected;
    protected float minusX, minusY, plusX, plusY;
    protected static boolean changingDimensions;
    Paint textPaint = new Paint();
    Paint myPaintDrawOutline = new Paint();



    public EditorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
        EditorActivity.okToGo =false;
    }

    public void setCurrPage(Page currPage) {
        this.currPage = currPage;
    }

    private void init(){
        System.out.println("is new game?: "+isNew);

        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(50f);

        myPaintDrawOutline.setColor(Color.BLUE);
        myPaintDrawOutline.setStrokeWidth(15.0f);
        myPaintDrawOutline.setStyle(Paint.Style.STROKE);

        //We create a game object and add an initial page (just to test out, this initial page will have three shapes in them)
        shapeNames = new ArrayList<String>();
        shapeNames.add("carrot");
        shapeNames.add("carrot2");
        shapeNames.add("death");
        shapeNames.add("duck");
        shapeNames.add("fire");
        shapeNames.add("mystic");
        shapeNames.add("TextBox");

        currShapePos = 0;
        currDrawShapeName = shapeNames.get(currShapePos);

        //Extra for drag testing
        myPaint = new Paint();
        myPaint.setColor(Color.rgb(140,21,21));
        isAShapeSelected = false;
        changingDimensions = false;

        //Creating a new page
        if (isNew) {
            gamePages = new ArrayList<Page>();

            Page firstPage = new Page("pg1");
            gamePages.add(firstPage);

        }

        currPagePos = 0;
        currPage = gamePages.get(currPagePos);

        Game.set(gamePages, currPagePos);
        Game.save(currGameName);

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //For Displaying Page Name
        String pageName = currPage.name;
        canvas.drawText(pageName,50f,50f, textPaint);


        //Draws all of the shapes on the page
        currPage.draw(canvas);

        //Redraws the selected shape
        //selectedShape = currPage.shapeTouched(xSelect,ySelect,true, true);



    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {

            case MotionEvent.ACTION_MOVE:
                //From Handout Code....might have to manipulate
                //Prob wont work with dragging feature
                System.out.println("IN THE ACTION MOVE CASE");
                if (isAShapeSelected) {
                    selectedX = event.getX();
                    selectedY = event.getY();
                    selectedShape.setCenterCoordinates(selectedX, selectedY, selectedShape.getWidth(), selectedShape.getHeight());
                }
                break;

            case MotionEvent.ACTION_DOWN:
//                xSelect = event.getX();
//                ySelect = event.getY();
//                x1 = event.getX();
//                y1 = event.getY();
                for (Shape shape: currPage.shapes) {
                    shape.setHighlightColor(Color.TRANSPARENT);
                }
                selectedX = event.getX();
                selectedY = event.getY();
                selectedShape = currPage.shapeTouched(selectedX, selectedY, true, true);
                if (selectedShape != null) {
                    isAShapeSelected = true;
                    currPage.makeTopMost(selectedShape);

                    if (!changingDimensions) {
                        selectedShape.setCenterCoordinates(selectedX, selectedY, selectedShape.getWidth(), selectedShape.getHeight());
                    }

                    if (selectedShape.isHidden()){
                        selectedShape.setHighlightColor(Color.BLACK);
                    } else {
                        selectedShape.setHighlightColor(Color.BLUE);
                    }
                } else {
                    isAShapeSelected = false;
                }
                break;

            case MotionEvent.ACTION_UP:
                if (isAShapeSelected){
                    Game.set(gamePages, currPagePos);
                    Game.save(currGameName);
                }
                break;
        }
        invalidate();
        changingDimensions =false;
        return true;
    }
}
