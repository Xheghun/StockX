package com.xheghun.stockx;

import android.provider.BaseColumns;

public class StockXDatabaseContract {
    public StockXDatabaseContract() {

    }

    public static final class CoinListInfo implements BaseColumns {
        public static final String TABLE_NAME = "coin_list";
        public static final String COLUMN_COIN_NAME = "coin_name";
        public static final String COLUMN_COIN_SYMBOL = "coin_symbol";
        public static final String COLUMN_COIN_PRICE = "coin_price";
        public static final String COLUMN_COIN_MARKET_CAPITAL = "coin_market_cap";

        // CREATE TABLE course_info (course_id, course_title)
        public static final String SQL_CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        _ID + " INTEGER PRIMARY KEY, " +
                        COLUMN_COIN_NAME + " TEXT UNIQUE NOT NULL, " +
                        COLUMN_COIN_SYMBOL + " TEXT NOT NULL, " +
                        COLUMN_COIN_PRICE + " TEXT NOT NULL, " +
                        COLUMN_COIN_MARKET_CAPITAL + " TEXT NOT NULL)";
    }
}