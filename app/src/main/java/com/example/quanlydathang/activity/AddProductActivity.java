package com.example.quanlydathang.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.quanlydathang.R;
import com.example.quanlydathang.dao.ProductDao;
import com.example.quanlydathang.dto.Product;
import com.example.quanlydathang.utils.CustomToast;

public class AddProductActivity extends AppCompatActivity {

    EditText editTextTenSP, editTextMaSP, editTextXuatXu, editTextDonGia;
    Button buttonThemSP;
    TextView textViewMaSP;
    boolean isupdate;
    int maSP;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_sp);
        setControl();

        Intent intent = new Intent(AddProductActivity.this, ProductActivity.class);
        maSP = getIntent().getIntExtra("MASP", 0);

        if (maSP != 0) {
            toolbar.setTitle("Sửa thông tin");
            ProductDao db = new ProductDao(getApplicationContext());
            editTextMaSP.setEnabled(false);
            isupdate = true;
            Product sanPham = db.getProduct(maSP);
            setInfo(sanPham);
            buttonThemSP.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_edit_24, 0, 0, 0);
            buttonThemSP.setText("Sửa thông tin");
        }
        if (maSP == 0) {
            textViewMaSP.setVisibility(View.INVISIBLE);
            editTextMaSP.setVisibility(View.INVISIBLE);
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
                Intent main = new Intent(getApplicationContext(), ProductActivity.class);
                startActivity(main);
            }
        });
    }

    private boolean checkInput(String tenSP, String xuatXu, String donGia) {
        if (tenSP.length() == 0) {
            CustomToast.makeText(AddProductActivity.this, "Chưa nhập tên sản phẩm!!",
                    CustomToast.LENGTH_LONG, CustomToast.WARNING).show();
            return false;
        } else if (xuatXu.length() == 0) {
            CustomToast.makeText(AddProductActivity.this, "Chưa nhập xuất xứ sản phẩm!!",
                    CustomToast.LENGTH_LONG, CustomToast.WARNING).show();
            return false;
        } else if (String.valueOf(donGia).length() == 0) {
            CustomToast.makeText(AddProductActivity.this, "Chưa nhập đơn giá sản phẩm!!",
                    CustomToast.LENGTH_LONG, CustomToast.WARNING).show();
            return false;
        } else if (isNumeric(donGia) == false) {
            CustomToast.makeText(AddProductActivity.this, "Định dạng đơn giá nhập vào chưa hợp lệ!!",
                    CustomToast.LENGTH_LONG, CustomToast.WARNING).show();
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
                String tenSP = editTextTenSP.getText().toString();
                String xuatXu = editTextXuatXu.getText().toString();
                String donGia = editTextDonGia.getText().toString();
                if (checkInput(tenSP, xuatXu, donGia) == true) {
                    ProductDao db = new ProductDao(getApplicationContext());
                    Product sanPham = new Product(tenSP, xuatXu, Integer.parseInt(donGia));
                    if (isupdate == false) {
                        try {
                            db.addProduct(sanPham);
                            CustomToast.makeText(AddProductActivity.this, "Thêm sản phẩm thành công", CustomToast.LENGTH_LONG, CustomToast.SUCCESS).show();
                            finish();
                        } catch (Exception e) {
                            CustomToast.makeText(AddProductActivity.this, e.toString(), CustomToast.LENGTH_LONG, CustomToast.ERROR).show();
                        }
                    }
                    if (isupdate == true) {
                        String maSP = editTextMaSP.getText().toString();
                        Product sp = new Product(Integer.parseInt(maSP),tenSP, xuatXu, Integer.parseInt(donGia));
                        db.updateProduct(sp);
                        CustomToast.makeText(AddProductActivity.this, "Sửa thông tin thành công!!", CustomToast.LENGTH_LONG, CustomToast.SUCCESS).show();
                        finish();
                    }
                }
            }
        });
    }

    private void setControl() {
        textViewMaSP = findViewById(R.id.textViewMaSP);
        toolbar = findViewById(R.id.toolbar);
        editTextMaSP = findViewById(R.id.editTextMaSP);
        editTextTenSP = findViewById(R.id.editTextTenSP);
        editTextXuatXu = findViewById(R.id.editTextXuatXuSP);
        editTextDonGia = findViewById(R.id.editTextDonGia);
        buttonThemSP = findViewById(R.id.buttonThemSP);
    }

    private void setInfo(Product sanPham) {
        editTextMaSP.setText(sanPham.getMaSP() + "");
        editTextTenSP.setText(sanPham.getTenSP());
        editTextXuatXu.setText(sanPham.getXuatXu());
        editTextDonGia.setText(String.valueOf(sanPham.getDonGia()));
    }
}