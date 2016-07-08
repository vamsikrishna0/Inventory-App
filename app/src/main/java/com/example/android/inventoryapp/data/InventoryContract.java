package com.example.android.inventoryapp.data;

import android.provider.BaseColumns;

/**
 * Created by Vamsi on 7/6/2016.
 */
public class InventoryContract {
    public static final class InventoryEntry implements BaseColumns {
        public static final String TABLE_NAME = "inventory";

        public static final String COLUMN_PRICE = "price";
        public static final String COLUMN_PRODUCT_NAME = "product_name";
        public static final String COLUMN_QUANTITY = "quantity";
        public static final String COLUMN_SUPPLIER = "supplier";
        public static final String COLUMN_PICTURE = "picture";
    }
}
