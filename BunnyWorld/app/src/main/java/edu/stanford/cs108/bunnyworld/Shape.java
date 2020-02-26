package edu.stanford.cs108.bunnyworld;

import android.graphics.Canvas;
import android.graphics.RectF;

public class Shape {

    String name;
    RectF coordinates;
    String imageName; // Name of the image this Shape can draw
    String text; // Some text that this Shape can draw
    int textSize; // The size of the text in case Shape needs to draw the text
    boolean hidden; // Whether this shape should be drawn out/clickable in Play time
    boolean movable; // Whether this shape can be dragged around during Play time
    String[] scripts;
    boolean highlighted;

    // Do not call this Shape constructor directly; use ShapeBuilder to construct a new Shape
    // See ShapeBuilder documentation for creating a new Shape
    public Shape(String name, RectF coordinates, String imageName, String text, int textSize,
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
    public int getTextSize() { return textSize; }
    public boolean isHidden() { return hidden; }
    public boolean isMovable() { return movable; }
    public String[] getScripts() { return scripts; }
    public boolean isHighlighted() { return highlighted; }

    // Below are some Setters

    // Set Coordinates directly with a RectF
    public void setCoordinates(RectF coordinates) {
        coordinates.sort();
        this.coordinates = coordinates;
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
        return coordinates.contains(x, y);
    }
}
