package edu.stanford.cs108.bunnyworld;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.media.MediaPlayer;
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

    private int currPagePos;

    private Game() {
        pages = new ArrayList<Page>();
    }

    public static List<String> getGameNames() {
        SQLiteDatabase db = Database.getInstance();
        Cursor cursor = db.rawQuery("SELECT DISTINCT name FROM games",null);

        ArrayList<String> results = new ArrayList<String>();
        while (cursor.moveToNext()) {
            results.add(cursor.getString(0));
        }

        return results;
    }

    /**
     * Load a Game of given game_name from the Database, and set the Singleton instance.
     * If game_name doesn't exist, this function doesn't do anything.
     * @param gameName String, name of the game
     * @return the Game Singleton instance that is the loaded game
     */
    public static void load(String gameName) {
        SQLiteDatabase db = Database.getInstance();

        String command = "SELECT * FROM games WHERE name='" + gameName + "'";
        String orderBy = " ORDER BY _id DESC";

        Cursor cursor = db.rawQuery(command + orderBy, null);

        if(cursor.moveToFirst()) {
            byte[] game_bytes = cursor.getBlob(1);
            Log.i("hi", "this loaded game's id is: " + Integer.toString(cursor.getInt(2)));
            deserialize(game_bytes);
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
    }


    public static void loadPrevious(String gameName) {
        SQLiteDatabase db = Database.getInstance();

        String selectCommand = "SELECT * FROM games WHERE name='" + gameName + "'";
        String orderBy = " ORDER BY _id DESC";

        // Does nothing if there are less than two entries for this game in the database
        Cursor precursor = db.rawQuery(selectCommand + orderBy, null);
        int entryCount = 0;
        while (precursor.moveToNext()) {
            entryCount++;
        }
        if (entryCount < 2) {
            return;
        }

        Cursor cursor = db.rawQuery(selectCommand + orderBy, null);

        int id = -1;
        if (cursor.moveToFirst()) {
            id = cursor.getInt(2);
        }
        Log.i("hi", Integer.toString(id));
        if (id != -1) {
            String deleteCommand = "DELETE FROM games WHERE _id=" + Integer.toString(id) + ";";
            db.execSQL(deleteCommand);
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        Game.load(gameName);

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

    public static void set(List<Page> pages, int currPagePos) {
        instance.pages = pages;
        instance.currPagePos = currPagePos;
    }

    public static List<Page> getPages() {
        return instance.pages;
    }

    public static int getCurrPagePos() {return instance.currPagePos; }

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

    private static void printShapesPrivate() {
        if (instance.pages.size() < 1) { return; }
        Page printPage = instance.pages.get(0);
        for (int i = 0; i < printPage.shapes.size(); i++) {
            Log.i("hi", printPage.shapes.get(i).imageName);
        }
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