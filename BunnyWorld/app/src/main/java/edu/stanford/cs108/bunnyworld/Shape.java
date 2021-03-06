package edu.stanford.cs108.bunnyworld;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import static edu.stanford.cs108.bunnyworld.BunnyWorldApplication.getGlobalContext;

public class Shape implements Serializable {

    private static final long serialVersionUID = 1267054998338566400L;

    static final float SQUARE_SIZE = 150f;

    static Shape animatedShape;
    static float animOriginalLeft;
    static float animOriginalTop;

    static final Map<String, Typeface> nameToTypeface = new HashMap<String, Typeface>() {{
        put("DEFAULT", Typeface.DEFAULT);
        put("MONOSPACE", Typeface.MONOSPACE);
        put("SANS_SERIF", Typeface.SANS_SERIF);
        put("SERIF", Typeface.SERIF);
    }};

    String name;
    transient RectF coordinates, inventoryCoordinates;
    String imageName; // Name of the image this Shape can draw
    String text = ""; // Some text that this Shape can draw
    boolean hidden = false; // Whether this shape should be drawn out/clickable in Play time
    boolean movable = false; // Whether this shape can be dragged around during Play time
    String typeface = "DEFAULT";
    Scripts scripts = new Scripts();
    int red = 0;
    int green = 0;
    int blue = 0;
    boolean bold = false;
    boolean italics = false;

    transient Paint paint, defaultPaint, highlightPaint;
    transient BitmapDrawable imageDrawable;

    /**
     * Default constructor for Shape - you can directly call this one.
     * This constructor will populate the rest of Shape's attributes using default values.
     */
    public Shape(String name, RectF coordinates) {
        this.name = name;
        this.coordinates = coordinates;
        this.inventoryCoordinates = new RectF(coordinates.left, coordinates.top, coordinates.left + SQUARE_SIZE, coordinates.top + SQUARE_SIZE);
        constantInit();
        paint.setTypeface(Typeface.create(nameToTypeface.get(typeface), Typeface.NORMAL));
        paint.setTextSize(50.0f);
        highlightPaint.setColor(Color.TRANSPARENT);
    }

    private void constantInit() {
        paint = new Paint();
        defaultPaint = new Paint();
        highlightPaint = new Paint();
        loadImage();

        defaultPaint.setColor(Color.LTGRAY);
        highlightPaint.setStyle(Paint.Style.STROKE);
        highlightPaint.setStrokeWidth(15.0f);
    }

    private void loadImage() {
        Context context = getGlobalContext();
        Resources resources = context.getResources();
        try {
            final int resourceId = resources.getIdentifier(imageName, "drawable", context.getPackageName());
            imageDrawable = (BitmapDrawable) resources.getDrawable(resourceId);
        } catch (Exception e) {
            imageDrawable = null;
        }
    }

    // Getters
    public RectF getRectF() { return coordinates; }
    public float getLeft() { return coordinates.left; }
    public float getRight() { return coordinates.right; }
    public float getBottom() { return coordinates.bottom; }
    public float getTop() { return coordinates.top; }
    public float getWidth() { return coordinates.width(); }
    public float getHeight() { return coordinates.height(); }
    public String getName() { return this.name; }
    public String getImageName() { return imageName; }
    public String getText() { return text; }
    public float getTextSize() { return paint.getTextSize(); }
    public int getColor() { return paint.getColor(); }
    public int getStyle() { return paint.getTypeface().getStyle(); }
    public String getTypeface() { return typeface; }
    public boolean isHidden() { return hidden; }
    public boolean isMovable() { return movable; }
    public Scripts getScripts() { return scripts; }
    public int getHighlightColor() { return highlightPaint.getColor(); }

    public int getBitmapHeight() {
        if (imageDrawable != null) {
            return imageDrawable.getBitmap().getHeight();
        } else {
            return -1;
        }
    }

    public int getBitmapWidth() {
        if (imageDrawable != null) {
            return imageDrawable.getBitmap().getWidth();
        } else {
            return -1;
        }
    }

    // Below are some Setters

    // Set Coordinates directly with a RectF
    public void setCoordinates(RectF coordinates) {
        coordinates.sort();
        this.coordinates.set(coordinates);
        this.inventoryCoordinates.offsetTo(coordinates.left, coordinates.top);
    }

    // Set Coordinates with (left, top, right, bottom)
    public void setCoordinates(float left, float top, float right, float bottom) {
        RectF coordinates = new RectF(left, top, right, bottom);
        setCoordinates(coordinates);
    }

    public void offSetCoordinates(float dx, float dy) {
        coordinates.offset(dx, dy);
        inventoryCoordinates.offset(dx, dy);
    }

    public void setCenterCoordinates(float x, float y, float width, float height){
        float newLeft = x - (width / 2);
        float newRight = x + (width / 2);
        float newTop = y - (height / 2);
        float newBottom = y + (height / 2);

        setCoordinates(newLeft, newTop, newRight, newBottom);
    }

    public void setText(String text, float textSize) {
        this.text = text;
        paint.setTextSize(textSize);
    }

