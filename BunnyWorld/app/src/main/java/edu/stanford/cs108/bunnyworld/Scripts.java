package edu.stanford.cs108.bunnyworld;

import android.content.Context;
import android.content.res.Resources;
import android.media.MediaPlayer;

import java.io.Serializable;
import java.util.*;

import static edu.stanford.cs108.bunnyworld.BunnyWorldApplication.getGlobalContext;

/**
 * Scripts class
 * Each shape contains a Scripts object
 * Scripts contains a list of Clause objects (either on click/enter/drop)
 * Each Clause object contains a list of actions performed when triggered
 */
public class Scripts implements Serializable {

    private final Set<String> triggerKeywords = new HashSet<String>
            (Arrays.asList("on click", "on enter", "on drop"));
    private final Set<String> actionKeywords = new HashSet<String>
            (Arrays.asList("goto", "play", "hide", "show"));

    String scriptStr;
    List<String> onClickClauses;
    List<String> onEnterClauses;
    List<String> onDropClauses;

    public Scripts() {
        scriptStr = "";
        onClickClauses = new ArrayList<String>();
        onEnterClauses = new ArrayList<String>();
        onDropClauses = new ArrayList<String>();
    }

    public void addScripts(String str) { scriptStr = str; }





    // getters and setters


}