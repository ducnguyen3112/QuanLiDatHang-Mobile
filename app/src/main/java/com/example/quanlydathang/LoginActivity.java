package com.example.quanlydathang;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.quanlydathang.dao.DonHangDao;
import com.example.quanlydathang.dao.UserDao;
import com.example.quanlydathang.database.CreateDatabase;
import com.example.quanlydathang.dto.DonHangDto;
import com.example.quanlydathang.dto.UserDto;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    SQLiteDatabase database;
    private UserDao userDao;
    private Button btnLogin;
    private EditText etUsername;
    private EditText etPasswd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnLogin = findViewById(R.id.btnLogin);
        etUsername=findViewById(R.id.etUser);
        etPasswd=findViewById(R.id.etPasswd);
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
    }
}