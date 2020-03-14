package edu.stanford.cs108.bunnyworld;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

/*
   We have to update the Game singleton in every method within this class.
   List of methods in sync with singleton: deletePage, renamePage
 */

public class NewGameActivity extends AppCompatActivity {

    private String selection, shapeSelection;
    private int selectionID, shapeSelectionID;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (EditorActivity.okToGo) {
            setContentView(R.layout.new_game_editor);
        }
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    public void changeDrawShape(View view){
        System.out.println("I CLICKED THE CHANGE SHAPE BUTTON");

        final ArrayList<String> shapes = EditorView.shapeNames;
        int numPages = shapes.size();
        final String[] arrayShapeNames = new String[numPages];

        for (int i = 0; i < numPages; i++){
            arrayShapeNames[i] = shapes.get(i);
        }


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick The Shape");

        int currPos = EditorView.currShapePos;



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
                EditorView.currShapePos = shapeSelectionID;
                EditorView.currDrawShapeName = shapes.get(shapeSelectionID);
                //So that latest added shape isnt added
                EditorView.left = -10f;
                EditorView myView = findViewById(R.id.myCustomView);
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

                EditorView.gamePages.add(newPage);
                int currentPos = EditorView.gamePages.indexOf(newPage);
                EditorView.currPagePos = currentPos;
                EditorView.currPage = EditorView.gamePages.get(currentPos);
                //So that latest added shape isnt added
                EditorView.left = -10f;

                EditorView myView = findViewById(R.id.myCustomView);
                myView.invalidate();

                Game.set(EditorView.gamePages, EditorView.currPagePos);
                Game.save(EditorView.currGameName);

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
                Page currPage = EditorView.currPage;

//                String currPageName = currPage.name;
//                Game.renamePage(currPageName, inputStr);


                currPage.setName(inputStr);

                EditorView myView = findViewById(R.id.myCustomView);
                myView.invalidate();

                Game.set(EditorView.gamePages, EditorView.currPagePos);
                Game.save(EditorView.currGameName);
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

        final ArrayList<Page> pages = EditorView.gamePages;

        int numPages = pages.size();
        final String[] arrayNames = new String[numPages];
        final ArrayList<String> pageNames = new ArrayList<String>();
        for (Page page : pages){
            pageNames.add(page.name);
        }
        System.out.println("Number of pages: "+numPages);

        for (int i = 0; i < numPages; i++){
            arrayNames[i] = pageNames.get(i);
        }


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick The Page");

        //to begin with, the selected page will be the current page we're on
        final int currPos = EditorView.currPagePos;
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
                EditorView.currPagePos = selectionID;
                EditorView.currPage = pages.get(selectionID);
                //So that latest added shape isnt added
                EditorView.left = -10f;
                EditorView myView = findViewById(R.id.myCustomView);
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

        int currentPagePosition = EditorView.currPagePos;

        System.out.println("current page position: " + currentPagePosition);

        int numPages = EditorView.gamePages.size();
        if (numPages == 1){
            Toast toast = Toast.makeText(getApplicationContext(),
                    "There is only 1 Page Left",
                    Toast.LENGTH_LONG);
            toast.show();
        } else{


            //Game.removePage(EditorView.gamePages.get(currentPagePosition));
            EditorView.gamePages.remove(currentPagePosition);
            int numPagesNew = EditorView.gamePages.size();
            if (currentPagePosition == numPagesNew){
                currentPagePosition = currentPagePosition -1;
            }

            Game.set(EditorView.gamePages, EditorView.currPagePos);
            Game.save(EditorView.currGameName);


            Page toChangeToPage = EditorView.gamePages.get(currentPagePosition);
            EditorView.currPage = toChangeToPage;
            EditorView.currPagePos = currentPagePosition;
            EditorView.left = -10f;

            EditorView myView = findViewById(R.id.myCustomView);
            myView.invalidate();
        }

    }

    public void addShape(View view){
        System.out.println("I CLICKED THE ADD SHAPE BUTTON");
        EditorView.createNewShape = true;


        // We save to database using set instead of customized functionality
        //Game.set(EditorView.gamePages, EditorView.currPagePos);
        //Game.save(EditorView.currGameName);

        EditorView myView = findViewById(R.id.myCustomView);
        myView.invalidate();

        Game.printShapesPrivate();

    }

