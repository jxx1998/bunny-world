package edu.stanford.cs108.bunnyworld;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import static edu.stanford.cs108.bunnyworld.BunnyWorldApplication.getGlobalContext;

public class Shape implements Serializable {

    static final Map<String, Typeface> nameToTypeface = new HashMap<String, Typeface>() {{
        put("DEFAULT", Typeface.DEFAULT);
        put("MONOSPACE", Typeface.MONOSPACE);
        put("SANS_SERIF", Typeface.SANS_SERIF);
        put("SERIF", Typeface.SERIF);
    }};

    String name;
    transient RectF coordinates;
    String imageName; // Name of the image this Shape can draw
    String text; // Some text that this Shape can draw
    boolean hidden = false; // Whether this shape should be drawn out/clickable in Play time
    boolean movable = true; // Whether this shape can be dragged around during Play time
    String typeface = "DEFAULT";
    Scripts scripts = new Scripts();
    int red = 0;
    int green = 0;
    int blue = 0;
    boolean bold = false;
    boolean italics = false;

    transient Paint paint, defaultPaint, highlightPaint;
    transient BitmapDrawable imageDrawable;

    /**
     * Default constructor for Shape - you can directly call this one.
     * This constructor will populate the rest of Shape's attributes using default values.
     */
    public Shape(String name, RectF coordinates) {
        this.name = name;
        this.coordinates = coordinates;
        constantInit();
        paint.setTypeface(Typeface.create(nameToTypeface.get(typeface), Typeface.NORMAL));
        paint.setTextSize(10.0f);
        highlightPaint.setColor(Color.TRANSPARENT);
    }

    private void constantInit() {
        paint = new Paint();
        defaultPaint = new Paint();
        highlightPaint = new Paint();
        loadImage();

        defaultPaint.setColor(Color.LTGRAY);
        highlightPaint.setStyle(Paint.Style.STROKE);
        highlightPaint.setStrokeWidth(15.0f);
    }

    private void loadImage() {
        Context context = getGlobalContext();
        Resources resources = context.getResources();
        try {
            final int resourceId = resources.getIdentifier(imageName, "drawable", context.getPackageName());
            imageDrawable = (BitmapDrawable) resources.getDrawable(resourceId);
        } catch (Exception e) {
            imageDrawable = null;
        }
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
    public float getTextSize() { return paint.getTextSize(); }
    public int getColor() { return paint.getColor(); }
    public int getStyle() { return paint.getTypeface().getStyle(); }
    public String getTypeface() { return typeface; }
    public boolean isHidden() { return hidden; }
    public boolean isMovable() { return movable; }
    public Scripts getScripts() { return scripts; }
    public int getHighlightColor() { return highlightPaint.getColor(); }

    // Below are some Setters

    // Set Coordinates directly with a RectF
    public void setCoordinates(RectF coordinates) {
        coordinates.sort();
        this.coordinates.set(coordinates);
    }

    // Set Coordinates with (left, top, right, bottom)
    public void setCoordinates(float left, float top, float right, float bottom) {
        RectF coordinates = new RectF(left, top, right, bottom);
        setCoordinates(coordinates);
    }

    public void setCenterCoordinates(float x, float y, float width, float height){
        float newLeft = x - (width / 2);
        float newRight = x + (width / 2);
        float newTop = y - (height / 2);
        float newBottom = y + (height / 2);

        setCoordinates(newLeft, newTop, newRight, newBottom);
    }

    public void setText(String text, float textSize) {
        this.text = text;
        paint.setTextSize(textSize);
    }

    public void setColor(int color) {
        paint.setColor(color);
    }

    public void setBold(boolean bolded) {
        if (bolded) {
            paint.setTypeface(Typeface.create(nameToTypeface.get(typeface), Typeface.BOLD));
            bold = true;
        } else {
            paint.setTypeface(Typeface.create(nameToTypeface.get(typeface), Typeface.NORMAL));
            bold = false;
        }
    }

    public void setItalic(boolean italiced) {
        if (italiced) {
            paint.setTypeface(Typeface.create(nameToTypeface.get(typeface), Typeface.ITALIC));
            italics = true;
        } else {
            paint.setTypeface(Typeface.create(nameToTypeface.get(typeface), Typeface.NORMAL));
            italics = false;
        }
    }

    public void setTypeface(String typeface) {
        if (nameToTypeface.containsKey(typeface)) {
            this.typeface = typeface;
            paint.setTypeface(Typeface.create(nameToTypeface.get(typeface), paint.getTypeface().getStyle()));
        }
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
        loadImage();
    }

    public void setHighlightColor(int highlightColor) {
        highlightPaint.setColor(highlightColor);
    }

    public void setName(String name) { this.name = name; }
    public void setHidden(boolean hidden) { this.hidden = hidden; }
    public void setMovable(boolean movable) { this.movable = movable; }
    public void setScripts(Scripts scripts) { this.scripts = scripts; }

    // Other methods
    public void draw(Canvas canvas) {
        canvas.drawRect(coordinates, highlightPaint);
        if (hidden) { return; }
        if (text != null) {
            canvas.drawText(text, this.getLeft(), this.getTop(), paint);
        } else if (imageDrawable != null) {
            canvas.drawBitmap(imageDrawable.getBitmap(), null, this.getRectF(), paint);
        } else {
            canvas.drawRect(this.getRectF(), defaultPaint);
        }
    }

    public void onClick() {
        scripts.onClick();
    }

    public void onEnter() {
        scripts.onEnter();
    }

    public boolean onDrop(String shapeName) {
        return scripts.onDrop(shapeName);
    }

    // Returns whether a given (x, y) is located within the Shape
    public boolean contains(float x, float y) {
        return coordinates.contains(x, y);
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();

        constantInit();

        float left = in.readFloat();
        float top = in.readFloat();
        float right = in.readFloat();
        float bottom = in.readFloat();
        this.coordinates = new RectF(left, top, right, bottom);

        float textSize = in.readFloat();
        paint.setTextSize(textSize);
        int textStyle = in.readInt();
        paint.setTypeface(Typeface.create(nameToTypeface.get(typeface), textStyle));
        int textColor = in.readInt();
        paint.setColor(textColor);

        int highlightColor = in.readInt();
        highlightPaint.setColor(highlightColor);
    }

    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();

        out.writeFloat(this.coordinates.left);
        out.writeFloat(this.coordinates.top);
        out.writeFloat(this.coordinates.right);
        out.writeFloat(this.coordinates.bottom);
        out.writeFloat(paint.getTextSize());
        out.writeInt(paint.getTypeface().getStyle());
        out.writeInt(paint.getColor());
        out.writeInt(highlightPaint.getColor());
    }

}