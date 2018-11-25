package com.ckm.settlethescore;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
// import com.firebase.ui.auth.IdpResponse; // - Unused
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.List;

public class SignInActivity extends AppCompatActivity {
    private int RC_SIGN_IN = 1;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RC_SIGN_IN) {
            if(resultCode == RESULT_OK) {
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                final DatabaseReference databaseReference = database.getReference().child("Players").child(user.getUid());

                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        Object snapshotData = snapshot.getValue();
                        boolean needsNewAccount = false;

                        if(snapshotData == null) {
                            needsNewAccount = true;
                        }

                        if (!needsNewAccount){
                            // update online status
                            databaseReference.child("status").setValue(1); // 1 = online

                            // successful sign-in > user exists > go to main activity
                            Intent mainIntent = new Intent(SignInActivity.this, MainActivity.class);
                            startActivity(mainIntent);

                            // notify user of sign-in
                            Toast.makeText(SignInActivity.this, "Successfully Logged In", Toast.LENGTH_SHORT).show();
                        } else {
                            // push all given info to table... send to register activity pre-filled with
                            databaseReference.child("display_name").setValue(user.getDisplayName());
                            databaseReference.child("full_name").setValue(user.getDisplayName());
                            databaseReference.child("email").setValue(user.getEmail());
                            databaseReference.child("user_id").setValue(user.getUid());
                            databaseReference.child("status").setValue(1); // 1 = online ... is_connected in player object

                            // successful sign-in > new user > go to register activity
                            Intent registerIntent = new Intent(SignInActivity.this, RegisterActivity.class);
                            startActivity(registerIntent);

                            // notify user of sign-up
                            Toast.makeText(SignInActivity.this, "New Account Created", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // unused
                    }
                });
            } else {
                Toast.makeText(this, "Sign In Failed", Toast.LENGTH_SHORT).show();
                Intent cancelledSignInIntent = new Intent(this, StartupActivity.class);
                startActivity(cancelledSignInIntent);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        createSignInIntent();
    }

    public void createSignInIntent() {
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());

        Intent signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setTheme(R.style.LoginTheme)
                .setLogo(R.drawable.sts_logo)
                .build();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
}
