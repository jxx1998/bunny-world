package edu.stanford.cs108.bunnyworld;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import static android.content.Context.MODE_PRIVATE;

public class Database {

    private static SQLiteDatabase instance;

    /**
     * Call by MainActivity when the application starts up.
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
    }

    public static SQLiteDatabase getInstance() {
        return instance;
    }
}
