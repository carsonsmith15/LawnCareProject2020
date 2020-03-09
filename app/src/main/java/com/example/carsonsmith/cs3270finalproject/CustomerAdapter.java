package com.example.carsonsmith.cs3270finalproject;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.CustomerViewHolder>
{

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private ChildEventListener mChildListener;
    public ArrayList<Customer> customerArrayList;

    public ArrayList<Customer> getCustomerArrayList() {
        return customerArrayList;
    }

    public void setCustomerArrayList(ArrayList<Customer> customerArrayList) {
        this.customerArrayList = customerArrayList;
    }

    public CustomerAdapter()
    {
        FirebaseUtil.openFbReference2("customers");
        mFirebaseDatabase = FirebaseUtil.mFirebaseDatabase;
        mDatabaseReference = FirebaseUtil.mDatabaseReference;
        customerArrayList = FirebaseUtil.customers;
        mChildListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                Customer customer;
                Payment payment = new Payment();
                MowingHistory mowingHistory = new MowingHistory();
                ArrayList<Payment> paymentArrayList = new ArrayList<>();
                ArrayList<MowingHistory> mowingHistoryArrayList = new ArrayList<>();

                customer = dataSnapshot.getValue(Customer.class);

                for(DataSnapshot userSnapshot : dataSnapshot.child("payments").getChildren())
                {
                    payment = userSnapshot.getValue(Payment.class);
                    paymentArrayList.add(payment);
                    customer.setPaymentArrayList(paymentArrayList);
                }

                for(DataSnapshot userSnapshot : dataSnapshot.child("mowinghistory").getChildren())
                {
                    mowingHistory = userSnapshot.getValue(MowingHistory.class);
                    mowingHistoryArrayList.add(mowingHistory);
                    customer.setMowingHistoryArrayList(mowingHistoryArrayList);
                }

                //Log.d("Customer: ", customer.getFirstName());

                customer.setCustomerID(dataSnapshot.getKey());

                customerArrayList.add(customer);


                notifyItemInserted(customerArrayList.size() - 1);
                notifyItemInserted(paymentArrayList.size() - 1);
                notifyItemInserted(mowingHistoryArrayList.size() - 1);

                setCustomerArrayList(customerArrayList);
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
        mDatabaseReference.addChildEventListener(mChildListener);

    }

    @NonNull
    @Override
    public CustomerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();

        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.rv_row, parent, false);
                return new CustomerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerViewHolder holder, int position) {
        Customer customer = customerArrayList.get(position);
        holder.bind(customer);
    }

    @Override
    public int getItemCount() {
        return customerArrayList.size();
    }

    public class CustomerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener
    {
        TextView tvCustomerName;
        TextView tvPhoneNumber;
        TextView tvWeeklyPrice;

        public CustomerViewHolder(View itemView)
        {
            super(itemView);

            tvCustomerName = (TextView) itemView.findViewById(R.id.tvCustomerName);
            tvPhoneNumber = (TextView) itemView.findViewById(R.id.tvPhoneNumber);
            tvWeeklyPrice = (TextView) itemView.findViewById(R.id.tvWeeklyPrice);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        public void bind(Customer customer)
        {
            tvCustomerName.setText(customer.getFirstName() + " " + customer.getLastName());
            tvPhoneNumber.setText(customer.getPhoneNumber());
            tvWeeklyPrice.setText("$" + customer.getWeeklyPrice());
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();

            Log.d("Position: ", String.valueOf(position));

            Customer customer = customerArrayList.get(position);

            Intent intent = new Intent(itemView.getContext(), CustomerActivity.class);

            intent.putExtra("Customer", customer);

            view.getContext().startActivity(intent);

        }

        @Override
        public boolean onLongClick(View view) {

            int position = getAdapterPosition();

            Log.d("Position: ", String.valueOf(position));

            Customer customer = customerArrayList.get(position);

            Intent intent = new Intent(itemView.getContext(), CustomerViewActivity.class);

            intent.putExtra("Customer", customer);

            view.getContext().startActivity(intent);
            return false;
        }

    }

    public void updateList(ArrayList<Customer> list){
        setCustomerArrayList(list);
        notifyDataSetChanged();
    }
}
