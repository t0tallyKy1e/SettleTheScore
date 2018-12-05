package com.ckm.settlethescore;

import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScoreData {

    public static final List<ScoreItem> ITEMS = new ArrayList<ScoreItem>();

    public static final Map<String, ScoreItem> ITEM_MAP = new HashMap<String, ScoreItem>();

    private static String[] sampleScores = {"Kyle Won: Paper beats Rock",
            "Marvin rolled a 20",
            "Chris: 20 life, Kyle: 18 life"};

    private static String[] sampleStates = {"Won", "Lost", "In Progress"};
    private static int COUNT = 0;

    private static ArrayList<SessionData> sessions = new ArrayList<SessionData>();

    static {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = database.getReference();
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot2 : dataSnapshot.child("Sessions").getChildren()) {
                    SessionData session = dataSnapshot2.getValue(SessionData.class);
                    session.key = dataSnapshot2.getKey();
                    sessions.add(session);
                    ScoreItem score = new ScoreItem(
                            session.key,
                            session.game_type,
                            sampleScores[COUNT++ % 3],
                            session.host);
                    addItem(score);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        databaseReference.addValueEventListener(postListener);
        for (int i = 1; i <= COUNT; i++) {
            addItem(createScore(i));
        }
    }

    private static void addItem(ScoreItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

    private static ScoreItem createScore(int position) {
        return new ScoreItem(String.valueOf(position), sampleScores[position % sampleScores.length], makeDetails(position), "Unknown");
    }

    /*
        Sample Score Item to be replaced with actual data
     */
    public static class ScoreItem {
        public final String game_type;
        public final String id;
        public final String score;
        public final String host;

        public ScoreItem(String id, String game_type, String score, String host) {
            this.id = id;
            this.game_type = game_type;
            this.score = score;
            this.host = host;
        }

        @Override
        public String toString() {
            return game_type;
        }
    }

    @IgnoreExtraProperties
    public static class SessionData {
        public String game_type;
        public String host;
        public String key;
        public Integer dice_roll;
    }
}
