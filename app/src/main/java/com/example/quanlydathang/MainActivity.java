package com.example.quanlydathang;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.quanlydathang.activity.ProductActivity;
import com.example.quanlydathang.activitydonhang.DonDatHangActivity;
import com.example.quanlydathang.ui.KhachHang.KhachHangActivity;

public class MainActivity extends AppCompatActivity {

    private CardView cvDH,cvKH,cvThoat,cardViewSanPham;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cvDH=findViewById(R.id.cvDonHang);
        cvDH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this, DonDatHangActivity.class);
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

        cvThoat=findViewById(R.id.cvThoat);
        cvThoat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("Bạn muốn đăng xuất khỏi ứng dụng ?")
                        .setPositiveButton("Đăng xuất", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        }).setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.create().show();
            }
        });
    }

    private void handleClickCardViewSanPham() {
        cardViewSanPham = findViewById(R.id.cvSanPham);
        cardViewSanPham.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ProductActivity.class);
                startActivity(intent);
            }
        });
    }
}