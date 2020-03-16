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

    private static final long serialVersionUID = -2856451602345386105L;

    private static final Set<String> soundFiles = new HashSet<String>
            (Arrays.asList("carrotcarrotcarrot", "evillaugh", "fire", "hooray", "munch", "munching", "rain", "woof"));
    private final Set<String> triggerKeywords = new HashSet<String>
            (Arrays.asList("on click", "on enter", "on drop"));
    private final Set<String> actionKeywords = new HashSet<String>
            (Arrays.asList("goto", "play", "hide", "show", "switch", "move", "bounce", "ambient"));
    private final Set<String> conditionalActionKeywords = new HashSet<String>
            (Arrays.asList("inventory"));

    String scriptStr;
    List<Action> onClickClauses; //these Strings actually contain two words each, e.g. goto page3
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

    public boolean setScripts(String str) {
        scriptStr = str;
        StringTokenizer st = new StringTokenizer(scriptStr, "\n");
        boolean onClickSet = false;
        while (st.hasMoreTokens()) {
            String clause = st.nextToken();
            StringTokenizer stClause = new StringTokenizer(clause.substring(0, clause.length() - 1), " ");
            if (stClause.countTokens() < 2) {
                throwToast("Incomplete clause string!");
                continue;
            }
            String trigger;
            if (!stClause.hasMoreElements()) {
                throwToast("Incomplete trigger!");
                continue;
            } else {
                trigger = stClause.nextToken();
            }
            if (!stClause.hasMoreElements()) {
                throwToast("Incomplete trigger!");
                continue;
            } else {
                trigger += " " + stClause.nextToken();
            }
            if (!triggerKeywords.contains(trigger)) {
                throwToast("Invalid clause trigger word detected!");
                continue;
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
                    continue;
                }
                String shapeName = stClause.nextToken();
                checkShapeExists(shapeName);
                onDropClauses.put(shapeName, new ArrayList<Action>());
                actions = onDropClauses.get(shapeName);
            }

            //ADD CONDITIONALS
//            if (stClause.hasMoreTokens() && stClause.nextToken().equals("if")) {
//                String keyword = stClause.nextToken();
//                if (!conditionalActionKeywords.contains(keyword)) {
//                    throwToast("Input includes invalid conditional action primitive!");
//                }
//                if (!stClause.hasMoreTokens()) {
//                    throwToast("Incomplete conditional clause string detected!");
//                }
//                String name = stClause.nextToken();
//
//                //If inventory contains object with name "name", then...
//
//                //TO-DO: How to check conditionals
//
//
//            }
            while (stClause.hasMoreTokens()) {
                String keyword = stClause.nextToken();
                if (!actionKeywords.contains(keyword)) {
                    throwToast("Input includes invalid action primitive!");
                    break;
                }
                if (!stClause.hasMoreTokens()) {
                    throwToast("Incomplete clause string detected!");
                    break;
                }
                String name = stClause.nextToken();
                if (!checkValidity(keyword, name)) {
                    return false;
                }
                Action a = new Action(keyword, name);
                actions.add(a);
            }
        }
        return true;
    }

    private boolean checkValidity(String keyword, String name) {
        if (keyword.equals("goto")) {
            boolean pageExists = false;
            for (Page individualPage: EditorView.gamePages) {
                if (individualPage.name.equals(name)) {
                    pageExists = true;
                }
            }
            if (!pageExists) {
                throwToast("WARNING: Script contains page that doesn't exist yet!");
            }
        } else if (keyword.equals("play")) {
            if (!soundFiles.contains(name)) {
                throwToast("ERROR: Script contains nonexistent sound file!");
                return false;
            }
        } else if (keyword.equals("hide") || keyword.equals("show")) {
            checkShapeExists(name);
        }
        return true;
    }

    private boolean checkShapeExists(String name) {
        for (Page page : EditorView.gamePages) {
            for (Shape shape : page.shapes) {
                if (name.equals(shape.name)) {
                    return true;
                }
            }
        }
        throwToast("WARNING: Script contains shape that doesn't exist yet!");
        return false;
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