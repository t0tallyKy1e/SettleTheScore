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
import android.widget.ImageButton;
import android.widget.TextView;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Dice extends AppCompatActivity {

    private Player activePlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent oldIntent = getIntent();
        activePlayer = Player.getPlayerFromLastActivity(oldIntent);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dice);
        final TextView result = findViewById(R.id.result);

        final EditText addPlayerField = findViewById(R.id.add_email);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
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
                        Intent i = new Intent(Dice.this, Dice.class);
                        int id = menuItem.getItemId();
                        boolean shouldStayOnCurrentActivity = false;
                        if (id == R.id.nav_straws) {
                            i = new Intent(Dice.this, DrawStraw.class);
                        } else if (id == R.id.nav_life) {
                            i = new Intent(Dice.this, Life.class);
                        } else if (id == R.id.nav_scores) {
                            i = new Intent(Dice.this, ScoreBoard.class);
                        } else if (id == R.id.nav_home){
                            i = new Intent(Dice.this, MainActivity.class);
                        } else if (id == R.id.nav_rps) {
                            i = new Intent(Dice.this, RocPapSci.class);
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
            Session currentSession = new Session();

            currentSession = Session.getSessionFromLastActivity(oldIntent);

            if(currentSession == null) {
                currentSession = new Session(Game.TYPE.DICE);
            } else {
                currentSession.gameType = Game.TYPE.DICE;
            }

            // add session to player's games
            activePlayer.addGame(currentSession.getID());
            // add player to session
            currentSession.addPlayer(activePlayer.getUserId());
            currentSession.addHost(activePlayer.getUserId());
            currentSession.updateDatabase();


        final Session finalCurrentSession = currentSession;

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int addAsFriend = 0;
                int addToGame = 1;
                activePlayer.addPlayerByEmail(addPlayerField.getText().toString(), addToGame, finalCurrentSession.getID());
            }
        });

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = database.getReference().child("Sessions").child(finalCurrentSession.getID());


        ImageButton imageButton = findViewById(R.id.roll_two);
        imageButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Integer value = (int)(Math.random() * 2) + 1;
                databaseReference.child("dice_roll").setValue(value);
            }

        });

        imageButton = findViewById(R.id.roll_four);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer value = (int)(Math.random() * 4) + 1;
                databaseReference.child("dice_roll").setValue(value);
            }
        });

        imageButton = findViewById(R.id.roll_six);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer value = (int)(Math.random() * 6) + 1;
                databaseReference.child("dice_roll").setValue(value);
            }
        });

        imageButton = findViewById(R.id.roll_eight);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer value = (int)(Math.random() * 8) + 1;
                databaseReference.child("dice_roll").setValue(value);
            }
        });

        imageButton = findViewById(R.id.roll_ten);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer value = (int)(Math.random() * 10) + 1;
                databaseReference.child("dice_roll").setValue(value);
            }
        });

        imageButton = findViewById(R.id.roll_twelve);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer value = (int)(Math.random() * 12) + 1;
                databaseReference.child("dice_roll").setValue(value);
            }
        });

        imageButton = findViewById(R.id.roll_twenty);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer value = (int)(Math.random() * 20) + 1;
                databaseReference.child("dice_roll").setValue(value);
            }
        });

        findViewById(R.id.n_roll_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText n_edit = findViewById(R.id.n_text_edit);

                if(n_edit != null){
                    Integer n = Integer.parseInt(n_edit.getText().toString());
                    Integer value = (int)(Math.random() * n) + 1;
                    databaseReference.child("dice_roll").setValue(value);
                }
            }
        });

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
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            Intent diceGameIntent = new Intent(Dice.this, Dice.class);
//            startActivity(diceGameIntent);
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}
