package edu.stanford.cs108.bunnyworld;

import android.content.Context;
import android.content.res.Resources;
import android.media.MediaPlayer;

import static android.app.Application.getProcessName;
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

    public void execute() {
        if (keyword == "goto") {
            int pageNum = -1;
            for (int i = 0; i < CustomView.gamePages.size(); i++) {
                if (CustomView.gamePages.get(i).name == this.name) {
                    pageNum = i;
                    break;
                }
            }
            if (pageNum < 0) {
                throw new RuntimeException("Invalid page name");
            }
            CustomView.currPagePos = pageNum;
            CustomView.currPage = CustomView.gamePages.get(pageNum);

           // CustomView myView = getView().findViewById(R.id.myCustomView);

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
