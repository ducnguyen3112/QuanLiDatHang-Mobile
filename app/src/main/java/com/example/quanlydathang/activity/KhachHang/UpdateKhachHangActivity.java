package com.example.quanlydathang.activity.KhachHang;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quanlydathang.R;
import com.example.quanlydathang.dao.KhachHangDao;
import com.example.quanlydathang.dto.KhachHangDto;
import com.example.quanlydathang.utils.CustomToast;

public class UpdateKhachHangActivity extends AppCompatActivity  {
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
                if (validationKH()) {
                    long result = khachHangDao.updateKH(new KhachHangDto(Integer.parseInt(tvUpdateId.getText().toString()), edtUpdateNameKH.getText().toString().trim(),
                            edtUpdateAddressKH.getText().toString().trim(),
                            edtUpdatePhoneKH.getText().toString().trim()));
                    if (result == -1) {
                        CustomToast.makeText(UpdateKhachHangActivity.this, "Cập nhật thất bại",
                                CustomToast.LENGTH_LONG, CustomToast.ERROR).show();
                    } else {
                        CustomToast.makeText(UpdateKhachHangActivity.this, "Cập nhật thành công",
                                CustomToast.LENGTH_LONG, CustomToast.SUCCESS).show();
                    }
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

    boolean validationKH() {
        String nameKh, addressKh, phoneKh;
        nameKh = edtUpdateNameKH.getText().toString().trim();
        addressKh = edtUpdateAddressKH.getText().toString().trim();
        phoneKh = edtUpdatePhoneKH.getText().toString().trim();

        if (nameKh.isEmpty()) {
            CustomToast.makeText(UpdateKhachHangActivity.this, "Không được để trống trường tên",
                    CustomToast.LENGTH_LONG, CustomToast.WARNING).show();
            return false;
        } else if (nameKh.length() < 2) {
            CustomToast.makeText(UpdateKhachHangActivity.this, "Tên khách hàng tối thiểu 2 ký tự",
                    CustomToast.LENGTH_LONG, CustomToast.WARNING).show();
            return false;
        } else if (nameKh.length() > 30) {
            CustomToast.makeText(UpdateKhachHangActivity.this, "Tên khách hàng không 30 ký tự",
                    CustomToast.LENGTH_LONG, CustomToast.WARNING).show();
            return false;
        } else if (addressKh.isEmpty()) {
            CustomToast.makeText(UpdateKhachHangActivity.this, "Không được để trống trường tên",
                    CustomToast.LENGTH_LONG, CustomToast.WARNING).show();
            return false;
        } else if (addressKh.length() < 2) {
            CustomToast.makeText(UpdateKhachHangActivity.this, "Địa chỉ tối thiểu 2 ký tự",
                    CustomToast.LENGTH_LONG, CustomToast.WARNING).show();
            return false;
        } else if (phoneKh.length() > 12) {
            CustomToast.makeText(UpdateKhachHangActivity.this, "Số điện thoại tối đa 11 số",
                    CustomToast.LENGTH_LONG, CustomToast.WARNING).show();
            return false;
        }
        return true;
    }
}