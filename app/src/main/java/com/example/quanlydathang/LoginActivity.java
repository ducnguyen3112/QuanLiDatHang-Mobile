package com.example.quanlydathang;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlydathang.activity.AddProductActivity;
import com.example.quanlydathang.dao.UserDao;
import com.example.quanlydathang.database.CreateDatabase;
import com.example.quanlydathang.utils.CustomToast;

public class LoginActivity extends AppCompatActivity {
    private UserDao userDao;
    private Button btnLogin;
    private EditText etUsername;
    private EditText etPasswd;
    private TextView tvLoginOTP;

    boolean doubleBackToExitPressedOnce = false;
    public static String userNameLg;
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        anhXa();

        userDao =new UserDao(this);
        btnLogin.setOnClickListener(view -> {
            btnLogin.startAnimation(buttonClick);
            Boolean check=false;
            if (etUsername.getText().toString().isEmpty()){
                CustomToast.makeText(LoginActivity.this, "Không được để trống tên đăng nhập!",
                        CustomToast.LENGTH_LONG, CustomToast.WARNING).show();
                return;
            }else if (etPasswd.getText().toString().isEmpty()){
                CustomToast.makeText(LoginActivity.this, "Không được để trống tên mật khẩu!",
                        CustomToast.LENGTH_LONG, CustomToast.WARNING).show();
                return;
            }else {
                check = userDao.kiemTraDangNhap(
                        etUsername.getText().toString().toLowerCase(),
                        etPasswd.getText().toString()
                );
            }
            if (true){

                CustomToast.makeText(LoginActivity.this, "Đăng nhập thành công!",
                        CustomToast.LENGTH_LONG, CustomToast.SUCCESS).show();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                userNameLg=etUsername.getText().toString();
                intent.putExtra("userNameLogin",userNameLg );
                startActivity(intent);
            }else{

                CustomToast.makeText(LoginActivity.this, "Sai tên đăng nhập hoặc mật khẩu!",
                        CustomToast.LENGTH_LONG, CustomToast.ERROR).show();
            }

        });
        tvLoginOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SendOTPActivity.class);
                startActivity(intent);
            }
        });
    }
    private void anhXa() {
        btnLogin = findViewById(R.id.btnLogin);
        etUsername=findViewById(R.id.etUser);
        etPasswd=findViewById(R.id.etPasswd);
        tvLoginOTP=findViewById(R.id.tvLoginBangOTP);

    }
    @Override
    public void onBackPressed(){
        if (doubleBackToExitPressedOnce){
            finish();
            System.exit(0);
            return;
        }
        this.doubleBackToExitPressedOnce=true;
        Toast.makeText(this, "Nhấn BACK một lần nữa để thoát!", Toast.LENGTH_SHORT).show();
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}