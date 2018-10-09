package com.ckm.settlethescore;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final TextView displayNameField = (TextView) findViewById(R.id.txtRegisterDisplayNameField);
        final TextView fullNameField = (TextView) findViewById(R.id.txtRegisterFullNameField);
        final TextView emailField = (TextView) findViewById(R.id.txtRegisterEmailField);
        final TextView phoneNumberField = (TextView) findViewById(R.id.txtRegisterPhoneNumberField);
        final ImageView profilePhotoField = (ImageView) findViewById(R.id.imgRegisterProfilePhoto);

        Button registerButton = (Button) findViewById(R.id.btnRegisterSubmit);

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        displayNameField.setText(user.getDisplayName());
        fullNameField.setText(user.getDisplayName());
        emailField.setText(user.getEmail());
        phoneNumberField.setText(user.getPhoneNumber());
        // profilePhotoField.setImageIcon(); upload photo button

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference databaseReference = database.getReference().child("Players").child(user.getUid());

                databaseReference.child("display_name").setValue(displayNameField.getText().toString().trim());
                databaseReference.child("full_name").setValue(fullNameField.getText().toString().trim());
                databaseReference.child("email").setValue(emailField.getText().toString().trim());
                databaseReference.child("phone_number").setValue(phoneNumberField.getText().toString().trim());

                Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(mainIntent);

                Toast.makeText(RegisterActivity.this, "User Profile Updated", Toast.LENGTH_SHORT);
            }
        });
    }
}
