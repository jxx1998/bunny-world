package edu.stanford.cs108.bunnyworld;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = openOrCreateDatabase("GamesDB", MODE_PRIVATE, null);
        Cursor tablesCursor = db.rawQuery("SELECT * FROM sqlite_master WHERE type='table' AND name='games';", null);
        if (tablesCursor.getCount() == 0){ setupDatabase(); }

        testDatabase(); // Testing only - could be deleted later
    }

    /**
     * This function is for testing serialization/database. Could be deleted later.
     */
    private void testDatabase() {
        setupDatabase();

        Shape testShape = new ShapeBuilder().coordinates(10.0f, 10.0f, 10.0f, 10.0f).name("testDude").buildShape();
        testShape.playSound("woof");
        Page testPage  = new Page("pageDude");
        testPage.addShape(testShape);
        Game testGame = new Game();
        testGame.addPage(testPage);

        saveGame("test_game", testGame);

        Game loaded_game = loadGame("test_game");
        Log.i("hi", loaded_game.pages.get(0).name);
        Log.i("hi", loaded_game.pages.get(0).shapes.get(0).name);
        Log.i("hi", Float.toString(loaded_game.pages.get(0).shapes.get(0).coordinates.getRectF().top));
    }

    public void onChooseEditor(View view) {
        System.out.println("In the onChooseEditor method");
        Intent intent = new Intent(this, EditorActivity.class);
        System.out.println("After the intent creation");
        startActivity(intent);
    }

    private void setupDatabase() {
        db.execSQL("DROP TABLE IF EXISTS games");

        String setupStr = "CREATE TABLE games ("
                + "name TEXT, data BLOB, "
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT"
                + ");";

        db.execSQL(setupStr);
    }


    private void deleteGame(String game_name) {
        String command = "DELETE FROM games WHERE name='" + game_name + "'";
        db.execSQL(command);
    }

    private void saveGame(String game_name, Game game) {
        deleteGame(game_name);

        byte[] game_bytes = game.serialize();
        String command = "INSERT INTO games (name, data) VALUES (?, ?)";
        SQLiteStatement insertStatement = db.compileStatement(command);
        insertStatement.clearBindings();
        insertStatement.bindString(1, game_name);
        insertStatement.bindBlob(2, game_bytes);
        insertStatement.executeInsert();
    }

    private void printDatabaseGames() {
        Cursor cursor = db.rawQuery("SELECT * FROM games",null);
        String output = "";

        while (cursor.moveToNext()) {
            Log.i("hi", cursor.getString(0));
            output += cursor.getString(0) + "\n";
        }

        Log.i("hi", "The table contains " + output);
    }


    private Game loadGame(String game_name) {
        String command = "SELECT * FROM games WHERE name='" + game_name + "'";
        Cursor cursor = db.rawQuery(command, null);

        if (cursor.getCount() == 0) {
            Log.i("hi", "the cursor count is also 0");
        }


        Game this_game = null;
        if(cursor.moveToFirst()) {
            byte[] game_bytes = cursor.getBlob(1);
            this_game = Game.deserialize(game_bytes);
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return this_game;
    }
}
