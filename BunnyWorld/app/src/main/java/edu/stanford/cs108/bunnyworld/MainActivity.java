package edu.stanford.cs108.bunnyworld;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Database.createInstance(this);

        testGame(); // Testing only - could be deleted later

        //printDatabaseGames();
        //ArrayList<String> testList = getGameNames();
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
        Log.i("hi", Float.toString(Game.getPages().get(0).shapes.get(0).coordinates.top));

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

    /*
    // This function can eventually be used to return a list of game names instead for a dropdown menu for game selection.
    private ArrayList<String> getGameNames() {
        SQLiteDatabase db = Database.getInstance();
        // Log.i("hi", "entered the printDatabaseGames method");
        Cursor cursor = db.rawQuery("SELECT DISTINCT name FROM games",null);
        ArrayList<String> output = new ArrayList<String>();


        while (cursor.moveToNext()) {
            Log.i("hi", cursor.getString(0));
            output.add(cursor.getString(0));
        }

        for (int i = 0; i < output.size(); i++) {
            Log.i("hi", "table contains " + output.get(i));
        }
        return output;
    }

     */

}
