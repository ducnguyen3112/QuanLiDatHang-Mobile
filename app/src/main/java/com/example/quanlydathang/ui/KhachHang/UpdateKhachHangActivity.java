package com.example.quanlydathang.ui.KhachHang;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quanlydathang.R;
import com.example.quanlydathang.dao.KhachHangDao;
import com.example.quanlydathang.dto.KhachHangDto;

public class UpdateKhachHangActivity extends AppCompatActivity {
    private TextView tvUpdateId;
    private EditText edtUpdateNameKH, edtUpdateAddressKH, edtUpdatePhoneKH;
    private Button btnUpdateKH, btnCancelUpdateKH;
    private KhachHangDao khachHangDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_khach_hang);
        setControl();
        getAndSetIntentData();
        setEvent();

    }


    void getAndSetIntentData() {
        Intent intent = getIntent();
        KhachHangDto dto = (KhachHangDto) intent.getSerializableExtra("KH");
        tvUpdateId.setText(String.valueOf(dto.getId()));
        edtUpdateNameKH.setText(dto.getName());
        edtUpdateAddressKH.setText(dto.getAddress());
        edtUpdatePhoneKH.setText(dto.getPhone());
    }

    private void setControl() {
        tvUpdateId = findViewById(R.id.tvUpdateId);
        edtUpdateNameKH = findViewById(R.id.edtUpdateNameKH);
        edtUpdateAddressKH = findViewById(R.id.edtUpdateAddressKH);
        edtUpdatePhoneKH = findViewById(R.id.edtUpdatePhoneKH);
        btnUpdateKH = findViewById(R.id.btnUpdateKH);
        btnCancelUpdateKH = findViewById(R.id.btnCancelUpdateKH);
        khachHangDao = new KhachHangDao(UpdateKhachHangActivity.this);
    }

    private void setEvent() {
        btnUpdateKH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long result = khachHangDao.updateKH(new KhachHangDto(Integer.parseInt(tvUpdateId.getText().toString()), edtUpdateNameKH.getText().toString().trim(),
                        edtUpdateAddressKH.getText().toString().trim(),
                        edtUpdatePhoneKH.getText().toString().trim()));
                if (result == -1) {
                    Toast.makeText(UpdateKhachHangActivity.this, "Cập nhật thất bại", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(UpdateKhachHangActivity.this, "Cập nhật thành công", Toast.LENGTH_LONG).show();

                }
            }
        });
        btnCancelUpdateKH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UpdateKhachHangActivity.this, KhachHangActivity.class);
                startActivity(intent);
            }
        });

    }
}