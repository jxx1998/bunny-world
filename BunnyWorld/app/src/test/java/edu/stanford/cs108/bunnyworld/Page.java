package edu.stanford.cs108.bunnyworld;

import android.graphics.Canvas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Page implements Serializable {

    String name;
    List<Shape> shapes;
    String backGroundMusic;
    String backGroundImage;

    public Page(String name, List<Shape> shapes) {
        this.name = name;
        this.shapes = shapes;
    }

    public Page(String name) {
        this.name = name;
        this.shapes = new ArrayList<Shape>();
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

    // Sets the Shapes of the Page in bulk
    public void setShapes(List<Shape> shapes) {
        this.shapes = shapes;
    }

    /**
     * Given a (x, y) touch coordinate, returns the top-most Shape that contains (x, y) in the Page
     * Returns null if no Shape in the Page contains (x, y)
     * You can specify whether you want hidden/unmovable Shapes included in the function return as well.
     *
     * @param (x, y) the touch coordinates; returnHidden, return Unmovable
     * @return Shape: the Shape that contains (x, y)
     */
    public Shape shapeTouched(float x, float y, boolean returnHidden, boolean returnUnmovable) {
        for (int i = shapes.size() - 1; i >= 0; i--) {
            if (returnHidden == false && shapes.get(i).isHidden()) {
                continue;
            }
            if (returnUnmovable == false && shapes.get(i).isMovable() == false) {
                continue;
            }
            if (shapes.get(i).getRectF().contains(x, y)) {
                return shapes.get(i);
            }
        }
        return null;
    }

    /**
     * Given a Shape, returns all shapes in the current Page that have at least some overlap with
     * the given Shape in a List.
     * You can specify whether you want hidden/unmovable Shapes included in the function return as well
     *
     * @param shape, returnHidden, returnUnmovable
     * @return List of Shapes
     */
    public List<Shape> shapeOverlapped(Shape shape, boolean returnHidden, boolean returnUnmovable) {
        List<Shape> overlaps = new ArrayList<Shape>();
        for (Shape candidate: shapes) {
            if (returnHidden == false && candidate.isHidden()) {
                continue;
            }
            if (returnUnmovable == false && candidate.isMovable() == false) {
                continue;
            }
            if (candidate.coordinates.getRectF().intersect(shape.coordinates.getRectF())) {
                overlaps.add(candidate);
            }
        }
        return overlaps;
    }
}