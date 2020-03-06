package com.example.carsonsmith.cs3270finalproject;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FirebaseUtil
{
    public static FirebaseDatabase mFirebaseDatabase;
    public static DatabaseReference mDatabaseReference;
    private static FirebaseUtil firebaseUtil;
    public static FirebaseAuth mFirebaseAuth;
    public static FirebaseAuth.AuthStateListener mAuthListener;
    private static final int RC_SIGN_IN = 123;
    private static MainActivity caller;
    private static StartUpActivity caller1;
    private static CustomerActivity custCaller;
    public static boolean isAdmin;

    public static ArrayList<Customer> customers;
    private static String userID;


    public static String getUserID() {
        if (userID == null)
        {
            if (FirebaseAuth.getInstance().getCurrentUser() == null)
            {
                FirebaseUtil.signIn();
            } else {
                userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
            }
        }
        return userID;
    }

    private FirebaseUtil(){}

    public static void openFbReference(String ref, final MainActivity callerActivity)
    {
        if(firebaseUtil == null)
        {
            firebaseUtil = new FirebaseUtil();
            mFirebaseDatabase = FirebaseDatabase.getInstance();
            mFirebaseAuth = FirebaseAuth.getInstance();
            caller = callerActivity;
            mAuthListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    if(firebaseAuth.getCurrentUser() == null)
                    {
                        FirebaseUtil.signIn(callerActivity);
                    }

                    userID = firebaseAuth.getUid();
                    //checkAdmin(userID);
                    Toast.makeText(callerActivity.getBaseContext(), "Welcome Back", Toast.LENGTH_LONG).show();
                }

            };


        }
        customers = new ArrayList<Customer>();

        mDatabaseReference = mFirebaseDatabase.getReference().child(ref);
    }

    public static void openFbReference3(String ref, final CustomerActivity callerActivity)
    {
        if(firebaseUtil == null)
        {
            firebaseUtil = new FirebaseUtil();
            mFirebaseDatabase = FirebaseDatabase.getInstance();
            mFirebaseAuth = FirebaseAuth.getInstance();
            custCaller = callerActivity;
            mAuthListener = new FirebaseAuth.AuthStateListener()
            {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth)
                {
                    if(firebaseAuth.getCurrentUser() == null)
                    {
                        FirebaseUtil.signIn(callerActivity);
                    }

                    userID = firebaseAuth.getUid();
                    //checkAdmin(userID);
                    Toast.makeText(callerActivity.getBaseContext(), "Welcome Back", Toast.LENGTH_LONG).show();
                }
            };


        }
        customers = new ArrayList<Customer>();

        mDatabaseReference = mFirebaseDatabase.getReference().child("Users").child(userID).child(ref);
    }



    public static void openFbReference2(String ref)
    {
        if(firebaseUtil == null)
        {
            firebaseUtil = new FirebaseUtil();
            mFirebaseDatabase = FirebaseDatabase.getInstance();
            mFirebaseAuth = FirebaseAuth.getInstance();

        }

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() == null)
                {
                    FirebaseUtil.signIn();
                }

                userID = firebaseAuth.getUid();
            }
        };

        customers = new ArrayList<Customer>();

        mDatabaseReference = mFirebaseDatabase.getReference().child("Users").child(FirebaseUtil.getUserID()).child(ref);
    }

    public static void attachListener()
    {
        mFirebaseAuth.addAuthStateListener(mAuthListener);
    }

    public static void detachListener()
    {
        mFirebaseAuth.removeAuthStateListener(mAuthListener);
    }

    public static void signIn(Activity caller)
    {
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());


        // Create and launch sign-in intent
        caller.startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);
    }

    public static void signIn()
    {
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());


        // Create and launch sign-in intent
        caller.startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);
    }

    private static void checkAdmin(String uID)
    {
        FirebaseUtil.isAdmin = false;

        DatabaseReference ref = mFirebaseDatabase.getReference().child("administrators").child(uID);

        ChildEventListener listener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                FirebaseUtil.isAdmin=true;
                Log.d("Admin", "You are an administrator");
                caller.showMenu();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        ref.addChildEventListener(listener);
    }
}
