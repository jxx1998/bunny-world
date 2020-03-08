package edu.stanford.cs108.bunnyworld;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.List;


public class MainActivity extends AppCompatActivity {

    int selectedGameIndex;
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Database.createInstance(this);

        // testGame(); // Testing only - could be deleted later
        // Database.setupDatabase();

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

    public void onChooseGame(View view) {
        List<String> gameNameList = Game.getGameNames();
        final String[] gameNames = gameNameList.toArray(new String[gameNameList.size()]);
        selectedGameIndex = -1;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select a Game To Play");
        builder.setSingleChoiceItems(gameNames, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                selectedGameIndex = i;
            }
        });
        builder.setPositiveButton("Start Game", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (selectedGameIndex != -1) {
                    Game.load(gameNames[selectedGameIndex]);
                    Intent intent = new Intent(context, GameActivity.class);
                    startActivity(intent);
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {}
        });
        builder.show();
    }

}
