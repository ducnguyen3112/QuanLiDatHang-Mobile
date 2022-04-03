package com.example.quanlydathang.ui.KhachHang;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.quanlydathang.R;
import com.example.quanlydathang.dao.KhachHangDao;
import com.example.quanlydathang.dto.KhachHangDto;


public class AddKhachHangActivity extends AppCompatActivity {

    private EditText edtAddNameKH, edtAddAddressKH,edtAddPhoneKH;
    private Button btnAddKH,btnCancelAddKH;
    private KhachHangDao khachHangDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_khach_hang);
        setControl();
        setEvent();
    }

    private void setControl() {
        edtAddNameKH = findViewById(R.id.edtAddNameKH);
        edtAddAddressKH = findViewById(R.id.edtAddAddressKH);
        edtAddPhoneKH = findViewById(R.id.edtAddPhoneKH);
        btnAddKH = findViewById(R.id.btnAddKH);
        btnCancelAddKH = findViewById(R.id.btnCancelAddKH);
        khachHangDao =new KhachHangDao(AddKhachHangActivity.this);
    }
    private void setEvent() {
        btnAddKH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               long result = khachHangDao.addKH(new KhachHangDto(1,edtAddNameKH.getText().toString().trim(),
                       edtAddAddressKH.getText().toString().trim(),
                       edtAddPhoneKH.getText().toString().trim()));

                if (result == -1) {
                    Toast.makeText(AddKhachHangActivity.this, "Thêm thất bại", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(AddKhachHangActivity.this, "Thêm thành công", Toast.LENGTH_LONG).show();

                }
            }
        });
        btnCancelAddKH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AddKhachHangActivity.this, KhachHangActivity.class);
                startActivity(intent);
            }
        });
    }
}