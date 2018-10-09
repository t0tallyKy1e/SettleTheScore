package com.ckm.settlethescore;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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
}
