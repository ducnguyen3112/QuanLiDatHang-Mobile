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
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quanlydathang.R;
import com.example.quanlydathang.dao.KhachHangDao;
import com.example.quanlydathang.dto.KhachHangDto;
import com.example.quanlydathang.utils.CustomToast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class UpdateKhachHangActivity extends AppCompatActivity {
    private TextView tvUpdateId;
    private EditText edtUpdateNameKH, edtUpdateAddressKH, edtUpdatePhoneKH;
    private Button btnUpdateKH, btnCancelUpdateKH, btnEditChooseImg;
    private KhachHangDao khachHangDao;
    private ImageView ivEditAvatar;
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
                            ivEditAvatar.setImageBitmap(bitmap);
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
        setContentView(R.layout.activity_update_khach_hang);
        setControl();
        getAndSetIntentData();
        setEvent();
    }



    void getAndSetIntentData() {
        Intent intent = getIntent();
        int id = Integer.parseInt(intent.getStringExtra("idKH"));
        KhachHangDto dto = khachHangDao.getKHById(id);
        if (dto.getImage() != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(dto.getImage(), 0, dto.getImage().length);
            ivEditAvatar.setVisibility(View.VISIBLE);
            ivEditAvatar.setImageBitmap(bitmap);
        }
        tvUpdateId.setText(String.valueOf(dto.getId()));
        edtUpdateNameKH.setText(dto.getName());
        edtUpdateAddressKH.setText(dto.getAddress());
        edtUpdatePhoneKH.setText(dto.getPhone());
    }

    private void setControl() {
        tvUpdateId = findViewById(R.id.tvUpdateId);
        ivEditAvatar = findViewById(R.id.ivEditAvatar);
        edtUpdateNameKH = findViewById(R.id.edtUpdateNameKH);
        edtUpdateAddressKH = findViewById(R.id.edtUpdateAddressKH);
        edtUpdatePhoneKH = findViewById(R.id.edtUpdatePhoneKH);
        btnUpdateKH = findViewById(R.id.btnUpdateKH);
        btnCancelUpdateKH = findViewById(R.id.btnCancelUpdateKH);
        btnEditChooseImg = findViewById(R.id.btnEditChooseImg);
        khachHangDao = new KhachHangDao(UpdateKhachHangActivity.this);
    }

    private void setEvent() {
        btnCancelUpdateKH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UpdateKhachHangActivity.this, KhachHangActivity.class);
                startActivity(intent);
            }
        });


        btnEditChooseImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] arrayPermissions = {
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                };
                ActivityCompat.requestPermissions(UpdateKhachHangActivity.this, arrayPermissions, REQUEST_CODE_FOLDER);
            }
        });

        btnUpdateKH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validationKH()) {
                    long result;
                    if (ivEditAvatar.getDrawable() == null) {
                        result = khachHangDao.updateKH(new KhachHangDto(Integer.parseInt(tvUpdateId.getText().toString()), edtUpdateNameKH.getText().toString().trim(),
                                edtUpdateAddressKH.getText().toString().trim(),
                                edtUpdatePhoneKH.getText().toString().trim()));
                    } else {
                        BitmapDrawable bitmapDrawable = (BitmapDrawable) ivEditAvatar.getDrawable();
                        Bitmap bitmap = bitmapDrawable.getBitmap();
                        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArray);
                        byte[] hinh = byteArray.toByteArray();

                        result = khachHangDao.updateKH(new KhachHangDto(Integer.parseInt(tvUpdateId.getText().toString()), edtUpdateNameKH.getText().toString().trim(),
                                edtUpdateAddressKH.getText().toString().trim(),
                                edtUpdatePhoneKH.getText().toString().trim(), hinh));
                    }

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
    }

    boolean validationKH() {
        String nameKh, addressKh, phoneKh;
        nameKh = edtUpdateNameKH.getText().toString().trim();
        addressKh = edtUpdateAddressKH.getText().toString().trim();
        phoneKh = edtUpdatePhoneKH.getText().toString().trim();

        if (nameKh.isEmpty()) {
            CustomToast.makeText(UpdateKhachHangActivity.this, "Không được để trống trường tên",
                    CustomToast.LENGTH_SHORT, CustomToast.WARNING).show();
            return false;
        } else if (nameKh.length() < 2) {
            CustomToast.makeText(UpdateKhachHangActivity.this, "Tên khách hàng tối thiểu 2 ký tự",
                    CustomToast.LENGTH_SHORT, CustomToast.WARNING).show();
            return false;
        } else if (nameKh.length() > 30) {
            CustomToast.makeText(UpdateKhachHangActivity.this, "Tên khách hàng không 30 ký tự",
                    CustomToast.LENGTH_SHORT, CustomToast.WARNING).show();
            return false;
        } else if (addressKh.isEmpty()) {
            CustomToast.makeText(UpdateKhachHangActivity.this, "Không được để trống trường địa chỉ",
                    CustomToast.LENGTH_SHORT, CustomToast.WARNING).show();
            return false;
        } else if (addressKh.length() < 2) {
            CustomToast.makeText(UpdateKhachHangActivity.this, "Địa chỉ tối thiểu 2 ký tự",
                    CustomToast.LENGTH_SHORT, CustomToast.WARNING).show();
            return false;
        } else if (phoneKh.length() > 12) {
            CustomToast.makeText(UpdateKhachHangActivity.this, "Số điện thoại tối đa 11 số",
                    CustomToast.LENGTH_SHORT, CustomToast.WARNING).show();
            return false;
        } else if (phoneKh.length() < 10) {
            CustomToast.makeText(UpdateKhachHangActivity.this, "Số điện thoại tối thiểu 10 số",
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