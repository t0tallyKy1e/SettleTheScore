package com.ckm.settlethescore;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import android.content.Intent;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.io.File;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
        private FirebaseUser firebaseUser;

    private Player activePlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Start of default code
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            final NavigationView navView = (NavigationView) findViewById(R.id.nav_view);
            final View headerView = navView.getHeaderView(0);
            final ImageView imgDrawerProfilePicture = (ImageView) headerView.findViewById(R.id.imgDrawerProfilePicture);

            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){
                public void onDrawerOpened(View drawerView) {
                    super.onDrawerOpened(drawerView);
                    // get drawer fields
                    // -- need headerView.findViewByID() when working with drawers
                    final TextView drawerDisplayName = (TextView) headerView.findViewById(R.id.txtDrawerUserName);
                    final TextView drawerEmail = (TextView) headerView.findViewById(R.id.txtDrawerEmail);
                    final ImageView imgDrawerProfilePicture = (ImageView) headerView.findViewById(R.id.imgDrawerProfilePicture);

                    // set text in drawer fields
                    drawerDisplayName.setText(activePlayer.getDisplayName().toString().trim());
                    drawerEmail.setText(activePlayer.getEmail().toString().trim());
                }
            };
            drawer.addDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);
        // end of default code

        // get current firebase user and their ID
        FirebaseAuth firebaseAuthentication = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuthentication.getCurrentUser();
        final String userID = firebaseUser.getUid();

        // get reference to user's profile picture
        final ImageView imgProfilePictureField = (ImageView) findViewById(R.id.imgProfilePicture);
        StorageReference profilePhotoReference = FirebaseStorage.getInstance().getReference("profile_pictures/" + userID);

        // make sure user is valid
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = database.getReference().child("Players").child(userID);
        firebaseAuthentication.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseUser == null) {
                    // User somehow got here without signing in... make sure they sign in
                    findViewById(R.id.txtNotSignedIn).setVisibility(View.VISIBLE);
                    findViewById(R.id.txtSignedIn).setVisibility(View.INVISIBLE);
                    Intent startupIntent = new Intent(getApplicationContext(), StartupActivity.class);
                    startActivity(startupIntent);
                } else {
                    activePlayer = Player.generatePlayerFromFirebaseUser(firebaseUser); // used in register activity

                    findViewById(R.id.txtNotSignedIn).setVisibility(View.INVISIBLE);
                    findViewById(R.id.txtSignedIn).setVisibility(View.VISIBLE);
                }
            }
        });

        // display user's info
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // get fields from layout
                TextView txtUserNameDisplay = (TextView) findViewById(R.id.txtUserNameDisplay);
                TextView txtUserEmailDisplay = (TextView) findViewById(R.id.txtUserEmailDisplay);
                TextView txtUserPhoneDisplay = (TextView) findViewById(R.id.txtUserPhoneDisplay);
                TextView txtUserIDDisplay = (TextView) findViewById(R.id.txtUserIDDisplay);

                // get info from database and set values in Player object
                activePlayer.setUserId(dataSnapshot.child("user_id").getValue().toString().trim());
                activePlayer.setPhoneNumber(dataSnapshot.child("phone_number").getValue().toString().trim());
                activePlayer.setDisplayName(dataSnapshot.child("display_name").getValue().toString().trim());
                activePlayer.setEmail(dataSnapshot.child("email").getValue().toString().trim());

                // set text in fields
                txtUserNameDisplay.setText(activePlayer.getDisplayName().toString().trim());
                txtUserEmailDisplay.setText(activePlayer.getEmail().toString().trim());
                txtUserPhoneDisplay.setText(activePlayer.getPhoneNumber().toString().trim());
                txtUserIDDisplay.setText(activePlayer.getUserId().toString().trim());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // unused
            }
        });

        // download profile picture
        try {
            final File profilePictureDownload = File.createTempFile("profile_photos", userID.toString());
            FileDownloadTask profilePictureDownloadTask = profilePhotoReference.getFile(profilePictureDownload);

            profilePictureDownloadTask.addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    imgProfilePictureField.setImageURI(Uri.fromFile(profilePictureDownload));
                    imgDrawerProfilePicture.setImageURI(Uri.fromFile(profilePictureDownload));
                }
            });

        } catch (Exception e) {
            Toast.makeText(MainActivity.this, "ERROR: " + e.toString(), Toast.LENGTH_SHORT).show();
        }

        // sign out and remove current active player
        Button btnSignOut = findViewById(R.id.btnSignOut);
        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference databaseReference = database.getReference().child("Players").child(user.getUid());

                databaseReference.child("status").setValue(0);

                AuthUI.getInstance().signOut(MainActivity.this);

                Intent signOutIntent = new Intent(MainActivity.this, StartupActivity.class);
                startActivity(signOutIntent);
            }
        });
    }


    // everything below is default code
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
