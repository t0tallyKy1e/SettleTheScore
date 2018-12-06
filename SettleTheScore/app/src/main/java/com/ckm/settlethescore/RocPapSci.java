package com.ckm.settlethescore;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.graphics.Color;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.firebase.ui.auth.data.model.Resource;

import static java.sql.Types.NULL;

public class RocPapSci extends AppCompatActivity {

    enum Choices {ROCK, PAPER, SCISSOR}
    enum Status {LOSS, TIE, WIN}
    Session currentSession;
    /*
    public class Players {
        Choices choice;
        Status status;
    }
	*/

    boolean is_clicked = false;
    private Player activePlayer;

    String host = "NULL";
    String friendID = "NULL";

    String player_1_choice = "NULL";
    String player_2_choice = "NULL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent oldIntent = getIntent();
        activePlayer = Player.getPlayerFromLastActivity(oldIntent);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roc_pap_sci);

        final EditText addPlayerField = (EditText) findViewById(R.id.add_email);
        final TextView result = findViewById(R.id.result);

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


        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        Intent i = new Intent();
                        int id = menuItem.getItemId();
                        boolean shouldStayOnCurrentActivity = false;
                        if (id == R.id.nav_straws) {
                            i = new Intent(RocPapSci.this, DrawStraw.class);
                        } else if (id == R.id.nav_life) {
                            i = new Intent(RocPapSci.this, Life.class);
                        } else if (id == R.id.nav_scores) {
                            i = new Intent(RocPapSci.this, ScoreBoard.class);
                        } else if (id == R.id.nav_home){
                            i = new Intent(RocPapSci.this, MainActivity.class);
                        } else if (id == R.id.nav_dice) {
                            i = new Intent(RocPapSci.this, Dice.class);
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

        // create new session

        currentSession = Session.getSessionFromLastActivity(oldIntent);

//        if(currentSession != null) {
//            loadFromDatabase();
//        }

        if(currentSession == null) {
            currentSession = new Session(Game.TYPE.ROCK_PAP_SCI);
        	currentSession.addHost(activePlayer.getUserId());
            host = activePlayer.getUserId();
            activePlayer.addGame(currentSession.getID());
            loadFromDatabase();
        }

        currentSession.addPlayer(activePlayer.getUserId());
        currentSession.updateDatabase();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = database.getReference().child("Sessions").child(currentSession.getID());

        //init player 1 and 2 choices.
        databaseReference.child("player_1_choice").setValue("NULL");
        databaseReference.child("player_2_choice").setValue("NULL");

        final Session finalCurrentSession = currentSession;

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            final DatabaseReference databaseReference = database.getReference().child("Sessions").child(currentSession.getID());

            @Override
            public void onClick(View view) {
                int addToGame = 1;
                boolean addedPlayer = false;

                if(friendID == "NULL") {
                    activePlayer.addPlayerByEmail(addPlayerField.getText().toString(), addToGame, finalCurrentSession.getID());
                    friendID = getPlayerIDByEmail(addPlayerField.getText().toString());
                    addedPlayer = true;

                    Toast.makeText(RocPapSci.this, "Player added to game", Toast.LENGTH_LONG).show();
                    theirChoice(Choices.ROCK);
                    databaseReference.child("player_2_choice").setValue("ROCK");
                    player_2_choice = "ROCK";
                    updateDatabase();
                    RPS_Calculate();
                }
                if(!addedPlayer) {
                    Toast.makeText(RocPapSci.this, "Player not added to game... Game might be full", Toast.LENGTH_LONG).show();
                }
            }
        });

        final ImageView imgRock = (ImageView) findViewById(R.id.play_rock);
        imgRock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (is_clicked == false) {
	                imgRock.setBackgroundResource(R.drawable.button_outline);
	                is_clicked = true;
	                if (activePlayer.getUserId().equals(host)) {
                        databaseReference.child("player_1_choice").setValue("ROCK");
                        player_1_choice = "ROCK"; // theoretically updateDatabase should update the code with database values but it doesnt.
                        myChoice(Choices.ROCK);
                        RPS_Calculate();
                    }
                    else {
                    	databaseReference.child("player_2_choice").setValue("ROCK");
                    	theirChoice(Choices.ROCK);
                    	//RPS_Calculate();
                    }
	                /*
	                    we select rock
	                */
                }

            }
        });

        final ImageView imgPaper = (ImageView) findViewById(R.id.play_paper);
        imgPaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (is_clicked == false) {
	                imgPaper.setBackgroundResource(R.drawable.button_outline);
	                is_clicked = true;
	                if (activePlayer.getUserId().equals(host)){
	                	databaseReference.child("player_1_choice").setValue("PAPER");
	                	player_1_choice = "PAPER"; // theoretically updateDatabase should update the code with database values but it doesnt.
	                	myChoice(Choices.PAPER);
	                	RPS_Calculate();
	                }
	                else {
	                	databaseReference.child("player_2_choice").setValue("PAPER");
	                	theirChoice(Choices.PAPER);
	                	//RPS_Calculate();
	                }
	                /*
	                    we select Paper
	                */
                }
            }
        });


        final ImageView imgScissors = (ImageView) findViewById(R.id.play_scissors);
        imgScissors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (is_clicked == false) {
	                imgScissors.setBackgroundResource(R.drawable.button_outline);
	                is_clicked = true;
	                if (activePlayer.getUserId().equals(host)){
						databaseReference.child("player_1_choice").setValue("SCISSOR");
						player_1_choice = "SCISSOR"; // theoretically updateDatabase should update the code with database values but it doesnt.
						myChoice(Choices.SCISSOR);
						RPS_Calculate();
	                }
	                else {
	                	databaseReference.child("player_2_choice").setValue("SCISSOR");
	                	theirChoice(Choices.SCISSOR);
	                	//RPS_Calculate();
	                }
	                /*
	                    we select Scissors
	                */
                }
            }
        });


    	databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                /*
                get host
                */
                Object dbObject = dataSnapshot.child("host").getValue();
                if (dbObject != null) {
                    host = dbObject.toString();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
	        });
    	}

        /*
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Object dbObject = dataSnapshot.child("dice_roll").getValue();
                String dbValue = "";

                if(dbObject != null) {
                    dbValue = dbObject.toString();
                }

                if(!dbValue.equals("")) {
                    result.setText(dbValue);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        */

    public void RPS_Calculate() {
    	int loss = -1;
    	int tie = 0;
    	int win = 1;
        if (player_1_choice.equals(player_2_choice)) {
            changeGameState(tie);
        } else {
            switch (player_1_choice) {
                case "ROCK":
                    if (player_2_choice.equals("PAPER")) {
                    	changeGameState(loss);
                    }
                    else if (player_2_choice.equals("SCISSOR")) {
                        changeGameState(win);
                    }
                    break;

                case "PAPER":
                    if (player_2_choice.equals("SCISSOR")) {
                        changeGameState(loss);
                    }
                    else if (player_2_choice.equals("ROCK")) {
                        changeGameState(win);
                    }
                    break;

                case "SCISSOR":
                    if (player_2_choice.equals("ROCK")) {
                        changeGameState(loss);
                    }
                    else if (player_2_choice.equals("PAPER")) {
                        changeGameState(win);
                    }
                    break;
            } }
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

    //function takes a specific status. status dictates who wins/who loses.
    public void changeGameState(int status) {
        ImageView myChoice = findViewById(R.id.my_choice);
        ImageView theirChoice = findViewById(R.id.their_choice);

        switch (status){
            case -1: // i lose, they won
                myChoice.setBackground(getResources().getDrawable(R.drawable.lose_btn_outline));
                theirChoice.setBackground(getResources().getDrawable(R.drawable.win_btn_outline));
                break;
            case 0: // tie
                myChoice.setBackground(getResources().getDrawable(R.drawable.tie_btn_outline));
                theirChoice.setBackground(getResources().getDrawable(R.drawable.tie_btn_outline));
                break;
            case 1: // i win, they lose
                myChoice.setBackground(getResources().getDrawable(R.drawable.win_btn_outline));
                theirChoice.setBackground(getResources().getDrawable(R.drawable.lose_btn_outline));
                break;
            default:
                break;
        }
    }

    //function takes a choice, sets the the bottom box with my choice
    public void myChoice(Choices choice){
        ImageView myChoice = findViewById(R.id.my_choice);
        switch(choice){
            case ROCK: // i choose rock
                myChoice.setImageResource(R.drawable.rock);
                break;
            case PAPER: // i choose paper
                myChoice.setImageResource(R.drawable.paper);
                break;
            case SCISSOR: // i choose scissors
                myChoice.setImageResource(R.drawable.scissors);
                break;
            default:
                break;
        }
    }

    //function takes a choice, sets the the top box with the choice of opponent
    public void theirChoice(Choices choice){
        ImageView theirChoice = (ImageView) findViewById(R.id.their_choice);
        switch(choice){
            case ROCK: // they choose rock
                theirChoice.setImageResource(R.drawable.rock);
                break;
            case PAPER: // they choose paper
                theirChoice.setImageResource(R.drawable.paper);
                break;
            case SCISSOR: // they choose scissors
                theirChoice.setImageResource(R.drawable.scissors);
                break;
            default:
                break;
        }
    }

    // not to be used alone... call loadFromDatabase() instead
	public void getValuesFromDatabase(final DatabaseCallback dbCallback){
		final Session session = currentSession;
		FirebaseDatabase database = FirebaseDatabase.getInstance();
		final DatabaseReference databaseReference = database.getReference().child("Sessions").child(session.getID());

		databaseReference.addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
				// save value of "host" from database to "host" (which is a custom key added to the callback that holds the new value)
					dbCallback.onCallback("host", dataSnapshot.child("host").getValue().toString());
					dbCallback.onCallback("player_1_choice", dataSnapshot.child("player_1_choice").getValue().toString());
                    dbCallback.onCallback("player_2_choice", dataSnapshot.child("player_2_choice").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
	}

	public void loadFromDatabase() {
		getValuesFromDatabase(new DatabaseCallback() {
			@Override
			public void onCallback(String key, String value) {
				switch(key) {
					case "host" :
						host = value;
						break;
                    case "player_1_choice":
                        player_1_choice = value;
                        break;
                    case "player_2_choice":
                        player_2_choice = value;
                        break;
					default:
						break;
				}
			}
		});
	}
    public void updateDatabase() {
        final Session session = currentSession;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = database.getReference().child("Sessions").child(session.getID());

        // host
        if(!host.equals("NULL")) { // "NULL" is the default value stored in intialPushToDatabase()
            databaseReference.child("host").setValue(host);
        }
        if(!player_1_choice.equals("NULL")) { // "NULL" is the default value stored in intialPushToDatabase()
            databaseReference.child("player_1_choice").setValue(player_1_choice);
        }
        if(!player_2_choice.equals("NULL")) { // "NULL" is the default value stored in intialPushToDatabase()
            databaseReference.child("player_2_choice").setValue(player_2_choice);
        }
    }

}