package com.ckm.settlethescore;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Session {
    private boolean expired; // used to remove game from database?

    private String currentPlayerIndex;
    public Game.TYPE gameType;
    private Game.STATE gameState;
    private int maxNumberOfPlayers = 10;

    private String sessionID;
    private String hostID;
    private ArrayList<String> players;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    public Session() {

    }

    public Session(Game.TYPE game_type) {
        gameType = game_type;

        currentPlayerIndex = "0";
        players = new ArrayList<String>();
        // --- players must be added later via addPlayer(playerID)

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference().child("Sessions");
        sessionID = databaseReference.push().getKey().toString().trim();
        databaseReference = databaseReference.child(sessionID);

        // push all info to DB
        updateDatabase();
    }

    // recreate previous game
    public Session(String id) {
        sessionID = id;

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference().child("Sessions").child(id);

        currentPlayerIndex = "0";

        players = new ArrayList<String>();

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentPlayerIndex = dataSnapshot.child("Players").child("number_of_players").getValue().toString();
                Object type = dataSnapshot.child("game_type").getValue();

                if(type == null) {
                    type = new String();
                }

                int number_of_players = new Integer(currentPlayerIndex + 1);
                for (int i = 0; i < number_of_players; i++) {
                    Object tempPlayerIDObject = dataSnapshot.child("Players").child(new Integer(i).toString()).getValue();
                    if (tempPlayerIDObject != null) {
                        String tempPlayerID = tempPlayerIDObject.toString();
                        if (!tempPlayerID.equals(null)) {
                            players.add(i, tempPlayerID);
                        }
                    }
                }

                if (type.equals("DICE")) {
                    gameType = Game.TYPE.DICE;
                } else if (type.equals("LIFE")) {
                    gameType = Game.TYPE.LIFE;
                } else if (type.equals("ROC_PAP_SCI")) {
                    gameType = Game.TYPE.ROCK_PAP_SCI;
                } else if (type.equals("STRAWS")) {
                    gameType = Game.TYPE.STRAWS;
                } else if(type.equals(null)) {
                    Log.e("CKM", "ERROR WHEN GETTING GAME TYPE FROM DATABASE.");
                }

                // if(!dataSnapshot.child("host").getValue().equals(null))
                // add host_id

                updateDatabase();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    String getID() {
        return sessionID;
    }

    public void addHost(String playerID) {
        databaseReference.child("host").setValue(playerID);
        hostID = playerID;
    }

    public void addPlayer(String playerID) {
        // if maxNumberOfPlayers > 0 ... check if too many added
        int newIndex = Integer.parseInt(currentPlayerIndex);
        Integer index = new Integer(newIndex);

        // add player to session
        databaseReference.child("Players").child(playerID).child("user_id").setValue(playerID);

        // add session to player
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = database.getReference().child("Players").child(playerID).child("Games");

        Player tempPlayer = new Player(playerID);
        tempPlayer.addGame(sessionID);

        index++;
        currentPlayerIndex = index.toString();
        updateDatabase();
    }

    public void updateDatabase() {
        databaseReference.child("game_type").setValue(gameType); // might have to save game ID

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int sumOfPlayers = 0;

                for(DataSnapshot ds : dataSnapshot.child("Players").getChildren()) {
                    if(!ds.getKey().equals("number_of_players")) {
                        sumOfPlayers++;
                    }
                }

                databaseReference.child("Players").child("number_of_players").setValue(sumOfPlayers);

                databaseReference.child("game_type").setValue(gameType);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static void sendPreviousSessionToActivity(Intent intent, String session_id) {
        intent.putExtra("session_id", session_id);
    }

    public static Session getSessionFromLastActivity(Intent intent) {
        Session currentSession = new Session();

        if(intent.getStringExtra("session_id") != null) {
            currentSession = new Session(intent.getStringExtra("session_id"));
        } else {
            currentSession = null;
        }

        return currentSession;
    }
}

/*
    EXAMPLE USE

    Button sessionButton = (Button) findViewById(R.id.start_session_button);
    final TextView sessionID = (TextView) findViewById(R.id.session_id_label);

    sessionButton.setOnClickListener(new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            int DICE = 0;
            Session currentSession = new Session(DICE);
            currentSession.addPlayer(activePlayer.getUserId());

            if(currentSession.getID() != null) {
                sessionID.setText(currentSession.getID());
            }
        }
    });

 */
