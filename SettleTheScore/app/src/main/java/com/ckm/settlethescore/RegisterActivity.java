package com.ckm.settlethescore;

import android.content.Intent;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class RegisterActivity extends AppCompatActivity {
    int IMAGE_SELECTED = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final TextView displayNameField = (TextView) findViewById(R.id.txtRegisterDisplayNameField);
        final TextView fullNameField = (TextView) findViewById(R.id.txtRegisterFullNameField);
        final TextView emailField = (TextView) findViewById(R.id.txtRegisterEmailField);
        final TextView phoneNumberField = (TextView) findViewById(R.id.txtRegisterPhoneNumberField);
        final ImageView profilePhotoField = (ImageView) findViewById(R.id.imgRegisterProfilePhoto);
        final TextView profilePhotoUriField = (TextView) findViewById(R.id.txtProfilePictureUri);

        Button registerButton = (Button) findViewById(R.id.btnRegisterSubmit);

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        displayNameField.setText(user.getDisplayName());
        fullNameField.setText(user.getDisplayName());
        emailField.setText(user.getEmail());
        phoneNumberField.setText(user.getPhoneNumber());

        profilePhotoField.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent selectProfilePicture = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(selectProfilePicture, IMAGE_SELECTED);
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                final DatabaseReference databaseReference = database.getReference().child("Players").child(user.getUid());

                // add user's info to database
                databaseReference.child("display_name").setValue(displayNameField.getText().toString().trim());
                databaseReference.child("full_name").setValue(fullNameField.getText().toString().trim());
                databaseReference.child("email").setValue(emailField.getText().toString().trim());
                databaseReference.child("phone_number").setValue(phoneNumberField.getText().toString().trim());

                // add image to firebase storage, save as user's ID
                StorageReference playerProfilePictureStorage = FirebaseStorage.getInstance().getReference("profile_pictures/" + user.getUid().toString());
                UploadTask uploadPhoto = playerProfilePictureStorage.putFile(Uri.parse(profilePhotoUriField.getText().toString().trim()));

                uploadPhoto.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
                        startActivity(mainIntent);

                        databaseReference.child("profile_photo").setValue(taskSnapshot.getUploadSessionUri().toString().trim());

                        Toast.makeText(RegisterActivity.this, "User Profile Updated", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegisterActivity.this, "Error: Failed to upload photo, please try again", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == IMAGE_SELECTED) {
            if(resultCode == RESULT_OK) {
                final ImageView profilePhotoField = (ImageView) findViewById(R.id.imgRegisterProfilePhoto);
                final TextView profilePhotoUriField = (TextView) findViewById(R.id.txtProfilePictureUri);
                Uri uri = data.getData();
                profilePhotoField.setImageURI(uri);
                profilePhotoUriField.setText(uri.toString().trim());
            }
        }
    }
}
