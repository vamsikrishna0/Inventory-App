package com.example.android.inventoryapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.android.inventoryapp.data.InventoryDBHelper;

import java.io.IOException;

public class AddProductActivity extends AppCompatActivity {
    private int PICK_IMAGE_REQUEST = 1;


    ImageView uploadedImageView ;
    static String imageLocation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        uploadedImageView = (ImageView) findViewById(R.id.uploaded_image);

        //Add image from the gallery
        final ImageButton addImageButton = (ImageButton) findViewById(R.id.add_image_button);
        addImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                if (Build.VERSION.SDK_INT < 19){
                    intent = new Intent();
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    startActivityForResult(intent, PICK_IMAGE_REQUEST);
                } else {
                    intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.setType("image/*");
                    startActivityForResult(intent, PICK_IMAGE_REQUEST);
                }
                addImageButton.setVisibility(View.GONE);
            }

        });

        final EditText addProductNameTextView = (EditText) findViewById(R.id.add_product_name_text_view);
        final EditText addQuantityTextView = (EditText) findViewById(R.id.add_quantity_text_view);

        final EditText addPriceTextView = (EditText) findViewById(R.id.add_price_text_view);
        final EditText addSellerNameTextView = (EditText) findViewById(R.id.add_seller_name_text_view);

        //Add product OnClickListener
        Button addSubmitButton = (Button) findViewById(R.id.add_submit_button);
        addSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!areEmpty(new EditText[]{addProductNameTextView,
                        addQuantityTextView, addSellerNameTextView,addPriceTextView})){

                    InventoryDBHelper dbHelper = new InventoryDBHelper(AddProductActivity.this);
                    boolean status = dbHelper.insertProduct(addProductNameTextView.getText().toString(),
                            Integer.parseInt(addQuantityTextView.getText().toString()),
                            Integer.parseInt(addPriceTextView.getText().toString()),
                            imageLocation,addSellerNameTextView.getText().toString());
                    if(status){
                        Toast.makeText(AddProductActivity.this, "Successfully added", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AddProductActivity.this, MainActivity.class);
                        startActivity(intent);
                    }else
                        Toast.makeText(AddProductActivity.this, "Product not added. Check your input", Toast.LENGTH_SHORT).show();
                }else
                    Toast.makeText(AddProductActivity.this, "Some of the fields are empty", Toast.LENGTH_SHORT).show();

            }
            public boolean areEmpty(EditText[] editText){
                for(EditText x : editText){
                    if(x.getText().toString().equals(""))
                        return true;
                }
                return false;
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            try {
                Uri uri = data.getData();
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                imageLocation = uri.toString();
                uploadedImageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
