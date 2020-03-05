package edu.stanford.cs108.bunnyworld;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

import static edu.stanford.cs108.bunnyworld.BunnyWorldApplication.getGlobalContext;

public class Shape implements Serializable {

    String name;
    SerializableRectF coordinates;
    String imageName; // Name of the image this Shape can draw
    String text; // Some text that this Shape can draw
    float textSize = 10.0f; // The size of the text in case Shape needs to draw the text
    boolean hidden = false; // Whether this shape should be drawn out/clickable in Play time
    boolean movable = true; // Whether this shape can be dragged around during Play time
    Scripts scripts;
    boolean highlighted = false;
    transient Paint textPaint, defaultPaint, highlightPaint;
    transient BitmapDrawable imageDrawable;

    /**
     * Default constructor for Shape - you can directly call this one.
     * This constructor will populate the rest of Shape's attributes using default values.
     */
    public Shape(String name, RectF coordinates) {
        this.name = name;
        this.coordinates = new SerializableRectF(coordinates);
        this.imageName = "";
        this.text = "";
        this.scripts = new Scripts();
        init();
    }

    /**
     * Constructor intended to be called by ShapeBuilder only
     * Calling this Shape constructor is not recommended; use ShapeBuilder to customize & construct the new Shape
     */
    public Shape(String name, SerializableRectF coordinates, String imageName, String text, float textSize,
                 boolean hidden, boolean movable, Scripts scripts, boolean highlighted) {
        this.name = name;
        this.coordinates = coordinates;
        this.imageName = imageName;
        this.text = text;
        this.textSize = textSize;
        this.hidden = hidden;
        this.movable = movable;
        this.scripts = scripts;
        this.highlighted = highlighted;
        init();
    }

    private void init() {
        textPaint = new Paint();
        textPaint.setTextSize(this.textSize);
        defaultPaint = new Paint();
        defaultPaint.setColor(Color.LTGRAY);
        highlightPaint = new Paint();
        highlightPaint.setColor(Color.GREEN);
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
    public RectF getRectF() { return coordinates.getRectF(); }
    public float getLeft() { return coordinates.getRectF().left; }
    public float getRight() { return coordinates.getRectF().right; }
    public float getBottom() { return coordinates.getRectF().bottom; }
    public float getTop() { return coordinates.getRectF().top; }
    public float getWidth() { return coordinates.getRectF().width(); }
    public float getHeight() { return coordinates.getRectF().height(); }
    public String getName() { return this.name; }
    public String getImageName() { return imageName; }
    public String getText() { return text; }
    public float getTextSize() { return textSize; }
    public boolean isHidden() { return hidden; }
    public boolean isMovable() { return movable; }
    public Scripts getScripts() { return scripts; }
    public boolean isHighlighted() { return highlighted; }

    // Below are some Setters

    // Set Coordinates directly with a RectF
    public void setCoordinates(RectF coordinates) {
        coordinates.sort();
        this.coordinates.setRectF(coordinates);
    }

    // Set Coordinates with (left, top, right, bottom)
    public void setCoordinates(float left, float top, float right, float bottom) {
        RectF coordinates = new RectF(left, top, right, bottom);
        setCoordinates(coordinates);
    }

    public void createCoordinates(float x, float y, float width, float height){
        float newleft = x - (width/2);
        float newright = x + (width/2);
        float newtop = y - (height/2);
        float newbot = y + (height/2);

        RectF coordinates = new RectF(newleft,newtop,newright,newbot);
        setCoordinates(coordinates);
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

    public void setName(String name) { this.name = name; }
    public void setHidden(boolean hidden) { this.hidden = hidden; }
    public void setMovable(boolean movable) { this.movable = movable; }
    public void setScripts(Scripts scripts) { this.scripts = scripts; }
    public void setHighlighted(boolean highlighted) { this.highlighted = highlighted; }

    // Other methods
    public void draw(Canvas canvas) {
        if (hidden) { return; }
        if (!text.equals("")) {
            canvas.drawText(text, this.getLeft(), this.getTop(), textPaint);
        } else if (imageDrawable != null) {
            canvas.drawBitmap(imageDrawable.getBitmap(), null, this.getRectF(), null);
        } else {
            canvas.drawRect(this.getRectF(), defaultPaint);
        }
        if (highlighted) {
            canvas.drawRect(coordinates.getRectF(), highlightPaint);
        }
    }

    // Returns whether a given (x, y) is located within the Shape
    public boolean contains(float x, float y) {
        return coordinates.getRectF().contains(x, y);
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        init();
    }
}