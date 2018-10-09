package com.ckm.settlethescore;

import com.google.firebase.auth.FirebaseUser;

public class Player {
    private boolean isConnected;

    private int currentFriendIndex;

    private FirebaseUser firebaseUser;

    private String displayName;
    private String email;
    private String fullName;
    private String phoneNumber;
    private String userId;

    private String[] friends;

    // Defaults
    private final boolean defaultIsConnected = false;

    private final FirebaseUser defaultFirebaseUser = null;

    private final String defaultDisplayName = "Default Player";
    private final String defaultEmail = "default_email@email_website.com";
    private final String defaultFullName = "Default Full Name";
    private final String defaultPhoneNumber = "(555) 555-5555";
    private final String defaultUserId = "-1";

    private final String[] defaultFriends = null;

    // Default Constructor
    private Player() {
        isConnected = defaultIsConnected;

        userId = defaultUserId;
        friends = defaultFriends;

        firebaseUser = defaultFirebaseUser;

        displayName = defaultDisplayName;
        email = defaultEmail;
        fullName = defaultFullName;
        phoneNumber = defaultPhoneNumber;
    }

    // Setters / Getters
    public boolean isConnected() {
        return isConnected;
    }

    public void setIsConnected(boolean status) {
        isConnected = status;
    }

    public void setFirebaseUser(FirebaseUser user) {
        firebaseUser = user;
    }

    public FirebaseUser getFirebaseUser() {
        return firebaseUser;
    }

    public void setDisplayName(String name) {
        displayName = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setEmail(String new_email) {
        email = new_email;
    }

    public String getEmail() {
        return email;
    }

    public void setFullName(String name) {
        fullName = name;
    }

    public String getFullName() {
        return fullName;
    }

    public void setPhoneNumber(String number) {
        phoneNumber = number;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    // array of friends setters / getters

    public String getUserId() {
        return userId;
    }

    public void setUserId(String id) {
        userId = id;
    }




    @Override
    public String toString() {
        return "User I.D.: " + userId + "\n" +
                "Display Name: " + displayName + "\n" +
                "Full Name: " + fullName + "\n" +
                "Email: " + email + "\n" +
                "Phone Number: " + phoneNumber + "\n" +
                "Connected: " + isConnected + "\n";
    }




    public static Player generatePlayerFromFirebaseUser(FirebaseUser user) {
        Player player = new Player();
        player.setFirebaseUser(user);
        player.setEmail(user.getEmail());
        player.setDisplayName(user.getDisplayName());
        player.setFullName(user.getDisplayName());
        player.setIsConnected(true);
        player.setPhoneNumber(user.getPhoneNumber());
        player.setUserId(user.getUid());

        return player;
    }

    /*
    *
    * Sets all values to their Default settings
    *
    * */
    public void signOut() {
        isConnected = defaultIsConnected;
        userId = defaultUserId;
        friends = defaultFriends;
        firebaseUser = defaultFirebaseUser;
        displayName = defaultDisplayName;
        email = defaultEmail;
        fullName = defaultFullName;
        phoneNumber = defaultPhoneNumber;
    }
}
