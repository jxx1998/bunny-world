package edu.stanford.cs108.bunnyworld;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class EditorActivity extends AppCompatActivity {

    private String selection, shapeSelection;
    private int selectionID, shapeSelectionID;
    private Dialog dialog;
    protected static boolean okToGo = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

    }

    public void goToEditor(View view){
        //ADD AN ERROR HANDLER IF NO INPUT NAME WAS GIVEN (ie okToGo boolean)
        Intent intent = new Intent(this,NewGameActivity.class);
        startActivity(intent);
    }

    public void onOpenNewGame(View view) {

        System.out.println("I CLICKED THE CREATE NEW GAME BUTTON");
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        //maybe change this to .setMessage
        alert.setTitle("Name New Game");

        final EditText input = new EditText(this);
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                String inputStr = input.getText().toString();
                EditorView.currGameName = inputStr;
                EditorView.isNew = true;
                okToGo = true;

            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        alert.show();



    }

    public void openExistingGames(View view){
        Toast toast = Toast.makeText(getApplicationContext(),
                "You've selected to open an existing Game",
                Toast.LENGTH_SHORT);
        toast.show();
        System.out.println("I CLICKED THE CHANGE PAGE BUTTON");

        final List<String> gameNames = Game.getGameNames();
        int numGames = gameNames.size();
        final String[] arrayNames = new String[numGames];
        final ArrayList<String> pageNames = new ArrayList<String>();

        for (int i = 0; i < numGames; i++){
            arrayNames[i] = gameNames.get(i);
        }


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick The Game");

        //to begin with, the selected page will be the current page we're on
       // final int currPos = CustomView.currPagePos;
        //selection = arrayNames[currPos];
        //selectionID = currPos;

        builder.setSingleChoiceItems(arrayNames, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                selection = arrayNames[i];
               // selectionID = i;
            }
        });

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "You've clicked ok",
                        Toast.LENGTH_SHORT);
                toast.show();
                Game.load(selection);
                EditorView.gamePages = (ArrayList) Game.getPages();
                EditorView.isNew = false;
//                CustomView.currPagePos = selectionID;
//                CustomView.currPage = pages.get(selectionID);
//                //So that latest added shape isnt added
//                CustomView.left = -10f;
//                CustomView myView = findViewById(R.id.myCustomView);
//                myView.invalidate();

            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        builder.show();

    }
}
