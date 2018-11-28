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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.firebase.ui.auth.data.model.Resource;

public class RocPapSci extends AppCompatActivity {
    /*
    enum Choices {ROCK, PAPER, SCISSOR}
    enum Status {LOSS, TIE, WIN}
    public class Players {
        Choices choice;
        Status status;
    }

    void RPS_WinLoss(Players host, Players guest) {
        if (host.choice == guest.choice) {
            host.status = Status.TIE;
            guest.status = Status.TIE;
        } else {
            switch (host.choice) {
                case ROCK:
                    if (guest.choice == Choices.PAPER) {
                        host.status = Status.LOSS;
                        guest.status = Status.WIN;
                    }
                    if (guest.choice == Choices.SCISSOR) {
                        host.status = Status.WIN;
                        guest.status = Status.LOSS;
                    }
                    break;

                case PAPER:
                    if (guest.choice == Choices.ROCK) {
                        host.status = Status.WIN;
                        guest.status = Status.LOSS;
                    }
                    if (guest.choice == Choices.SCISSOR) {
                        host.status = Status.LOSS;
                        guest.status = Status.WIN;
                    }
                    break;

                case SCISSOR:
                    if (guest.choice == Choices.ROCK) {
                        host.status = Status.LOSS;
                        guest.status = Status.WIN;
                    }
                    if (guest.choice == Choices.PAPER) {
                        host.status = Status.WIN;
                        guest.status = Status.LOSS;
                    }
                    break;
            } }
    }*/

    private Player activePlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent oldIntent = getIntent();
        final Player activePlayer = Player.getPlayerFromLastActivity(oldIntent);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roc_pap_sci);
        final TextView result = findViewById(R.id.result);

        final EditText addPlayerField = (EditText) findViewById(R.id.add_email);

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
                  
        Session currentSession = new Session(Game.TYPE.ROCK_PAP_SCI);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = database.getReference().child("Sessions").child(currentSession.getID());

        final Session currentSession = new Session(Game.TYPE.ROCK_PAP_SCI);

//        activePlayer.addGame(currentSession.getID());

//        currentSession.addPlayer(activePlayer.getUserId());
//        currentSession.addHost(activePlayer.getUserId());

        /*
        dictate creator of the game as host
        dictate invited of game as guest
        */

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int addAsFriend = 0;
                int addToGame = 1;
//                activePlayer.addPlayerByEmail(addPlayerField.getText().toString(), addToGame, currentSession.getID());
            }
        });

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = database.getReference().child("Sessions").child(currentSession.getID());

        final ImageView imgRock = (ImageView) findViewById(R.id.play_rock);
        imgRock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgRock.setBackgroundResource(R.drawable.button_outline);
                databaseReference.child("player_1_choice").setValue("rock");
                /*
                    we select rock
                */

            }
        });

        final ImageView imgPaper = (ImageView) findViewById(R.id.play_paper);
        imgPaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgPaper.setBackgroundResource(R.drawable.button_outline);
                /*
                    we select Paper
                */
            }
        });


        final ImageView imgScissors = (ImageView) findViewById(R.id.play_scissors);
        imgScissors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgScissors.setBackgroundResource(R.drawable.button_outline);
                /*
                    we select Scissors
                */
            }
        });

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
    }

    //function takes a specific status. status dictates who wins/who loses.
    public void changeGameState(Integer status) {
        ImageView myChoice = findViewById(R.id.my_choice);
        ImageView theirChoice = findViewById(R.id.their_choice);

        switch (status){
            case -1: // i lose, they won
                myChoice.setBackground(getResources().getDrawable(R.drawable.lose_btn_outline));
                theirChoice.setBackground(getResources().getDrawable(R.drawable.win_btn_outline));
                break;
            case 0: // tie
                myChoice.setBackground(getResources().getDrawable(R.drawable.button_outline));
                theirChoice.setBackground(getResources().getDrawable(R.drawable.button_outline));
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
    public void myChoice(Integer choice){
        ImageView myChoice = findViewById(R.id.my_choice);
        switch(choice){
            case 1: // i choose rock
                myChoice.setImageResource(R.drawable.rock);
                break;
            case 2: // i choose paper
                myChoice.setImageResource(R.drawable.paper);
                break;
            case 3: // i choose scissors
                myChoice.setImageResource(R.drawable.scissors);
                break;
            default:
                break;
        }

    }

    //function takes a choice, sets the the top box with the choice of opponent
    public void theirChoice(Integer choice){
        ImageView theirChoice = (ImageView) findViewById(R.id.their_choice);
        switch(choice){
            case 1: // they choose rock
                theirChoice.setImageResource(R.drawable.rock);
                break;
            case 2: // they choose paper
                theirChoice.setImageResource(R.drawable.paper);
                break;
            case 3: // they choose scissors
                theirChoice.setImageResource(R.drawable.scissors);
                break;
            default:
                break;
        }

    }

}
/*
single instance; do you want to play again?
    - popup play_again.xml
        - n = quit RPS, goto main menu
        - y =
            - if opponent.answer = y
                - reset fields

function checks winner of game
    - if games == rounds set
        - if user.games_won > opponent.games_won
            - user wins
        - else
            - user loses
*/
