package edu.stanford.cs108.bunnyworld;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static edu.stanford.cs108.bunnyworld.BunnyWorldApplication.getGlobalContext;

public class Page implements Serializable {

    String name;
    List<Shape> shapes;
    String backGroundMusic;
    String backGroundImage;
    transient BitmapDrawable imageDrawable;

    public Page(String name, List<Shape> shapes) {
        this(name);
        this.shapes = shapes;
    }

    public Page(String name) {
        this.name = name;
        this.shapes = new ArrayList<Shape>();
        this.backGroundImage = "";
        this.backGroundMusic = "";
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
        loadImage();
    }

    private void loadImage() {
        Context context = getGlobalContext();
        Resources resources = context.getResources();
        try {
            final int resourceId = resources.getIdentifier(backGroundImage, "drawable", context.getPackageName());
            imageDrawable = (BitmapDrawable) resources.getDrawable(resourceId);
        } catch (Resources.NotFoundException e) {
            imageDrawable = null;
        }
    }

    public void addShape(Shape shape) {
        this.shapes.add(shape);
    }

    public void removeShape(Shape shape) {
        this.shapes.remove(shape);
    }

    public Shape getShape(String name) {
        for (Shape shape: shapes) {
            if (shape.name == name) {
                return shape;
            }
        }
        return null;
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

    public void makeTopMost(Shape selectedShape){
        removeShape(selectedShape);
        addShape(selectedShape);
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
            if (candidate.coordinates.intersect(shape.coordinates)) {
                overlaps.add(candidate);
            }
        }
        return overlaps;
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        loadImage();
    }
}
