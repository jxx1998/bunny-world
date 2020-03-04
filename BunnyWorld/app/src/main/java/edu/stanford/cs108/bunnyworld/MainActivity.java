package edu.stanford.cs108.bunnyworld;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Database.createInstance(this);

        testGame(); // Testing only - could be deleted later
    }

    /**
     * This function is for testing game database serialization. Will be deleted eventually.
     */
    private void testGame() {
        Database.setupDatabase();

        Shape testShape = new Shape("testDude", new RectF(10.0f, 10.0f, 10.0f, 10.0f));
        Page testPage  = new Page("pageDude");
        testPage.addShape(testShape);
        Game.addPage(testPage);

        Game.save("test_game");
        Game.load("test_game");

        Log.i("hi", Game.getPages().get(0).name);
        Log.i("hi", Game.getPages().get(0).shapes.get(0).name);
        Log.i("hi", Float.toString(Game.getPages().get(0).shapes.get(0).coordinates.getRectF().top));
    }

    public void onChooseEditor(View view) {
        System.out.println("In the onChooseEditor method");
        Intent intent = new Intent(this, EditorActivity.class);
        System.out.println("After the intent creation");
        startActivity(intent);
    }

    /**
     * This function is for testing the database. Will be deleted eventually.
     */
    private void printDatabaseGames() {
        SQLiteDatabase db = Database.getInstance();
        Cursor cursor = db.rawQuery("SELECT * FROM games",null);
        String output = "";

        while (cursor.moveToNext()) {
            Log.i("hi", cursor.getString(0));
            output += cursor.getString(0) + "\n";
        }

        Log.i("hi", "The table contains " + output);
    }

}
