package edu.stanford.cs108.bunnyworld;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
    private Dialog dialog, pageDialog;
    static final float SQUARE_SIZE = 100.0f;
    static final float START_X = 200.0f;
    static final float START_Y = 200.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (EditorActivity.okToGo) {
            setContentView(R.layout.new_game_editor);
        }
    }

    @Override
    public void onBackPressed() {
        System.out.println("PRESSED THE BACK BUTTON ON EDITOR PAGE");
        EditorActivity.currGameName = "No Game Selected";
        super.onBackPressed();

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
        alert.setPositiveButton("Ok",null);
        alert.setNegativeButton("Cancel", null);
        final AlertDialog dialogNewPage = alert.create();

        dialogNewPage.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button button = ((AlertDialog) dialogNewPage).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        String inputStr = input.getText().toString(); //name of new page
                        boolean isError = checkPageError(inputStr); //returns true if another page in the game has the same name

                        if (isError){
                            Toast toast = Toast.makeText(getApplicationContext(),
                                    "ERROR: Another page already has that name!",
                                    Toast.LENGTH_LONG);
                            toast.show();
                            return;
                        }
                        Page newPage = new Page(inputStr);

                        EditorView.gamePages.add(newPage);
                        int currentPos = EditorView.gamePages.indexOf(newPage);
                        EditorView.currPagePos = currentPos;
                        EditorView.currPage = EditorView.gamePages.get(currentPos);
                        //So that latest added shape isnt added
                        EditorView.left = -10f;

                        EditorView myView = findViewById(R.id.myCustomView);
                        if (EditorView.selectedShape != null) {
                            EditorView.selectedShape.setHighlightColor(Color.TRANSPARENT);
                            EditorView.selectedShape = null;
                        }
                        myView.invalidate();

                        Game.set(EditorView.gamePages, EditorView.currPagePos);
                        Game.save(EditorView.currGameName);

                        dialogNewPage.dismiss();
                    }
                });
            }
        });

        dialogNewPage.show();

    }

    private boolean checkPageError(String pageName){
        ArrayList<Page> allPagesInGame = EditorView.gamePages;
        for (Page page : allPagesInGame){
            if (pageName.equals(page.name)){
                return true;
            }
        }
        return false;

    }

    public void getPageProps(View view){

        System.out.println("I CLICKED THE RENAME PAGE BUTTON");
        pageDialog = new Dialog(this);
        Window window = pageDialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        pageDialog.setTitle("Edit Page Properties");
        pageDialog.setContentView(R.layout.page_properites_dialog_layout);

        EditText pageNameText = (EditText) pageDialog.findViewById(R.id.pageName);
        RadioGroup group = (RadioGroup) pageDialog.findViewById(R.id.pageBackgroundGroup);

        pageNameText.setText(EditorView.currPage.name);

        String back = EditorView.currPage.getBackgroundImage();
        int currButton = 0;
        if (back .equals("background_blue")){
            currButton = 1;
        } else if (back .equals("background_metal")){
            currButton = 2;
        } else if (back .equals("background_wall")){
            currButton = 3;
        } else if (back .equals("background_yellow")) {
            currButton = 4;
        }



        for(int i = 0; i < group.getChildCount(); i++){
            if (i == currButton) {
                ((RadioButton) group.getChildAt(i)).setChecked(true);
            } else{
                ((RadioButton)group.getChildAt(i)).setChecked(false);
            }

        }

        pageDialog.show();


    }

    public void updatePageProps(View view){
        System.out.println("I CLICKED ON THE UPDATE PROPS BUTTON");

        EditText pageNameText = (EditText) pageDialog.findViewById(R.id.pageName);
        RadioGroup group = (RadioGroup) pageDialog.findViewById(R.id.pageBackgroundGroup);
        String backgroundStr = "None";

        int currentCheck = group.getCheckedRadioButtonId();
        switch(currentCheck){
            case R.id.noBack:
                break;
            case R.id.blueBack:
                backgroundStr = "background_blue";
                break;
            case R.id.metalBack:
                backgroundStr = "background_metal";
                break;
            case R.id.wallBack:
                backgroundStr = "background_wall";
                break;
            case R.id.yellowBack:
                backgroundStr = "background_yellow";
                break;
        }


        String newName = pageNameText.getText().toString();
        Page currPage = EditorView.currPage;


        currPage.setName(newName);
        currPage.setBackGroundImage(backgroundStr);

        EditorView myView = findViewById(R.id.myCustomView);
        myView.invalidate();

        Game.set(EditorView.gamePages, EditorView.currPagePos);
        Game.save(EditorView.currGameName);



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
                if (EditorView.selectedShape != null) {
                    EditorView.selectedShape.setHighlightColor(Color.TRANSPARENT);
                    EditorView.selectedShape = null;
                }
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
        float startleft= START_X - SQUARE_SIZE;
        float starttop = START_Y - SQUARE_SIZE;
        float startright = START_X + SQUARE_SIZE;
        float startbottom = START_Y + SQUARE_SIZE;

        Shape newShape;
        if (!EditorView.currDrawShapeName.equals("TextBox") && !EditorView.currDrawShapeName.equals("Button")) {
            newShape = new Shape("NewShape", new RectF(startleft, starttop, startright, startbottom));
            newShape.setImageName(EditorView.currDrawShapeName);
        } else if (EditorView.currDrawShapeName.equals("Button")) {
            newShape = new Shape("NewShape", new RectF(startleft, starttop, startright, startbottom));
            newShape.setText("", 50.0f);
            newShape.setImageName("Button");

        } else {
            newShape = new Shape("NewShape", new RectF(startleft, starttop, startright, startbottom));
            newShape.setText("This is my shapeText", 50.0f);
            newShape.setImageName("TextBox");
            // newShape.setText(shapeText);
        }
        //This is what I used for the click, create, and move feature
        //Shape newShape = new ShapeBuilder().name("AddedShape").coordinates(left,top,right,bottom).imageName(currDrawShapeName).buildShape();
        EditorView.currPage.addShape(newShape);
        EditorView.mostRecentAddedShape = newShape;

        Game.set(EditorView.gamePages, EditorView.currPagePos);
        Game.save(EditorView.currGameName);

        EditorView.isAShapeSelected = true;
        EditorView.selectedX = START_X;
        EditorView.selectedY = START_Y;


        // We save to database using set instead of customized functionality
        //Game.set(EditorView.gamePages, EditorView.currPagePos);
        //Game.save(EditorView.currGameName);

        EditorView myView = findViewById(R.id.myCustomView);
        myView.invalidate();

        // Game.printShapesPrivate();

    }

    public void getProps(View view){
        System.out.println("I CLICKED THE GET PROPS BUTTON");

        //final Dialog dialog= new Dialog(this);
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.properties_dialog_layout);

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
        Button defaultSize = (Button) dialog.findViewById(R.id.defaultSizeButton);

        SeekBar redView = (SeekBar) dialog.findViewById(R.id.redProgress);
        SeekBar greenView = (SeekBar) dialog.findViewById(R.id.greenProgress);
        SeekBar blueView = (SeekBar) dialog.findViewById(R.id.blueProgress);
        SeekBar scaleView = (SeekBar) dialog.findViewById(R.id.imageScaling);
        scaleView.setProgress(100);


        Switch isMoveable = (Switch) dialog.findViewById(R.id.moveable);
        Switch isVisible = (Switch) dialog.findViewById(R.id.visible);
        if (EditorView.selectedShape != null) {

            shapeNameText.setText(EditorView.selectedShape.name);
            imageNameText.setText(EditorView.selectedShape.imageName);
            String shapeText = EditorView.selectedShape.getImageName();
            //SHOULD THIS BUT .equals?
            if (!shapeText.equals("TextBox")) {
                if (shapeText.equals("Button")){
                    defaultSize.setClickable(false);
                }
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
                scaleView.setEnabled(false);
                defaultSize.setClickable(false);


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
        else{
            shapeNameText.setInputType(0);
            shapeNameText.setClickable(false);
            imageNameText.setInputType(0);
            shapeNameText.setClickable(false);
            leftText.setInputType(0);
            leftText.setClickable(false);
            rightText.setInputType(0);
            rightText.setClickable(false);
            botText.setInputType(0);
            botText.setClickable(false);
            topText.setInputType(0);
            topText.setClickable(false);
            textText.setInputType(0);
            textText.setClickable(false);
            sizeText.setInputType(0);
            sizeText.setClickable(false);


            isBold.setEnabled(false);
            isItalic.setEnabled(false);
            redView.setEnabled(false);
            blueView.setEnabled(false);
            greenView.setEnabled(false);
            for(int i = 0; i < group.getChildCount(); i++){
                ((RadioButton)group.getChildAt(i)).setEnabled(false);
            }

            isMoveable.setEnabled(false);
            isVisible.setEnabled(false);
            scaleView.setEnabled(false);


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
            String name = shapeNameText.getText().toString();


            // 0 = NewShape
            // 1 = Error: Another userSpecified shape name is already in use
            // 2 = In the clear
            int isShapeNameOk = checkShapeName(name);


            if (isShapeNameOk == 0){
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Warning: Unique shape name is not specified.",
                        Toast.LENGTH_SHORT);
                toast.show();
            } else if (isShapeNameOk == 1){
                Toast toast = Toast.makeText(getApplicationContext(),
                        "ERROR: Shape name is already in use!",
                        Toast.LENGTH_SHORT);
                toast.show();
                return;
            }
            //else: isShapeName is guaranteed to be 2, so we're good


            System.out.println("We've passed the toast code");




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
            SeekBar scaleView = (SeekBar) dialog.findViewById(R.id.imageScaling);

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

            int scaleProgress = scaleView.getProgress();
            if (scaleProgress != 100){
                float floatProgress = (float) scaleProgress;
                float newWidth = EditorView.selectedShape.getWidth() * (floatProgress/100);
                newRight = newLeft + newWidth;

                float newHeight = EditorView.selectedShape.getHeight() * (floatProgress/100);
                newBot = newTop + newHeight;
            }

            EditorView.selectedShape.setCoordinates(newLeft,newTop,newRight,newBot);
            //EditorView.selectedShape.setCenterCoordinates(EditorView.selectedShape.coordinates.centerX(), EditorView.selectedShape.coordinates.centerY(),newWidth,newHeight);
            if (textText.isClickable()) {
                EditorView.selectedShape.setText(text, textSize);
                EditorView.selectedShape.setBold(isBold.isChecked());
                EditorView.selectedShape.setItalic(isItalic.isChecked());
                EditorView.selectedShape.solidifyTextStyle();
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

    private int checkShapeName(String currentShapeName){
        //If "NewShape" is the shape name, which is default name for newly added shapes, return 0
        if (currentShapeName.equals("NewShape")){
            return 0;
        }
        //If other shape name is found in the shape names list, then another shape is already named that way; return 1
        else{
            for (Page currPage : EditorView.gamePages){
                for (Shape shape : currPage.shapes){
                    if (currentShapeName.equals(shape.name) && shape!= EditorView.selectedShape){
                        return 1;
                    }
                }
            }
            return 2;
        }

    }

    public void setDefaultSize(View view){
        //String currShapeName = EditorView.selectedShape.getImageName();
        //&& !currShapeName.equals("TextBox") && !currShapeName.equals("Button")

        if (EditorView.selectedShape != null ) {


            EditText leftText = (EditText) dialog.findViewById(R.id.left);
            EditText rightText = (EditText) dialog.findViewById(R.id.right);
            EditText topText = (EditText) dialog.findViewById(R.id.top);
            EditText botText = (EditText) dialog.findViewById(R.id.bottom);

            float defaultWidth = EditorView.selectedShape.getBitmapWidth();
            float defaultHeight = EditorView.selectedShape.getBitmapHeight();

            float left = Float.parseFloat(leftText.getText().toString());
            float right = Float.parseFloat(rightText.getText().toString());
            float top = Float.parseFloat(topText.getText().toString());
            float bot = Float.parseFloat(botText.getText().toString());

            float newRight = left + defaultWidth;
            float newBot = top + defaultHeight;

            rightText.setText(Float.toString(newRight));
            botText.setText(Float.toString(newBot));




            EditorView.selectedShape.setCoordinates(left,top,newRight,newBot);
            //EditorView.selectedShape.setCenterCoordinates(EditorView.selectedShape.coordinates.centerX(), EditorView.selectedShape.coordinates.centerY(),newWidth,newHeight);


            //This is so that the immediate drawing of the shape can be changed without a reference to the selected x and y point
            EditorView.changingDimensions = true;
            EditorView myView = findViewById(R.id.myCustomView);
            myView.invalidate();

//            left = Float.toString(EditorView.selectedShape.getLeft());
//            right = Float.toString(EditorView.selectedShape.getRight());
//            top = Float.toString(EditorView.selectedShape.getTop());
//            bot = Float.toString(EditorView.selectedShape.getBottom());
//
//            System.out.println("Selected Shape's attributes: (left, right, top, bot): (" + left + "," + right + "," + top + "," + bot + "," + ")");

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
        Game.deleteGame(EditorView.currGameName);
        Game.set(EditorView.gamePages, EditorView.currPagePos);
        Game.save(EditorView.currGameName);
    }

    private void throwToast(String message) {
        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG);
        toast.show();
    }

    public void onClickCut(MenuItem item) {
        if (EditorView.selectedShape != null) {
            EditorView.clipboard = EditorView.selectedShape;
            EditorView.currPage.removeShape(EditorView.selectedShape);
            EditorView.selectedShape = null;
            Game.set(EditorView.gamePages, EditorView.currPagePos);
            Game.save(EditorView.currGameName);
            EditorView.instance.invalidate();
        } else {
            throwToast("No shape is selected for clipboard!");
        }
    }

    public void onClickCopy(MenuItem item) {
        if (EditorView.selectedShape != null) {
            cloneClipboard(EditorView.selectedShape);
        } else {
            throwToast("No shape is selected for clipboard!");
        }
    }

    private void cloneClipboard(Shape shape) {
        EditorView.clipboard = new Shape(shape.name + "_new", new RectF(shape.coordinates));
        EditorView.clipboard.setImageName(new String(shape.getImageName()));
        EditorView.clipboard.setHighlightColor(shape.getHighlightColor());
        EditorView.clipboard.setHidden(shape.isHidden());
        EditorView.clipboard.setBold(shape.bold);
        EditorView.clipboard.setItalic(shape.italics);
        EditorView.clipboard.solidifyTextStyle();
        EditorView.clipboard.setColor(shape.getColor());
        EditorView.clipboard.red = shape.red;
        EditorView.clipboard.green = shape.green;
        EditorView.clipboard.blue = shape.blue;
        EditorView.clipboard.setMovable(shape.isMovable());
        EditorView.clipboard.scripts.setScripts(shape.scripts.scriptStr);
        EditorView.clipboard.setText(new String(shape.text), shape.getTextSize());
        EditorView.clipboard.setTypeface(new String(shape.typeface));
    }

    public void onClickPaste(MenuItem item) {
        if (EditorView.clipboard != null) {
            if (EditorView.selectedShape != null) {
                EditorView.selectedShape.setHighlightColor(Color.TRANSPARENT);
            }
            EditorView.selectedShape = EditorView.clipboard;
            EditorView.currPage.addShape(EditorView.clipboard);
            cloneClipboard(EditorView.clipboard);
            EditorView.instance.invalidate();
            Game.set(EditorView.gamePages, EditorView.currPagePos);
            Game.save(EditorView.currGameName);
        } else {
            throwToast("No shape is in the clipboard!");
        }
    }

    public void editScript(MenuItem item) {
        final EditText input = new EditText(this);
        input.setText(EditorView.selectedShape.scripts.getScripts());
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setView(input).setTitle("Edit Script").setPositiveButton("OK", null).setNegativeButton("Cancel", null);
        final AlertDialog alert = alertBuilder.create();

        alert.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button button = ((AlertDialog) alert).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        boolean success = EditorView.selectedShape.scripts.setScripts(input.getText().toString());

                        if (success) {
                            Game.set(EditorView.gamePages, EditorView.currPagePos);
                            Game.save(EditorView.currGameName);
                            alert.dismiss();
                        }
                    }
                });
            }
        });

        alert.show();

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