    public void getProps(View view){
        System.out.println("I CLICKED THE GET PROPS BUTTON");

        //final Dialog dialog= new Dialog(this);
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.properties_dialog_layout);
        if (EditorView.selectedShape != null) {
            EditText shapeNameText = (EditText) dialog.findViewById(R.id.shapeName);
            TextView imageNameText = (TextView) dialog.findViewById(R.id.imageName);
            EditText leftText = (EditText) dialog.findViewById(R.id.left);
            EditText rightText = (EditText) dialog.findViewById(R.id.right);
            EditText topText = (EditText) dialog.findViewById(R.id.top);
            EditText botText = (EditText) dialog.findViewById(R.id.bottom);
            EditText textText = (EditText) dialog.findViewById(R.id.text);
            EditText sizeText = (EditText) dialog.findViewById(R.id.textSize);
            Switch isBold = (Switch) dialog.findViewById(R.id.bold);
            Switch isItalic = (Switch) dialog.findViewById(R.id.italics);
            RadioGroup group = (RadioGroup) dialog.findViewById(R.id.font_group);

            SeekBar redView = (SeekBar) dialog.findViewById(R.id.redProgress);
            SeekBar greenView = (SeekBar) dialog.findViewById(R.id.greenProgress);
            SeekBar blueView = (SeekBar) dialog.findViewById(R.id.blueProgress);


            Switch isMoveable = (Switch) dialog.findViewById(R.id.moveable);
            Switch isVisible = (Switch) dialog.findViewById(R.id.visible);

            shapeNameText.setText(EditorView.selectedShape.name);
            imageNameText.setText(EditorView.selectedShape.imageName);
            String shapeText = EditorView.selectedShape.getText();
            if (shapeText == null){
                textText.setText("No Text");
                textText.setInputType(0);
                textText.setClickable(false);
                sizeText.setText(Float.toString(EditorView.selectedShape.getTextSize()));
                sizeText.setInputType(0);
                isBold.setClickable(false);
                isItalic.setClickable(false);
                redView.setEnabled(false);
                blueView.setEnabled(false);
                greenView.setEnabled(false);
                for(int i = 0; i < group.getChildCount(); i++){
                    ((RadioButton)group.getChildAt(i)).setEnabled(false);
                }
            } else {
                textText.setText(EditorView.selectedShape.getText());
                sizeText.setText(Float.toString(EditorView.selectedShape.getTextSize()));
                int red = EditorView.selectedShape.red;
                int green = EditorView.selectedShape.green;
                int blue = EditorView.selectedShape.blue;

                redView.setProgress(red);
                blueView.setProgress(blue);
                greenView.setProgress(green);


                String fontType = EditorView.selectedShape.getTypeface();
                System.out.println(fontType);
                int currButton = 0;
                if (fontType.equals("MONOSPACE")){
                    currButton = 1;
                } else if (fontType.equals("SANS_SERIF")){
                    currButton = 2;
                } else if (fontType.equals("SERIF")){
                    currButton = 3;
                }



                for(int i = 0; i < group.getChildCount(); i++){
                    if (i == currButton) {
                        ((RadioButton) group.getChildAt(i)).setChecked(true);
                    } else{
                        ((RadioButton)group.getChildAt(i)).setChecked(false);
                    }

                }

                isBold.setChecked(EditorView.selectedShape.bold);
                isItalic.setChecked((EditorView.selectedShape.italics));


            }

            imageNameText.setKeyListener(null);
            leftText.setText(Float.toString(EditorView.selectedShape.getLeft()));
            rightText.setText(Float.toString(EditorView.selectedShape.getRight()));
            topText.setText(Float.toString(EditorView.selectedShape.getTop()));
            botText.setText(Float.toString(EditorView.selectedShape.getBottom()));


            isMoveable.setChecked(EditorView.selectedShape.isMovable());
            isVisible.setChecked(!EditorView.selectedShape.isHidden());

        }

