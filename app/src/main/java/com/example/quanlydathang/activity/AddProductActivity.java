package com.example.quanlydathang.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.quanlydathang.R;
import com.example.quanlydathang.dao.ProductDao;
import com.example.quanlydathang.dto.Product;
import com.example.quanlydathang.utils.Constants;
import com.example.quanlydathang.utils.CustomToast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class AddProductActivity extends AppCompatActivity {

    EditText editTextTenSP, editTextMaSP, editTextXuatXu, editTextDonGia;
    Button buttonThemSP;
    TextView textViewMaSP;
    boolean isupdate;
    int maSP;
    Toolbar toolbar;
    ImageView imageViewChonAnh, imageView, imageViewSelectFromFile;

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

    private boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
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
        } else if (Integer.parseInt(donGia) <= 0) {
            CustomToast.makeText(AddProductActivity.this, "Đơn giá > 0 !!",
                    CustomToast.LENGTH_LONG, CustomToast.WARNING).show();
            return false;
        }
        return true;
    }

    private void setControl() {
        textViewMaSP = findViewById(R.id.textViewMaSP);
        toolbar = findViewById(R.id.toolbar);
        editTextMaSP = findViewById(R.id.editTextMaSP);
        editTextTenSP = findViewById(R.id.editTextTenSP);
        editTextXuatXu = findViewById(R.id.editTextXuatXuSP);
        editTextDonGia = findViewById(R.id.editTextDonGia);
        buttonThemSP = findViewById(R.id.buttonThemSP);

        imageViewChonAnh = findViewById(R.id.imageViewChonAnh);
        imageView = findViewById(R.id.imageView);

        imageViewSelectFromFile = findViewById(R.id.imageViewSelectFromFile);
    }

    public void handleClickSelectImage() {
        imageViewChonAnh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, Constants.REQUEST_CODE);
            }
        });
    }

    public void handleClickSelectFromImageFile() {
        imageViewSelectFromFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(in, Constants.SelectFromImageFile);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_CODE && resultCode == RESULT_OK) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            imageView.setVisibility(View.VISIBLE);
            imageView.setImageBitmap(bitmap);
        } else if (requestCode == Constants.SelectFromImageFile && resultCode == RESULT_OK && data != null) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                imageView.setVisibility(View.VISIBLE);
                imageView.setImageBitmap(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                CustomToast.makeText(AddProductActivity.this, "Something went wrong!!",
                        CustomToast.LENGTH_LONG, CustomToast.WARNING).show();
            }
        }
    }

    public byte[] ConverttoArrayByte(ImageView img) {
        BitmapDrawable bitmapDrawable = (BitmapDrawable) img.getDrawable();
        Bitmap bitmap = bitmapDrawable.getBitmap();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_sp);
        setControl();
//        Intent intent = new Intent(AddProductActivity.this, ProductActivity.class);
        maSP = getIntent().getIntExtra("MASP", 0);
        if (maSP != 0) {
            imageView.setVisibility(View.VISIBLE);
            toolbar.setTitle("Sửa thông tin");
            ProductDao db = new ProductDao(getApplicationContext());
            editTextMaSP.setEnabled(false);
            isupdate = true;
            Product product = db.getProduct(maSP);
            setInfo(product);
            buttonThemSP.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_edit_24, 0, 0, 0);
            buttonThemSP.setText("Sửa thông tin");
        }
        if (maSP == 0) {
            imageView.setVisibility(View.INVISIBLE);
            textViewMaSP.setVisibility(View.INVISIBLE);
            editTextMaSP.setVisibility(View.INVISIBLE);
        }

        handleClickButtonThemSP();
        setActionBar();
        handleClickSelectImage();
        handleClickSelectFromImageFile();
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
                    if (isupdate == false) {
                        try {
                            db.insertProduct(tenSP, xuatXu,
                                    Integer.parseInt(donGia), ConverttoArrayByte(imageView));

                            CustomToast.makeText(AddProductActivity.this, "Thêm sản phẩm thành công", CustomToast.LENGTH_LONG, CustomToast.SUCCESS).show();
                            Intent intent = new Intent(AddProductActivity.this, ProductActivity.class);
                            startActivityForResult(intent, Constants.RESULT_PRODUCT_ACTIVITY);
                        } catch (Exception e) {
                            CustomToast.makeText(AddProductActivity.this, e.toString(), CustomToast.LENGTH_LONG, CustomToast.ERROR).show();
                        }
                    }
                    if (isupdate == true) {
                        String maSP = editTextMaSP.getText().toString();
                        db.updateProduct(Integer.parseInt(maSP), tenSP, xuatXu,
                                Integer.parseInt(donGia), ConverttoArrayByte(imageView));
                        CustomToast.makeText(AddProductActivity.this, "Sửa thông tin thành công!!", CustomToast.LENGTH_LONG, CustomToast.SUCCESS).show();
                        Intent intent = new Intent(AddProductActivity.this, ProductActivity.class);
                        startActivityForResult(intent, Constants.RESULT_PRODUCT_ACTIVITY);
                    }
                }
            }
        });
    }

    private void setInfo(Product product) {
        editTextMaSP.setText(product.getMaSP() + "");
        editTextTenSP.setText(product.getTenSP());
        editTextXuatXu.setText(product.getXuatXu());
        editTextDonGia.setText(String.valueOf(product.getDonGia()));
        if (product.getImage() != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(product.getImage(), 0, product.getImage().length);
            imageView.setVisibility(View.VISIBLE);
            imageView.setImageBitmap(bitmap);
        }
    }
}