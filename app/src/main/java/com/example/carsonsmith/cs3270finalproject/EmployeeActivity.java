package com.example.carsonsmith.cs3270finalproject;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class EmployeeActivity extends AppCompatActivity
{

    //region Private Members

    private TextInputEditText customerID, employeeFirstName, employeeLastName, EmailAddress, WeeklyPrice, City, State,
            ZipCode, PhoneNumber, StreetAddress;

    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee);
        Toolbar toolbar = findViewById(R.id.toolbar);
       setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        actionBar.setTitle("Add New Employee");
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));

        employeeFirstName = (TextInputEditText) findViewById(R.id.firstName);
        employeeLastName = (TextInputEditText) findViewById(R.id.lastName);
        EmailAddress = (TextInputEditText) findViewById(R.id.emailAddress);
        WeeklyPrice = (TextInputEditText) findViewById(R.id.weeklyPrice);
        PhoneNumber = (TextInputEditText) findViewById(R.id.phone_number);
        City = (TextInputEditText) findViewById(R.id.city);
        State = (TextInputEditText) findViewById(R.id.state);
        StreetAddress = (TextInputEditText) findViewById(R.id.streetAddress);
        ZipCode = (TextInputEditText) findViewById(R.id.zipCode);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.menu_add_employee, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        switch(item.getItemId())
        {
            case R.id.action_save_employee:

                //Checking if first and last name have been entered
                String firstNameCheck = employeeFirstName.getText().toString();
                String lastNameCheck = employeeLastName.getText().toString();
                if(firstNameCheck.equals("") || lastNameCheck.equals(""))
                {
                    Toast.makeText(this, "First and Last Name Required", Toast.LENGTH_LONG).show();
                }
                else
                {
                    saveEmployee();
                    Toast.makeText(this, "Employee Saved", Toast.LENGTH_LONG).show();
                    backToList();
                    clean();
                }

                return true;

            case R.id.action_delete:
                if(true)
                {
                    deleteEmployee();
                    Toast.makeText(this, "Employee Deleted", Toast.LENGTH_LONG).show();
                    backToList();
                }
                //Toast.makeText(this, "Add customer before deleting", Toast.LENGTH_LONG).show();
                return true;
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void deleteEmployee()
    {
    }

    private void saveEmployee()
    {
    }

    private void backToList()
    {
    }

    private void clean()
    {
    }
}
