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
    ArrayList<Action> onClickClauses; //these Strings actually contain two words each, e.g. goto page 3
    ArrayList<Action> onEnterClauses;
    HashMap<String, ArrayList<Action>> onDropClauses;

    public Scripts() {
        scriptStr = "";
        onClickClauses = new ArrayList<Action>();
        onEnterClauses = new ArrayList<Action>();
        onDropClauses = new HashMap<String, ArrayList<Action>>();
    }

    public void setScripts(String str) {
        scriptStr = str;
        StringTokenizer st = new StringTokenizer(scriptStr, "\n");
        while (st.hasMoreTokens()) {
            String clause = st.nextToken();
            StringTokenizer stClause = new StringTokenizer(clause.substring(0, clause.length() - 1), " ");
            if (stClause.countTokens() < 2) {
                throw new RuntimeException("Invalid TextEdit clause string");
            }
            String trigger = st.nextToken();
            trigger += " " + st.nextToken();
            if (!triggerKeywords.contains(trigger)) {
                throw new RuntimeException("Invalid TextEdit clause string");
            }

            ArrayList<Action> actions = new ArrayList<Action>();
            if (trigger == "on click") {
                actions = onClickClauses; // Shouldn't this be onClickClauses = actions?
            } else if (trigger == "on enter") {
                actions = onEnterClauses;
            } else if (trigger == "on drop") {
                if (!stClause.hasMoreTokens()) {
                    throw new RuntimeException("Invalid on drop clause");
                }
                String shapeName = stClause.nextToken();
                onDropClauses.put(shapeName, new ArrayList<Action>());
                actions = onDropClauses.get(shapeName);
            }

            while (stClause.hasMoreTokens()) {
                String keyword = stClause.nextToken();
                if (!stClause.hasMoreTokens()) {
                    throw new RuntimeException("Invalid TextEdit clause string");
                }
                String name = stClause.nextToken();
                Action a = new Action(keyword, name);
                actions.add(a);
            }

        }

    }

    // getters and setters


}