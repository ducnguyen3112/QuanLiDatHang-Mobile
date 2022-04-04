package com.example.quanlydathang.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quanlydathang.MainActivity;
import com.example.quanlydathang.R;
import com.example.quanlydathang.database.DBSanPham;
import com.example.quanlydathang.dto.SanPham;
import com.example.quanlydathang.utils.CustomToast;

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
            buttonThemSP.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_edit_24, 0, 0, 0);
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
                Intent main = new Intent(getApplicationContext(), SanPhamActivity.class);
                startActivity(main);
            }
        });
    }

    private boolean checkInput(String maSP, String tenSP, String xuatXu, String donGia) {
        if (maSP.length() == 0) {
            CustomToast.makeText(ThemSPActivity.this,"Chưa nhập mã sản phẩm!!",
                    CustomToast.LENGTH_LONG,CustomToast.WARNING).show();
            return false;
        } else if (tenSP.length() == 0) {
            CustomToast.makeText(ThemSPActivity.this,"Chưa nhập tên sản phẩm!!",
                    CustomToast.LENGTH_LONG,CustomToast.WARNING).show();
            return false;
        } else if (xuatXu.length() == 0) {
            CustomToast.makeText(ThemSPActivity.this,"Chưa nhập xuất xứ sản phẩm!!",
                    CustomToast.LENGTH_LONG,CustomToast.WARNING).show();
            return false;
        } else if (String.valueOf(donGia).length() == 0) {
            CustomToast.makeText(ThemSPActivity.this,"Chưa nhập đơn giá sản phẩm!!",
                    CustomToast.LENGTH_LONG,CustomToast.WARNING).show();
            return false;
        } else if (isNumeric(donGia) == false) {
            CustomToast.makeText(ThemSPActivity.this,"Định dạng đơn giá nhập vào chưa hợp lệ!!",
                    CustomToast.LENGTH_LONG,CustomToast.WARNING).show();
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
                                CustomToast.makeText(ThemSPActivity.this,"Thêm sản phẩm thành công",CustomToast.LENGTH_LONG,CustomToast.SUCCESS).show();
                                finish();
                            } else {
                                CustomToast.makeText(ThemSPActivity.this,"Mã sản phẩm đã tồn tại!!",CustomToast.LENGTH_LONG,CustomToast.WARNING).show();
                            }
                        } catch (Exception e) {
                            CustomToast.makeText(ThemSPActivity.this,e.toString(),CustomToast.LENGTH_LONG,CustomToast.ERROR).show();
                        }
                    }
                    if (isupdate == true) {
                        db.suaSanPham(sanPham);
                        CustomToast.makeText(ThemSPActivity.this,"Sửa thông tin thành công!!",CustomToast.LENGTH_LONG,CustomToast.SUCCESS).show();
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

    /*public void showDialog(String msg){
        Dialog dialog = new Dialog(getApplicationContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_alert);

        TextView text = (TextView) dialog.findViewById(R.id.title);
        text.setText(msg);

        Button buttonDongY = (Button) dialog.findViewById(R.id.buttonDongY);
        buttonDongY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }*/
}