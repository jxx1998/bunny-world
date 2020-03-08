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

public class EditorActivity extends AppCompatActivity {

    private String selection, shapeSelection;
    private int selectionID, shapeSelectionID;
    private Dialog dialog;
    protected static boolean okToGo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        okToGo = false;

    }

    public void goToEditor(View view){
        //ADD AN ERROR HANDLER IF NO INPUT NAME WAS GIVEN (ie okToGo boolean)
        if (!okToGo) {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Please Select a Game or Create a New One",
                    Toast.LENGTH_SHORT);
            toast.show();
        } else {
            Intent intent = new Intent(this, NewGameActivity.class);
            startActivity(intent);
        }
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
                okToGo = false;
            }
        });

        alert.show();



    }

    public void openExistingGames(View view){
        System.out.println("I CLICKED THE CHANGE PAGE BUTTON");

        final ArrayList<String> gameNames = getGameNames();
        int numGames = gameNames.size();
        final String[] arrayNames = new String[numGames];
        final ArrayList<String> pageNames = new ArrayList<String>();

        for (int i = 0; i < numGames; i++){
            arrayNames[i] = gameNames.get(i);
        }


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick The Game");


        builder.setSingleChoiceItems(arrayNames, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                selection = arrayNames[i];
                okToGo = false;
            }
        });

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (selection != null) {
                    Game.load(selection);
                    EditorView.gamePages = (ArrayList) Game.getPages();
                    EditorView.isNew = false;
                    okToGo = true;
                }

            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                okToGo = false;

            }
        });

        builder.show();

    }

    // This function can eventually be used to return a list of game names instead for a dropdown menu for game selection.
    private ArrayList<String> getGameNames() {
        SQLiteDatabase db = Database.getInstance();
        Cursor cursor = db.rawQuery("SELECT DISTINCT name FROM games",null);
        ArrayList<String> output = new ArrayList<String>();


        while (cursor.moveToNext()) {
            output.add(cursor.getString(0));
        }

        return output;
    }
}
