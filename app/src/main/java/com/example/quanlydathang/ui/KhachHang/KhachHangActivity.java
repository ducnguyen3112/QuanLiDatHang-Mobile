package com.example.quanlydathang.ui.KhachHang;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlydathang.MainActivity;
import com.example.quanlydathang.R;
import com.example.quanlydathang.adapter.KhachHangAdapter;
import com.example.quanlydathang.dao.KhachHangDao;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class KhachHangActivity extends AppCompatActivity {

    private FloatingActionButton fabAddKh;
    private RecyclerView rcvKH;
    private KhachHangAdapter khachHangAdapter;
    private KhachHangDao khachHangDao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_khach_hang);
        setControl();
        setEvent();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvKH.setLayoutManager(linearLayoutManager);
        rcvKH.setAdapter(khachHangAdapter);
    }

    private void setControl() {
        fabAddKh = findViewById(R.id.fabAddKH);
        rcvKH = findViewById(R.id.rcvKH);
        khachHangDao = new KhachHangDao(KhachHangActivity.this);
        khachHangAdapter = new KhachHangAdapter(KhachHangActivity.this,khachHangDao.getListKH());
    }
    private void setEvent() {
        fabAddKh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(KhachHangActivity.this, AddKhachHangActivity.class);
                startActivity(intent);
            }
        });
    }
}