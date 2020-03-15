package edu.stanford.cs108.bunnyworld;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
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

    static float animDivideX;
    float shapeOriginalLeft, shapeOriginalTop;
    Paint dividerPaint;
    static Shape shapeSelected;
    static Page currentPage;
    long mouseDownTime;
    static GameView instance;
    static final int MAX_CLICK_DURATION = 200;
    static Page previousPage;

    static List<Shape> inventory = new ArrayList<Shape>();

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        instance = this;
        dividerPaint = new Paint();
        dividerPaint.setStyle(Paint.Style.STROKE);
        dividerPaint.setStrokeWidth(5.0f);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        shapeSelected = null;
        previousPage = null;
        GameView.inventory.clear();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                changePage(Game.getPages().get(0));
            }
        }, 100);
    }

    private static void clearDivider(Shape shape, boolean init) {
        float dividerY = (2f / 3f) * instance.getHeight();
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
        float dividerY = (2f / 3f) * instance.getHeight();
        if (y >= dividerY) {
            shapeSelected = inventoryShapeTouched(x, y);
        } else {
            shapeSelected = currentPage.shapeTouched(x, y, false, true);
        }
        if (shapeSelected!= null) {
            currentPage.makeTopMost(shapeSelected);
            shapeSelected.setHighlightColor(Color.BLUE);
            Log.i("hello", shapeSelected.scripts.scriptStr);
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
            if (inventory.get(i).inventoryContains(x, y)) {
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
        if (shapeSelected != null) {
            clearDivider(shapeSelected, false);
            Page oldCurrentPage = currentPage;
            Shape oldShapeSelected = shapeSelected;
            if (mouseUpTime - mouseDownTime <= MAX_CLICK_DURATION) {
                shapeSelected.onClick();
            } else {
                if (shapeSelected.isMovable() && currentPage.processOnDrop(shapeSelected)) {
                    shapeSelected.coordinates.offsetTo(shapeOriginalLeft, shapeOriginalTop);
                }
            }
            if (shapeSelected != null && shapeSelected.isHidden()) {
                shapeSelected.setHighlightColor(Color.TRANSPARENT);
                shapeSelected = null;
            }
            float dividerY = (2f / 3f) * instance.getHeight();
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
        // Clear ambient sound
        if (!newPage.equals(currentPage) && MainActivity.ambientSound != null) {
            MainActivity.ambientSound.stop();
            MainActivity.ambientSound = null;
        }

        previousPage = currentPage;
        currentPage = newPage;
        shapeSelected = null;
        for (Shape shape: currentPage.shapes) {
            clearDivider(shape, true);
        }
        clearSelection();
        currentPage.onEnter();

        ValueAnimator valueAnimator = ValueAnimator.ofFloat(instance.getWidth(), 0.0f);
        valueAnimator.setDuration(2500);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                animDivideX = (float) valueAnimator.getAnimatedValue();
                instance.invalidate();
            }
        });
        valueAnimator.start();
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
        if (currentPage == null) { return; }
        float dividerY = (2f / 3f) * instance.getHeight();
        canvas.drawLine(0, dividerY, getWidth(), dividerY, dividerPaint);
        canvas.save();
        canvas.clipRect(0.0f, 0.0f, animDivideX, dividerY);
        if (previousPage != null) {
            previousPage.draw(canvas);
        } else {
            canvas.drawColor(getSolidColor());
        }
        canvas.restore();
        canvas.save();
        canvas.clipRect(animDivideX, 0.0f, getWidth(), dividerY);
        currentPage.draw(canvas);
        canvas.restore();
        for (Shape shape: inventory) {
            shape.inventoryDraw(canvas);
        }
    }
}
