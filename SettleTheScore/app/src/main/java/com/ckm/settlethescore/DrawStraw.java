package com.ckm.settlethescore;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

public class DrawStraw extends AppCompatActivity {

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
                        int id = menuItem.getItemId();
                        if (id == R.id.nav_dice) {
                            Intent i = new Intent(DrawStraw.this, Dice.class);
                            startActivity(i);
                        } else if (id == R.id.nav_life) {
                            Intent i = new Intent(DrawStraw.this, Life.class);
                            startActivity(i);
                        } else if (id == R.id.nav_rps) {
                            Intent i = new Intent(DrawStraw.this, RocPapSci.class);
                            startActivity(i);
                        } else if (id == R.id.nav_home){
                            Intent i = new Intent(DrawStraw.this, MainActivity.class);
                            startActivity(i);
                        }

                        finalDrawer.closeDrawers();
                        return true;
                    }
                });
    }
}
