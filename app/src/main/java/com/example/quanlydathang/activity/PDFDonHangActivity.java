package com.example.quanlydathang.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.quanlydathang.R;
import com.example.quanlydathang.activitydonhang.DonDatHangActivity;
import com.example.quanlydathang.adapter.PdfAdapter;
import com.example.quanlydathang.dao.ProductDao;
import com.example.quanlydathang.dto.DonHangDto;
import com.example.quanlydathang.dto.KhachHangDto;
import com.example.quanlydathang.dto.PDFDonHang;
import com.example.quanlydathang.utils.Constants;
import com.example.quanlydathang.utils.CustomToast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class PDFDonHangActivity extends AppCompatActivity {

    TextView user_name, user_phone, user_address, id, date, total;
    RecyclerView recyclerView;
    Button button,viewpdf;
    Toolbar toolbar;

    KhachHangDto khachHangDto;
    ProductDao dao;
    DonHangDto donHangDto;

    PdfDocument document;
    ArrayList<PDFDonHang> list = new ArrayList<>();
    PdfAdapter pdfAdapter;

    String path;
    int totalProduct = 0;
    int pageWith = 400;
    int sum = 0;
    int idDH = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_donhang);
        dao = new ProductDao(getApplicationContext());

        setControl();
        idDH = getIntent().getIntExtra("id", 1);
        getData();
        setInfo(khachHangDto, donHangDto);
        setActionBar();

        ActivityCompat.requestPermissions(PDFDonHangActivity.this, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED
        );

        handleClickSavePDF();
        handleClickViewPDF();
    }

    private void getData() {

        donHangDto = dao.getDonHang(idDH);
        khachHangDto = dao.getUser(donHangDto.getMaDH());
        totalProduct = dao.getDonHangInfo(list, donHangDto.getMaDH());
    }

    private void handleClickViewPDF() {
        viewpdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(PDFDonHangActivity.this, ViewPDFActivity.class);
                intent1.putExtra("path", path);
                intent1.putExtra("id", idDH);
                startActivity(intent1);
            }
        });
    }

    private void handleClickSavePDF() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (totalProduct > 0) {
                    createPDFFromText();
                }
            }
        });
    }

    private void setControl() {
        user_name = findViewById(R.id.user_name);
        user_phone = findViewById(R.id.user_phone);
        user_address = findViewById(R.id.user_address);
        id = findViewById(R.id.id);
        date = findViewById(R.id.date);
        total = findViewById(R.id.total);
        recyclerView = findViewById(R.id.recyclerView);
        button = findViewById(R.id.button);
        viewpdf = findViewById(R.id.viewpdf);
        viewpdf.setEnabled(false);
        viewpdf.setBackgroundColor(Color.GRAY);
        toolbar = findViewById(R.id.toolbar);

        pdfAdapter = new PdfAdapter(this, list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(pdfAdapter);

    }

    private void setInfo(KhachHangDto khachHangDto, DonHangDto donHangDto) {
        user_name.setText(khachHangDto.getName());
        user_phone.setText(khachHangDto.getPhone());
        user_address.setText(khachHangDto.getAddress());
        id.setText(donHangDto.getMaDH() + "");
        date.setText(donHangDto.getNgayDH());
        total.setText(total() + "");
    }

    public int total() {
        int totallist = 0;
        for (PDFDonHang item : list) {
            totallist += item.getProduct().getDonGia() * item.getChiTietDonHang().getSoLuong();
        }
        return totallist;
    }

    private void setActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent main = new Intent(getApplicationContext(), DonDatHangActivity.class);
                startActivity(main);
            }
        });
    }

    private void createPDFFromText() {
        document = new PdfDocument();
        Paint paint = new Paint();
        Paint paintTittle = new Paint();

        PdfDocument.PageInfo myPageInfo = new
                PdfDocument.PageInfo.Builder(400, 600, 1)
                .create();
        PdfDocument.Page page = document.startPage(myPageInfo);
        Canvas canvas = page.getCanvas();

        paintTittle.setTextAlign(Paint.Align.CENTER);
        paintTittle.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        canvas.drawText("THÔNG TIN ĐƠN HÀNG", pageWith / 2, 30, paintTittle);
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setColor(Color.BLACK);
        canvas.drawText("Tên khách hàng: " + khachHangDto.getName(),
                20, 60, paint);

        canvas.drawText("Số điện thoại: " + khachHangDto.getPhone(),
                20, 90, paint);

        canvas.drawText("Địa chỉ: " + khachHangDto.getAddress(),
                20, 120, paint);

        paint.setTextAlign(Paint.Align.RIGHT);
        paint.setColor(Color.BLACK);
        canvas.drawText("Mã số hóa đơn: " + donHangDto.getMaDH(),
                pageWith - 20, 60, paint);

        canvas.drawText(donHangDto.getNgayDH(),
                pageWith - 20, 90, paint);

        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(1);
        canvas.drawRect(20, 140, pageWith - 20, 180, paint);

        paint.setTextAlign(Paint.Align.LEFT);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawText("Mã", 40, 160, paint);
        canvas.drawText("Tên sản phẩm ", 80, 160, paint);
        canvas.drawText("Đơn giá (đ)", 170, 160, paint);
        canvas.drawText("Số lượng", 240, 160, paint);
        canvas.drawText("Tổng ", 300, 160, paint);

        int width = 0;
        for (PDFDonHang item : list) {
            canvas.drawText(item.getProduct().getMaSP() + "", 40, 200 + width, paint);
            canvas.drawText(item.getProduct().getTenSP(), 80, 200 + width, paint);
            canvas.drawText(item.getProduct().getDonGia() + "", 170, 200 + width, paint);
            canvas.drawText(item.getChiTietDonHang().getSoLuong() + "", 240, 200 + width, paint);
            sum += item.getProduct().getDonGia() * item.getChiTietDonHang().getSoLuong();
            canvas.drawText(item.getProduct().getDonGia() * item.getChiTietDonHang().getSoLuong() + "", 300, 200 + width, paint);
            width += 30;
        }

        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(1);
        canvas.drawRect(pageWith / 2, 210 + (30 * totalProduct), pageWith - 20, 210 + (30 * totalProduct), paint);

        paint.setTextAlign(Paint.Align.RIGHT);
        paint.setColor(Color.BLACK);
        canvas.drawText("Tổng cộng: " + sum + "(đ)", pageWith - 20, 210 + (30 * totalProduct) + 30, paint);

        document.finishPage(page);

        File file = new File(Environment.getExternalStorageDirectory(), "/thongtindonhang" + idDH + ".pdf");
        path = file.getAbsolutePath();
        try {
            document.writeTo(new FileOutputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
        document.close();
        CustomToast.makeText(PDFDonHangActivity.this, "Lưu file thành công!! ",
                CustomToast.LENGTH_SHORT, CustomToast.SUCCESS).show();
        viewpdf.setBackgroundColor(Color.BLUE);
        viewpdf.setEnabled(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Constants.RESULT_PDF_ACTIVITY:
                int id1 = getIntent().getIntExtra("id", 1);
                getData();
                setInfo(khachHangDto, donHangDto);
                break;
            default:
                break;
        }
    }
}