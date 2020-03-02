package edu.stanford.cs108.bunnyworld;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;

import java.util.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

import static edu.stanford.cs108.bunnyworld.BunnyWorldApplication.getGlobalContext;

public class Shape implements Serializable {

    String name;
    SerializableRectF coordinates;
    String imageName; // Name of the image this Shape can draw
    String text; // Some text that this Shape can draw
    float textSize; // The size of the text in case Shape needs to draw the text
    boolean hidden; // Whether this shape should be drawn out/clickable in Play time
    boolean movable; // Whether this shape can be dragged around during Play time
    String[] scripts;
    boolean highlighted;
    transient Paint textPaint, defaultPaint;
    transient BitmapDrawable imageDrawable;

    // class defining scripts object
    public class Scripts {

        List<Clause> clauses;

        public Scripts(Shape shape, ArrayList<Clause> clauses) {
            this.clauses = clauses;
        }

        public class Clause {
            String trigger;
            List<Action> actions;

            public Clause(String trigger, ArrayList<Action> actions) {
                this.trigger = trigger;
                this.actions = actions;
            }

            // Actions inner class
            public class Action {
                String keyword;
                String name;

                public Action (String keyword, String name) {
                    this.keyword = keyword;
                    this.name = name;
                }

                public void execute() {
                    if (keyword == "goto") {
                        // TODO
                    } else if (keyword == "play") {
                        // Something like this
                        // Shape.this.playSound(name);
                    } else if (keyword == "hide") {
                        Shape.this.setHidden(true);
                    } else if (keyword == "show") {
                        Shape.this.setHidden(false);
                    }
                }
            }

            public void run () {
                for (Action a : actions) {
                    a.execute();
                }
            }

        }
    }

    // Do not call this Shape constructor directly; use ShapeBuilder to construct a new Shape
    // See ShapeBuilder documentation for creating a new Shape
    public Shape(String name, SerializableRectF coordinates, String imageName, String text, float textSize,
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
        textPaint = new Paint();
        textPaint.setTextSize(this.textSize);
        defaultPaint = new Paint();
        defaultPaint.setColor(Color.LTGRAY);
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
    public void setScripts(String[] scripts) { this.scripts = scripts; }
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
    }

    // Returns whether a given (x, y) is located within the Shape
    public boolean contains(float x, float y) {
        return coordinates.getRectF().contains(x, y);
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        textPaint = new Paint();
        textPaint.setTextSize(this.textSize);
        defaultPaint = new Paint();
        defaultPaint.setColor(Color.LTGRAY);
        loadImage();
    }
}