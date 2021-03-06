package edu.stanford.cs108.bunnyworld;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static edu.stanford.cs108.bunnyworld.BunnyWorldApplication.getGlobalContext;

public class Database {

    private static final boolean LOAD_DEFAULT_GAME = true;
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

        if (LOAD_DEFAULT_GAME) {
            List<String> fileNames = new ArrayList<String>();
            fileNames.add("default_game");
            fileNames.add("default_game_extensions");
            loadDefaultGame(fileNames);
        }
    }

    private static void loadDefaultGame(List<String> fileNames) {
        for (String fileName: fileNames) {
            Context context = getGlobalContext();
            InputStream ins = context.getResources().openRawResource(
                    context.getResources().getIdentifier(fileName,
                            "raw", context.getPackageName()));
            try {
                byte[] byteArray = new byte[ins.available()];
                ins.read(byteArray);
                Game.deserialize(byteArray);
                Log.d("serialize", Integer.toString(Game.getPages().size()));
                Game.save(fileName);
            } catch (Exception e) {
                Log.d("Load default game", e.toString());
            }
        }
    }

    public static SQLiteDatabase getInstance() {
        return instance;
    }
}
