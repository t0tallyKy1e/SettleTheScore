package com.ckm.settlethescore;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;


import com.firebase.ui.auth.data.model.Resource;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class RocPapSci extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roc_pap_sci);

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
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        int id = menuItem.getItemId();
                        if (id == R.id.nav_dice) {
                            Intent i = new Intent(RocPapSci.this, Dice.class);
                            startActivity(i);
                        } else if (id == R.id.nav_straws) {
                            Intent i = new Intent(RocPapSci.this, DrawStraw.class);
                            startActivity(i);
                        } else if (id == R.id.nav_life) {
                            Intent i = new Intent(RocPapSci.this, Life.class);
                            startActivity(i);
                        } else if (id == R.id.nav_scores) {
                            Intent i = new Intent(RocPapSci.this, ScoreBoard.class);
                            startActivity(i);
                        }else if (id == R.id.nav_home){
                            Intent i = new Intent(RocPapSci.this, MainActivity.class);
                            startActivity(i);
                        }

                        finalDrawer.closeDrawers();
                        return true;
                    }
                });
        Session currentSession = new Session(Game.TYPE.ROCK_PAP_SCI);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = database.getReference().child("Sessions").child(currentSession.getID());


        /*
        Rock = 1; either that or match by ID
        Paper = 2; either that or match by ID
        Scissor = 3; either that or match by ID
        */

 		final ImageView imgRock = (ImageView) findViewById(R.id.play_rock);
 		imgRock.setOnClickListener(new View.OnClickListener() {
 			@Override
 			public void onClick(View v) {
                imgRock.setBackgroundResource(R.drawable.button_outline);
 			    /*

 			    we select rock
 			    if opponent.choice == rock; (tie)
 			        - dictate its a tie!

 			    if opponent.choice == paper; (loss)
 			        - highlight red
 			        - games_elapsed += 1

 			    if opponent.choice == scissors; (win)
 			        - highlight green
 			        - games_elapsed += 1
 			        - user.games_won += 1

		    call game-checks function
		    */
            }
 		});


 		ImageView imgPaper = (ImageView) findViewById(R.id.play_paper);
 		imgPaper.setOnClickListener(new View.OnClickListener() {
 			@Override
 			public void onClick(View v) {
			/*
   				we select Paper
 			    if opponent.choice == rock; (win)
 			        - highlight green
 			        - games_elapsed += 1
 			        - user.games_won += 1

 			    if opponent.choice == paper; (tie)
 			        - dictate its a tie!

 			    if opponent.choice == scissors; (loss)
 			        - highlight red
 			        - games_elapsed += 1

			call game-checks function
			*/
 			}
 		});


 		ImageView imgScissors = (ImageView) findViewById(R.id.play_scissors);
 		imgScissors.setOnClickListener(new View.OnClickListener() {
 			@Override
 			public void onClick(View v) {
			/*
   				we select Scissors
 			    if opponent.choice == rock; (loss)
 			        - highlight red
 			        - games_elapsed += 1

 			    if opponent.choice == paper; (win)
 			        - highlight green
 			        - games_elapsed += 1
 			        - user.games_won += 1

 			    if opponent.choice == scissors; (tie)
 			        - dictate its a tie!

			call game-checks function
			*/
 			}
 		});

    }

    public void changeGameState(Integer status) {
        ImageView myChoice = findViewById(R.id.my_choice);
        ImageView theirChoice = findViewById(R.id.their_choice);

        switch (status){
            case -1:
                myChoice.setBackground(getResources().getDrawable(R.drawable.lose_btn_outline));
                theirChoice.setBackground(getResources().getDrawable(R.drawable.win_btn_outline));
                break;
            case 0:
                myChoice.setBackground(getResources().getDrawable(R.drawable.button_outline));
                theirChoice.setBackground(getResources().getDrawable(R.drawable.button_outline));
                break;
            case 1:
                myChoice.setBackground(getResources().getDrawable(R.drawable.win_btn_outline));
                theirChoice.setBackground(getResources().getDrawable(R.drawable.lose_btn_outline));
                break;
            default:
                break;
        }
    }

    public void myChoice(Integer choice){
        ImageView myChoice = findViewById(R.id.my_choice);
        switch(choice){
            case 1:
                myChoice.setImageResource(R.drawable.rock);
                break;
            case 2:
                myChoice.setImageResource(R.drawable.paper);
                break;
            case 3:
                myChoice.setImageResource(R.drawable.scissors);
                break;
            default:
                break;
        }

    }

    public void theirChoice(Integer choice){
        ImageView theirChoice = (ImageView) findViewById(R.id.their_choice);
        switch(choice){
            case 1:
                theirChoice.setImageResource(R.drawable.rock);
                break;
            case 2:
                theirChoice.setImageResource(R.drawable.paper);
                break;
            case 3:
                theirChoice.setImageResource(R.drawable.scissors);
                break;
            default:
                break;
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
}