    public void setColor(int color) {
        paint.setColor(color);
        red = (color >> 16) & 0xff;
        green = (color >>  8) & 0xff;
        blue = (color      ) & 0xff;
    }

    public void setBold(boolean bolded) {
        bold = bolded;
    }

    public void setItalic(boolean italiced) {
        italics = italiced;
    }

    public void solidifyTextStyle() {
        if (bold && italics) {
            paint.setTypeface(Typeface.create(nameToTypeface.get(typeface), Typeface.BOLD_ITALIC));
        } else if (bold) {
            paint.setTypeface(Typeface.create(nameToTypeface.get(typeface), Typeface.BOLD));
        } else if (italics) {
            paint.setTypeface(Typeface.create(nameToTypeface.get(typeface), Typeface.ITALIC));
        } else {
            paint.setTypeface(Typeface.create(nameToTypeface.get(typeface), Typeface.NORMAL));
        }
    }

    public void setTypeface(String typeface) {
        if (nameToTypeface.containsKey(typeface)) {
            this.typeface = typeface;
            paint.setTypeface(Typeface.create(nameToTypeface.get(typeface), paint.getTypeface().getStyle()));
        }
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
        loadImage();
    }

    public void setHighlightColor(int highlightColor) {
        highlightPaint.setColor(highlightColor);
    }

    public void setName(String name) { this.name = name; }
    public void setHidden(boolean hidden) { this.hidden = hidden; }
    public void setMovable(boolean movable) { this.movable = movable; }
    public void setScripts(Scripts scripts) { this.scripts = scripts; }

    // Other methods
    public void draw(Canvas canvas) {
        if (!hidden) {
            if (!text.equals("")) {
                canvas.drawText(text, this.getLeft(), this.getTop(), paint);
            } else if (imageDrawable != null) {
                canvas.drawBitmap(imageDrawable.getBitmap(), null, coordinates, paint);
            } else {
                canvas.drawRect(this.getRectF(), defaultPaint);
            }
        }
        canvas.drawRect(coordinates, highlightPaint);
    }

    public void inventoryDraw(Canvas canvas) {
        if (!hidden) {
            if (!text.equals("")) {
                canvas.drawText(text, this.getLeft(), this.getTop(), paint);
            } else if (imageDrawable != null) {
                canvas.drawBitmap(imageDrawable.getBitmap(), null, inventoryCoordinates, paint);
            } else {
                canvas.drawRect(this.getRectF(), defaultPaint);
            }
        }
        canvas.drawRect(inventoryCoordinates, highlightPaint);
    }

    public void onClick() {
        scripts.onClick(this);
    }

    public void onEnter() {
        scripts.onEnter(this);
    }

    public boolean onDrop(String shapeName) {
        return scripts.onDrop(shapeName, this);
    }

    // Returns whether a given (x, y) is located within the Shape's coordinates
    public boolean contains(float x, float y) {
        return coordinates.contains(x, y);
    }

    // Returns whether a given (x, y) is located within the Shape's default coordinates
    public boolean inventoryContains(float x, float y) {
        return inventoryCoordinates.contains(x, y);
    }

    public void animateX(float dx, long duration) {
        animOriginalLeft = this.coordinates.left;
        animOriginalTop = this.coordinates.top;
        animatedShape = this;
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0.0f, dx);
        valueAnimator.setDuration(duration);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float currentDx = (float) valueAnimator.getAnimatedValue();
                animatedShape.coordinates.offsetTo(animOriginalLeft + currentDx, animOriginalTop);
                GameView.instance.invalidate();
            }
        });
        valueAnimator.start();
    }

    public void animateY(float dy, long duration) {
        animOriginalLeft = this.coordinates.left;
        animOriginalTop = this.coordinates.top;
        animatedShape = this;
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0.0f, dy);
        valueAnimator.setDuration(duration);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float currentDy = (float) valueAnimator.getAnimatedValue();
                animatedShape.coordinates.offsetTo(animOriginalLeft, animOriginalTop + currentDy);
                GameView.instance.invalidate();
            }
        });
        valueAnimator.start();
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();

        constantInit();

        float left = in.readFloat();
        float top = in.readFloat();
        float right = in.readFloat();
        float bottom = in.readFloat();
        this.coordinates = new RectF(left, top, right, bottom);
        this.inventoryCoordinates = new RectF(left, top, left + SQUARE_SIZE, top + SQUARE_SIZE);

        float textSize = in.readFloat();
        paint.setTextSize(textSize);
        int textStyle = in.readInt();
        paint.setTypeface(Typeface.create(nameToTypeface.get(typeface), textStyle));
        int textColor = in.readInt();
        paint.setColor(textColor);

        int highlightColor = in.readInt();
        highlightPaint.setColor(highlightColor);
    }

    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();

        out.writeFloat(this.coordinates.left);
        out.writeFloat(this.coordinates.top);
        out.writeFloat(this.coordinates.right);
        out.writeFloat(this.coordinates.bottom);
        out.writeFloat(paint.getTextSize());
        out.writeInt(paint.getTypeface().getStyle());
        out.writeInt(paint.getColor());
        out.writeInt(highlightPaint.getColor());
    }

}