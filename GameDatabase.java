package edu.stanford.cs108.bunnyworld;

import android.database.sqlite.SQLiteDatabase;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.sql.Blob;

public class GameDatabase {

    private SQLiteDatabase db;

    public void initDatabase() {
        db = SQLiteDatabase.openOrCreateDatabase("GamesDB", null);

        String clearStr = "DROP TABLE IF EXISTS games";
        db.execSQL(clearStr);

        String setupStr = "CREATE TABLE games ("
                + "game_num INTEGER, "
                + "game_data BLOB, "
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT"
                + ");";

        System.err.println(setupStr);
        db.execSQL(setupStr);
    }

    public void saveGame(int gameNum, Game game) throws IOException {
       // Clear out database
        String deleteStr = "DELETE FROM games WHERE "
        + "game_num = " + Integer.toString(gameNum) + ";";

        db.execSQL(deleteStr);

        // Create new entry
        Blob game_blob = null;

        byte[] bytes = null;

        //Blob testBlob = org.hibernate.Hibernate.createBlob(bytes);



    }

    /*
    public Game getGame(int gameNum) {

    }

     */

}
