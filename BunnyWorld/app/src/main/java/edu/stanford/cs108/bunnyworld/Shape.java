package edu.stanford.cs108.bunnyworld;

import android.graphics.RectF;

public class Shape {

    String name;
    RectF coordinates;
    boolean inPossessionsArea;
    String pageName; // String is null if item is in Possessions Area
    String imageName; // Name of the image this Shape can draw
    String text; // Some text that this Shape can draw
    int textSize; // The size of the text in case Shape needs to draw the text
    boolean hidden; // Whether this shape should be drawn out/clickable in Play time
    boolean movable; // Whether this shape can be dragged around during Play time
    String[] scripts;
    boolean selected;

    public float getLeft() { return coordinates.left; }
    public float getRight() { return coordinates.right; }
    public float getBottom() { return coordinates.bottom; }
    public float getTop() { return coordinates.top; }
    public float getWidth() { return coordinates.width(); }
    public float getHeight() { return coordinates.height(); }
    
}
