package com.xheghun.stockx;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.xheghun.stockx.request.Datum;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class CoinDetailsActivity extends AppCompatActivity {

    Toolbar toolbar;
    private TextView price;
    private TextView date;
    private TextView symbol;
    private TextView slug;
    private TextView volume24h;
    private TextView circulating_supply;
    private TextView max_supply;
    private TextView market_cap;
    private TextView change1h;
    private TextView change24h;
    private TextView change7d;
    private Datum datum;

    private FloatingActionButton shareFAB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_details);

        shareFAB = findViewById(R.id.share_fab);

        Intent intent = getIntent();
        datum = (Datum) intent.getSerializableExtra("coin");

        toolbar = findViewById(R.id.coin_details_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(datum.getName() + " (" + datum.getSymbol() + ")");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        price = findViewById(R.id.price);
        date = findViewById(R.id.date);

        symbol = findViewById(R.id.symbol);
        slug = findViewById(R.id.slug);
        volume24h = findViewById(R.id.volume24h);
        circulating_supply = findViewById(R.id.circulating_supply);
        max_supply = findViewById(R.id.max_supply);
        market_cap = findViewById(R.id.market_cap);

        change1h = findViewById(R.id.change1h);
        change24h = findViewById(R.id.change24h);
        change7d = findViewById(R.id.changes_7_days);

        price.setText("$"+String.format("%,f", datum.getQuote().getUSD().getPrice()));
        date.setText(parseDate(datum.getLastUpdated()));
        symbol.setText(datum.getSymbol());
        slug.setText(datum.getSlug());

        volume24h.setText(String.format("%,d", Math.round(datum.getQuote().getUSD().getVolume24h())));
        circulating_supply.setText(String.format("%.0f", datum.getCirculatingSupply()) + " " + datum.getSymbol());
        max_supply.setText(String.format("%.0f", datum.getMaxSupply()) + " " + datum.getSymbol());
        market_cap.setText(String.format("%,d", Math.round(datum.getQuote().getUSD().getMarketCap())));
        change1h.setText(String.format("%.2f", datum.getQuote().getUSD().getPercentChange1h()) + "%");
        change24h.setText(String.format("%.2f", datum.getQuote().getUSD().getPercentChange24h()) + "%");
        change7d.setText(String.format("%.2f", datum.getQuote().getUSD().getPercentChange7d()) + "%");

        shareFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareDetails();
            }
        });
    }

    public void shareDetails() {
        String details = "\n CRYPTO FINDER APP" +
                "\n\nCOIN NAME: "+datum.getName() +" ("+ datum.getSymbol()+ ")" +
                "\nCOIN PRICE: $"+String.format("%,f",datum.getQuote().getUSD().getPrice()) +"" +
                "\nLAST UPDATED: "+parseDate(datum.getLastUpdated()) +
                "\nSYMBOL: " +datum.getSymbol()+
                "\nSLUG: "+datum.getSlug() +"" +
                "\nVOLUME IN THE LAST 24 Hours: " +datum.getQuote().getUSD().getVolume24h()+
                "\nCIRCULATING SUPPLY: $"+datum.getCirculatingSupply() + " "+ datum.getSymbol()+
                "\nMAX SUPPLY: " + datum.getMaxSupply() + " " + datum.getSymbol() +
                "\nMARKET CAP: "+ Math.round(datum.getQuote().getUSD().getMarketCap()) +
                "\nCHANGE IN THE LAST 1 Hour: "+ datum.getQuote().getUSD().getPercentChange1h() + "%"+
                "\nCHANGE IN THE LAST 24 Hours: " +datum.getQuote().getUSD().getPercentChange24h() + "%" +
                "\nCHANGE IN THE LAST 7 Hour: " + datum.getQuote().getUSD().getPercentChange7d() + "%";

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");


        intent.putExtra(Intent.EXTRA_TEXT,details);
        startActivity(intent);
    }

    private String parseDate(String lastUpdated) {
        //parse timestamp from server to UTC
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        //format to localtime zone
        SimpleDateFormat output = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
        output.setTimeZone(TimeZone.getDefault());

        Date date = null;
        try {
            date = simpleDateFormat.parse(lastUpdated);
        }catch (ParseException e) {
            e.printStackTrace();
        }
        return output.format(date);
    }
}
