package com.ckm.settlethescore;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
