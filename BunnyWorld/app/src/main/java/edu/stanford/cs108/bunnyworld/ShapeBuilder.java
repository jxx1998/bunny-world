package edu.stanford.cs108.bunnyworld;

import android.graphics.Color;
import android.graphics.RectF;

/**
 * The ShapeBuilder is a helper class that helps with constructing a new Shape.
 *
 * Example usage: Shape shape = new ShapeBuilder()
 *                              .name("my_shape")
 *                              .coordinates(left, top, right, bottom)
 *                              .imageName("bunny")
 *                              .text("Hello bunny!", 10.0f)
 *                              .hidden(true)
 *                              .movable(true)
 *                              .scripts(my_scripts)
 *                              .highlighted(true)
 *                              .buildShape();
 *
 * Required fields that must be specified: .name() and .coordinates()
 * If .name() or .coordinates() not specified, buildShape() will return null
 * By default, the Shape is not hidden, and is movable.
 * There are two ways to specify coordinates: either pass in a RectF, or (left, top, right, bottom) in floats.
 */

public class ShapeBuilder {

    String name;
    RectF coordinates;
    String imageName = ""; // Name of the image this Shape can draw
    String text; // Some text that this Shape can draw
    float textSize = 10.0f; // The size of the text in case Shape needs to draw the text
    boolean hidden = false; // Whether this shape should be drawn out/clickable in Play time
    boolean movable = true; // Whether this shape can be dragged around during Play time
    Scripts scripts = new Scripts();
    int highlightColor = Color.TRANSPARENT;

    public Shape buildShape() {
        if (name == null || coordinates == null) {
            return null;
        } else {
            RectF rectF = new RectF(coordinates);
            return new Shape(name, rectF, imageName, text, textSize, hidden, movable, scripts, highlightColor);
        }
    }

    public ShapeBuilder name(String name) {
        this.name = name;
        return this;
    }

    public ShapeBuilder coordinates(RectF coordinates) {
        this.coordinates = coordinates;
        return this;
    }

    public ShapeBuilder coordinates(float left, float top, float right, float bottom) {
        this.coordinates = new RectF(left, top, right, bottom);
        return coordinates(this.coordinates);
    }

    public ShapeBuilder imageName(String imageName) {
        this.imageName = imageName;
        return this;
    }

    public ShapeBuilder text(String text, float textSize) {
        this.text = text;
        this.textSize = textSize;
        return this;
    }

    public ShapeBuilder hidden(boolean hidden) {
        this.hidden = hidden;
        return this;
    }

    public ShapeBuilder movable(boolean movable) {
        this.movable = movable;
        return this;
    }

    public ShapeBuilder scripts(Scripts scripts) {
        this.scripts = scripts;
        return this;
    }

    public ShapeBuilder setHighlightColor(int highlightColor) {
        this.highlightColor = highlightColor;
        return this;
    }

}