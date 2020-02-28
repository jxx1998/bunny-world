package edu.stanford.cs108.bunnyworld;

import android.graphics.Canvas;
import android.graphics.RectF;

import java.io.Serializable;

public class Shape implements Serializable {

    String name;
    SerializableRectF coordinates;
    String imageName; // Name of the image this Shape can draw
    String text; // Some text that this Shape can draw
    int textSize; // The size of the text in case Shape needs to draw the text
    boolean hidden; // Whether this shape should be drawn out/clickable in Play time
    boolean movable; // Whether this shape can be dragged around during Play time
    String[] scripts;
    boolean highlighted;

    // Do not call this Shape constructor directly; use ShapeBuilder to construct a new Shape
    // See ShapeBuilder documentation for creating a new Shape
    public Shape(String name, SerializableRectF coordinates, String imageName, String text, int textSize,
                 boolean hidden, boolean movable, String[] scripts, boolean highlighted) {
        this.name = name;
        this.coordinates = coordinates;
        this.imageName = imageName;
        this.text = text;
        this.textSize = textSize;
        this.hidden = hidden;
        this.movable = movable;
        this.scripts = scripts;
        this.highlighted = highlighted;
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
    public int getTextSize() { return textSize; }
    public boolean isHidden() { return hidden; }
    public boolean isMovable() { return movable; }
    public String[] getScripts() { return scripts; }
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

    public void setName(String name) {
        this.name = name;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public void setMovable(boolean movable) {
        this.movable = movable;
    }

    public void setScripts(String[] scripts) {
        this.scripts = scripts;
    }

    public void setHighlighted(boolean highlighted) {
        this.highlighted = highlighted;
    }

    // Other methods
    public void draw(Canvas canvas) {

    }

    // Returns whether a given (x, y) is located within the Shape
    public boolean contains(float x, float y) {
        return coordinates.getRectF().contains(x, y);
    }
}
