package com.ckm.settlethescore;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


enum Choices {ROCK, PAPER, SCISSOR}
enum Status {LOSS, TIE, WIN}
public class Players {
	Choices choice;
	Status status;
}

void RPS_WinLoss(Players host, Players guest) {
	if (host.choice == guest.choice) {
		host.status = status.TIE;
		guest.status = status.TIE;
	} else {
		switch (host.choice) {
			case ROCK:
				if (guest.choice == choice.PAPER) {
					host.status = status.LOSS;
					guest.status = status.WIN;
				}
				if (guest.choice == choice.SCISSOR) {
					host.status = status.WIN;
					guest.status = status.LOSS;	
				}
				break;

			case PAPER:
				if (guest.choice == choice.ROCK) {
					host.status = status.WIN;
					guest.status = status.LOSS;	
				}
				if (guest.choice == choice.SCISSOR) {
					host.status = status.LOSS;
					guest.status = status.WIN;
				}
				break;

			case SCISSOR:
				if (guest.choice == choice.ROCK) {
					host.status = status.LOSS;
					guest.status = status.WIN;
				}
				if (guest.choice == choice.PAPER) {
					host.status = status.WIN;
					guest.status = status.LOSS;	
				}
				break;
	} }
}


public class RocPapSci extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roc_pap_sci);

        Session currentSession = new Session(Game.TYPE.ROCK_PAP_SCI);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = database.getReference().child("Sessions").child(currentSession.getID());

        /*
        dictate creator of the game as host
        dictate invited of game as guest
        */

 		final ImageView imgRock = (ImageView) findViewById(R.id.imageView5);
 		imgRock.setOnClickListener(new View.OnClickListener() {
 			@Override
 			public void onClick(View v) {
                imgRock.setBackgroundResource(R.drawable.button_outline);
				databaseReference.child("player_1_choice").setValue("rock");
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
 		
 		ImageView imgPaper = (ImageView) findViewById(R.id.imageView6);
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
 		
 		ImageView imgScissors = (ImageView) findViewById(R.id.imageView4);
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
