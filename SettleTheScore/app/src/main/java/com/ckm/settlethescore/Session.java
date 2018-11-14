package com.ckm.settlethescore;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Session {
    private boolean expired; // used to remove game from database?

    private int currentPlayerIndex;
    private int gameType; // Game class instead?
    private int maxNumberOfPlayers = 10;

    private String sessionID;
    private Player players[];
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    public Session() {
        // gameType not defined
        // --- gameType must be specified later

        currentPlayerIndex = 0;
        players = new Player[maxNumberOfPlayers];
        // --- players must be added later

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference().child("Sessions");
        sessionID = databaseReference.push().getKey().toString().trim();
        databaseReference = databaseReference.child(sessionID);

        // push all info to DB
        updateDatabase();
    }

    public Session(int game_type) {
        gameType = game_type;

        currentPlayerIndex = 0;
        players = new Player[maxNumberOfPlayers];
        // --- players must be added later via addPlayer(playerID)

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference().child("Sessions");
        sessionID = databaseReference.push().getKey().toString().trim();
        databaseReference = databaseReference.child(sessionID);

        // push all info to DB
        updateDatabase();
    }

    String getID() {
        return sessionID;
    }

    public void addPlayer(String playerID) {
        if(currentPlayerIndex < maxNumberOfPlayers) {
            players[currentPlayerIndex] = new Player(playerID);
            databaseReference.child("Players").child(new Integer(currentPlayerIndex).toString().trim()).setValue(playerID);
            currentPlayerIndex++;
        } else {
            Log.e("CKM", "Session Error: Attempted to add too many players to game session (ID: " + sessionID + ")");
        }
    }

    public void updateDatabase() {
        databaseReference.child("Game Type").setValue(gameType); // might have to save game ID

        for(int i = 0; i < currentPlayerIndex; ++i) {
            databaseReference.child("Players").child(new Integer(i).toString().trim()).setValue(players[i].getUserId());
        }
    }

    // something to track each player's progress
    // reportStatus(int status) <- called by the player
    // array of statuses?
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
