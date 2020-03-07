package edu.stanford.cs108.bunnyworld;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

public class EditorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
    }

    public void onOpenNewGame(View view) {
        Intent intent = new Intent(this,NewGameActivity.class);
        startActivity(intent);
    }

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
}
