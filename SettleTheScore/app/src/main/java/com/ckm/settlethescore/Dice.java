package com.ckm.settlethescore;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;


import java.util.Random;

public class Dice extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dice);

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
                        if (id == R.id.nav_straws) {
                            Intent i = new Intent(Dice.this, DrawStraw.class);
                            startActivity(i);
                        } else if (id == R.id.nav_life) {
                            Intent i = new Intent(Dice.this, Life.class);
                            startActivity(i);
                        } else if (id == R.id.nav_rps) {
                            Intent i = new Intent(Dice.this, RocPapSci.class);
                            startActivity(i);
                        } else if (id == R.id.nav_home){
                            Intent i = new Intent(Dice.this, MainActivity.class);
                            startActivity(i);
                        }

                        finalDrawer.closeDrawers();
                        return true;
                    }
                });



        ImageButton imageButton = findViewById(R.id.roll_two);
        imageButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                TextView result = findViewById(R.id.result);
                Integer value = (int)(Math.random() * 2) + 1;
                result.setText(value.toString());
            }

        });

        imageButton = findViewById(R.id.roll_four);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView result = findViewById(R.id.result);
                Integer value = (int)(Math.random() * 4) + 1;
                result.setText(value.toString());
            }
        });

        imageButton = findViewById(R.id.roll_six);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView result = findViewById(R.id.result);
                Integer value = (int)(Math.random() * 6) + 1;
                result.setText(value.toString());
            }
        });

        imageButton = findViewById(R.id.roll_eight);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView result = findViewById(R.id.result);
                Integer value = (int)(Math.random() * 8) + 1;
                result.setText(value.toString());
            }
        });

        imageButton = findViewById(R.id.roll_ten);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView result = findViewById(R.id.result);
                Integer value = (int)(Math.random() * 10) + 1;
                result.setText(value.toString());
            }
        });

        imageButton = findViewById(R.id.roll_twelve);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView result = findViewById(R.id.result);
                Integer value = (int)(Math.random() * 12) + 1;
                result.setText(value.toString());
            }
        });

        imageButton = findViewById(R.id.roll_twenty);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView result = findViewById(R.id.result);
                Integer value = (int)(Math.random() * 20) + 1;
                result.setText(value.toString());
            }
        });

        findViewById(R.id.n_roll_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText n_edit = findViewById(R.id.n_text_edit);

                if(n_edit != null){
                    Integer n = Integer.parseInt(n_edit.getText().toString());
                    TextView result = findViewById(R.id.result);
                    Integer value = (int)(Math.random() * n) + 1;
                    result.setText(value.toString());
                }
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
