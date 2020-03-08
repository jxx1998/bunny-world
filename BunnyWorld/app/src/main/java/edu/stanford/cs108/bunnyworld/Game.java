package edu.stanford.cs108.bunnyworld;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Game implements Serializable {

    private static Game instance = new Game();

    private List<Page> pages;

    private Game() {
        pages = new ArrayList<Page>();
    }

    /**
     * Load a Game of given game_name from the Database, and set the Singleton instance.
     * If game_name doesn't exist, this function doesn't do anything.
     * @param gameName String, name of the game
     * @return the Game Singleton instance that is the loaded game
     */
    public static void load(String gameName) {
        SQLiteDatabase db = Database.getInstance();

        String command = "SELECT * FROM games WHERE name='" + gameName + "'" + " ORDER BY _id DESC";

        Cursor cursor = db.rawQuery(command, null);

        if(cursor.moveToFirst()) {
            byte[] game_bytes = cursor.getBlob(1);
            deserialize(game_bytes);
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
    }

    /**
     * Saves the current Game into the Database with name "game_name".
     * @param gameName String, name of the game
     */
    public static void save(String gameName) {
        // deleteGame(gameName);
        SQLiteDatabase db = Database.getInstance();
        byte[] game_bytes = serialize();
        Log.d("game", Boolean.toString(game_bytes == null));
        String command = "INSERT INTO games (name, data) VALUES (?, ?)";
        SQLiteStatement insertStatement = db.compileStatement(command);
        insertStatement.clearBindings();
        insertStatement.bindString(1, gameName);
        insertStatement.bindBlob(2, game_bytes);
        insertStatement.executeInsert();
    }

    public static void addPage(Page page) {
        instance.pages.add(page);
    }

    public static void addShape(int pageIndex, Shape shape) {
        instance.pages.get(pageIndex).addShape(shape);
    }

    public static void setPages(List<Page> pages) {
        instance.pages = pages;
    }

    public static List<Page> getPages() {
        return instance.pages;
    }

    public static void removePage(Page page) {
        instance.pages.remove(page);
    }

    public static void renamePage(String oldName, String newName) {
        for (int i = 0; i < instance.pages.size(); i++) {
            if (instance.pages.get(i).name == oldName) {
                instance.pages.get(i).name = newName;
                return;
            }
        }
    }

    public static Shape getShape(String name) {
        for (Page page: instance.pages) {
            Shape shape = page.getShape(name);
            if (shape != null) {
                return shape;
            }
        }
        return null;
    }

    // Below are private helper methods
    private static void deleteGame(String gameName) {
        SQLiteDatabase db = Database.getInstance();
        String command = "DELETE FROM games WHERE name='" + gameName + "'";
        db.execSQL(command);
    }

    private static byte[] serialize() {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] gameBytes = null;
        try {
            ObjectOutputStream out = new ObjectOutputStream(bos);
            out.writeObject(instance);
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

    private static void deserialize(byte[] gameBytes) {
        ByteArrayInputStream bis = new ByteArrayInputStream(gameBytes);
        ObjectInput in = null;
        try {
            in = new ObjectInputStream(bis);
            instance = (Game) in.readObject();
        } catch (Exception ignored) {
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception ignored) {}
        }
    }
}