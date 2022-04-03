package com.example.quanlydathang;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.quanlydathang.database.CreateDatabase;
import com.example.quanlydathang.ui.KhachHang.KhachHangActivity;

public class MainActivity extends AppCompatActivity {

<<<<<<< HEAD
    private CardView cvDH;
    private CardView cardViewSanPham;

=======
    private CardView cvDH,cvKH;
>>>>>>> 935b8f2e028d44fd17021a30f1389b9334cdbc69
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
<<<<<<< HEAD

        handleClickCardViewSanPham();
    }

    private void handleClickCardViewSanPham() {
        cardViewSanPham = findViewById(R.id.cvSanPham);
        cardViewSanPham.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SanPhamActivity.class);
=======
        cvKH = findViewById(R.id.cvKhachHang);
        cvKH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this, KhachHangActivity.class);
>>>>>>> 935b8f2e028d44fd17021a30f1389b9334cdbc69
                startActivity(intent);
            }
        });
    }
}