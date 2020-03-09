package com.example.carsonsmith.cs3270finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class EmployeesActivity extends AppCompatActivity
{

    //region Private Members


    //endregion

    //region On Create

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employees);
    }

    //endregion

    //region OnCreateOptionsMenu

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.menu_employees, menu);

        return true;
    }

    //endregion

    //region onOptionsItemsSelected

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {

            case R.id.action_signout:
                AuthUI.getInstance()
                        .signOut(this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task)
                            {
                                Toast.makeText(getApplicationContext(), "You're signed out", Toast.LENGTH_SHORT).show();
                                FirebaseUtil.attachListener();
                            }
                        });
                FirebaseUtil.detachListener();
                return true;

            case R.id.action_add_new_employee:
                Intent insertNewEmployeeIntent = new Intent(this, EmployeeActivity.class);
                startActivity(insertNewEmployeeIntent);
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    //endregion
}
