package com.example.carsonsmith.cs3270finalproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class MowingInvoiceAdapter extends ArrayAdapter<MowingHistory> {

    public MowingInvoiceAdapter(Context context, ArrayList<MowingHistory> mowingHistories) {
        super(context, 0, mowingHistories);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        MowingHistory mowingHistory = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listview_invoices, parent, false);
        }
        // Lookup view for data population
        TextView tvDate = (TextView) convertView.findViewById(R.id.tvDate);
        TextView tvChargeAmount = (TextView) convertView.findViewById(R.id.tvChargeAmount);
        // Populate the data into the template view using the data object

        //SimpleDateFormat formatter = new SimpleDateFormat("E, MM dd, yyyy", Locale.US);
        String dateInString = mowingHistory.getDateMowed(); //11/18/2018

        //String input = "Thu Jun 18 20:56:02 EDT 2009";
        SimpleDateFormat parser = new SimpleDateFormat("MM/dd/yyyy");
        Date date = null;
        try {
            date = parser.parse(dateInString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat formatter = new SimpleDateFormat("EEEE, MMMM dd, yyyy");
        String formattedDate = formatter.format(date);

        tvDate.setText(formattedDate);

        //Formatting the payment Amount to Local US currency
        NumberFormat format = NumberFormat.getCurrencyInstance(Locale.US);

        double chargeAmount = Double.parseDouble(mowingHistory.getAmountCharged());
        String currency = format.format(chargeAmount);

        tvChargeAmount.setText(currency);
        // Return the completed view to render on screen
        return convertView;
    }
}