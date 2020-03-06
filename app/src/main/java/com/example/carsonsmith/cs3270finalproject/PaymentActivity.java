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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class PaymentActivity extends AppCompatActivity
{
    //region PaymentActivityClassVariables
    Customer customer;
    private static final String TAG = "PaymentActivity";
    private TextView mDisplayDate;
    private EditText edtPayment;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase mFirebaseDatabase;
    RadioGroup radioGroup;
    RadioButton radioButton;
    //endregion

    //region onCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        mDisplayDate = (TextView) findViewById(R.id.tvDate);
        edtPayment = (EditText) findViewById(R.id.edtPayment);
        radioGroup = (RadioGroup) findViewById(R.id.radioCashOrCheck);
        mFirebaseDatabase = FirebaseUtil.mFirebaseDatabase;
        mDatabaseReference = FirebaseUtil.mDatabaseReference;

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();
        Customer customer = (Customer) intent.getSerializableExtra("Customer");

        this.customer = customer;

        setTitle(customer.getFullName());

        mDisplayDate.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();

                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(PaymentActivity.this,
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
                String validationCheck1 = edtPayment.getText().toString();
                String validationCheck2 = mDisplayDate.getText().toString();

                if(validationCheck1.equals("") || validationCheck2.equals("Click Here"))
                {
                    Toast.makeText(PaymentActivity.this, "Enter Payment Amount and Date", Toast.LENGTH_LONG).show();
                }
                else
                {
                    saveMowPaymentAndDate();

                    backToList();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    //endregion

    //region saveMowPaymentAndDate
    private void saveMowPaymentAndDate()
    {
        //Saving the Payment, Date, and if is is cash or check
        Payment payment = new Payment();

        //Found this example online about getting the id of radio button clicked. Had to debug to see what it is doing.
        int selectedId = radioGroup.getCheckedRadioButtonId();

        //Setting up instance of radioButton that was selected
        radioButton = (RadioButton) findViewById(selectedId);

        String cashOrCheck = (String) radioButton.getText();

        String id = mDatabaseReference.child("payments").push().getKey();
        payment.setID(id);
        payment.setCashOrCheck(cashOrCheck);
        payment.setPaymentAmount(edtPayment.getText().toString());
        payment.setPaymentDate(mDisplayDate.getText().toString());

        mDatabaseReference.child(customer.getCustomerID()).child("payments").push().setValue(payment).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(PaymentActivity.this, "Payment Stored Successfully", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(PaymentActivity.this, "Error storing payment...", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    //endregion

    //region backToList (used to go back to MainActivity)
    private void backToList() {
        Intent intent = new Intent(this, MainActivity.class);
        //intent.putExtra("Customer", customer);
        startActivity(intent);
    }
    //endregion
}
