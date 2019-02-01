package com.xheghun.stockx;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.xheghun.stockx.request.CryptoList;
import com.xheghun.stockx.request.Datum;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    CurrencyListAdapter adapter;
    APIInterface apiInterface;
    private RecyclerView recyclerView;
    private List<Datum> coinList = null;
    ProgressDialog progressDialog;
    public static final String BASE_NAME = "com.xheghun.stockx.";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Menu menu = navigationView.getMenu();
        menu.findItem(R.id.coin_list).setChecked(true);
        init();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.coin_list) {
            // Handle the camera action
            init();
        }
        
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }




    public void init() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Fetching Data");
        progressDialog.setMessage(" Please wait...");


        apiInterface = APIClient.getClient().create(APIInterface.class);
        coinList = new ArrayList<>();

        initRecylerView();
        getCoinList();
    }

    private void initRecylerView() {
        recyclerView = findViewById(R.id.coin_list_);
        adapter = new CurrencyListAdapter(coinList);

        recyclerView.setAdapter(adapter);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);

        adapter.setItemClickListener(new CurrencyListAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(MainActivity.this, CoinDetailsActivity.class);
                intent.putExtra("coin",adapter.getItem(position));
                startActivity(intent);
            }
        });
    }

    private void getCoinList() {
        progressDialog.show();
        Call<CryptoList> call = apiInterface.getList("100");
        call.enqueue(new Callback<CryptoList>() {
            @Override
            public void onResponse(Call<CryptoList> call, Response<CryptoList> response) {
                progressDialog.dismiss();
                CryptoList list = response.body();
                coinList.clear();
                coinList.addAll(list.getData());
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<CryptoList> call, Throwable t) {
                progressDialog.dismiss();
                View view = findViewById(R.id.main_activity);
                Snackbar snackbar = Snackbar.make(view,"Unable to display details at this time",Snackbar.LENGTH_INDEFINITE);
                snackbar.setAction("dismiss", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        snackbar.dismiss();
                    }
                });
                snackbar.show();
                Log.d(BASE_NAME, t.getLocalizedMessage());
                call.cancel();
                // getCoinList();
            }
        });
    }
}
