package com.example.carsonsmith.cs3270finalproject;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class CustomerActivity extends AppCompatActivity
{
    //region Class Variables
    private TextInputEditText customerID, customerFirstName, customerLastName, EmailAddress, WeeklyPrice, City, State, ZipCode, PhoneNumber, StreetAddress;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    Customer customer;
    String userID;
    //endregion

    @Override
    protected void onStart() {
        super.onStart();

        userID = FirebaseUtil.getUserID();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        FirebaseUtil.openFbReference3("customers", this);
        mFirebaseDatabase = FirebaseUtil.mFirebaseDatabase;
        mDatabaseReference = FirebaseUtil.mDatabaseReference;

        customerFirstName = (TextInputEditText) findViewById(R.id.firstName);
        customerLastName = (TextInputEditText) findViewById(R.id.lastName);
        EmailAddress = (TextInputEditText) findViewById(R.id.emailAddress);
        WeeklyPrice = (TextInputEditText) findViewById(R.id.weeklyPrice);
        PhoneNumber = (TextInputEditText) findViewById(R.id.phone_number);
        City = (TextInputEditText) findViewById(R.id.city);
        State = (TextInputEditText) findViewById(R.id.state);
        StreetAddress = (TextInputEditText) findViewById(R.id.streetAddress);
        ZipCode = (TextInputEditText) findViewById(R.id.zipCode);

        Intent intent = getIntent();
        Customer customer = (Customer) intent.getSerializableExtra("Customer");

        if (customer == null)
        {
            customer = new Customer();
        }

        this.customer = customer;

        //customerID.setText(customer.getCustomerID());
        customerFirstName.setText(customer.getFirstName());
        customerLastName.setText(customer.getLastName());
        EmailAddress.setText(customer.getEmailAddress());
        WeeklyPrice.setText(customer.getWeeklyPrice());
        PhoneNumber.setText(customer.getPhoneNumber());
        City.setText(customer.getCity());
        State.setText(customer.getState());
        StreetAddress.setText(customer.getStreetAddress());
        ZipCode.setText(customer.getZip());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        //If it is a new customer show just save button else show both delete and save button
        if(customer.getCustomerID() == null)
        {
            //Save New customer mode
            inflater.inflate(R.menu.menu_add_customer, menu);
        }
        else
        {
            //Edit/Delete customer mode
            inflater.inflate(R.menu.menu_delete_or_edit_customer, menu);
        }

        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_save:
                //Checking if first and last name have been entered
                String firstNameCheck = customerFirstName.getText().toString();
                String lastNameCheck = customerLastName.getText().toString();
                if(firstNameCheck.equals("") || lastNameCheck.equals(""))
                {
                    Toast.makeText(this, "First and Last Name Required", Toast.LENGTH_LONG).show();
                }
                else
                {
                    saveCustomer();
                    Toast.makeText(this, "Customer Saved", Toast.LENGTH_LONG).show();
                    backToList();
                    clean();
                }

                return true;

            case R.id.action_delete:
                if(customer.getCustomerID() != null)
                {
                    deleteCustomer();
                    Toast.makeText(this, "Customer Deleted", Toast.LENGTH_LONG).show();
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

    private void clean()
    {
        customerFirstName.setText("");
        customerLastName.setText("");
        EmailAddress.setText("");
        WeeklyPrice.setText("");
        PhoneNumber.setText("");
        City.setText("");
        State.setText("");
        StreetAddress.setText("");
        ZipCode.setText("");
        customerFirstName.requestFocus();
    }

    private void saveCustomer()
    {
        //String customerid = customerID.getText().toString();
        customer.setFirstName(customerFirstName.getText().toString());
        customer.setLastName(customerLastName.getText().toString());
        customer.setEmailAddress(EmailAddress.getText().toString());
        customer.setWeeklyPrice(WeeklyPrice.getText().toString());
        customer.setPhoneNumber(PhoneNumber.getText().toString());
        customer.setCity(City.getText().toString());
        customer.setState(State.getText().toString());
        customer.setZip(ZipCode.getText().toString());
        customer.setStreetAddress(StreetAddress.getText().toString());

        if(customer.getCustomerID() == null)
        {
            mDatabaseReference.push().setValue(customer);
        }
        else
        {
            mDatabaseReference.child(customer.getCustomerID()).setValue(customer);
        }
    }

    private void deleteCustomer()
    {
        if(customer.getCustomerID() == null)
        {
            Toast.makeText(this, "Save the customer before deleting", Toast.LENGTH_SHORT).show();
            return;
        }

        mDatabaseReference.child(customer.getCustomerID()).removeValue();
    }

    private void backToList()
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
