package com.example.android.inventoryapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.inventoryapp.data.InventoryContract;
import com.example.android.inventoryapp.data.InventoryDBHelper;

import java.io.IOException;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_item);
        long rowId = getIntent().getLongExtra("ID", 0);
        Log.v("DetailActivity", ""+rowId);

        InventoryDBHelper dbHelper = new InventoryDBHelper(this);
        Cursor cursor = dbHelper.readProductDetails(rowId);
        cursor.moveToFirst();
        TextView nameTextView = (TextView) findViewById(R.id.detail_product_name_textview);
        nameTextView.setText(cursor.getString(cursor.getColumnIndex
                (InventoryContract.InventoryEntry.COLUMN_PRODUCT_NAME)));

        TextView priceTextView = (TextView) findViewById(R.id.detail_price_textview);
        priceTextView.setText(cursor.getString(cursor.getColumnIndex
                (InventoryContract.InventoryEntry.COLUMN_PRICE)));

        TextView quantityTextView = (TextView) findViewById(R.id.detail_quantity_textview);

        quantityTextView.setText(cursor.getString(cursor.getColumnIndex
                (InventoryContract.InventoryEntry.COLUMN_QUANTITY)));

        TextView supplierTextView = (TextView) findViewById(R.id.detail_supplier_textview);
        supplierTextView.setText(cursor.getString(cursor.getColumnIndex
                (InventoryContract.InventoryEntry.COLUMN_SUPPLIER)));

        //Check if there is an Image
        ImageView picture = (ImageView) findViewById(R.id.detail_picture_image);
        String imageReturned = cursor.getString(cursor.getColumnIndex
                (InventoryContract.InventoryEntry.COLUMN_PICTURE));
        if(imageReturned != null){
            Uri selectedImageUri = Uri.parse(imageReturned);
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                picture.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else
            picture.setVisibility(View.GONE);


        quantityTextView = (TextView) findViewById(R.id.detail_quantity_textview);
        //Make sale button ONCLickListener
        final Button makeSaleButton = (Button) findViewById(R.id.detail_make_sale_button);
        final int id = cursor.getInt(cursor.getColumnIndex
                (InventoryContract.InventoryEntry._ID));
        final TextView finalQuantityTextView = quantityTextView;
        makeSaleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InventoryDBHelper dbHelper = new InventoryDBHelper(DetailActivity.this);
                int presentQuantity = dbHelper.getQuantity(id);
                if (presentQuantity > 0){
                    int q = presentQuantity - 1;
                    if(dbHelper.updateQuantity(id, q)) {
                        finalQuantityTextView.setText(q+"");
                    }else{
                        Toast.makeText(DetailActivity.this, "Update Failed", Toast.LENGTH_SHORT).show();
                    }
                }else
                    Toast.makeText(DetailActivity.this, "Quantity is zero", Toast.LENGTH_SHORT).show();
            }
        });

        //Modify Quantity OnClickListener
        final EditText newQuantity = (EditText) findViewById(R.id.detail_modified_quantity_edittext);

        Button modifyQuantityButton = (Button) findViewById(R.id.detail_modify_quantity_button);
        modifyQuantityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int q = Integer.parseInt(newQuantity.getText().toString());
                InventoryDBHelper dbHelper = new InventoryDBHelper(DetailActivity.this);
                if(q > 0){
                    if(dbHelper.updateQuantity(id, q)) {
                        finalQuantityTextView.setText(q+"");
                    }else{
                        Toast.makeText(DetailActivity.this, "Update Failed", Toast.LENGTH_SHORT).show();
                    }
                }else
                    Toast.makeText(DetailActivity.this, "Enter valid quantity Failed", Toast.LENGTH_SHORT).show();


            }
        });

        //Delete OnClickListener with AlertDialog for Confirmation
        final Button deleteButton = (Button) findViewById(R.id.detail_delete_button);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(DetailActivity.this)
                        .setTitle("Title")
                        .setMessage("Are u sure u want to delete the product?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                InventoryDBHelper dbHelper = new InventoryDBHelper(DetailActivity.this);
                                if(dbHelper.deleteProduct(id)) {
                                    Intent intent = new Intent(DetailActivity.this, MainActivity.class);
                                    startActivity(intent);
                                }else{
                                    Toast.makeText(DetailActivity.this, "Delete Failed", Toast.LENGTH_SHORT).show();

                                }
                            }})
                        .setNegativeButton(android.R.string.no, null).show();
            }
        });

        //Order button to send to mail
        Button orderButton = (Button) findViewById(R.id.detail_order_button);
        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:"));
                intent.putExtra(Intent.EXTRA_EMAIL, R.string.supplier_email);

                InventoryDBHelper dbHelper = new InventoryDBHelper(DetailActivity.this);
                Cursor cursor = dbHelper.readProductDetails(id);
                cursor.moveToFirst();
                String emailText = cursor.getString(1) + "\n" +
                        cursor.getString(2) + "\n" +
                        cursor.getString(3) + "\n" +
                        cursor.getString(5) + "\n";
                intent.putExtra(Intent.EXTRA_TEXT, emailText);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });
    }
}
