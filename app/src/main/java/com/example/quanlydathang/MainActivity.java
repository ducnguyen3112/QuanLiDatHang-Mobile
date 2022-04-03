package com.example.quanlydathang;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.quanlydathang.activity.SanPhamActivity;
import com.example.quanlydathang.database.CreateDatabase;
import com.example.quanlydathang.ui.KhachHang.KhachHangActivity;

public class MainActivity extends AppCompatActivity {

    private CardView cvDH,cvKH;
    private CardView cardViewSanPham;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cvDH=findViewById(R.id.cvDonHang);
        cvDH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,DonDatHangActivity.class);
                startActivity(intent);
            }
        });
        cvKH = findViewById(R.id.cvKhachHang);
        cvKH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this, KhachHangActivity.class);
                startActivity(intent);
            }
        });

        handleClickCardViewSanPham();
    }

    private void handleClickCardViewSanPham() {
        cardViewSanPham = findViewById(R.id.cvSanPham);
        cardViewSanPham.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SanPhamActivity.class);
                startActivity(intent);
            }
        });
    }
}