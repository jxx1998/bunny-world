package edu.stanford.cs108.bunnyworld;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static edu.stanford.cs108.bunnyworld.BunnyWorldApplication.getGlobalContext;

/**
 * View class for game play
 */
public class GameView extends View {

    static float width, height, dividerY;
    static float animationDivider;
    float shapeOriginalLeft, shapeOriginalTop;
    Paint dividerPaint;
    static Shape shapeSelected;
    static Page currentPage;
    long mouseDownTime;
    static GameView instance;

    static final int MAX_CLICK_DURATION = 200;

    static List<Shape> inventory = new ArrayList<Shape>();

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        instance = this;
        init();
        invalidate();
    }

    private void init() {
        dividerPaint = new Paint();
        dividerPaint.setStyle(Paint.Style.STROKE);
        dividerPaint.setStrokeWidth(5.0f);
        changePage(Game.getPages().get(0));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
        dividerY = 2.0f / 3.0f * height;
    }

    private static void clearDivider(Shape shape, boolean init) {
        if (shape.coordinates.bottom > dividerY && shape.coordinates.top < dividerY) {
            float centerY = shape.coordinates.centerY();
            if (centerY < dividerY || init) {
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
                processActionMove(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_DOWN:
                processActionDown(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_UP:
                processActionUp();
                break;
        }
        invalidate();
        return true;
    }

    private void processActionMove(float x, float y) {
        if (shapeSelected != null && shapeSelected.isMovable()) {
            shapeSelected.setCenterCoordinates(x, y, shapeSelected.coordinates.width(), shapeSelected.coordinates.height());
        }
    }

    private void processActionDown(float x, float y) {
        mouseDownTime = Calendar.getInstance().getTimeInMillis();
        clearSelection();
        shapeSelected = null;
        if (y >= dividerY) {
            shapeSelected = inventoryShapeTouched(x, y);
        } else {
            shapeSelected = currentPage.shapeTouched(x, y, false, true);
        }
        if (shapeSelected!= null) {
            shapeSelected.setHighlightColor(Color.BLUE);
            shapeOriginalLeft = shapeSelected.getLeft();
            shapeOriginalTop = shapeSelected.getTop();
            for (Shape shape: currentPage.shapes) {
                if (shape == shapeSelected) {
                    continue;
                }
                if (shape.scripts.onDropClauses.containsKey(shapeSelected.name)) {
                    shape.setHighlightColor(Color.GREEN);
                }
            }
        }
    }

    private Shape inventoryShapeTouched(float x, float y) {
        for (int i = inventory.size() - 1; i >= 0; i --) {
            if (inventory.get(i).contains(x, y)) {
                return inventory.get(i);
            }
        }
        return null;
    }

    private void processActionUp() {
        long mouseUpTime = Calendar.getInstance().getTimeInMillis();
        for (Shape shape: currentPage.shapes) {
            if (shape.getHighlightColor() == Color.GREEN) {
                shape.setHighlightColor(Color.TRANSPARENT);
            }
        }
        if (shapeSelected != null && shapeSelected.isMovable()) {
            clearDivider(shapeSelected, false);
            Page oldCurrentPage = currentPage;
            Shape oldShapeSelected = shapeSelected;
            if (mouseUpTime - mouseDownTime <= MAX_CLICK_DURATION) {
                shapeSelected.onClick();
            } else {
                if (currentPage.processOnDrop(shapeSelected)) {
                    shapeSelected.coordinates.offsetTo(shapeOriginalLeft, shapeOriginalTop);
                }
            }
            if (oldShapeSelected.getTop() >= dividerY) {
                inventory.remove(oldShapeSelected);
                inventory.add(oldShapeSelected);
                oldCurrentPage.removeShape(oldShapeSelected);
            } else {
                oldCurrentPage.removeShape(oldShapeSelected);
                oldCurrentPage.addShape(oldShapeSelected);
                inventory.remove(oldShapeSelected);
            }
        }
    }

    public static void changePage(Page newPage) {
        // clear ambient sound
        if (!newPage.equals(currentPage) && MainActivity.ambientSound != null) {
            MainActivity.ambientSound.stop();
            MainActivity.ambientSound = null;
        }

        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0.0f, width);
        valueAnimator.setDuration(1000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                animationDivider = (float) valueAnimator.getAnimatedValue();
                // instance.invalidate();
            }
        });

        currentPage = newPage;
        shapeSelected = null;
        for (Shape shape: currentPage.shapes) {
            clearDivider(shape, true);
        }
        clearSelection();
        currentPage.onEnter();
    }

    private static void clearSelection() {
        for (Shape shape: currentPage.shapes) {
            shape.setHighlightColor(Color.TRANSPARENT);
        }
        for (Shape shape: inventory) {
            shape.setHighlightColor(Color.TRANSPARENT);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawLine(0, dividerY, width, dividerY, dividerPaint);
        for (Shape shape: inventory) {
            shape.draw(canvas);
        }
        currentPage.draw(canvas);
    }
}
