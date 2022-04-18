package com.example.quanlydathang.activity.KhachHang;

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
import com.example.quanlydathang.utils.CustomToast;


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
                if (validationKH()) {
                    long result = khachHangDao.addKH(new KhachHangDto(1, edtAddNameKH.getText().toString().trim(),
                            edtAddAddressKH.getText().toString().trim(),
                            edtAddPhoneKH.getText().toString().trim()));

                    if (result == -1) {
                        CustomToast.makeText(AddKhachHangActivity.this, "Thêm thất bại",
                                CustomToast.LENGTH_LONG, CustomToast.ERROR).show();
                    } else {
                        CustomToast.makeText(AddKhachHangActivity.this, "Thêm thành công",
                                CustomToast.LENGTH_LONG, CustomToast.SUCCESS).show();
                    }
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

    boolean validationKH() {
        String nameKh, addressKh, phoneKh;
        nameKh = edtAddNameKH.getText().toString().trim();
        addressKh = edtAddNameKH.getText().toString().trim();
        phoneKh = edtAddNameKH.getText().toString().trim();

        if (nameKh.isEmpty()) {
            CustomToast.makeText(AddKhachHangActivity.this, "Không được để trống trường tên",
                    CustomToast.LENGTH_LONG, CustomToast.WARNING).show();
            return false;
        } else if (nameKh.length() < 2) {
            CustomToast.makeText(AddKhachHangActivity.this, "Tên khách hàng tối thiểu 2 ký tự",
                    CustomToast.LENGTH_LONG, CustomToast.WARNING).show();
            return false;
        } else if (nameKh.length() > 30) {
            CustomToast.makeText(AddKhachHangActivity.this, "Tên khách hàng không 30 ký tự",
                    CustomToast.LENGTH_LONG, CustomToast.WARNING).show();
            return false;
        } else if (addressKh.isEmpty()) {
            CustomToast.makeText(AddKhachHangActivity.this, "Không được để trống trường tên",
                    CustomToast.LENGTH_LONG, CustomToast.WARNING).show();
            return false;
        } else if (addressKh.length() < 2) {
            CustomToast.makeText(AddKhachHangActivity.this, "Địa chỉ tối thiểu 2 ký tự",
                    CustomToast.LENGTH_LONG, CustomToast.WARNING).show();
            return false;
        } else if (phoneKh.length() > 12) {
            CustomToast.makeText(AddKhachHangActivity.this, "Số điện thoại tối đa 11 số",
                    CustomToast.LENGTH_LONG, CustomToast.WARNING).show();
            return false;
        }
        return true;
    }
}