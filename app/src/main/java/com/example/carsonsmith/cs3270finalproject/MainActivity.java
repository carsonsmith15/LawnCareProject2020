package com.example.carsonsmith.cs3270finalproject;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
//import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import android.app.SearchManager;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, SearchView.OnQueryTextListener
{
    //region MainActivity Variables
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;
    CustomerAdapter customerAdapter;
    ArrayList<Customer> customerArrayList;
    ArrayList<Customer> globalCustomerArrayList;
    //endregion

    //region onCreate
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        customerAdapter = new CustomerAdapter();
        globalCustomerArrayList = customerAdapter.getCustomerArrayList();

        mDrawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    //endregion

    //region onCreateOptionsMenu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.menu_main_page, menu);

        MenuItem searchMenuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) searchMenuItem.getActionView();

        searchView.setOnQueryTextListener(this);

        return true;
    }
    //endregion

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    //region onOptionsItemSelected
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        //Working with the side bar navigation
        if(actionBarDrawerToggle.onOptionsItemSelected(item))
        {
            return true;
        }

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

            case R.id.action_insert_menu:
                Intent intent = new Intent(this, CustomerActivity.class);
                startActivity(intent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
    //endregion


    @Override
    protected void onPause() {
        super.onPause();
        FirebaseUtil.detachListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseUtil.openFbReference("customers", this);

        RecyclerView rvCustomer = (RecyclerView) findViewById(R.id.rvCustomerList);

        customerAdapter = new CustomerAdapter();
        globalCustomerArrayList.clear();
        globalCustomerArrayList = customerAdapter.getCustomerArrayList();

        rvCustomer.setAdapter(customerAdapter);

        LinearLayoutManager customersLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvCustomer.setLayoutManager(customersLayoutManager);
        FirebaseUtil.attachListener();
    }

    public void showMenu()
    {
        invalidateOptionsMenu();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_action_logout)
        {
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
        }

        if( id == R.id.nav_action_new_customer)
        {
            Intent intent = new Intent(this, CustomerActivity.class);
            startActivity(intent);
            return true;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        String userInput = query.toLowerCase();
        ArrayList<Customer> customerList = new ArrayList<Customer>();
        ArrayList<Customer> customerArrayList = new ArrayList<Customer>();
        customerArrayList = customerAdapter.getCustomerArrayList();

        for(Customer c: customerArrayList)
        {
            if(c.getFullName().toLowerCase().startsWith(userInput) || c.getFullName().toLowerCase().contains(userInput)){
                customerList.add(c);
            }
        }

        if(userInput.equals("") || userInput.equals(" "))
        {
            if(customerList != null){
                customerList.clear();
            }

            for (Customer c : globalCustomerArrayList) {
                customerList.add(c);
            }
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String userInput = newText.toLowerCase();
        ArrayList<Customer> customerList = new ArrayList<Customer>();

        ArrayList<Customer> customerArrayList = new ArrayList<Customer>();
        customerArrayList = customerAdapter.getCustomerArrayList();

        for(Customer c: customerArrayList)
        {
            if(c.getFullName().toLowerCase().startsWith(userInput) || c.getFullName().toLowerCase().contains(userInput)){
                customerList.add(c);
            }
        }

        if(userInput.equals(""))
        {
            if(customerList != null){
                customerList.clear();
            }
            for (Customer c : globalCustomerArrayList)
            {
                customerList.add(c);
            }
        }

        customerAdapter.updateList(customerList);
        return true;
    }
}
