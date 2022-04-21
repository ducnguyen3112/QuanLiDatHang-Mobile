package com.example.quanlydathang.activity.KhachHang;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.quanlydathang.R;
import com.example.quanlydathang.dao.KhachHangDao;
import com.example.quanlydathang.dto.KhachHangDto;
import com.example.quanlydathang.utils.CustomToast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;


public class AddKhachHangActivity extends AppCompatActivity {

    private EditText edtAddNameKH, edtAddAddressKH, edtAddPhoneKH;
    private Button btnAddKH, btnCancelAddKH, btnAddChooseImg;
    private KhachHangDao khachHangDao;
    private ImageView ivAvatar;
    private static final int REQUEST_CODE_FOLDER = 123;


    private ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        Uri uri = data.getData();
                        try {
                            InputStream inputStream = getContentResolver().openInputStream(uri);
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            ivAvatar.setImageBitmap(bitmap);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
    );

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
        ivAvatar = findViewById(R.id.ivAvatar);
        btnAddKH = findViewById(R.id.btnAddKH);
        btnCancelAddKH = findViewById(R.id.btnCancelAddKH);
        btnAddChooseImg = findViewById(R.id.btnAddChooseImg);
        khachHangDao = new KhachHangDao(AddKhachHangActivity.this);
    }

    private void setEvent() {
        btnAddKH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validationKH()) {
                    long result;
                    if (ivAvatar.getDrawable() == null) {
                        result = khachHangDao.addKH(new KhachHangDto(1, edtAddNameKH.getText().toString().trim(),
                                edtAddAddressKH.getText().toString().trim(),
                                edtAddPhoneKH.getText().toString().trim()));
                    } else {
                        BitmapDrawable bitmapDrawable = (BitmapDrawable) ivAvatar.getDrawable();
                        Bitmap bitmap = bitmapDrawable.getBitmap();
                        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArray);
                        byte[] hinh = byteArray.toByteArray();

                        result = khachHangDao.addKH(new KhachHangDto(1, edtAddNameKH.getText().toString().trim(),
                                edtAddAddressKH.getText().toString().trim(),
                                edtAddPhoneKH.getText().toString().trim(), hinh));
                    }
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
                Intent intent = new Intent(AddKhachHangActivity.this, KhachHangActivity.class);
                startActivity(intent);
            }
        });

        btnAddChooseImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] arrayPermissions = {
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                };
                ActivityCompat.requestPermissions(AddKhachHangActivity.this, arrayPermissions, REQUEST_CODE_FOLDER);
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
                    CustomToast.LENGTH_SHORT, CustomToast.WARNING).show();
            return false;
        } else if (nameKh.length() < 2) {
            CustomToast.makeText(AddKhachHangActivity.this, "Tên khách hàng tối thiểu 2 ký tự",
                    CustomToast.LENGTH_SHORT, CustomToast.WARNING).show();
            return false;
        } else if (nameKh.length() > 30) {
            CustomToast.makeText(AddKhachHangActivity.this, "Tên khách hàng không 30 ký tự",
                    CustomToast.LENGTH_SHORT, CustomToast.WARNING).show();
            return false;
        } else if (addressKh.isEmpty()) {
            CustomToast.makeText(AddKhachHangActivity.this, "Không được để trống trường địa chỉ",
                    CustomToast.LENGTH_SHORT, CustomToast.WARNING).show();
            return false;
        } else if (addressKh.length() < 2) {
            CustomToast.makeText(AddKhachHangActivity.this, "Địa chỉ tối thiểu 2 ký tự",
                    CustomToast.LENGTH_SHORT, CustomToast.WARNING).show();
            return false;
        } else if (phoneKh.length() > 12) {
            CustomToast.makeText(AddKhachHangActivity.this, "Số điện thoại tối đa 11 số",
                    CustomToast.LENGTH_SHORT, CustomToast.WARNING).show();
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_FOLDER)
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                activityResultLauncher.launch(intent);
            }
    }
}

