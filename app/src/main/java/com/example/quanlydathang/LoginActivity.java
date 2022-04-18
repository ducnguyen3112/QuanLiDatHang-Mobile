package com.example.quanlydathang;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlydathang.dao.UserDao;

public class LoginActivity extends AppCompatActivity {
    SQLiteDatabase database;
    private UserDao userDao;
    private Button btnLogin;
    private EditText etUsername;
    private EditText etPasswd;
    private TextView tvresendOTP;
    private LinearLayout llResendOTP;
    private Button btnsendOTP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        anhXa();

        userDao =new UserDao(this);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean check=userDao.kiemTraDangNhap(
                        etUsername.getText().toString().toLowerCase(),
                        etPasswd.getText().toString()
                );
                if (true){
                    Toast.makeText(LoginActivity.this
                            ,"Đăng nhập thành công!",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(LoginActivity.this
                            ,"Sai tên đăng nhập hoặc mật khẩu!",Toast.LENGTH_SHORT).show();
                }

            }
        });
        btnsendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llResendOTP.setVisibility(View.VISIBLE);
            }
        });
    }
    private void anhXa() {
        btnLogin = findViewById(R.id.btnLogin);
        etUsername=findViewById(R.id.etUser);
        etPasswd=findViewById(R.id.etPasswd);
        tvresendOTP=findViewById(R.id.tvGuiLaiOTP);
        llResendOTP=findViewById(R.id.llresendOTP);
        btnsendOTP=findViewById(R.id.btnsendOTP);
    }
}