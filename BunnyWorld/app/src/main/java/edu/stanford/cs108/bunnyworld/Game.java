package edu.stanford.cs108.bunnyworld;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class Game implements Serializable {
    ArrayList<Page> pages;

    public Game() {
        pages = new ArrayList<Page>();
    }

    public Game(ArrayList<Page> pageList) {
        pages = pageList;
    }

    public void addPage(Page page) {
        pages.add(page);
    }

    public void removePage(Page page) { pages.remove(page); }

    public byte[] serialize() {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = null;
        byte[] gameBytes = null;
        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(this);
            out.flush();
            gameBytes = bos.toByteArray();
        } catch (Exception ignored) {
        } finally {
            try {
                bos.close();
            } catch (IOException ignored) {}
        }
        return gameBytes;
    }

    public static Game deserialize(byte[] gameBytes) {
        ByteArrayInputStream bis = new ByteArrayInputStream(gameBytes);
        ObjectInput in = null;
        Game game = null;
        try {
            in = new ObjectInputStream(bis);
            game = (Game) in.readObject();
        } catch (Exception ignored) {
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception ignored) {}
        }
        return game;
    }
}