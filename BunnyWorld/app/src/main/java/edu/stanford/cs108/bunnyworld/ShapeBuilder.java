package edu.stanford.cs108.bunnyworld;

import android.graphics.RectF;

/**
 * The ShapeBuilder is a helper class that helps with constructing a new Shape.
 *
 * Example usage: Shape shape = ShapeBuilder()
 *                              .name("my_shape")
 *                              .coordinates(left, top, right, bottom)
 *                              .imageName("bunny")
 *                              .text("Hello bunny!", 20)
 *                              .hidden(true)
 *                              .movable(true)
 *                              .scripts(my_scripts)
 *                              .selected(true)
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
    String imageName; // Name of the image this Shape can draw
    String text; // Some text that this Shape can draw
    int textSize; // The size of the text in case Shape needs to draw the text
    boolean hidden = false; // Whether this shape should be drawn out/clickable in Play time
    boolean movable = true; // Whether this shape can be dragged around during Play time
    String[] scripts;
    boolean selected = false;

    public ShapeBuilder() {
        scripts = new String[0];
    }

    public Shape buildShape() {
        if (name == null || coordinates == null) {
            return null;
        } else {
            return new Shape(name, coordinates, imageName, text, textSize, hidden, movable, scripts, selected);
        }
    }

    public ShapeBuilder name(String name) {
        this.name = name;
        return this;
    }

    public ShapeBuilder coordinates(RectF coordinates) {
        coordinates.sort();
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

    public ShapeBuilder text(String text, int textSize) {
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

    public ShapeBuilder scripts(String[] scripts) {
        this.scripts = scripts;
        return this;
    }

    public ShapeBuilder selected(boolean selected) {
        this.selected = selected;
        return this;
    }

}
