package edu.stanford.cs108.bunnyworld;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.io.ObjectInputStream;
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
    private static final Set<String> triggerKeywords = new HashSet<String>
            (Arrays.asList("on click", "on enter", "on drop"));
    private static final Set<String> actionKeywords = new HashSet<String>
            (Arrays.asList("goto", "play", "hide", "show", "switch", "move", "randomMove", "ambient", "text", "randomChar", "randomElement", "randomCreatorName", "mobilize", "immobilize"));
//    private static final Set<String> conditionalActionKeywords = new HashSet<String>
//            (Arrays.asList("inventory"));

    String scriptStr;
    List<Action> onClickClauses; //these Strings actually contain two words each, e.g. goto page3
    List<Action> onEnterClauses;
    Map<String, List<Action>> onDropClauses;

    List<String> onClickConditionals;
    List<String> onEnterConditionals;
    Map<String, List<String>> onDropConditionals;


    public Scripts() {
        scriptStr = "";
        onClickClauses = new ArrayList<Action>();
        onEnterClauses = new ArrayList<Action>();
        onDropClauses = new HashMap<String, List<Action>>();
        onClickConditionals = new ArrayList<String>();
        onEnterConditionals = new ArrayList<String>();
        onDropConditionals = new HashMap<String, List<String>>();
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
                return false;
            }
            String trigger;
            if (!stClause.hasMoreElements()) {
                throwToast("Incomplete trigger!");
                return false;
            } else {
                trigger = stClause.nextToken();
            }
            if (!stClause.hasMoreElements()) {
                throwToast("Incomplete trigger!");
                return false;
            } else {
                trigger += " " + stClause.nextToken();
            }
            if (!triggerKeywords.contains(trigger)) {
                throwToast("Invalid clause trigger word detected!");
                return false;
            }
            String currOnDropShapeName = "";
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
                    return false;
                }
                currOnDropShapeName = stClause.nextToken();
                checkShapeExists(currOnDropShapeName);
                onDropClauses.put(currOnDropShapeName, new ArrayList<Action>());
                actions = onDropClauses.get(currOnDropShapeName);
            }
            if (!stClause.hasMoreTokens()) {
                throwToast("Incomplete if statement!");
                break;
            } else {
                String ifString = stClause.nextToken();
                if (!ifString.equals("if")) {
                    String keyword = "";
                    boolean firstIt = true;
                    while (stClause.hasMoreTokens()) {
                        if (firstIt) {
                            keyword = ifString;
                            firstIt = false;
                        } else {
                            keyword = stClause.nextToken();
                        }
                        if (!actionKeywords.contains(keyword)) {
                            throwToast("Input includes invalid action primitive!");
                            return false;
                        }
                        if (!stClause.hasMoreTokens()) {
                            throwToast("Incomplete clause string detected!");
                            return false;
                        }
                        String name = stClause.nextToken();
                        if (!checkValidity(keyword, name)) {
                            return false;
                        }
                        Action a = new Action(keyword, name);
                        actions.add(a);
                    }
                } else {
                    if (!stClause.hasMoreTokens()) {
                        throwToast("Fails to specify conditionals, if any, after if statement");
                        break;
                    } else {
                        String firstWord = stClause.nextToken();
                        if (firstWord.equals("end")) {
                            //for testing
//                            throwToast("on click cons: " + onClickConditionals.toString());
                            //end testing
                        } else {
                            if (trigger.equals("on click")) {
                                onClickConditionals.add(firstWord);
                            } else if (trigger.equals("on enter")) {
                                onEnterConditionals.add(firstWord);
                            } else if (trigger.equals("on drop")) {
                                onDropConditionals.put(currOnDropShapeName, new ArrayList<String>());
                                onDropConditionals.get(currOnDropShapeName).add(firstWord);
                            }
                            if (stClause.hasMoreTokens()) {
                                String secondWord = stClause.nextToken();
                                String currWord = secondWord;
                                while (stClause.hasMoreTokens() && !currWord.equals("end")) {
                                    if (trigger.equals("on click")) {
                                        onClickConditionals.add(currWord);
                                    } else if (trigger.equals("on enter")) {
                                        onEnterConditionals.add(currWord);
                                    } else if (trigger.equals("on drop")) {
                                        onDropConditionals.get(currOnDropShapeName).add(currWord);
                                    }
                                    String nextWord = stClause.nextToken();
                                    currWord = nextWord;
                                }
                            }
                        }
                        while (stClause.hasMoreTokens()) {
                            String keyword = stClause.nextToken();
                            if (!actionKeywords.contains(keyword)) {
                                throwToast("Input includes invalid action primitive!");
                                return false;
                            }
                            if (!stClause.hasMoreTokens()) {
                                throwToast("Incomplete clause string detected!");
                                return false;
                            }
                            String name = stClause.nextToken();
                            if (!checkValidity(keyword, name)) {
                                return false;
                            }
                            Action a = new Action(keyword, name);
                            actions.add(a);
                        }
                    }
                }

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

    private boolean inventoryContains(String conditionalName) {
        for (Shape shape: GameView.inventory) {
            if (shape.getName().equals(conditionalName)) {
                return true;
            }
        }
        return false;
    }

    public void onEnter() {
        boolean execute = true;
        for (String shapeName: onEnterConditionals) {
            if (!inventoryContains(shapeName)) {
                execute = false;
            }
        }
        if (execute) {
            for (Action action : onEnterClauses) {
                action.execute();
            }
        }
    }

    public void onClick() {
        boolean execute = true;
        for (String shapeName: onClickConditionals) {
            if (!inventoryContains(shapeName)) {
                execute = false;
            }
        }
        if (execute) {
            for (Action action: onClickClauses) {
                action.execute();
            }
        }
    }

    // Returns true if on-drop clause exists for shapeName
    public boolean onDrop(String shapeName) {
        if (onDropClauses.containsKey(shapeName)) {
            boolean execute = true;
            for (String shape: onDropConditionals.get(shapeName)) {
                if (!inventoryContains(shape)) {
                    execute = false;
                }
            }
            if (execute) {
                for (Action action: onDropClauses.get(shapeName)) {
                    action.execute();
                }
                return true;
            }
            return false;
        } else {
            return false;
        }
    }

    // getters and setters
}