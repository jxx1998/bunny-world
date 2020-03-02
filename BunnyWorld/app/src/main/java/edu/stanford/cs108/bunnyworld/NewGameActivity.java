package edu.stanford.cs108.bunnyworld;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class NewGameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_game_editor);

    }

    public void changePageExample(View view){
        System.out.println("I CLICKED THE CHANGE PAGE BUTTON");
        Page otherPage = new Page("changingPageExample");
        Shape shape1 = new ShapeBuilder().name("shape1").coordinates(80f,250f,400f,450f).imageName("fire").buildShape();
        Shape shape2 = new ShapeBuilder().name("shape2").coordinates(1000f,250f,1300f,350f).imageName("fire").buildShape();
        Shape shape3 = new ShapeBuilder().name("shape3").coordinates(500f,50f,1000f,550f).imageName("fire").buildShape();
        otherPage.addShape(shape1);
        otherPage.addShape(shape2);
        otherPage.addShape(shape3);

        CustomView.currPage = otherPage;
        CustomView.left = -10f;

        CustomView myView = findViewById(R.id.myCustomView);
        myView.invalidate();

    }



}
