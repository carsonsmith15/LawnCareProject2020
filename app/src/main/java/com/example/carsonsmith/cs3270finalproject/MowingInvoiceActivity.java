package com.example.carsonsmith.cs3270finalproject;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class MowingInvoiceActivity extends AppCompatActivity {

    Customer customer;
    String userID;

    @Override
    protected void onStart() {
        super.onStart();

        userID = FirebaseUtil.getUserID();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accounts_receivable);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if(customer == null)
        {
            Intent intent = getIntent();
            customer = (Customer) intent.getSerializableExtra("Customer");
        }

        this.customer = customer;

        setTitle(R.string.detailed_invoices);

        // Construct the data source
        ArrayList<MowingHistory> mowingCharges = customer.getMowingHistoryArrayList();

        if(mowingCharges != null)
        {
            // Create the adapter to convert the array to views
            MowingInvoiceAdapter adapter = new MowingInvoiceAdapter(this, mowingCharges);
            // Attach the adapter to a ListView
            ListView listView = (ListView) findViewById(R.id.detailedInvoiceListView);
            listView.setAdapter(adapter);
        }
        else
        {
            setTitle(R.string.no_invoices);
        }
    }

    //region onCreateOptionsMenu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.menu_export_invoicepdf, menu);

        return true;
    }
    //endregion

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            //Didn't complete this...
            case R.id.action_export:

                createPDF();

                return true;
            //Didn't complete this...
            case R.id.action_deleteInvoice:
                //deleteInvoice();
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Wanted to delete an individual payment or mowing charge but did not have time and couldn't figure it out
    private void deleteInvoice() {
        DatabaseReference deleteInvoice = FirebaseDatabase.getInstance().getReference("Users").child(userID).child(customer.getCustomerID()).child("mowinghistory");

        deleteInvoice.removeValue();

        Toast.makeText(this, "Invoice Deleted", Toast.LENGTH_SHORT ).show();

        Intent customerViewIntent = new Intent(MowingInvoiceActivity.this, MainActivity.class);
        startActivity(customerViewIntent);
    }

    private void createPDF() {


    }


}
