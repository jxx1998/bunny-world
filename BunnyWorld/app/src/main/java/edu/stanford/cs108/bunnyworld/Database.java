package edu.stanford.cs108.bunnyworld;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static edu.stanford.cs108.bunnyworld.BunnyWorldApplication.getGlobalContext;

public class Database {

    private static SQLiteDatabase instance;

    /**
     * Called by MainActivity when the application starts up.
     * @param context
     */
    public static void createInstance(Context context) {
        instance = context.openOrCreateDatabase("GamesDB", MODE_PRIVATE, null);
        Cursor tablesCursor = instance.rawQuery("SELECT * FROM sqlite_master WHERE type='table' AND name='games';", null);
        if (tablesCursor.getCount() == 0) { setupDatabase(); }
    }

    /**
     * Call this function to reset data in the database.
     */
    public static void setupDatabase() {
        instance.execSQL("DROP TABLE IF EXISTS games");

        String setupStr = "CREATE TABLE games ("
                + "name TEXT, data BLOB, "
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT"
                + ");";

        instance.execSQL(setupStr);
        Context context = getGlobalContext();
        InputStream ins = context.getResources().openRawResource(
                context.getResources().getIdentifier("example_game_file",
                        "raw", context.getPackageName()));
        List<Integer> byteList = new ArrayList<Integer>();
        try {
            Integer byteInt = ins.read();
            while (byteInt != -1) {
                byteList.add(byteInt);
                byteInt = ins.read();
            }
        } catch (Exception ignored) {}
        byte[] gameBytes = new byte[byteList.size()];
        int index = 0;
        for (Integer byteInt : byteList) {
            gameBytes[index++] = byteInt.byteValue();
        }
        Game.saveBytes(gameBytes, "default_game");
        Log.d("serialize", Arrays.toString(gameBytes));
    }

    public static SQLiteDatabase getInstance() {
        return instance;
    }
}
