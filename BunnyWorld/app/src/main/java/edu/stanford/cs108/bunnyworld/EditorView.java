package edu.stanford.cs108.bunnyworld;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
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
    protected static boolean createNewShape;
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
        createNewShape = false;
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
        Paint textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(50f);
        String pageName = currPage.name;
        canvas.drawText(pageName,50f,50f, textPaint);

        if(createNewShape){
            float startleft= START_X - SQUARE_SIZE;
            float starttop = START_Y - SQUARE_SIZE;
            float startright = START_X + SQUARE_SIZE;
            float startbottom = START_Y + SQUARE_SIZE;

            Shape newShape;
            if (!currDrawShapeName.equals("TextBox")) {
                newShape = new Shape("NewShape", new RectF(startleft, starttop, startright, startbottom));
                newShape.setImageName(currDrawShapeName);
            } else{
                newShape = new Shape("NewShape", new RectF(startleft, starttop, startright, startbottom));
                newShape.setText("This is my shapeText", 50);

               // newShape.setText(shapeText);
            }
            //This is what I used for the click, create, and move feature
            //Shape newShape = new ShapeBuilder().name("AddedShape").coordinates(left,top,right,bottom).imageName(currDrawShapeName).buildShape();
            currPage.addShape(newShape);
            mostRecentAddedShape = newShape;

            Game.set(EditorView.gamePages, EditorView.currPagePos);
            Game.save(EditorView.currGameName);

            createNewShape = false;
            isAShapeSelected = true;
            selectedX = START_X;
            selectedY = START_Y;
        }



        //Draws all of the shapes on the page
        currPage.draw(canvas);

        //Redraws the selected shape
        //selectedShape = currPage.shapeTouched(xSelect,ySelect,true, true);

        selectedShape = currPage.shapeTouched(selectedX, selectedY, true, true);


        if (selectedShape != null) {
            isAShapeSelected = true;
            currPage.makeTopMost(selectedShape);

            if (!changingDimensions) {
                selectedShape.setCenterCoordinates(selectedX, selectedY, selectedShape.getWidth(), selectedShape.getHeight());
            }

            if (selectedShape.isHidden()){
                selectedShape.setHighlightColor(Color.BLACK);
            }
            //selectedShape.setCoordinates();
            currPage.draw(canvas);
            selectedLeft = selectedShape.getLeft();
            selectedRight = selectedShape.getRight();
            selectedTop = selectedShape.getTop();
            selectedBot = selectedShape.getBottom();
            //System.out.println("Selected Shape's attributes: (left, right, top, bot): (" + left + "," + right + "," + top + "," + bot + "," + ")");

            Paint myPaintDrawOutline = new Paint();
            myPaintDrawOutline.setColor(Color.BLUE);
            myPaintDrawOutline.setStrokeWidth(15.0f);
            myPaintDrawOutline.setStyle(Paint.Style.STROKE);
            canvas.drawRect(selectedLeft,selectedTop,selectedRight,selectedBot,myPaintDrawOutline);
            invalidate();

        } else{
            isAShapeSelected = false;
        }


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
                    invalidate();
                }

            case MotionEvent.ACTION_DOWN:
//                xSelect = event.getX();
//                ySelect = event.getY();
//                x1 = event.getX();
//                y1 = event.getY();
                System.out.println("IN THE ACTION DOWN CASE");
                selectedX = event.getX();
                selectedY = event.getY();
                invalidate();


            case MotionEvent.ACTION_UP:
                System.out.println("IN THE ACTION UP CASE");
                if (isAShapeSelected){
                    Game.set(gamePages, currPagePos);
                    Game.save(currGameName);
                }

        }

        changingDimensions =false;
        return true;
    }
}
