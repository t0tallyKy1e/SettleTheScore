package com.ckm.settlethescore;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.Random;

public class DrawStraw extends AppCompatActivity {
    private Player activePlayer;
    Session currentSession;
    String current_player_number = "0";

    int number_of_players = 2;
    int max_number_of_players = 4;
    int number_of_straws = 5;
    int current_number_of_players = 0;

    String players[] = new String[max_number_of_players]; // player IDs
    String player_choices[] = new String[max_number_of_players]; // straw number chosen
    ImageView player_straws[] = new ImageView[max_number_of_players];

    ImageView straws[] = new ImageView[number_of_straws];

    String straw_lengths[] = new String[number_of_straws]; // random lengths saved directly to DB
    String straw_chosen[] = new String[number_of_straws]; // true of false
    int number_of_straws_chosen = 0;

    String host = "NULL";
    String loser = "NULL";
    String friendID = "NULL";
    String score = "NULL";

    String is_full = "false";
    String is_complete = "false";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent oldIntent = getIntent();
        activePlayer = Player.getPlayerFromLastActivity(oldIntent);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_straw);

        final EditText addPlayerField = findViewById(R.id.add_email);
        final TextView addPlayerLabel = findViewById(R.id.add_email_label);

        straws[0] = findViewById(R.id.straw_one);
        straws[1] = findViewById(R.id.straw_two);
        straws[2] = findViewById(R.id.straw_three);
        straws[3] = findViewById(R.id.straw_four);
        straws[4] = findViewById(R.id.straw_five);

        player_straws[0] = findViewById(R.id.player_1_straw);
        player_straws[1] = findViewById(R.id.player_2_straw);
        player_straws[2] = findViewById(R.id.player_3_straw);
        player_straws[3] = findViewById(R.id.player_4_straw);

        for(int i = 0; i < players.length; i++) {
            players[i] = "NULL";
            player_choices[i] = "NULL";
        }

        for(int i = 0; i < straws.length; i++) {
            straw_chosen[i] = "NULL";
            straw_lengths[i] = "NULL";
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        drawer = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        final DrawerLayout finalDrawer = drawer;
        final Player finalActivePlayer = activePlayer;
        TextView txtView = navigationView.getHeaderView(0).findViewById(R.id.txtDrawerUserName);
        txtView.setText(activePlayer.getDisplayName());
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        Intent i = new Intent(DrawStraw.this, DrawStraw.class);
                        int id = menuItem.getItemId();
                        boolean shouldStayOnCurrentActivity = false;
                        if (id == R.id.nav_dice) {
                            i = new Intent(DrawStraw.this, Dice.class);
                        } else if (id == R.id.nav_life) {
                            i = new Intent(DrawStraw.this, Life.class);
                        } else if (id == R.id.nav_scores) {
                            i = new Intent(DrawStraw.this, ScoreBoard.class);
                        } else if (id == R.id.nav_home){
                            i = new Intent(DrawStraw.this, MainActivity.class);
                        } else if (id == R.id.nav_rps) {
                            i = new Intent(DrawStraw.this, RocPapSci.class);
                        } else {
                            // clicked on self
                            shouldStayOnCurrentActivity = true;
                        }
                      
                        if(!shouldStayOnCurrentActivity) {
                            finalActivePlayer.sendPlayerToNextActivity(i);
                            startActivity(i);
                        }

                        finalDrawer.closeDrawers();
                        return true;
                    }
                });

        // typical session info needed for each game
            // create new session
            currentSession = null;
            currentSession = Session.getSessionFromLastActivity(oldIntent);

        if(currentSession != null) {
            loadFromDatabase();
        }

        String default_lengths[] = {"1", "2", "3", "4", "5"};
        int max_value = 4;

        // get lengths for each straw
            for(int i = 0; i < default_lengths.length; i++) {
                // get random index
                int random_index;
                if(max_value > 0) {
                    Random rand = new Random();
                    random_index = rand.nextInt(max_value);
                } else {
                    random_index = 0;
                }

                // set current straw length to random index's value
                straw_lengths[i] = default_lengths[random_index];

                // swap random index and last index
                int temp_value = Integer.parseInt(default_lengths[random_index]);
                default_lengths[random_index] = default_lengths[max_value];
                default_lengths[max_value] = new Integer(temp_value).toString();

                // lower maximum possible value for random index
                max_value--;
            }

        // check if current game was successfully loaded
            if(currentSession == null) {
                Log.e("LOAD", "unsuccessful");
                currentSession = new Session(Game.TYPE.STRAWS);
                initialPushToDatabase();
                currentSession.addHost(activePlayer.getUserId());
                host = activePlayer.getUserId();
                players[0] = activePlayer.getUserId();
                activePlayer.addGame(currentSession.getID());
                current_player_number = "1";
                current_number_of_players++;
                updateDatabase();
            } else {
                loadFromDatabase();
                printCurrent();
            }

        // add straw click listeners
            for(int i = 0; i < straws.length; i++) {
                addStrawListener(straws[i], i);
            }
        // check if current player is the host
            if(activePlayer.getUserId() == host) {
                FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int addToGame = 1;
                        boolean addedPlayer = false;

                        for(int i = 0; i < players.length && !addedPlayer; i++) {
                            if(players[i] == "NULL") {
                                activePlayer.addPlayerByEmail(addPlayerField.getText().toString(), addToGame, currentSession.getID());
                                players[i] = getPlayerIDByEmail(addPlayerField.getText().toString());
                                addedPlayer = true;
                                current_number_of_players++;

                                if(i == number_of_players) {
                                    is_full = "true";
                                }

                                Toast.makeText(DrawStraw.this, "Player added to game", Toast.LENGTH_LONG).show();
                            }
                        }

                        if(!addedPlayer) {
                            Toast.makeText(DrawStraw.this, "Player not added to game... Game might be full", Toast.LENGTH_LONG).show();
                        }

                        updateDatabase();
                    }
                });
            } else {
                addPlayerField.setVisibility(View.INVISIBLE);
                addPlayerLabel.setVisibility(View.INVISIBLE);
            }


        // reload databse when data is updated
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            final DatabaseReference databaseReference = database.getReference().child("Sessions").child(currentSession.getID());
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    loadFromDatabase();

                    if(isGameComplete()) {
                        displayResults();
                        databaseReference.removeEventListener(this);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
    }

    public void addStrawListener(ImageView straw, int number) {
        final int n = number + 1;
        final Session session = currentSession;

        straw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // make sure player has a number to work with
                    if(current_player_number == "0") {
                        boolean playerFound = false;
                        currentSession.gameType = Game.TYPE.STRAWS;
                        for(int i = 0; i < players.length && !playerFound; i++) {
                            if(activePlayer.getUserId() == players[i]) {
                                current_player_number = Integer.toString(i + 1);
                                playerFound = true;
                            }
                        }

                        if(!playerFound && is_full.equals("false")) {
                            boolean addedPlayer = false;

                            for(int i = 0; i < players.length && !addedPlayer; i++) {
                                if(players[i] == "NULL") {
                                    current_number_of_players++;

                                    if(i == number_of_players) {
                                        is_full = "true";
                                    }

                                    addedPlayer = true;
                                }
                            }
                        }

                        updateDatabase();
                    }

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                final DatabaseReference databaseReference = database.getReference().child("Sessions").child(session.getID());

                databaseReference.child("straw" + new Integer(n).toString() + "_chosen").setValue("true");
                databaseReference.child("player" + current_player_number + "_choice").setValue(n);
                player_choices[Integer.parseInt(current_player_number) - 1] = new Integer(n).toString();

                number_of_straws_chosen++;
                databaseReference.child("number_of_straws_chosen").setValue(number_of_straws_chosen);
                databaseReference.child("score").setValue("Player " + current_player_number + " chose straw " + n);
                score = "Player " + current_player_number + " chose straw " + n;

                straw_chosen[n-1] = "true";
                updateDatabase();
                loadFromDatabase();
                updateVisuals();

                if(isGameComplete()) {
                    displayResults();
                }

                removeStrawListeners();
            }
        });
    }

    public void displayResults() {
        loadFromDatabase();

        TextView gameResult = findViewById(R.id.game_result);

        int lowestScoringPlayer = -1;
        int lowestScore = -1;


        for(int i = 0; i < number_of_players; i++) {
            int currentPlayerStrawLength = Integer.parseInt(player_choices[i]);

            if(currentPlayerStrawLength < lowestScore) {
                lowestScoringPlayer = i + 1;
                lowestScore = currentPlayerStrawLength;
            }
        }

        if(lowestScoringPlayer != -1) {
            score = "Player " + new Integer(lowestScoringPlayer).toString() + " loses!";

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            final DatabaseReference databaseReference = database.getReference().child("Sessions").child(currentSession.getID());

            gameResult.setText(score.toString());
            loser = players[lowestScoringPlayer - 1];

            updateVisuals();
            updateDatabase();
        }
    }

    public String getPlayerIDByEmail(final String email) {
        String id = "";

        getPlayerIDByEmail_Inner(email, new DatabaseCallback() {
            @Override
            public void onCallback(String key, String value) {
                switch(key) {
                    case "id":
                        friendID = value;
                        break;
                    default:
                        Log.e("ERROR", "");
                }
            }
        });

        id = friendID;

        return id;
    }

    public void getPlayerIDByEmail_Inner(final String email, final DatabaseCallback dbCallback) {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = database.getReference().child("Players");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    if(ds.child("email").getValue().toString().equals(email)) {
                        dbCallback.onCallback("id", ds.getKey().toString());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    // not to be used alone... call loadFromDatabase() instead
    public void getValuesFromDatabase(final DatabaseCallback dbCallback) {
        final Session session = currentSession;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = database.getReference().child("Sessions").child(session.getID());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // host
                    dbCallback.onCallback("host", dataSnapshot.child("host").getValue().toString());

                // players
                    dbCallback.onCallback("player1", dataSnapshot.child("player1").getValue().toString());
                    dbCallback.onCallback("player2", dataSnapshot.child("player2").getValue().toString());
                    dbCallback.onCallback("player3", dataSnapshot.child("player3").getValue().toString());
                    dbCallback.onCallback("player4", dataSnapshot.child("player4").getValue().toString());

                // player choices
                    dbCallback.onCallback("player1_choice", dataSnapshot.child("player1_choice").getValue().toString());
                    dbCallback.onCallback("player2_choice", dataSnapshot.child("player2_choice").getValue().toString());
                    dbCallback.onCallback("player3_choice", dataSnapshot.child("player3_choice").getValue().toString());
                    dbCallback.onCallback("player4_choice", dataSnapshot.child("player4_choice").getValue().toString());

                // straw lengths
                    dbCallback.onCallback("straw1_length", dataSnapshot.child("straw1_length").getValue().toString());
                    dbCallback.onCallback("straw2_length", dataSnapshot.child("straw2_length").getValue().toString());
                    dbCallback.onCallback("straw3_length", dataSnapshot.child("straw3_length").getValue().toString());
                    dbCallback.onCallback("straw4_length", dataSnapshot.child("straw4_length").getValue().toString());
                    dbCallback.onCallback("straw5_length", dataSnapshot.child("straw5_length").getValue().toString());

                // straw chosen status
                    dbCallback.onCallback("straw1_chosen", dataSnapshot.child("straw1_chosen").getValue().toString());
                    dbCallback.onCallback("straw2_chosen", dataSnapshot.child("straw2_chosen").getValue().toString());
                    dbCallback.onCallback("straw3_chosen", dataSnapshot.child("straw3_chosen").getValue().toString());
                    dbCallback.onCallback("straw4_chosen", dataSnapshot.child("straw4_chosen").getValue().toString());
                    dbCallback.onCallback("straw5_chosen", dataSnapshot.child("straw5_chosen").getValue().toString());

                // number of straws chosen
                    dbCallback.onCallback("number_of_straws_chosen", dataSnapshot.child("number_of_straws_chosen").getValue().toString());

                // loser
                    dbCallback.onCallback("loser", dataSnapshot.child("loser").getValue().toString());

                // statuses
                    dbCallback.onCallback("is_full", dataSnapshot.child("is_full").getValue().toString());
                    dbCallback.onCallback("is_complete", dataSnapshot.child("is_complete").getValue().toString());

                // score
                    dbCallback.onCallback("score", dataSnapshot.child("score").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void initialPushToDatabase() {
        int array_size = straw_lengths.length;
        final Session session = currentSession;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = database.getReference().child("Sessions").child(session.getID());

        // host
        databaseReference.child("host").setValue("NULL");

        // players
        databaseReference.child("player1").setValue("NULL");
        databaseReference.child("player2").setValue("NULL");
        databaseReference.child("player3").setValue("NULL");
        databaseReference.child("player4").setValue("NULL");

        // player choices
        databaseReference.child("player1_choice").setValue("NULL");
        databaseReference.child("player2_choice").setValue("NULL");
        databaseReference.child("player3_choice").setValue("NULL");
        databaseReference.child("player4_choice").setValue("NULL");

        // straw lengths
        databaseReference.child("straw1_length").setValue(straw_lengths[0]);
        databaseReference.child("straw2_length").setValue(straw_lengths[1]);
        databaseReference.child("straw3_length").setValue(straw_lengths[2]);
        databaseReference.child("straw4_length").setValue(straw_lengths[3]);
        databaseReference.child("straw5_length").setValue(straw_lengths[4]);

        // straw chosen status
        databaseReference.child("straw1_chosen").setValue("false");
        databaseReference.child("straw2_chosen").setValue("false");
        databaseReference.child("straw3_chosen").setValue("false");
        databaseReference.child("straw4_chosen").setValue("false");
        databaseReference.child("straw5_chosen").setValue("false");

        // number of straws chosen
        databaseReference.child("number_of_straws_chosen").setValue("false");

        // loser
        databaseReference.child("loser").setValue("NULL");

        // statuses
        databaseReference.child("is_full").setValue("false");
        databaseReference.child("is_complete").setValue("false");

        //score
        databaseReference.child("score").setValue(score);

    }

    public boolean isGameComplete() {
        boolean result = false;

        loadFromDatabase();

        if(number_of_straws_chosen == number_of_players) {
            is_complete = "true";
            updateDatabase();
            removeStrawListeners();
            updateVisuals();
            Toast.makeText(DrawStraw.this, "Game is complete", Toast.LENGTH_LONG).show();
            result = true;
        }

        return result;
    }

    public void loadFromDatabase() {
        getValuesFromDatabase(new DatabaseCallback() {
            @Override
            public void onCallback(String key, String value) {
                switch(key) {
                    case "host" :
                        host = value;
                        break;

                    case "player1" :
                        players[0] = value;
                        break;

                    case "player2" :
                        players[1] = value;
                        break;

                    case "player3" :
                        players[2] = value;
                        break;

                    case "player4" :
                        players[3] = value;
                        break;

                    case "player1_choice" :
                        player_choices[0] = value;
                        break;

                    case "player2_choice" :
                        player_choices[1] = value;
                        break;

                    case "player3_choice" :
                        player_choices[2] = value;
                        break;

                    case "player4_choice" :
                        player_choices[3] = value;
                        break;

                    case "straw1_length" :
                        straw_lengths[0] = value;
                        break;

                    case "straw2_length" :
                        straw_lengths[1] = value;
                        break;

                    case "straw3_length" :
                        straw_lengths[2] = value;
                        break;

                    case "straw4_length" :
                        straw_lengths[3] = value;
                        break;

                    case "straw5_length" :
                        straw_lengths[4] = value;
                        break;

                    case "straw1_chosen" :
                        straw_chosen[0] = value;
                        break;

                    case "straw2_chosen" :
                        straw_chosen[1] = value;
                        break;

                    case "straw3_chosen" :
                        straw_chosen[2] = value;
                        break;

                    case "straw4_chosen" :
                        straw_chosen[3] = value;
                        break;

                    case "straw5_chosen" :
                        straw_chosen[4] = value;
                        break;

                    case "number_of_straws_chosen" :
                        number_of_straws_chosen = Integer.parseInt(value);
                        break;

                    case "loser" :
                        loser = value;
                        break;

                    case "is_full" :
                        is_full = value;
                        break;

                    case "is_complete" :
                        is_complete = value;
                        break;

                    case "score" :
                        score = value;
                        break;

                    default:
                        Log.e("ERROR", key.toString() + " not saved in getValuesFromDatabse()");
                        break;
                }
            }
        });

        updateVisuals();
    }

    public void printCurrent() {
        for(int i = 0; i < players.length; i++) {
            Log.e("ERROR", players[i].toString());
        }

        for(int i = 0; i < player_choices.length; i++) {
            Log.e("ERROR", player_choices[i].toString());
        }

        for(int i = 0; i < straw_lengths.length; i++) {
            Log.e("ERROR", straw_lengths[i].toString());
        }

        for(int i = 0; i < straw_chosen.length; i++) {
            Log.e("ERROR", straw_chosen[i].toString());
        }

        Log.e("ERROR", new Integer(number_of_straws_chosen).toString());

        Log.e("ERROR", host.toString());

        Log.e("ERROR", loser.toString());

        Log.e("ERROR", is_full.toString());

        Log.e("ERROR", is_complete.toString());
    }

    public void removeStrawListeners() {
        for(int i = 0; i < straws.length; i++) {
            straws[i].setOnClickListener(null);
        }
    }

    public void updateDatabase() {
        final Session session = currentSession;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = database.getReference().child("Sessions").child(session.getID());

        // host
            databaseReference.child("host").setValue(host);

        // players
            databaseReference.child("player1").setValue(players[0]);
            databaseReference.child("player2").setValue(players[1]);
            databaseReference.child("player3").setValue(players[2]);
            databaseReference.child("player4").setValue(players[3]);

        // player choices
            databaseReference.child("player1_choice").setValue(player_choices[0]);
            databaseReference.child("player2_choice").setValue(player_choices[1]);
            databaseReference.child("player3_choice").setValue(player_choices[2]);
            databaseReference.child("player4_choice").setValue(player_choices[3]);

        // straw chosen status
            for(int i = 0; i < number_of_players; i++) {
                if(player_choices[i] != "NULL") {
                    straw_chosen[Integer.parseInt(player_choices[i]) - 1] = "true";
                }
            }

            databaseReference.child("straw1_chosen").setValue(straw_chosen[0]);
            databaseReference.child("straw2_chosen").setValue(straw_chosen[1]);
            databaseReference.child("straw3_chosen").setValue(straw_chosen[2]);
            databaseReference.child("straw4_chosen").setValue(straw_chosen[3]);
            databaseReference.child("straw5_chosen").setValue(straw_chosen[4]);

        // number of straws chosen
            number_of_straws_chosen = 0;
            for(int i = 0; i < straw_chosen.length; i++) {
                if(straw_chosen[i] != "NULL") {
                    number_of_straws_chosen++;
                }
            }

            databaseReference.child("number_of_straws_chosen").setValue(number_of_straws_chosen);

        // loser
            databaseReference.child("loser").setValue(loser);

        // statuses
            databaseReference.child("is_full").setValue(is_full);
            databaseReference.child("is_complete").setValue(is_complete);

        // score
            databaseReference.child("score").setValue(score);
    }

    public void updateVisuals() {
        for(int i = 0; i < straws.length; i++) {
            if(straw_chosen[i] != "NULL") {
                straws[i].setVisibility(View.INVISIBLE);
            }
        }

        for(int i = 0; i < players.length; i++) {
            switch(player_choices[i]) {
                case "1" :
                    player_straws[i].setImageDrawable(straws[0].getDrawable());
                    break;
                case "2" :
                    player_straws[i].setImageDrawable(straws[1].getDrawable());
                    break;
                case "3" :
                    player_straws[i].setImageDrawable(straws[2].getDrawable());
                    break;
                case "4" :
                    player_straws[i].setImageDrawable(straws[3].getDrawable());
                    break;
                case "5" :
                    player_straws[i].setImageDrawable(straws[4].getDrawable());
                    break;
                default :
                    player_straws[i].setImageDrawable(getResources().getDrawable(android.R.drawable.ic_menu_help));
                    break;
            }
        }
    }
}
