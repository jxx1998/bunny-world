package edu.stanford.cs108.bunnyworld;

import android.util.Log;
import android.widget.Toast;

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
            (Arrays.asList("goto", "play", "hide", "show", "switch", "move", "bounce"));

    String scriptStr;
    List<Action> onClickClauses; //these Strings actually contain two words each, e.g. goto page 3
    List<Action> onEnterClauses;
    Map<String, List<Action>> onDropClauses;

    public Scripts() {
        scriptStr = "";
        onClickClauses = new ArrayList<Action>();
        onEnterClauses = new ArrayList<Action>();
        onDropClauses = new HashMap<String, List<Action>>();
    }

    public String getScripts() {
        return scriptStr;
    }

    private void throwToast(String msg) {
        Toast toast = Toast.makeText(getGlobalContext(), msg, Toast.LENGTH_SHORT);
        toast.show();
    }

    public void setScripts(String str) {
        scriptStr = str;
        StringTokenizer st = new StringTokenizer(scriptStr, "\n");
        boolean onClickSet = false;
        while (st.hasMoreTokens()) {
            String clause = st.nextToken();
            StringTokenizer stClause = new StringTokenizer(clause.substring(0, clause.length() - 1), " ");
            if (stClause.countTokens() < 2) {
                throwToast("Incomplete clause string!");
            }
            String trigger = stClause.nextToken();
            trigger += " " + stClause.nextToken();
            if (!triggerKeywords.contains(trigger)) {
                throwToast("Invalid clause trigger word detected!");
            }

            List<Action> actions = new ArrayList<Action>();
            if (trigger.equals("on click")) {
                if (!onClickSet) {
                    onClickClauses = actions;
                    onClickSet = true;
                } else {
                    continue;
                }
            } else if (trigger.equals("on enter")) {
                onEnterClauses = actions;
            } else if (trigger.equals("on drop")) {
                if (!stClause.hasMoreTokens()) {
                    throwToast("Incomplete on drop clause!");
                }
                String shapeName = stClause.nextToken();
                onDropClauses.put(shapeName, new ArrayList<Action>());
                actions = onDropClauses.get(shapeName);
            }

            while (stClause.hasMoreTokens()) {
                String keyword = stClause.nextToken();
                if (!actionKeywords.contains(keyword)) {
                    throwToast("Input includes invalid action primitive!");
                }
                if (!stClause.hasMoreTokens()) {
                    throwToast("Incomplete clause string detected!");
                }
                String name = stClause.nextToken();
                Action a = new Action(keyword, name);
                actions.add(a);
            }
        }

    }

    public void onEnter() {
        for (Action action: onEnterClauses) {
            action.execute();
        }
    }

    public void onClick() {
        for (Action action: onClickClauses) {
            action.execute();
        }
    }

    // Returns true if on-drop clause exists for shapeName
    public boolean onDrop(String shapeName) {
        if (onDropClauses.containsKey(shapeName)) {
            for (Action action: onDropClauses.get(shapeName)) {
                action.execute();
            }
            return true;
        } else {
            return false;
        }
    }

    // getters and setters


}