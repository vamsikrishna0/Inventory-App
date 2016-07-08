package com.example.android.inventoryapp;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.inventoryapp.data.InventoryContract;
import com.example.android.inventoryapp.data.InventoryDBHelper;

/**
 * Created by Vamsi on 7/6/2016.
 */
public class InventoryCursorAdapter extends CursorAdapter {
    private static Context mContext;
    private LayoutInflater mInflater;


    public InventoryCursorAdapter(Context context, int layout, Cursor c, int flags, InventoryDBHelper mDbHelper){
        super(context, c, flags);
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public View newView(final Context context, Cursor cursor, ViewGroup viewGroup) {
        View currentView = mInflater.inflate(R.layout.list_item, viewGroup, false);

        //Set OnClickListener on sale button
        final Button makeSaleButton = (Button) currentView.findViewById(R.id.make_sale_button);
        final int id = cursor.getInt(cursor.getColumnIndex
                (InventoryContract.InventoryEntry._ID));
        final TextView finalQuantityTextView = (TextView) currentView.findViewById(R.id.quantity_text_view);
        makeSaleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InventoryDBHelper dbHelper = new InventoryDBHelper(context);
                int presentQuantity = dbHelper.getQuantity(id);

                if (presentQuantity > 0){
                    int q = presentQuantity - 1;
                    if(dbHelper.updateQuantity(id, q)) {
                        finalQuantityTextView.setText("Quantity: "+q+"");
                    }else{
                        Toast.makeText(context, "Update Failed", Toast.LENGTH_SHORT).show();
                    }
                }else
                    Toast.makeText(context, "Quantity is zero", Toast.LENGTH_SHORT).show();
            }
        });


        return currentView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView nameTextView = (TextView) view.findViewById(R.id.product_name_text_view);
        nameTextView.setText(mContext.getString(R.string.name) +": "+ cursor.getString(cursor.getColumnIndex
                (InventoryContract.InventoryEntry.COLUMN_PRODUCT_NAME)));

        TextView priceTextView = (TextView) view.findViewById(R.id.price_text_view);
        priceTextView.setText(mContext.getString(R.string.price) +": "+ cursor.getString(cursor.getColumnIndex
                (InventoryContract.InventoryEntry.COLUMN_PRICE)));

        TextView quantityTextView = (TextView) view.findViewById(R.id.quantity_text_view);

        quantityTextView.setText(mContext.getString(R.string.quantity) +": "+ cursor.getString(cursor.getColumnIndex
                (InventoryContract.InventoryEntry.COLUMN_QUANTITY)));

    }

}
