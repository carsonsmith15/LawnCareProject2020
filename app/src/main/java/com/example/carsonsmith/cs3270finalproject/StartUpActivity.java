package com.example.carsonsmith.cs3270finalproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

public class StartUpActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseUtil firebaseUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_up);
    }

    @Override
    protected void onStart() {
        super.onStart();

        //Activity used to check if user is signed in or not. If they are send them to Main Activity. If not send them to firebase sign in page

        if(firebaseAuth.getInstance().getCurrentUser() == null)
        {
            firebaseUtil.signIn(this);
        }
        else
        {
            Intent mainActivityIntent = new Intent(this, MainActivity.class);
            startActivity(mainActivityIntent);
        }
    }
}
