package com.xheghun.stockx;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xheghun.stockx.request.Datum;

import java.util.List;


public class CurrencyListAdapter extends RecyclerView.Adapter<CurrencyListAdapter.ViewHolder> {
    private ItemClickListener itemClickListener;
    private List<Datum> datum;

    CurrencyListAdapter(List<Datum> data) {
        this.datum = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.currency_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Datum datum = this.datum.get(position);

        TextView profile = holder.profile;
        profile.setText(String.valueOf(datum.getName().charAt(0)));
        TextView coin_name = holder.coinName;
        coin_name.setText(datum.getName());
        TextView coin_symbol = holder.coinSymbol;
        coin_symbol.setText(datum.getSymbol());
        TextView coin_price = holder.coinPrice;
        coin_price.setText("$"+String.format("%,f", datum.getQuote().getUSD().getPrice()));
        TextView coin_market_cap = holder.coinMarketCap;
        coin_market_cap.setText(String.format("%,d", Math.round(datum.getQuote().getUSD().getMarketCap())));
    }

    @Override
    public int getItemCount() {
        return datum.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView coinName;
        TextView coinSymbol;
        TextView coinPrice;
        TextView coinMarketCap;
        TextView profile;

        ViewHolder(View view) {
            super(view);
            profile = itemView.findViewById(R.id.profile);
            coinName = itemView.findViewById(R.id.card_coin_name);
            coinSymbol = itemView.findViewById(R.id.card_coin_symbol);
            coinPrice = itemView.findViewById(R.id.card_coin_price);
            coinMarketCap = itemView.findViewById(R.id.card_market_cap);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (itemClickListener != null)
                itemClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    Datum getItem(int id) {
        return datum.get(id);
    }

    //capture click action
    void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    //implement in parent activity for responding to click action
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}