package com.ckm.settlethescore;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DrawStraw extends AppCompatActivity {
    private FirebaseUser firebaseUser;

    private Player activePlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_straw);

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
                        Intent i = new Intent(DrawStraw.this, DrawStraw.class);
                        int id = menuItem.getItemId();
                        if (id == R.id.nav_dice) {
                            i = new Intent(DrawStraw.this, Dice.class);
                        } else if (id == R.id.nav_life) {
                            i = new Intent(DrawStraw.this, Life.class);
                        } else if (id == R.id.nav_scores) {
                            i = new Intent(DrawStraw.this, ScoreBoard.class);
                        } else if (id == R.id.nav_rps) {
                            i = new Intent(DrawStraw.this, RocPapSci.class);
                        }else if (id == R.id.nav_home){
                            i = new Intent(DrawStraw.this, MainActivity.class);
                        }
                        activePlayer.sendPlayerToNextActivity(i);
                        startActivity(i);
                        finalDrawer.closeDrawers();
                        return true;
                    }
            });
        // get current firebase user and their ID
        FirebaseAuth firebaseAuthentication = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuthentication.getCurrentUser();
        final String userID = firebaseUser.getUid();
        activePlayer = new Player(userID);
    }
}
