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
import com.example.quanlydathang.adapter.SanPhamAdapter;
import com.example.quanlydathang.database.DBSanPham;
import com.example.quanlydathang.dto.SanPham;

import java.util.ArrayList;

public class SanPhamActivity extends AppCompatActivity {
    private ListView listView;
    private ArrayList<SanPham> list = new ArrayList<>();
    private SanPhamAdapter adapter;
    Toolbar toolbar;
    Button buttonAdd;
    ImageButton buttonDelete;
    DBSanPham db;
    int RESULT_PRODUCT_ACTIVITY = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_san_pham);

        db = new DBSanPham(getApplicationContext());
        setControl();
        /*db.QueryData("drop table SANPHAM");*/
        /*db.insertData();*/
        db.loadDb(list);
        adapter = new SanPhamAdapter(SanPhamActivity.this, list);
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
                Intent intent = new Intent(SanPhamActivity.this, ThemSPActivity.class);
                /*intent.putExtra("isupdate", false);*/
                startActivityForResult(intent, RESULT_PRODUCT_ACTIVITY);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0:
                db.loadDb(list);
                adapter.notifyDataSetChanged();
                break;
            default:
                break;
        }
    }
}