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

        setContentView(R.layout.activity_startup);
        Button btnSignIn = (Button) findViewById(R.id.btnSignIn);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = new Intent(StartupActivity.this, SignInActivity.class);
                startActivity(signInIntent);
            }
        });

    }

    @Override
    public void onBackPressed() {
        // Don't allow user to press back if the last activity was the register / sign-in process
        // Send back to startup activity or do nothing
        super.onBackPressed();
    }
}
