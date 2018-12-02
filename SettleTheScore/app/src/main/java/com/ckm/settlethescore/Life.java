package com.ckm.settlethescore;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Life extends AppCompatActivity {
    private FirebaseUser firebaseUser;

    private Player activePlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent oldIntent = getIntent();
        final Player activePlayer = Player.getPlayerFromLastActivity(oldIntent);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_life);

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
                        Intent i = new Intent(Life.this, Life.class);
                        int id = menuItem.getItemId();
                        boolean shouldStayOnCurrentActivity = false;
                        if (id == R.id.nav_dice) {
                            i = new Intent(Life.this, Dice.class);
                        } else if (id == R.id.nav_straws) {
                            i = new Intent(Life.this, DrawStraw.class);
                        } else if (id == R.id.nav_scores) {
                            i = new Intent(Life.this, ScoreBoard.class);
                        } else if (id == R.id.nav_home){
                            i = new Intent(Life.this, MainActivity.class);
                        } else if (id == R.id.nav_rps) {
                            i = new Intent(Life.this, RocPapSci.class);
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

        // get current firebase user and their ID
        FirebaseAuth firebaseAuthentication = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuthentication.getCurrentUser();
        final String userID = firebaseUser.getUid();

        Button p1increment = findViewById(R.id.player_one_inc_life);
        p1increment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText p1LifeValue = findViewById(R.id.player_one_life);
                Integer health = Integer.parseInt(p1LifeValue.getText().toString().trim());
                health = health + 1;
                p1LifeValue.setText(health.toString());
            }
        });

        Button p1decrement = findViewById(R.id.player_one_dec_life);
        p1decrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText p1LifeValue = findViewById(R.id.player_one_life);
                Integer health = Integer.parseInt(p1LifeValue.getText().toString().trim());
                health = health - 1;
                p1LifeValue.setText(health.toString());
            }
        });

        Button p2increment = findViewById(R.id.player_two_inc_life);
        p2increment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText p2LifeValue = findViewById(R.id.player_two_life);
                Integer health = Integer.parseInt(p2LifeValue.getText().toString().trim());
                health = health + 1;
                p2LifeValue.setText(health.toString());
            }
        });

        Button p2decrement = findViewById(R.id.player_two_dec_life);
        p2decrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText p2LifeValue = findViewById(R.id.player_two_life);
                Integer health = Integer.parseInt(p2LifeValue.getText().toString().trim());
                health = health - 1;
                p2LifeValue.setText(health.toString());
            }
        });
    }
}
