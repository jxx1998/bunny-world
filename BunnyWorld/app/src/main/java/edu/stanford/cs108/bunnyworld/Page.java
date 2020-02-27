package edu.stanford.cs108.bunnyworld;

import android.graphics.Canvas;

import java.util.List;

public class Page {

    String name;
    List<Shape> shapes;
    String backGroundMusic;
    String backGroundImage;

    public Page(String name, List<Shape> shapes) {
        this.name = name;
        this.shapes = shapes;
    }

    public void draw(Canvas canvas) {
        for (Shape shape: shapes) {
            shape.draw(canvas);
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBackGroundMusic(String backGroundMusic) {
        this.backGroundMusic = backGroundMusic;
    }

    public void setBackGroundImage(String backGroundImage) {
        this.backGroundImage = backGroundImage;
    }

    public void addShape(Shape shape) {
        this.shapes.add(shape);
    }

    public void removeShape(Shape shape) {
        this.shapes.remove(shape);
    }

    // Set Shapes of the page in bulk
    public void setShapes(List<Shape> shapes) {
        this.shapes = shapes;
    }

    // Given a (x, y) touch coordinate, returns the top-most Shape that contains (x, y) in the Page
    // Returns null if no Shape in the Page contains (x, y)
    // Note: This method returns hidden/not movable Shapes as well
    public Shape shapeTouched(float x, float y) {
        for (int i = shapes.size() - 1; i >= 0; i--) {
            if (shapes.get(i).contains(x, y)) {
                return shapes.get(i);
            }
        }
        return null;
    }
}
