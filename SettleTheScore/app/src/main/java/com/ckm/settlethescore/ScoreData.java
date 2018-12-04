package com.ckm.settlethescore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScoreData {

    public static final List<ScoreItem> ITEMS = new ArrayList<ScoreItem>();

    public static final Map<String, ScoreItem> ITEM_MAP = new HashMap<String, ScoreItem>();

    private static String[] sampleScores = { "Kyle Won: Paper beats Rock",
                                                "Marvin rolled a 20",
                                                "Chris: 20 life, Kyle: 18 life"};

    private static String[] sampleStates = { "Won", "Lost", "In Progress"};
    private static final int COUNT = 6;

    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createScore(i));
        }
    }

    private static void addItem(ScoreItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static ScoreItem createScore(int position) {
        return new ScoreItem(String.valueOf(position), sampleScores[position % sampleScores.length], makeDetails(position), position);
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }
    /*
        Sample Score Item to be replaced with actual data
     */
    public static class ScoreItem {
        public final String id;
        public final String content;
        public final String details;
        public final String gameStatus;
        public final String score;
        public final int count;

        public ScoreItem(String id, String content, String details, int count) {
            this.id = id;
            this.content = "Player 1";
            this.details = details;
            this.gameStatus = "In Progress";
            this.score = content;
            this.count = count;
        }

        @Override
        public String toString() {
            return content;
        }
    }
}
