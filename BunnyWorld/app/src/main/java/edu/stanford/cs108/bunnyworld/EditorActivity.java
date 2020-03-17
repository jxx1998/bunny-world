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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class EditorActivity extends AppCompatActivity {

    private String selection, shapeSelection;
    private int selectionID, shapeSelectionID;
    private Dialog dialog;
    protected static boolean okToGo;
    protected static String currGameName = "No Game Selected";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        okToGo = false;
        TextView selectedGameName = (TextView) findViewById(R.id.selectedGameName);
        selectedGameName.setText(currGameName);

    }

    @Override
    protected void onResume() {
        System.out.println("On Resume was called");
        okToGo = false;
        TextView selectedGameName = (TextView) findViewById(R.id.selectedGameName);
        selectedGameName.setText(currGameName);
        super.onResume();
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

        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        //maybe change this to .setMessage
        alert.setTitle("Name New Game");

        final EditText input = new EditText(this);
        alert.setView(input);
        alert.setPositiveButton("Ok",null);
        alert.setNegativeButton("Cancel", null);
        final AlertDialog dialogNewGame = alert.create();

        dialogNewGame.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button button = ((AlertDialog) dialogNewGame).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        String inputStr = input.getText().toString(); //name of new page
                        boolean isError = checkGameError(inputStr); //returns true if another page in the game has the same name

                        if (isError){
                            Toast toast = Toast.makeText(getApplicationContext(),
                                    "ERROR: Another game already has that name!",
                                    Toast.LENGTH_LONG);
                            toast.show();
                            return;
                        }

                        EditorView.currGameName = inputStr;
                        EditorView.isNew = true;
                        okToGo = true;
                        currGameName = inputStr;
                        TextView selectedGameName = (TextView) findViewById(R.id.selectedGameName);
                        selectedGameName.setText(currGameName);

                        dialogNewGame.dismiss();
                    }
                });
            }
        });

        dialogNewGame.show();



//        System.out.println("I CLICKED THE CREATE NEW GAME BUTTON");
//        AlertDialog.Builder alert = new AlertDialog.Builder(this);
//
//        //maybe change this to .setMessage
//        alert.setTitle("Name New Game");
//
//        final EditText input = new EditText(this);
//        alert.setView(input);
//
//        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int whichButton) {
//
//                String inputStr = input.getText().toString();
//                EditorView.currGameName = inputStr;
//                EditorView.isNew = true;
//                okToGo = true;
//                currGameName = inputStr;
//                TextView selectedGameName = (TextView) findViewById(R.id.selectedGameName);
//                selectedGameName.setText(currGameName);
//
//            }
//        });
//
//        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int whichButton) {
//                // Canceled.
//                okToGo = false;
//                currGameName = "No Game Selected";
//                TextView selectedGameName = (TextView) findViewById(R.id.selectedGameName);
//                selectedGameName.setText(currGameName);
//            }
//        });
//
//        alert.show();



    }


    private boolean checkGameError(String gameName){
        List<String> gameNames = Game.getGameNames();
        for (String individName : gameNames){
            if (individName.equals(gameName)){
                return true;
            }
        }
        return false;
    }

    public void openExistingGames(View view){
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
                    EditorView.gamePages = (ArrayList<Page>) Game.getPages();
                    EditorView.currGameName = selection;
                    EditorView.isNew = false;

                    // Game.printShapesPrivate();

                    okToGo = true;
                    currGameName = selection;
                    TextView selectedGameName = (TextView) findViewById(R.id.selectedGameName);
                    selectedGameName.setText(currGameName);
                }

            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                okToGo = false;
                currGameName = "No Game Selected";
                TextView selectedGameName = (TextView) findViewById(R.id.selectedGameName);
                selectedGameName.setText(currGameName);

            }
        });

        builder.show();

    }

    public void deleteGame(View view){
        if (!okToGo) {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Please Select a Game to Delete",
                    Toast.LENGTH_SHORT);
            toast.show();
        } else {
            Game.deleteGame(selection);
            okToGo = false;
            currGameName = "No Game Selected";
            TextView selectedGameName = (TextView) findViewById(R.id.selectedGameName);
            selectedGameName.setText(currGameName);
        }
    }
}
