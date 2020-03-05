package edu.stanford.cs108.bunnyworld;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class NewGameActivity extends AppCompatActivity {

    private String selection, shapeSelection;
    private int selectionID, shapeSelectionID;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_game_editor);

    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    public void showScript(MenuItem item) {
        //Display script
    }

    public void changeDrawShape(View view){
        System.out.println("I CLICKED THE CHANGE SHAPE BUTTON");

        final ArrayList<String> shapes = CustomView.shapeNames;
        int numPages = shapes.size();
        final String[] arrayShapeNames = new String[numPages];

        for (int i = 0; i < numPages; i++){
            arrayShapeNames[i] = shapes.get(i);
        }


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick The Shape");

        int currPos = CustomView.currShapePos;



        builder.setSingleChoiceItems(arrayShapeNames, currPos, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                shapeSelection = arrayShapeNames[i];
                shapeSelectionID = i;
            }
        });

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                CustomView.currShapePos = shapeSelectionID;
                CustomView.currDrawShapeName = shapes.get(shapeSelectionID);
                //So that latest added shape isnt added
                CustomView.left = -10f;
                CustomView myView = findViewById(R.id.myCustomView);
                myView.invalidate();

            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        builder.show();

    }

    public void createPage(View view){
        System.out.println("I CLICKED THE CREATE PAGE BUTTON");
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        //maybe change this to .setMessage
        alert.setTitle("Name New Page");

        final EditText input = new EditText(this);
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String inputStr = input.getText().toString();
                Page newPage = new Page(inputStr);

                CustomView.gamePages.add(newPage);
                int currentPos = CustomView.gamePages.indexOf(newPage);
                CustomView.currPagePos = currentPos;
                CustomView.currPage = CustomView.gamePages.get(currentPos);
                //So that latest added shape isnt added
                CustomView.left = -10f;

                CustomView myView = findViewById(R.id.myCustomView);
                myView.invalidate();
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        alert.show();

    }

    public void renamePage(View view){

        System.out.println("I CLICKED THE RENAME PAGE BUTTON");
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        //maybe change this to .setMessage
        alert.setTitle("Rename Current Page");

        final EditText input = new EditText(this);
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String inputStr = input.getText().toString();
                Page currPage = CustomView.currPage;
                currPage.setName(inputStr);

                CustomView myView = findViewById(R.id.myCustomView);
                myView.invalidate();
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        alert.show();


    }

    public void changePage(View view){
        System.out.println("I CLICKED THE CHANGE PAGE BUTTON");

        final ArrayList<Page> pages = CustomView.gamePages;
        int numPages = pages.size();
        final String[] arrayNames = new String[numPages];
        final ArrayList<String> pageNames = new ArrayList<String>();
        for (Page page : pages){
            pageNames.add(page.name);
        }

        for (int i = 0; i < numPages; i++){
            arrayNames[i] = pageNames.get(i);
        }


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick The Page");

        //to begin with, the selected page will be the current page we're on
        final int currPos = CustomView.currPagePos;
        selection = arrayNames[currPos];
        selectionID = currPos;

        builder.setSingleChoiceItems(arrayNames, currPos, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                selection = arrayNames[i];
                selectionID = i;
            }
        });

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                CustomView.currPagePos = selectionID;
                CustomView.currPage = pages.get(selectionID);
                //So that latest added shape isnt added
                CustomView.left = -10f;
                CustomView myView = findViewById(R.id.myCustomView);
                myView.invalidate();

            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        builder.show();

    }


    public void deletePage(View view){
        System.out.println("I CLICKED THE DELETE BUTTON");

        int currentPagePosition = CustomView.currPagePos;

        System.out.println("current page position: " + currentPagePosition);

        int numPages = CustomView.gamePages.size();
        if (numPages == 1){
            Toast toast = Toast.makeText(getApplicationContext(),
                    "There is only 1 Page Left",
                    Toast.LENGTH_LONG);
            toast.show();
        } else{



            CustomView.gamePages.remove(currentPagePosition);
            int numPagesNew = CustomView.gamePages.size();
            if (currentPagePosition == numPagesNew){
                currentPagePosition = currentPagePosition -1;
            }


            Page toChangeToPage = CustomView.gamePages.get(currentPagePosition);
            CustomView.currPage = toChangeToPage;
            CustomView.currPagePos = currentPagePosition;
            CustomView.left = -10f;

            CustomView myView = findViewById(R.id.myCustomView);
            myView.invalidate();
        }

    }

    public void addShape(View view){
        System.out.println("I CLICKED THE ADD SHAPE BUTTON");
        CustomView.createNewShape = true;
        CustomView myView = findViewById(R.id.myCustomView);
        myView.invalidate();

    }

    public void getProps(View view){
        System.out.println("I CLICKED THE GET PROPS BUTTON");

        //final Dialog dialog= new Dialog(this);
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.properties_dialog_layout);
        if (CustomView.selectedShape != null) {
            EditText shapeNameText = (EditText) dialog.findViewById(R.id.shapeName);
            TextView imageNameText = (TextView) dialog.findViewById(R.id.imageName);
            EditText leftText = (EditText) dialog.findViewById(R.id.left);
            EditText rightText = (EditText) dialog.findViewById(R.id.right);
            EditText topText = (EditText) dialog.findViewById(R.id.top);
            EditText botText = (EditText) dialog.findViewById(R.id.bottom);

            shapeNameText.setText(CustomView.selectedShape.name);
            imageNameText.setText(CustomView.selectedShape.imageName);
            imageNameText.setKeyListener(null);
            leftText.setText(Float.toString(CustomView.selectedLeft));
            rightText.setText(Float.toString(CustomView.selectedRight));
            topText.setText(Float.toString(CustomView.selectedTop));
            botText.setText(Float.toString(CustomView.selectedBot));


        }

        dialog.show();

    }

    public void updateProps(View view){
        System.out.println("I CLICKED ON THE UPDATE PROPS BUTTON");

        if (CustomView.selectedShape != null) {
            EditText shapeNameText = (EditText) dialog.findViewById(R.id.shapeName);
            EditText leftText = (EditText) dialog.findViewById(R.id.left);
            EditText rightText = (EditText) dialog.findViewById(R.id.right);
            EditText topText = (EditText) dialog.findViewById(R.id.top);
            EditText botText = (EditText) dialog.findViewById(R.id.bottom);

            String newName = shapeNameText.getText().toString();
            float newLeft = Float.parseFloat(leftText.getText().toString());
            float newRight = Float.parseFloat(rightText.getText().toString());
            float newTop = Float.parseFloat(topText.getText().toString());
            float newBot = Float.parseFloat(botText.getText().toString());

            CustomView.selectedShape.setName(newName);
            //I THINK I ALSO HAVE TO UPDATE THE SHAPE IN THE PAGE
            CustomView.selectedShape.setCoordinates(newLeft,newTop,newRight,newBot);
            CustomView.selectedLeft = newLeft;
            CustomView.selectedRight = newRight;
            CustomView.selectedTop = newTop;
            CustomView.selectedBot = newBot;

//            CustomView myView = findViewById(R.id.myCustomView);
//            myView.invalidate();

        }

    }

    public void createScript(View view){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        //maybe change this to .setMessage
        alert.setTitle("Create Script");

        final EditText input = new EditText(this);
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
               CustomView.selectedShape.scripts.addScripts(input.getText().toString());
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        alert.show();

    public void saveGame(View view) {

    }

}
