package com.example.carsonsmith.cs3270finalproject;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.CellIdentity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.List;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class CustomerViewActivity extends AppCompatActivity
{
    private static final int STORAGE_CODE = 1000;
    Customer customer;
    TextView tvStateAndCity, tvCustomerName, tvAddress, tvEmailAddress, tvPhoneNumber, tvWeeklyPrice, tvTotalOwed;
    Button btnDetailedInvoice, accountsReceivableBtn;
    FirebaseAuth mFirebaseAuth;
    FirebaseDatabase mFirebaseDatabase;
    String userID;
    private DatabaseReference mDatabaseReference;

    // Pdf invoice
    final private int REQUEST_CODE_ASK_PERMISSIONS = 111;
    private static final String TAG = "PdfCreatorActivity";
    private File pdfFile;
    Context context;
    double totalOwed = 0.0;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_view);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        context = this;

        btnDetailedInvoice = (Button) findViewById(R.id.detailedInvoice);
        accountsReceivableBtn = (Button) findViewById(R.id.accountsReceivableBtn);
        tvStateAndCity = (TextView) findViewById(R.id.tvStateAndCity);
        tvEmailAddress = (TextView) findViewById(R.id.tvEmailAddress);
        tvCustomerName = (TextView) findViewById(R.id.tvCustomerName);
        tvAddress = (TextView) findViewById(R.id.tvAddress);
        tvPhoneNumber = (TextView) findViewById(R.id.tvPhoneNumber);
        tvWeeklyPrice = (TextView) findViewById(R.id.tvWeeklyPrice);
        tvTotalOwed = (TextView) findViewById(R.id.tvTotalOwed);

        if(customer == null)
        {
            Intent intent = getIntent();
            customer = (Customer) intent.getSerializableExtra("Customer");
        }

        this.customer = customer;

        setTitle(customer.getFullName());

        tvStateAndCity.setText(customer.getCity() + ", " + customer.getState());
        tvCustomerName.setText(customer.getFullName());
        tvEmailAddress.setText(customer.getEmailAddress());
        tvAddress.setText(customer.getStreetAddress());
        tvPhoneNumber.setText(customer.getPhoneNumber());
        tvWeeklyPrice.setText("$" + customer.getWeeklyPrice());

        ArrayList<Payment> paymentArrayList = customer.getPaymentArrayList();
        ArrayList<MowingHistory> mowingHistoryArrayList = customer.getMowingHistoryArrayList();

        totalOwed = 0.00;
        double totalPayments = 0.00;
        double mowingCharges = 0.00;

        if(paymentArrayList != null)
        {
            for(Payment payment : paymentArrayList)
            {

                totalPayments += Double.parseDouble(payment.getPaymentAmount());
            }
        }

        if(mowingHistoryArrayList != null)
        {
            for(MowingHistory mowingHistory : mowingHistoryArrayList)
            {
                mowingCharges += Double.parseDouble(mowingHistory.getAmountCharged());
            }
        }

        totalOwed = mowingCharges - totalPayments;

        NumberFormat format = NumberFormat.getCurrencyInstance(Locale.US);

        String currency = format.format(totalOwed);

        tvTotalOwed.setText(currency);


        btnDetailedInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CustomerViewActivity.this, PaymentDetailsActivity.class);
                intent.putExtra("Customer", customer);
                startActivity(intent);
            }
        });

        accountsReceivableBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CustomerViewActivity.this, MowingInvoiceActivity.class);
                intent.putExtra("Customer", customer);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        //userID = mFirebaseAuth.getCurrentUser().getUid();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_payments_mowing, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.action_payment:
                Intent paymentIntent = new Intent(this, PaymentActivity.class);
                paymentIntent.putExtra("Customer", customer);
                startActivity(paymentIntent);
                return true;

            case R.id.action_mowed:
                Intent mowIntent = new Intent(this, MowActivity.class);
                mowIntent.putExtra("Customer", customer);
                startActivity(mowIntent);
                return true;

            case R.id.action_email:

                // Pdf Creation
                try
                {
                    createPdfWrapper();
                }
                catch(FileNotFoundException ex)
                {
                    ex.printStackTrace();
                }
                catch(DocumentException ex)
                {
                    ex.printStackTrace();
                }

               /* Intent emailIntent = new Intent(Intent.ACTION_SEND);
                String[] emailArray = new String[]{customer.getEmailAddress()};

                emailIntent.putExtra(Intent.EXTRA_EMAIL, emailArray);

                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Email to Carson");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Testing...");

                emailIntent.setType("message/rfc822");
                startActivity(Intent.createChooser(emailIntent, "Choose an email client"));*/

            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void createPdfWrapper() throws FileNotFoundException, DocumentException
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
            {
                //Permission was not granted, request it
                String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permissions, STORAGE_CODE);
            }
            else
            {
                //Permission already granted, call createPDF
                savePdf();
            }
        }
        else
        {
            savePdf();
        }

    }

    //Handle permission result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode){
            case STORAGE_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //Permission was granted from popup
                    try
                    {
                        savePdf();

                    }
                    catch (Exception ex){

                    }

                }
                else{
                    // Permission was denied from popup
                    Toast.makeText(this, "Permission was denied...!", Toast.LENGTH_SHORT).show();
                }
        }
    }

    private void savePdf()
    {
        Document doc = new Document();
        String mFileName = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(System.currentTimeMillis());

        String mFilePath = Environment.getExternalStorageDirectory() + "/" + mFileName + ".pdf";

        try
        {
            PdfPTable table = new PdfPTable(4);
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            table.getDefaultCell().setFixedHeight(100);
            table.setTotalWidth(PageSize.A4.getWidth());
            table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell("Description");
            table.addCell("Cost");
            table.addCell("Date");
            table.addCell("Cash Or Check");
            table.setHeaderRows(1);

            PdfPCell[] cells = table.getRow(0).getCells();
            for(int j = 0; j < cells.length; j++){
                cells[j].setBackgroundColor(BaseColor.LIGHT_GRAY);
            }

            ArrayList<MowingHistory> mowingHistoryArrayList = customer.getMowingHistoryArrayList();
            ArrayList<Payment> paymentArrayList = customer.getPaymentArrayList();

            for (MowingHistory mowHist : mowingHistoryArrayList) {
                table.addCell("Mowing");
                table.addCell("$" + mowHist.getAmountCharged());
                table.addCell(mowHist.getDateMowed());
                table.addCell("");
            }

            for (Payment payment : paymentArrayList) {
                table.addCell("Payment");
                table.addCell("$" + payment.getPaymentAmount());
                table.addCell(payment.getPaymentDate());
                table.addCell(payment.getCashOrCheck());
            }

            PdfWriter.getInstance(doc, new FileOutputStream(mFilePath));

            doc.open();

            doc.addTitle("Invoice");

            Font BoldAndBig = new Font(Font.FontFamily.TIMES_ROMAN, 30.0F, Font.BOLD, BaseColor.GREEN);

            Paragraph invoiceText = new Paragraph("INVOICE", BoldAndBig);
            invoiceText.setAlignment(Element.ALIGN_LEFT);

            PdfPTable table2 = new PdfPTable(2);
            table2.setWidthPercentage(100);
            table2.addCell(getCell("BILLED TO", PdfPCell.ALIGN_LEFT));
            table2.addCell(getCell("UNPLUGGED", PdfPCell.ALIGN_RIGHT));

            PdfPTable table3 = new PdfPTable(2);
            table3.setWidthPercentage(100);
            table3.addCell(getCell(customer.getFullName(), PdfPCell.ALIGN_LEFT));
            table3.addCell(getCell("Company Address", PdfPCell.ALIGN_RIGHT));

            PdfPTable table4 = new PdfPTable(2);
            table4.setWidthPercentage(100);
            table4.addCell(getCell(customer.getStreetAddress(), PdfPCell.ALIGN_LEFT));
            table4.addCell(getCell("(801)-917-4544", PdfPCell.ALIGN_RIGHT));

            PdfPTable table5 = new PdfPTable(2);
            table5.setWidthPercentage(100);
            table5.addCell(getCell(customer.getCity() + ", " + customer.getState(), PdfPCell.ALIGN_LEFT));
            table5.addCell(getCell("uplawns@gmail.com", PdfPCell.ALIGN_RIGHT));

            PdfPTable table6 = new PdfPTable(1);
            table6.setWidthPercentage(100);
            table6.addCell(getCell(customer.getZip(), PdfPCell.ALIGN_LEFT));

            PdfPTable InvoiceTotal = new PdfPTable(1);
            InvoiceTotal.setWidthPercentage(25);
            InvoiceTotal.setHorizontalAlignment(Element.ALIGN_RIGHT);
            InvoiceTotal.addCell(getCellBorder("Total Owed: $" + totalOwed, PdfPCell.ALIGN_RIGHT));

            doc.add(invoiceText);

            doc.add(Chunk.NEWLINE);

            doc.add(table2);
            doc.add(table3);
            doc.add(table4);
            doc.add(table5);
            doc.add(table6);

            doc.add(Chunk.NEWLINE);

            doc.add(table);

            doc.add(Chunk.NEWLINE);


            doc.add(InvoiceTotal);

            doc.close();

            Toast.makeText(this,"Added", Toast.LENGTH_SHORT).show();

        }
        catch(Exception e){

        }

    }

    private PdfPCell getCell(String text, int alignment)
    {
        PdfPCell cell = new PdfPCell(new Phrase(text));
        cell.setPadding(0);
        cell.setHorizontalAlignment(alignment);
        cell.setBorder(PdfPCell.NO_BORDER);
        return cell;
    }

    private PdfPCell getCellBorder(String text, int alignment)
    {
        PdfPCell cell = new PdfPCell(new Phrase(text));
        cell.setPadding(0);
        cell.setHorizontalAlignment(alignment);

        return cell;
    }

    private void createPdf() throws FileNotFoundException, DocumentException{

        File docFolder = new File(Environment.getExternalStorageDirectory() + "/Documents");
        if (!docFolder.exists()){
            docFolder.mkdir();
            Log.i(TAG, "Created a new directory for PDF");
        }

        String pdfName = "Invoice.pdf";

        pdfFile = new File(docFolder.getAbsolutePath(), pdfName);
        OutputStream output = new FileOutputStream(pdfFile);
        Document document = new Document(PageSize.A4);
        PdfPTable table = new PdfPTable(new float[]{3,3,3,3});
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table.getDefaultCell().setFixedHeight(50);
        table.setTotalWidth(PageSize.A4.getWidth());
        table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell("Date Mowed");
        table.addCell("Amount Charged");
        table.addCell("Amount Charged");
        table.addCell("Amount Charged");
        table.addCell("Amount Charged");
        table.setHeaderRows(1);

        PdfPCell[] cells = table.getRow(0).getCells();
        for(int j = 0; j < cells.length; j++){
            cells[j].setBackgroundColor(BaseColor.LIGHT_GRAY);
        }

        ArrayList<MowingHistory> mowingHistoryArrayList = customer.getMowingHistoryArrayList();

        for (MowingHistory mowHist : mowingHistoryArrayList) {
            table.addCell(mowHist.getDateMowed());
            table.addCell(mowHist.getAmountCharged());
        }

        PdfWriter.getInstance(document, output);
        document.open();

        Font f = new Font(Font.FontFamily.TIMES_ROMAN, 30.0F, Font.UNDERLINE, BaseColor.GREEN);

        document.add(new Paragraph(customer.getFullName() + "Invoice", f));
        document.add(table);

        document.close();

        previewPdf();

    }

    private void previewPdf() {

        PackageManager packageManager = context.getPackageManager();
        Intent testIntent = new Intent(Intent.ACTION_VIEW);
        testIntent.setType("application/pdf");

        List list = (List) packageManager.queryIntentActivities(testIntent, PackageManager.MATCH_DEFAULT_ONLY);

        if (list.size() > 0){
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            Uri uri = Uri.fromFile(pdfFile);
            intent.setDataAndType(uri, "application/pdf");
            context.startActivity(intent);
        }
        else{
            Toast.makeText(context, "Download a PDF Viewer to see the generated PDF", Toast.LENGTH_LONG).show();
        }
    }
}