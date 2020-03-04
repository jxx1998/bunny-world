package edu.stanford.cs108.bunnyworld;

import android.content.Context;
import android.content.res.Resources;
import android.media.MediaPlayer;

import java.util.*;

import static edu.stanford.cs108.bunnyworld.BunnyWorldApplication.getGlobalContext;

/**
 * Scripts class
 * Each shape contains a Scripts object
 * Scripts contains a list of Clause objects (either on click/enter/drop)
 * Each Clause object contains a list of actions performed when triggered
 */
public class Scripts {

    List<Clause> clauses;

    private final Set<String> triggerKeywords = new HashSet<String>
            (Arrays.asList("on click", "on enter", "on drop"));
    private final Set<String> actionKeywords = new HashSet<String>
            (Arrays.asList("goto", "play", "hide", "show"));

    public Scripts() { this.clauses = new ArrayList<Clause>(); }
    public Scripts(ArrayList<Clause> clauses) {
        this.clauses = clauses;
    }

    // Can overload this function that does the same thing given a string
    public void addClause(Clause newClause) {
        clauses.add(newClause);
    }

    // TODO
    public void editScripts() {
        return;
    }

    // getters and setters
    public List<Clause> getClauses() { return clauses; }
    public Clause getClauseAt(int index) { return clauses.get(index); }
    public void setClauseAt(int index, Clause newClause) { clauses.set(index, newClause); }

    /**
     * Clause inner class
     * trigger: on click/enter/drop
     */
    public class Clause {
        String trigger;
        List<Action> actions;

        public Clause(String trigger, ArrayList<Action> actions) {
            this.trigger = trigger;
            this.actions = actions;
        }

        /*
        public Clause(String clauseStr) {
            // exclude the semicolon
            StringTokenizer st = new StringTokenizer(
                    clauseStr.substring(0, clauseStr.length() - 1), " ");
            if (!st.hasMoreTokens()) {
                throw new RuntimeException("Cannot create empty clause.");
            }

            String trigger = st.nextToken();
        }
         */

        // Run all actions in clause
        public void runClause() {
            for (Action a : actions) {
                a.runAction();
            }
        }

        // getters and setters
        public String getTrigger() { return this.trigger; }
        public void setTrigger(String trigger) { this.trigger = trigger; }


        /**
         * Actions inner class
         */
        public class Action {
            String keyword;
            String name;

            public Action (String keyword, String name) {
                this.keyword = keyword;
                this.name = name;
            }

            // getters and setters
            public String getKeyword() { return keyword; }
            public String getName() { return name; }
            public void setKeyword(String keyword) { this.keyword = keyword; }
            public void setName(String name) { this.name = name; }

            /**
             * Plays a sound with filename stored in this.name
             * If file not found, this method does nothing
             */
            public void playSound() {
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

            public void runAction() {
                if (keyword.equals("goto")) {
                    // TODO
                } else if (keyword.equals("play")) {
                    playSound();
                } else if (keyword.equals("hide")) {
                    // Shape.this.setHidden(true);
                } else if (keyword.equals("show")) {
                    // Shape.this.setHidden(false);
                }
            }
        }
    }
}