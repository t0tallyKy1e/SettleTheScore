package com.ckm.settlethescore;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

public class Player {
    private String isConnected; // 0 = not connected ...... 1 = connected

    private FirebaseUser firebaseUser;

    private String displayName;
    private String email;
    private String fullName;
    private String phoneNumber;
    private String userId;

    private ArrayList friends = new ArrayList(); // list of user IDs
    private ArrayList games = new ArrayList(); // array of game IDs

    private String numberOfFriends = "0";
    private String numberOfGames = "0";

    // Defaults
    private final int defaultIsConnected = 0;

    private final FirebaseUser defaultFirebaseUser = null;

    private final String defaultDisplayName = "Default Player";
    private final String defaultEmail = "default_email@email_website.com";
    private final String defaultFullName = "Default Full Name";
    private final String defaultPhoneNumber = "(555) 555-5555";
    private final String defaultUserId = "-1";

    // Default Constructor
    public Player() {
        isConnected = "1";

        userId = defaultUserId;

        firebaseUser = defaultFirebaseUser;

        displayName = defaultDisplayName;
        email = defaultEmail;
        fullName = defaultFullName;
        phoneNumber = defaultPhoneNumber;
    }

    public Player(String user_id) {
        userId = user_id;
        update();
    }

    // Setters / Getters
    public String isConnected() {
        return isConnected;
    }

    public void setIsConnected(String status) {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = database.getReference().child("Players").child(userId);
        
        isConnected = status;
        databaseReference.child("status").setValue(status);
    }

    public void setFirebaseUser(FirebaseUser user) {
        firebaseUser = user;
    }

    public FirebaseUser getFirebaseUser() {
        return firebaseUser;
    }

