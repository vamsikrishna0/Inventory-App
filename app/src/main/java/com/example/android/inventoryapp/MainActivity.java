package com.example.android.inventoryapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.inventoryapp.data.InventoryContract;
import com.example.android.inventoryapp.data.InventoryDBHelper;
import com.facebook.stetho.Stetho;

public class MainActivity extends AppCompatActivity {

    static int x;
    static InventoryDBHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(x <= 0 ){
            Stetho.initialize(
                    Stetho.newInitializerBuilder(this)
                            .enableDumpapp(
                                    Stetho.defaultDumperPluginsProvider(this))
                            .enableWebKitInspector(
                                    Stetho.defaultInspectorModulesProvider(this))
                            .build());
            x++;
        }

        dbHelper = new InventoryDBHelper(this);

        final Cursor cursor = dbHelper.readAll();

        InventoryCursorAdapter adapter = new InventoryCursorAdapter(this, R.layout.list_item, cursor, 0, dbHelper);
        ListView listView = (ListView) findViewById(R.id.activity_main);
        TextView emptyTextView = (TextView) findViewById(R.id.empty_list_textview);

        listView.setEmptyView(emptyTextView);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.v("MainActivity", ""+id);
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra("ID", id);
                startActivity(intent);
            }
        });

        Button addProductButton = (Button) findViewById(R.id.add_product_button);
        addProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddProductActivity.class);
                startActivity(intent);
            }
        });

    }
}
