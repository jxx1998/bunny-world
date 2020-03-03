package edu.stanford.cs108.bunnyworld;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class NewGameActivity extends AppCompatActivity {

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

        


        int numPages = CustomView.gamePages.size();
        int currentPagePosition = CustomView.currPagePos;

        System.out.println("num pages size" + numPages);
        System.out.println("current page position: " + currentPagePosition);

        if (currentPagePosition == numPages - 1){
            currentPagePosition = -1;
        }

        System.out.println("current page position after if statement that should cause the loop + 1: " + currentPagePosition);
        int toChangePagePos = currentPagePosition + 1;
        System.out.println("toChangePage position: " + toChangePagePos);

        Page toChangeToPage = CustomView.gamePages.get(toChangePagePos);
        CustomView.currPage = toChangeToPage;
        CustomView.currPagePos = toChangePagePos;
        CustomView.left = -10f;

        CustomView myView = findViewById(R.id.myCustomView);
        myView.invalidate();

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

}