    public void setDisplayName(String name) {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = database.getReference().child("Players").child(userId);

        displayName = name;
        databaseReference.child("display_name").setValue(displayName);
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setEmail(String new_email) {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = database.getReference().child("Players").child(userId);

        email = new_email;

        databaseReference.child("email").setValue(email);
    }

    public String getEmail() {
        return email;
    }

    public void setFullName(String name) {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = database.getReference().child("Players").child(userId);

        fullName = name;

        databaseReference.child("full_name").setValue(fullName);
    }

    public String getFullName() {
        return fullName;
    }

    public void setNumberOfFriends(String number) { numberOfFriends = number; }

    public String getNumberOfFriends() { return numberOfFriends; }

    public void setNumberOfGames(String number) {
        numberOfGames = number;
    }

    public String getNumberOfGames() {
        return numberOfGames;
    }

    public void setPhoneNumber(String number) {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = database.getReference().child("Players").child(userId);

        phoneNumber = number;

        databaseReference.child("phone_number").setValue(phoneNumber);
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    // array of friends setters / getters

    public String getUserId() {
        return userId;
    }

    public void setUserId(String id) {
        userId = id;
    }

    @Override
    public String toString() {
        return "User I.D.: " + userId + "\n" +
                "Display Name: " + displayName + "\n" +
                "Full Name: " + fullName + "\n" +
                "Email: " + email + "\n" +
                "Phone Number: " + phoneNumber + "\n" +
                "Connected: " + isConnected;
    }

    public void addFriend(String id) {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = database.getReference().child("Players").child(userId);

        friends.add(id);

        databaseReference.child("Friends").child(numberOfFriends).setValue(id);
        Integer tempNumber = new Integer(numberOfFriends);
        tempNumber++;
        numberOfFriends = tempNumber.toString();
        databaseReference.child("Friends").child("number_of_friends").setValue(numberOfFriends);

        updateCountsInDatabase();
    }

    public void addGame(String id) {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = database.getReference().child("Players").child(userId);

        games.add(id);

        databaseReference.child("Games").child(id).child("game_id").setValue(id);
        Integer tempNumber = new Integer(numberOfGames);
        tempNumber++;
        numberOfGames = tempNumber.toString();

        updateCountsInDatabase();
    }

    public static Player generatePlayerFromFirebaseUser(FirebaseUser user) {
        Player player = new Player();
        player.setFirebaseUser(user);
        player.setEmail(user.getEmail());
        player.setDisplayName(user.getDisplayName());
        player.setFullName(user.getDisplayName());
        player.setIsConnected("1");
        player.setPhoneNumber(user.getPhoneNumber());
        player.setUserId(user.getUid());

        return player;
    }

    public void sendPlayerToNextActivity(Intent intent) {
        intent.putExtra("player_id", getUserId());
        intent.putExtra("player_display_name", getDisplayName());
        intent.putExtra("player_full_name", getFullName());
        intent.putExtra("player_phone_number", getPhoneNumber());
        intent.putExtra("player_email", getEmail());
        intent.putExtra("is_connected", isConnected());
        intent.putExtra("number_of_games", numberOfGames);
        intent.putExtra("number_of_friends", numberOfFriends);
    }

    public static Player getPlayerFromLastActivity(Intent intent) {
        Player activePlayer = new Player(intent.getStringExtra("player_id"));
        activePlayer.setDisplayName(intent.getStringExtra("player_display_name"));
        activePlayer.setFullName(intent.getStringExtra("player_full_name"));
        activePlayer.setPhoneNumber(intent.getStringExtra("player_phone_number"));
        activePlayer.setEmail(intent.getStringExtra("player_email"));
        activePlayer.setIsConnected(intent.getStringExtra("is_connected"));
        activePlayer.setNumberOfGames(intent.getStringExtra("number_of_games"));
        activePlayer.setNumberOfFriends(intent.getStringExtra("number_of_friends"));

        return activePlayer;
    }

    // wrapper for updatePlayerFromDatabase
    public void update() {
        updatePlayerFromDatabase(new DatabaseCallback() {
            @Override
            public void onCallback(String key, String value) {
            switch(key) {
                case "user_id":
                    setUserId(value);
                    break;
                case "phone_number":
                    setPhoneNumber(value);
                    break;
                case "is_connected":
                    setIsConnected(value);
                    break;
                case "full_name":
                    setFullName(value);
                    break;
                case "display_name":
                    setDisplayName(value);
                    break;
                case "email":
                    setEmail(value);
                    break;
                case "number_of_games":
                    setNumberOfGames(value);
                    break;
                case "number_of_friends":
                    updateCountsInDatabase();
                    break;
                default:
                    Log.e("CKM", "Player(): error on " + value + "no case matched " + key);
                    break;
            }
            }
        });
    }

    public void updatePlayerFromDatabase(final DatabaseCallback databaseCallback) {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final DatabaseReference databaseReference = database.getReference().child("Players").child(userId);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                databaseCallback.onCallback("user_id", userId);
                databaseCallback.onCallback("phone_number", dataSnapshot.child("phone_number").getValue().toString());
                databaseCallback.onCallback("is_connected", dataSnapshot.child("status").getValue().toString());
                databaseCallback.onCallback("full_name", dataSnapshot.child("full_name").getValue().toString());
                databaseCallback.onCallback("display_name", dataSnapshot.child("display_name").getValue().toString());
                databaseCallback.onCallback("email", dataSnapshot.child("email").getValue().toString());
                databaseCallback.onCallback("number_of_games", dataSnapshot.child("Games").child("number_of_games").getValue().toString());
                databaseCallback.onCallback("number_of_friends", dataSnapshot.child("Friends").child("number_of_friends").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void addPlayerByEmail(final String email, final int type, final String sessionID) {
        final String user_id = userId;
        final int addAsFriend = 0;
        final int addToGame = 1;

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = database.getReference().child("Players");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    if(ds.child("email").getValue().toString().equals(email)) {
                        String friendID = ds.getKey();

                        switch(type) {
                            case addAsFriend :
                                addFriend(friendID);
                                Player tempPlayer = new Player(friendID);
                                tempPlayer.update();
                                tempPlayer.addFriend(user_id);
                                break;
                            case addToGame :
                                Session session = new Session(sessionID);
                                session.addPlayer(friendID);
                                session.updateDatabase();
                                break;
                            default :
                                break;
                        }

                        updateCountsInDatabase();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    // update values for:
    // -- number_of_friends
    // -- number_of_games
    public void updateCountsInDatabase() {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final DatabaseReference databaseReference = database.getReference().child("Players").child(userId);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // -- FRIENDS
                int sumOfPlayers = 0;

                for(DataSnapshot ds : dataSnapshot.child("Friends").getChildren()) {
                    if(!ds.getKey().equals("number_of_friends")) {
                        sumOfPlayers++;
                    }
                }

                databaseReference.child("Friends").child("number_of_friends").setValue(sumOfPlayers);
                numberOfFriends = new Integer(sumOfPlayers).toString();

                // -- GAMES
                int sumOfGames = 0;

                for(DataSnapshot ds : dataSnapshot.child("Games").getChildren()) {
                    if(!ds.getKey().equals("number_of_games")) {
                        sumOfGames++;
                    }
                }

                databaseReference.child("Games").child("number_of_games").setValue(sumOfGames);
                numberOfGames = new Integer(sumOfGames).toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void printToLog(String value) {
        Log.e("CKM", value);
    }

    // Sets all values to their Default settings
    public void signOut() {
        isConnected = "0";
        userId = defaultUserId;
        friends = new ArrayList();
        firebaseUser = defaultFirebaseUser;
        displayName = defaultDisplayName;
        email = defaultEmail;
        fullName = defaultFullName;
        phoneNumber = defaultPhoneNumber;
        numberOfFriends = "0";
        numberOfGames = "0";
    }
}
