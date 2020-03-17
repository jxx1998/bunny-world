package edu.stanford.cs108.bunnyworld;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.util.Log;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static edu.stanford.cs108.bunnyworld.BunnyWorldApplication.getGlobalContext;

public class Action implements Serializable {

    private static final long serialVersionUID = 8775500265792743431L;

    String keyword;
    String name;
    transient BitmapDrawable imageDrawable;

    public Action(String str1, String str2) {
        keyword = str1;
        name = str2;
    }

    /**
     * Plays a sound with filename stored in this.name
     * If file not found, this method does nothing
     */
    private void playSound(boolean ambient) {
        Context context = getGlobalContext();
        Resources resources = context.getResources();
        final int resourceId = resources.getIdentifier(name, "raw", context.getPackageName());

        // play once
        if (!ambient) {
            final MediaPlayer mp = MediaPlayer.create(context, resourceId);
            if (mp == null) { return; }
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                public void onCompletion(MediaPlayer mp) {
                    mp.release();
                }
            });
            mp.start();
            return;
        }

        // ambient sound
        if (MainActivity.ambientSound != null) {
            MainActivity.ambientSound.stop();
        }
        MainActivity.ambientSound = MediaPlayer.create(context, resourceId);
        if (MainActivity.ambientSound == null) { return; }
        MainActivity.ambientSound.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                MainActivity.ambientSound.release();
            }
        });
        MainActivity.ambientSound.setLooping(ambient);
        MainActivity.ambientSound.start();
    }

    private void throwToast(String msg) {
        Toast toast = Toast.makeText(getGlobalContext(), msg, Toast.LENGTH_SHORT);
        toast.show();
    }


    public void execute() {
        if (keyword.equals("goto")) {
            Page page = null;
            for (Page candidatePage: Game.getPages()) {
                if (candidatePage.name.equals(name)) {
                    page = candidatePage;
                    break;
                }
            }
            if (page == null) {
                throwToast("Invalid page name in scripts!");
            } else {
                GameView.changePage(page);
            }
        } else if (keyword.equals("play")) {
            playSound(false);
        } else if (keyword.equals("ambient")) {
            playSound(true);
        } else if (keyword.equals("hide")) {
            Shape shape = Game.getShape(name);
            if (shape != null) {
                shape.setHidden(true);
            }
            for (Shape inventory_shape: GameView.inventory) {
                if (inventory_shape.name.equals(name)) {
                    inventory_shape.setHidden(true);
                }
            }
        } else if (keyword.equals("show")) {
            Shape shape = Game.getShape(name);
            if (shape != null) {
                shape.setHidden(false);
            }
            for (Shape inventory_shape: GameView.inventory) {
                if (inventory_shape.name.equals(name)) {
                    inventory_shape.setHidden(false);
                }
            }
        } else if (keyword.equals("switch")) {
            Shape shape = GameView.shapeSelected;
            if (!EditorView.shapeNames.contains(name)) {
                throwToast("Invalid shape name in scripts!");
            }
            shape.setImageName(name);
        } else if (keyword.equals("move")) {
            // final float offset = 0.5f;
            char dir = name.charAt(0);
            float dis = Float.parseFloat(name.substring(1));
            Shape shape = GameView.shapeSelected;
            // float d = 0.0;
            if (dir == 'r') {
                shape.offSetCoordinates(dis, 0f);
            } else if (dir == 'l') {
                shape.offSetCoordinates(-dis, 0f);
            } else if (dir == 'u') {
                shape.offSetCoordinates(0f, -dis);
            } else if (dir == 'd') {
                shape.offSetCoordinates(0f, dis);
            }
        } else if (keyword.equals("randomMove")) {
            float dis = Float.parseFloat(name);
            Shape shape = GameView.shapeSelected;
            Random random = new Random();
            int randomNum = random.nextInt((4 - 1) + 1) + 1;
            if (randomNum == 1) {
                shape.offSetCoordinates(dis, 0f);
            } else if (randomNum == 2) {
                shape.offSetCoordinates(-dis, 0f);
            } else if (randomNum == 3) {
                shape.offSetCoordinates(0f, -dis);
            } else if (randomNum == 4) {
                shape.offSetCoordinates(0f, dis);
            }
        } else if (keyword.equals("text")) {
            Shape shape = GameView.shapeSelected;
            String newText = name;
            shape.setText(newText, 50.0f);
        } else if (keyword.equals("randomChar")) {
            Shape shape = GameView.shapeSelected;
            float textSize = Float.parseFloat(name);
            Random random = new Random();
            char randomChar = (char) (random.nextInt(26) + 'a');
            String str = "";
            str += randomChar;
            shape.setText(str, textSize);
        } else if (keyword.equals("randomElement")) {
            Shape shape = GameView.shapeSelected;
            float textSize = Float.parseFloat(name);
            Random random = new Random();
            int randomNum = random.nextInt((4 - 1) + 1) + 1;
            if (randomNum == 1) {
                shape.setText("water", textSize);
            } else if (randomNum == 2) {
                shape.setText("fire", textSize);
            } else if (randomNum == 3) {
                shape.setText("earth", textSize);
            } else if (randomNum == 4) {
                shape.setText("air", textSize);
            }
        } else if (keyword.equals("randomCreatorName")) {
            Shape shape = GameView.shapeSelected;
            float textSize = Float.parseFloat(name);
            Random random = new Random();
            int randomNum = random.nextInt((5 - 1) + 1) + 1;
            if (randomNum == 1) {
                shape.setText("Harry", textSize);
            } else if (randomNum == 2) {
                shape.setText("Constantine", textSize);
            } else if (randomNum == 3) {
                shape.setText("Blake", textSize);
            } else if (randomNum == 4) {
                shape.setText("Kevin", textSize);
            } else if (randomNum == 5) {
                shape.setText("Katherine", textSize);
            }
        }
    }
}
