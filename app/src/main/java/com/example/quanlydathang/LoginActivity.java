package com.example.quanlydathang;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlydathang.dao.UserDao;

public class LoginActivity extends AppCompatActivity {
    private UserDao userDao;
    private Button btnLogin;
    private EditText etUsername;
    private EditText etPasswd;
    private TextView tvLoginOTP;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        anhXa();

        userDao =new UserDao(this);
        btnLogin.setOnClickListener(view -> {
            Boolean check=false;
            if (etUsername.getText().toString().isEmpty()){
                Toast.makeText(LoginActivity.this
                        ,"Không được để trống tên đăng nhập!",Toast.LENGTH_SHORT).show();
                return;
            }else if (etPasswd.getText().toString().isEmpty()){
                Toast.makeText(LoginActivity.this
                        ,"Không được để trống mật khẩu!",Toast.LENGTH_SHORT).show();
                return;
            }else {
                check = userDao.kiemTraDangNhap(
                        etUsername.getText().toString().toLowerCase(),
                        etPasswd.getText().toString()
                );
            }
            if (check){
                Toast.makeText(LoginActivity.this
                        ,"Đăng nhập thành công!",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }else{
                Toast.makeText(LoginActivity.this
                        ,"Sai tên đăng nhập hoặc mật khẩu!",Toast.LENGTH_SHORT).show();
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
}