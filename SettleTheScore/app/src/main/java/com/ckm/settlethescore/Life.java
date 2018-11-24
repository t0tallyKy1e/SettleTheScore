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

public class Life extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        int id = menuItem.getItemId();
                        if (id == R.id.nav_dice) {
                            Intent i = new Intent(Life.this, Dice.class);
                            startActivity(i);
                        } else if (id == R.id.nav_straws) {
                            Intent i = new Intent(Life.this, DrawStraw.class);
                            startActivity(i);
                        } else if (id == R.id.nav_rps) {
                            Intent i = new Intent(Life.this, RocPapSci.class);
                            startActivity(i);
                        } else if (id == R.id.nav_home){
                            Intent i = new Intent(Life.this, MainActivity.class);
                            startActivity(i);
                        }

                        finalDrawer.closeDrawers();
                        return true;
                    }
                });
    }
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