        dialog.show();

    }

    public void updateProps(View view){
        System.out.println("I CLICKED ON THE UPDATE PROPS BUTTON");

        if (EditorView.selectedShape != null) {

            String left = Float.toString(EditorView.selectedShape.getLeft());
            String right = Float.toString(EditorView.selectedShape.getRight());
            String top = Float.toString(EditorView.selectedShape.getTop());
            String bot = Float.toString(EditorView.selectedShape.getBottom());

            System.out.println("Selected Shape's attributes: (left, right, top, bot): (" + left + "," + right + "," + top + "," + bot + "," + ")");

            EditText shapeNameText = (EditText) dialog.findViewById(R.id.shapeName);
            EditText leftText = (EditText) dialog.findViewById(R.id.left);
            EditText rightText = (EditText) dialog.findViewById(R.id.right);
            EditText topText = (EditText) dialog.findViewById(R.id.top);
            EditText botText = (EditText) dialog.findViewById(R.id.bottom);
            EditText textText = (EditText) dialog.findViewById(R.id.text);
            EditText sizeText = (EditText) dialog.findViewById(R.id.textSize);
            Switch isMoveable = (Switch) dialog.findViewById(R.id.moveable);
            Switch isVisible = (Switch) dialog.findViewById(R.id.visible);
            RadioGroup group = (RadioGroup) dialog.findViewById(R.id.font_group);
            Switch isBold = (Switch) dialog.findViewById(R.id.bold);
            Switch isItalic = (Switch) dialog.findViewById(R.id.italics);

            String fontStr = "DEFAULT";

            int currentCheck = group.getCheckedRadioButtonId();
            switch(currentCheck){
                case R.id.defaultFont:
                    break;
                case R.id.monospace:
                    fontStr = "MONOSPACE";
                    break;
                case R.id.sansSerif:
                    fontStr = "SANS_SERIF";
                    break;
                case R.id.serif:
                    fontStr = "SERIF";
                    break;
            }
            SeekBar redView = (SeekBar) dialog.findViewById(R.id.redProgress);
            SeekBar greenView = (SeekBar) dialog.findViewById(R.id.greenProgress);
            SeekBar blueView = (SeekBar) dialog.findViewById(R.id.blueProgress);

            int red = redView.getProgress();
            int green = greenView.getProgress();
            int blue = blueView.getProgress();

            int newTextColor = Color.rgb(red,green,blue);

            String newName = shapeNameText.getText().toString();
            float newLeft = Float.parseFloat(leftText.getText().toString());
            float newRight = Float.parseFloat(rightText.getText().toString());
            float newTop = Float.parseFloat(topText.getText().toString());
            float newBot = Float.parseFloat(botText.getText().toString());
            String text = textText.getText().toString();
            float textSize = Float.parseFloat(sizeText.getText().toString());
            boolean moveable = isMoveable.isChecked();
            boolean visible = isVisible.isChecked();
            boolean hidden = !visible;



            EditorView.selectedShape.setName(newName);
            EditorView.selectedShape.setCoordinates(newLeft,newTop,newRight,newBot);
            //EditorView.selectedShape.setCenterCoordinates(EditorView.selectedShape.coordinates.centerX(), EditorView.selectedShape.coordinates.centerY(),newWidth,newHeight);
            if (textText.isClickable()) {
                EditorView.selectedShape.setText(text, textSize);
                EditorView.selectedShape.setBold(isBold.isChecked());
                EditorView.selectedShape.setItalic(isItalic.isChecked());
                EditorView.selectedShape.setColor(newTextColor);
                EditorView.selectedShape.red = red;
                EditorView.selectedShape.green = green;
                EditorView.selectedShape.blue = blue;

                EditorView.selectedShape.setTypeface(fontStr);

            }
            EditorView.selectedShape.setMovable(moveable);
            EditorView.selectedShape.setHidden(hidden);









            //This is so that the immediate drawing of the shape can be changed without a reference to the selected x and y point
            EditorView.changingDimensions = true;
            EditorView myView = findViewById(R.id.myCustomView);
            myView.invalidate();

            left = Float.toString(EditorView.selectedShape.getLeft());
            right = Float.toString(EditorView.selectedShape.getRight());
            top = Float.toString(EditorView.selectedShape.getTop());
            bot = Float.toString(EditorView.selectedShape.getBottom());

            System.out.println("Selected Shape's attributes: (left, right, top, bot): (" + left + "," + right + "," + top + "," + bot + "," + ")");

            Game.set(EditorView.gamePages, EditorView.currPagePos);
            Game.save(EditorView.currGameName);



        }

    }

    public void deleteShape(View view){
        System.out.println("I CLICKED THE DELETE SHAPE BUTTON");

        int currentPagePosition = EditorView.currPagePos;
        boolean isThereASelectedShape = EditorView.isAShapeSelected;

        System.out.println("current page position: " + currentPagePosition);
        System.out.println("shape selected :" + isThereASelectedShape);

        int numPages = EditorView.gamePages.size();
        if (!isThereASelectedShape){
            Toast toast = Toast.makeText(getApplicationContext(),
                    "No Shape is Selected",
                    Toast.LENGTH_SHORT);
            toast.show();
        } else{

            Shape selectedShape = EditorView.selectedShape;
            Page currPage = EditorView.currPage;
            currPage.removeShape(selectedShape);

            Game.set(EditorView.gamePages, EditorView.currPagePos);
            Game.save(EditorView.currGameName);

            EditorView myView = findViewById(R.id.myCustomView);
            myView.invalidate();



        }

    }

    public void saveGame(View view){
        Game.set(EditorView.gamePages, EditorView.currPagePos);
        Game.save(EditorView.currGameName);
    }

    public void editScript(MenuItem item) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        //maybe change this to .setMessage
        alert.setTitle("Edit Script");

        final EditText input = new EditText(this);
        input.setText(EditorView.selectedShape.scripts.getScripts());
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                EditorView.selectedShape.scripts.setScripts(input.getText().toString());
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        alert.show();

        // Save game without using customized functions
        Game.set(EditorView.gamePages, EditorView.currPagePos);
        Game.save(EditorView.currGameName);
    }


    public void undo(View view){
        System.out.println("We've clicked the undo button");

        Game.loadPrevious(EditorView.currGameName);
        EditorView.gamePages =(ArrayList<Page>) Game.getPages();
        EditorView.currPagePos = Game.getCurrPagePos();
        EditorView.currPage = EditorView.gamePages.get(EditorView.currPagePos);

        EditorView myView = findViewById(R.id.myCustomView);
        myView.invalidate();

    }

}
