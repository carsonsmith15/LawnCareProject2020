package com.example.carsonsmith.cs3270finalproject;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class MowActivity extends AppCompatActivity
{
    //region MowActivityClassVariables
    Customer customer;
    MowingHistory mowingHistory;
    private static final String TAG = "MowActivity";
    private TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    EditText mowingCostAmount;
    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase mFirebaseDatabase;
    String userID;
    //endregion

    //region onCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mow);

        //used to create the back arrow
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //Setting Text Views up
        mDisplayDate = (TextView) findViewById(R.id.tvDate);
        mowingCostAmount = (EditText) findViewById(R.id.tvMowingCost);

        //Firebase Access
        mFirebaseDatabase = FirebaseUtil.mFirebaseDatabase;
        mDatabaseReference = FirebaseUtil.mDatabaseReference;

        //Getting the intent and customer class so we can know which customer the mowing is for
        Intent intent = getIntent();
        customer = (Customer) intent.getSerializableExtra("Customer");

        mowingHistory = new MowingHistory();

        setTitle(customer.getFullName());

        //Date Click Here button, Selecting a date
        mDisplayDate.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();

                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(MowActivity.this,
                        AlertDialog.THEME_DEVICE_DEFAULT_LIGHT, mDateSetListener, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                dialog.show();

            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;

                String date = month + "/" + dayOfMonth + "/" + year;
                mDisplayDate.setText(date);
            }
        };
    }
    //endregion

    //region onStart
    @Override
    protected void onStart() {
        super.onStart();

        userID = FirebaseUtil.getUserID();
    }
    //endregion

    //region onResume
    @Override
    protected void onResume() {
        super.onResume();
        userID = FirebaseUtil.getUserID();
    }
    //endregion

    //region onCreateOptionsMenu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_save_mow_date_and_amount, menu);
        return true;
    }
    //endregion

    //region onOptionsItemSelected
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_saveMow:
                String validationCheck1 = mowingCostAmount.getText().toString();
                String validationCheck2 = mDisplayDate.getText().toString();

                if(validationCheck1.equals("") || validationCheck2.equals("Click Here"))
                {
                    Toast.makeText(MowActivity.this, "Enter Charge Amount and Date", Toast.LENGTH_LONG).show();
                }
                else
                {
                    saveMowPaymentAndDate();
                    //Toast.makeText(MowActivity.this, "Mowing Charge and Date Saved", Toast.LENGTH_SHORT).show();
                    backToList();
                }

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    //endregion

    //region backToList (Used to go back to MainActivity)
    private void backToList() {
        Intent intent = new Intent(this, MainActivity.class);
        //intent.putExtra("Customer", customer);
        startActivity(intent);
    }
    //endregion

    //region saveMowPaymentAndDate
    private void saveMowPaymentAndDate()
    {
        MowingHistory mowingHistory = new MowingHistory();

        String id = mDatabaseReference.child("mowinghistory").push().getKey();
        mowingHistory.setId(id);
        mowingHistory.setAmountCharged(mowingCostAmount.getText().toString());
        mowingHistory.setDateMowed(mDisplayDate.getText().toString());

        mDatabaseReference.child(customer.getCustomerID()).child("mowinghistory").push().setValue(mowingHistory).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(MowActivity.this, "Mowing Date and Charged Amount Stored", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(MowActivity.this, "Error storing...", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    //endregion
}
