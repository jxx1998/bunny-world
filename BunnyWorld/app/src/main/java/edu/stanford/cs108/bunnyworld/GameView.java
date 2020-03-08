package edu.stanford.cs108.bunnyworld;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * View class for game play
 */
public class GameView extends View {

    float width, height, dividerY;
    Paint dividerPaint;
    Shape shapeSelected;
    Page currentPage;

    List<Shape> inventory;

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        invalidate();
    }

    private void init() {
        dividerPaint = new Paint();
        dividerPaint.setStyle(Paint.Style.STROKE);
        dividerPaint.setStrokeWidth(5.0f);
        loadGame("game_0");
        inventory = new ArrayList<Shape>();
        currentPage = Game.getPages().get(0);
    }

    private void loadGame(String gameName) {
        Game.load(gameName);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
        dividerY = 2.0f / 3.0f * height;
    }

    private void clearDivider(Shape shape) {
        if (shape.coordinates.bottom > dividerY && shape.coordinates.top < dividerY) {
            float centerY = shape.coordinates.centerY();
            if (centerY < dividerY) {
                shape.coordinates.offset(0.0f, dividerY - shape.coordinates.bottom);
            } else {
                shape.coordinates.offset(0.0f, dividerY - shape.coordinates.top);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                if (shapeSelected != null) {
                    shapeSelected.setCenterCoordinates(event.getX(), event.getY(), shapeSelected.coordinates.width(), shapeSelected.coordinates.height());
                }
                break;
            case MotionEvent.ACTION_DOWN:
                shapeSelected = currentPage.shapeTouched(event.getX(), event.getY(), false, true);
                break;
            case MotionEvent.ACTION_UP:
                if (shapeSelected != null) {
                    clearDivider(shapeSelected);
                    processOnDrop(shapeSelected);
                }
                break;
        }
        return true;
    }

    private void processOnDrop(Shape shape) {
        List<Shape> candidateShapes = currentPage.shapeOverlapped(shape, false, true);
        for (Shape candidateShape: candidateShapes) {
            candidateShape.
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawLine(0, dividerY, width, dividerY, dividerPaint);

    }
}
