package edu.stanford.cs108.bunnyworld;

import android.graphics.RectF;

import java.io.IOException;
import java.io.Serializable;

public class SerializableRectF implements Serializable {

    RectF mRect;

    public SerializableRectF(RectF rect) {
        mRect = rect;
    }

    public RectF getRectF() {
        return mRect;
    }

    public void setRectF(RectF newRect) {
        mRect = newRect;
    }

    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        float left = mRect.left;
        float top = mRect.top;
        float right = mRect.right;
        float bottom = mRect.bottom;

        out.writeFloat(left);
        out.writeFloat(top);
        out.writeFloat(right);
        out.writeFloat(bottom);
    }

    private void readObject(java.io.ObjectInputStream in) throws IOException,
            ClassNotFoundException {
        float left = in.readFloat();
        float top = in.readFloat();
        float right = in.readFloat();
        float bottom = in.readFloat();

        mRect = new RectF(left, top, right, bottom);
    }
}
