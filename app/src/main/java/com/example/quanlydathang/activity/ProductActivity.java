package com.example.quanlydathang.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.quanlydathang.MainActivity;
import com.example.quanlydathang.R;
import com.example.quanlydathang.adapter.ProductAdapter;
import com.example.quanlydathang.dao.ProductDao;
import com.example.quanlydathang.dto.Product;

import java.util.ArrayList;

public class ProductActivity extends AppCompatActivity {
    private ListView listView;
    private ArrayList<Product> list = new ArrayList<>();
    private ProductAdapter adapter;
    Toolbar toolbar;
    Button buttonAdd;
    ImageButton buttonDelete;
    ProductDao dao;
    int RESULT_PRODUCT_ACTIVITY = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_san_pham);
        setControl();
        dao = new ProductDao(getApplicationContext());
        dao.loadDb(list);
        adapter = new ProductAdapter(ProductActivity.this, list);
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);

        handleClickAdd();
        setActionBar();
    }

    private void setActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent main = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(main);
            }
        });
    }

    private void setControl() {
        toolbar = findViewById(R.id.toolbar);
        listView = findViewById(R.id.listView);
        buttonAdd = findViewById(R.id.buttonAdd);
        buttonDelete = findViewById(R.id.buttonDelete);
    }

    public void handleClickAdd() {
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProductActivity.this, AddProductActivity.class);
                intent.putExtra("MASP",0);
                startActivityForResult(intent, RESULT_PRODUCT_ACTIVITY);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0:
                dao.loadDb(list);
                adapter.notifyDataSetChanged();
                break;
            default:
                break;
        }
    }
}