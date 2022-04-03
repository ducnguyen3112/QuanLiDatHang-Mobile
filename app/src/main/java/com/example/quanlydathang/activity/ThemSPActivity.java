package com.example.quanlydathang.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.quanlydathang.MainActivity;
import com.example.quanlydathang.R;
import com.example.quanlydathang.database.DBSanPham;
import com.example.quanlydathang.dto.SanPham;

import java.security.spec.ECField;
import java.util.ArrayList;

public class ThemSPActivity extends AppCompatActivity {

    EditText editTextTenSP, editTextMaSP, editTextXuatXu, editTextDonGia;
    Button buttonThemSP;
    boolean isupdate;
    String maSP;

    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_sp);
        setControl();

        Intent intent = new Intent(ThemSPActivity.this, SanPhamActivity.class);

        maSP = getIntent().getStringExtra("MASP");
        if (maSP != null) {
            toolbar.setTitle("Sửa thông tin");
            DBSanPham db = new DBSanPham(getApplicationContext());
            editTextMaSP.setEnabled(false);
            isupdate = true;
            SanPham sanPham = db.laySanPham(maSP);
            setInfo(sanPham);
            buttonThemSP.setText("Sửa thông tin");
        }

        handleClickButtonThemSP();
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

    private boolean checkInput(String maSP, String tenSP, String xuatXu, String donGia) {
        if (maSP.length() == 0) {
            Toast.makeText(getApplicationContext(), "Chưa nhập mã sản phẩm!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (tenSP.length() == 0) {
            Toast.makeText(getApplicationContext(), "Chưa nhập tên sản phẩm!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (xuatXu.length() == 0) {
            Toast.makeText(getApplicationContext(), "Chưa nhập xuất xứ sản phẩm!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (String.valueOf(donGia).length() == 0) {
            Toast.makeText(getApplicationContext(), "Chưa nhập đon giá sản phẩm!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (isNumeric(donGia) == false) {
            Toast.makeText(getApplicationContext(), "Định dạng đơn giá nhập vào chưa hợp lệ!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void handleClickButtonThemSP() {
        buttonThemSP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String maSP = editTextMaSP.getText().toString();
                String tenSP = editTextTenSP.getText().toString();
                String xuatXu = editTextXuatXu.getText().toString();
                String donGia = editTextDonGia.getText().toString();
                if (checkInput(maSP, tenSP, xuatXu, donGia) == true) {
                    DBSanPham db = new DBSanPham(getApplicationContext());
                    SanPham sanPham = new SanPham(maSP, tenSP, xuatXu, Integer.parseInt(donGia));
                    if (isupdate == false) {
                        try {
                            if (db.laySanPham(maSP) == null) {
                                db.themSanPham(sanPham);
                                Toast.makeText(getApplicationContext(), "Thêm sản phẩm thành công!!", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), "Mã sản phẩm đã tồn tại!!", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    if (isupdate == true) {
                        db.suaSanPham(sanPham);
                        Toast.makeText(getApplicationContext(), "Sửa thông tin thành công!!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            }
        });
    }

    private void setControl() {
        toolbar = findViewById(R.id.toolbar);
        editTextMaSP = findViewById(R.id.editTextMaSP);
        editTextTenSP = findViewById(R.id.editTextTenSP);
        editTextXuatXu = findViewById(R.id.editTextXuatXuSP);
        editTextDonGia = findViewById(R.id.editTextDonGia);
        buttonThemSP = findViewById(R.id.buttonThemSP);
    }

    private void setInfo(SanPham sanPham) {
        editTextMaSP.setText(sanPham.getMaSP());
        editTextTenSP.setText(sanPham.getTenSP());
        editTextXuatXu.setText(sanPham.getXuatXu());
        editTextDonGia.setText(String.valueOf(sanPham.getDonGia()));
    }
}