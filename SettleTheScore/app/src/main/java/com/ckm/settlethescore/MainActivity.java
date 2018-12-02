package com.ckm.settlethescore;

import java.util.ArrayList;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import android.content.Intent;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private FirebaseUser firebaseUser;

    private Player activePlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        Intent i = new Intent(MainActivity.this, MainActivity.class);
                        int id = menuItem.getItemId();
                        if (id == R.id.nav_dice) {
                            i = new Intent(MainActivity.this, Dice.class);
                            startActivity(i);
                        } else if (id == R.id.nav_straws) {
                            i = new Intent(MainActivity.this, DrawStraw.class);
                            startActivity(i);
                        } else if (id == R.id.nav_life) {
                            i = new Intent(MainActivity.this, Life.class);
                            startActivity(i);
                        } else if (id == R.id.nav_scores) {
                            i = new Intent(MainActivity.this, ScoreBoard.class);
                            startActivity(i);
                        } else if (id == R.id.nav_rps) {
                            i = new Intent(MainActivity.this, RocPapSci.class);
                            startActivity(i);
                        }else if (id == R.id.nav_home){
                            i = new Intent(MainActivity.this, MainActivity.class);
                            startActivity(i);
                        }
                        activePlayer.sendPlayerToNextActivity(i);
                        startActivity(i);
                        finalDrawer.closeDrawers();
                        return true;
                    }
            }
        );


        // get current firebase user and their ID
        FirebaseAuth firebaseAuthentication = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuthentication.getCurrentUser();
        final String userID = firebaseUser.getUid();
        activePlayer = new Player(userID);

        TextView txtView = navigationView.getHeaderView(0).findViewById(R.id.txtDrawerUserName);
        txtView.setText(firebaseUser.getDisplayName());

        // make sure user is valid
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = database.getReference().child("Players").child(userID);
        firebaseAuthentication.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseUser == null) {
                    // User somehow got here without signing in... make sure they sign in
                    Intent startupIntent = new Intent(getApplicationContext(), StartupActivity.class);
                    startActivity(startupIntent);
                } else {
                    activePlayer.update();
                    activePlayer.setIsConnected("1");
                }
            }
        });

        ImageView diceGameButton = findViewById(R.id.dice_game_btn);
        diceGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent diceGameIntent = new Intent(MainActivity.this, Dice.class);
                activePlayer.sendPlayerToNextActivity(diceGameIntent);
                startActivity(diceGameIntent);
            }
        });

        ImageView lifeGameButton = findViewById(R.id.life_game_btn);
        lifeGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent lifeGameIntent = new Intent(MainActivity.this, Life.class);
                activePlayer.sendPlayerToNextActivity(lifeGameIntent);
                startActivity(lifeGameIntent);
            }
        });

        ImageView strawsGameButton = findViewById(R.id.straw_game_btn);
        strawsGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent strawsGameIntent = new Intent(MainActivity.this, DrawStraw.class);
                activePlayer.sendPlayerToNextActivity(strawsGameIntent);
                startActivity(strawsGameIntent);
            }
        });

        ImageView rpsGameButton = findViewById(R.id.rps_game_btn);
        rpsGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent rpsGameIntent = new Intent(MainActivity.this, RocPapSci.class);
                activePlayer.sendPlayerToNextActivity(rpsGameIntent);
                startActivity(rpsGameIntent);
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // test out opening a previous session
//                Intent nextIntent = new Intent(MainActivity.this, Dice.class);
//                activePlayer.sendPlayerToNextActivity(nextIntent);
//                Session.sendPreviousSessionToActivity(nextIntent, "-LSMRlRcZyXsZG88D_Mp");
//                startActivity(nextIntent);
            }
        });

//        // test of getting list of all games
//        final FirebaseDatabase database = FirebaseDatabase.getInstance();
//        final DatabaseReference databaseReference = database.getReference().child("Sessions");
//
//        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for(DataSnapshot ds : dataSnapshot.getChildren()) {
//                    // change value of field to -> ds.getValue().toString();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                // nada
//            }
//        });


    }

    // everything below is default code
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent diceGameIntent = new Intent(MainActivity.this, Dice.class);
            startActivity(diceGameIntent);
        }

        return super.onOptionsItemSelected(item);
    }



}
