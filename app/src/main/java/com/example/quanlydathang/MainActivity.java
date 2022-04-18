package com.example.quanlydathang;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.quanlydathang.activity.ProductActivity;
import com.example.quanlydathang.activitydonhang.DonDatHangActivity;
import com.example.quanlydathang.dao.UserDao;
import com.example.quanlydathang.activity.KhachHang.KhachHangActivity;

public class MainActivity extends AppCompatActivity {

    private CardView cvDH,cvKH,cvThoat,cardViewSanPham;
    UserDao userDao;
    TextView tvUserName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cvDH=findViewById(R.id.cvDonHang);
        userDao=new UserDao(this);
        getDataIntent();
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
                xacNhapThoatDialog();
            }
        });
    }

    private void xacNhapThoatDialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Bạn muốn đăng xuất khỏi ứng dụng ?")
                .setPositiveButton("Đăng xuất", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent=new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);

                    }
                }).setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.create().show();
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
     private void getDataIntent(){
        String sdt=getIntent().getStringExtra("phoneNumber");
         Log.e("SDT", ""+sdt ,null );

        String userNameLogin=getIntent().getStringExtra("userNameLogin");
         Log.e("userNameLogin", ""+userNameLogin ,null );
        String userName="";
        if (sdt==null){
            userName=userNameLogin;
        }else if (userNameLogin==null){
            userName =userDao.getUserNameFromSDT(sdt);
        }

         Log.e("loginusername", "getDataIntent: "+userName ,null );
        tvUserName=findViewById(R.id.tvUserNameMain);
        tvUserName.setText(userName);
     }

    @Override
    public void onBackPressed() {
        xacNhapThoatDialog();
    }

    @Override
    protected void onResume() {
        tvUserName.setText(LoginActivity.userNameLg);
        super.onResume();

    }
}