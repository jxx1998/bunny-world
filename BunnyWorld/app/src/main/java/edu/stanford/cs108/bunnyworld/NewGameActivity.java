package edu.stanford.cs108.bunnyworld;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

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

    public void changePage(View view){
        System.out.println("I CLICKED THE CHANGE PAGE BUTTON");
//        Page otherPage = new Page("changingPageExample");
//        Shape shape1 = new ShapeBuilder().name("shape1").coordinates(80f,250f,400f,450f).imageName("fire").buildShape();
//        Shape shape2 = new ShapeBuilder().name("shape2").coordinates(1000f,250f,1300f,350f).imageName("fire").buildShape();
//        Shape shape3 = new ShapeBuilder().name("shape3").coordinates(500f,50f,1000f,550f).imageName("fire").buildShape();
//        otherPage.addShape(shape1);
//        otherPage.addShape(shape2);
//        otherPage.addShape(shape3);

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
