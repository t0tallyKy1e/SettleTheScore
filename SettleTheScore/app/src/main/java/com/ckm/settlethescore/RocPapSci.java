package com.ckm.settlethescore;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class RocPapSci extends AppCompatActivity {

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_roc_pap_sci);
//
//        Session currentSession = new Session(Game.TYPE.ROCK_PAP_SCI);
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        final DatabaseReference databaseReference = database.getReference().child("Sessions").child(currentSession.getID());
//
//        /*
//        Rock = 1; either that or match by ID
//        Paper = 2; either that or match by ID
//        Scissor = 3; either that or match by ID
//        */
//
// 		final ImageView imgRock = (ImageView) findViewById(R.id.image5);
// 		imgRock.setOnClickListener(new View.OnClickListener() {
// 			@Override
// 			public void onClick(View v) {
//                imgRock.setBackgroundResource(R.drawable.button_outline);
// 			    /*
//
// 			    we select rock
// 			    if opponent.choice == rock; (tie)
// 			        - dictate its a tie!
//
// 			    if opponent.choice == paper; (loss)
// 			        - highlight red
// 			        - games_elapsed += 1
//
// 			    if opponent.choice == scissors; (win)
// 			        - highlight green
// 			        - games_elapsed += 1
// 			        - user.games_won += 1
//
//		    call game-checks function
//		    */
//            }
// 		});
//
// 		ImageView imgPaper = (ImageView) findViewById(R.id.image6);
// 		imgPaper.setOnClickListener(new View.OnClickListener() {
// 			@Override
// 			public void onClick(View v) {
//			/*
//   				we select Paper
// 			    if opponent.choice == rock; (win)
// 			        - highlight green
// 			        - games_elapsed += 1
// 			        - user.games_won += 1
//
// 			    if opponent.choice == paper; (tie)
// 			        - dictate its a tie!
//
// 			    if opponent.choice == scissors; (loss)
// 			        - highlight red
// 			        - games_elapsed += 1
//
//			call game-checks function
//			*/
// 			}
// 		});
//
// 		ImageView imgScissors = (ImageView) findViewById(R.id.image4);
// 		imgScissors.setOnClickListener(new View.OnClickListener() {
// 			@Override
// 			public void onClick(View v) {
//			/*
//   				we select Scissors
// 			    if opponent.choice == rock; (loss)
// 			        - highlight red
// 			        - games_elapsed += 1
//
// 			    if opponent.choice == paper; (win)
// 			        - highlight green
// 			        - games_elapsed += 1
// 			        - user.games_won += 1
//
// 			    if opponent.choice == scissors; (tie)
// 			        - dictate its a tie!
//
//			call game-checks function
//			*/
// 			}
// 		});
//
//    }
//    /*
//    single instance; do you want to play again?
//        - popup play_again.xml
//            - n = quit RPS, goto main menu
//            - y =
//                - if opponent.answer = y
//                    - reset fields
//
//    function checks winner of game
//		- if games == rounds set
//			- if user.games_won > opponent.games_won
//				- user wins
//			- else
//				- user loses
//    */
}
