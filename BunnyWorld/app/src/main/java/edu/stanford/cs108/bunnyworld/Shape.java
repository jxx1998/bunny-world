package edu.stanford.cs108.bunnyworld;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

import static edu.stanford.cs108.bunnyworld.BunnyWorldApplication.getGlobalContext;

public class Shape implements Serializable {

    String name;
    transient RectF coordinates;
    String imageName = ""; // Name of the image this Shape can draw
    String text; // Some text that this Shape can draw
    float textSize = 10.0f; // The size of the text in case Shape needs to draw the text
    boolean hidden = false; // Whether this shape should be drawn out/clickable in Play time
    boolean movable = true; // Whether this shape can be dragged around during Play time
    Scripts scripts = new Scripts();
    int highlightColor = Color.TRANSPARENT;

    transient Paint textPaint, defaultPaint, highlightPaint;
    transient BitmapDrawable imageDrawable;

    /**
     * Default constructor for Shape - you can directly call this one.
     * This constructor will populate the rest of Shape's attributes using default values.
     */
    public Shape(String name, RectF coordinates) {
        this.name = name;
        this.coordinates = new RectF(coordinates);
        init();
    }

    /**
     * Constructor intended to be called by ShapeBuilder only
     * Calling this Shape constructor is not recommended; use ShapeBuilder to customize & construct the new Shape
     */
    public Shape(String name, RectF coordinates, String imageName, String text, float textSize,
                 boolean hidden, boolean movable, Scripts scripts, int highlightColor) {
        this.name = name;
        this.coordinates = coordinates;
        this.imageName = imageName;
        this.text = text;
        this.textSize = textSize;
        this.hidden = hidden;
        this.movable = movable;
        this.scripts = scripts;
        this.highlightColor = highlightColor;
        init();
    }

    private void init() {
        textPaint = new Paint();
        textPaint.setTextSize(this.textSize);
        defaultPaint = new Paint();
        defaultPaint.setColor(Color.LTGRAY);
        highlightPaint = new Paint();
        highlightPaint.setColor(highlightColor);
        highlightPaint.setStyle(Paint.Style.STROKE);
        highlightPaint.setStrokeWidth(15.0f);
        loadImage();
    }

    private void loadImage() {
        Context context = getGlobalContext();
        Resources resources = context.getResources();
        try {
            final int resourceId = resources.getIdentifier(imageName, "drawable", context.getPackageName());
            imageDrawable = (BitmapDrawable) resources.getDrawable(resourceId);
        } catch (Resources.NotFoundException e) {
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
    public float getTextSize() { return textSize; }
    public boolean isHidden() { return hidden; }
    public boolean isMovable() { return movable; }
    public Scripts getScripts() { return scripts; }
    public int getHighlightColor() { return highlightColor; }

    // Below are some Setters

    // Set Coordinates directly with a RectF
    public void setCoordinates(RectF coordinates) {
        coordinates.sort();
        this.coordinates.set(coordinates);
    }

    // Set Coordinates with (left, top, right, bottom)
    public void setCoordinates(float left, float top, float right, float bottom) {
        RectF coordinates = new RectF(left, top, right, bottom);
        setCoordinates(coordinates);
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
        this.textSize = textSize;
        textPaint.setTextSize(this.textSize);
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
        loadImage();
    }

    public void setHighlightColor(int highlightColor) {
        this.highlightColor = highlightColor;
        highlightPaint.setColor(this.highlightColor);
    }

    public void setName(String name) { this.name = name; }
    public void setHidden(boolean hidden) { this.hidden = hidden; }
    public void setMovable(boolean movable) { this.movable = movable; }
    public void setScripts(Scripts scripts) { this.scripts = scripts; }

    // Other methods
    public void draw(Canvas canvas) {
        canvas.drawRect(coordinates, highlightPaint);
        if (hidden) { return; }
        if (text != null) {
            canvas.drawText(text, this.getLeft(), this.getTop(), textPaint);
        } else if (imageDrawable != null) {
            canvas.drawBitmap(imageDrawable.getBitmap(), null, this.getRectF(), null);
        } else {
            canvas.drawRect(this.getRectF(), defaultPaint);
        }
    }

    public void onClick() {
        scripts.onClick();
    }

    public void onEnter() {
        scripts.onEnter();
    }

    public boolean onDrop(String shapeName) {
        return scripts.onDrop(shapeName);
    }

    // Returns whether a given (x, y) is located within the Shape
    public boolean contains(float x, float y) {
        return coordinates.contains(x, y);
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();

        float left = in.readFloat();
        float top = in.readFloat();
        float right = in.readFloat();
        float bottom = in.readFloat();
        this.coordinates = new RectF(left, top, right, bottom);

        init();
    }

    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();

        out.writeFloat(this.coordinates.left);
        out.writeFloat(this.coordinates.top);
        out.writeFloat(this.coordinates.right);
        out.writeFloat(this.coordinates.bottom);
    }

}