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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // get current firebase user and their ID
        FirebaseAuth firebaseAuthentication = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuthentication.getCurrentUser();
        final String userID = firebaseUser.getUid();
        activePlayer = Player.generatePlayerFromFirebaseUser(firebaseUser);

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
                    activePlayer.updatePlayerFromDatabase(userID);

                    Log.e("CKM", activePlayer.toString());

                    Toast.makeText(MainActivity.this, "Signed in as " + activePlayer.getDisplayName(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        ImageView diceGameButton = findViewById(R.id.dice_game_btn);
        diceGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent diceGameIntent = new Intent(MainActivity.this, Dice.class);
                startActivity(diceGameIntent);
            }
        });

        ImageView lifeGameButton = findViewById(R.id.life_game_btn);
        lifeGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent lifeGameIntent = new Intent(MainActivity.this, Life.class);
                startActivity(lifeGameIntent);
            }
        });

        ImageView strawsGameButton = findViewById(R.id.straw_game_btn);
        strawsGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent strawsGameIntent = new Intent(MainActivity.this, DrawStraw.class);
                startActivity(strawsGameIntent);
            }
        });

        ImageView rpsGameButton = findViewById(R.id.rps_game_btn);
        rpsGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent rpsGameIntent = new Intent(MainActivity.this, RocPapSci.class);
                startActivity(rpsGameIntent);
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
