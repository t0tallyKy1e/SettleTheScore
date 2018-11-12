package com.ckm.settlethescore;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Random;

public class StartupActivity extends AppCompatActivity {
    /*
        ____________________
       |                    |
       |                    |
       |                    |
       |                    |
       |                    |
       |                    |
       |                    |
       |                    |
       |                    |
       |                    |
       |                    |
       |                    |
       |____________________|

        ->

     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_roc_pap_sci);
//        Button btnSignIn = (Button) findViewById(R.id.btnSignIn);
//
//        btnSignIn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent signInIntent = new Intent(StartupActivity.this, SignInActivity.class);
//                startActivity(signInIntent);
//            }
//        });
//
//        ImageButton imageButton = findViewById(R.id.roll_two);
//        imageButton.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View arg0) {
//                TextView result = findViewById(R.id.result);
//                Integer value = (int)(Math.random() * 2) + 1;
//                result.setText(value.toString());
//            }
//
//        });
//
//        imageButton = findViewById(R.id.roll_four);
//        imageButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                TextView result = findViewById(R.id.result);
//                Integer value = (int)(Math.random() * 4) + 1;
//                result.setText(value.toString());
//            }
//        });
//
//        imageButton = findViewById(R.id.roll_six);
//        imageButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                TextView result = findViewById(R.id.result);
//                Integer value = (int)(Math.random() * 6) + 1;
//                result.setText(value.toString());
//            }
//        });
//
//        imageButton = findViewById(R.id.roll_eight);
//        imageButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                TextView result = findViewById(R.id.result);
//                Integer value = (int)(Math.random() * 8) + 1;
//                result.setText(value.toString());
//            }
//        });
//
//        imageButton = findViewById(R.id.roll_ten);
//        imageButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                TextView result = findViewById(R.id.result);
//                Integer value = (int)(Math.random() * 10) + 1;
//                result.setText(value.toString());
//            }
//        });
//
//        imageButton = findViewById(R.id.roll_twelve);
//        imageButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                TextView result = findViewById(R.id.result);
//                Integer value = (int)(Math.random() * 12) + 1;
//                result.setText(value.toString());
//            }
//        });
//
//        imageButton = findViewById(R.id.roll_twenty);
//        imageButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                TextView result = findViewById(R.id.result);
//                Integer value = (int)(Math.random() * 20) + 1;
//                result.setText(value.toString());
//            }
//        });
//
//        findViewById(R.id.n_roll_btn).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                EditText n_edit = findViewById(R.id.n_text_edit);
//
//                if(n_edit != null){
//                    Integer n = Integer.parseInt(n_edit.getText().toString());
//                    TextView result = findViewById(R.id.result);
//                    Integer value = (int)(Math.random() * n) + 1;
//                    result.setText(value.toString());
//                }
//            }
//        });


    }

    @Override
    public void onBackPressed() {
        // Don't allow user to press back if the last activity was the register / sign-in process
        // Send back to startup activity or do nothing
        super.onBackPressed();
    }
}
