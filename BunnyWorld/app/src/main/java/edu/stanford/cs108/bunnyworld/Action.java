package edu.stanford.cs108.bunnyworld;

import android.content.Context;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.widget.Toast;

import static edu.stanford.cs108.bunnyworld.BunnyWorldApplication.getGlobalContext;

public class Action {
    String keyword;
    String name;

    public Action(String str1, String str2) {
        keyword = str1;
        name = str2;
    }

    /**
     * Plays a sound with filename stored in this.name
     * If file not found, this method does nothing
     */
    private void playSound() {
        Context context = getGlobalContext();
        Resources resources = context.getResources();
        final int resourceId = resources.getIdentifier(name, "raw", context.getPackageName());
        final MediaPlayer mp = MediaPlayer.create(context, resourceId);
        if (mp == null) { return; }
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }
        });
        mp.start();
    }

    private void throwToast(String msg) {
        Toast toast = Toast.makeText(getGlobalContext(), msg, Toast.LENGTH_SHORT);
        toast.show();
    }

    public void execute() {
        if (keyword == "goto") {
            Page page = null;
            for (Page candidatePage: Game.getPages()) {
                if (candidatePage.name == name) {
                    page = candidatePage;
                    break;
                }
            }
            if (page == null) {
                throwToast("Invalid page name in scripts!");
            } else {
                GameView.changePage(page);
            }
        } else if (keyword == "play") {
            playSound();
        } else if (keyword == "hide") {
            Shape shape = Game.getShape(name);
            if (shape != null) {
                shape.setHidden(true);
            }
        } else if (keyword == "show") {
            Shape shape = Game.getShape(name);
            if (shape != null) {
                shape.setHidden(false);
            }
        }
    }
}
