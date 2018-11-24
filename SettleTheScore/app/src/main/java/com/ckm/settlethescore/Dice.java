package com.ckm.settlethescore;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

public class Dice extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent oldIntent = getIntent();
        Player activePlayer = Player.getPlayerFromLastActivity(oldIntent);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dice);
        final TextView result = findViewById(R.id.result);

        Session currentSession = new Session(Game.TYPE.DICE);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = database.getReference().child("Sessions").child(currentSession.getID());

        ImageButton imageButton = findViewById(R.id.roll_two);
        imageButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Integer value = (int)(Math.random() * 2) + 1;
                result.setText(value.toString());
            }

        });

        imageButton = findViewById(R.id.roll_four);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer value = (int)(Math.random() * 4) + 1;
                result.setText(value.toString());
            }
        });

        imageButton = findViewById(R.id.roll_six);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer value = (int)(Math.random() * 6) + 1;
                result.setText(value.toString());
            }
        });

        imageButton = findViewById(R.id.roll_eight);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer value = (int)(Math.random() * 8) + 1;
                result.setText(value.toString());
            }
        });

        imageButton = findViewById(R.id.roll_ten);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer value = (int)(Math.random() * 10) + 1;
                result.setText(value.toString());
            }
        });

        imageButton = findViewById(R.id.roll_twelve);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer value = (int)(Math.random() * 12) + 1;
                result.setText(value.toString());
            }
        });

        imageButton = findViewById(R.id.roll_twenty);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                    Integer value = (int)(Math.random() * n) + 1;
                    result.setText(value.toString());
                }
            }
        });

        result.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                databaseReference.child("dice_roll").setValue(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

}
