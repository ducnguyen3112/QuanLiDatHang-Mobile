package com.example.quanlydathang.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.quanlydathang.R;
import com.example.quanlydathang.activitydonhang.DonDatHangActivity;
import com.example.quanlydathang.adapter.TTDDH_Adapter;
import com.example.quanlydathang.dao.TTDDH_DAO;
import com.example.quanlydathang.dto.DonHangDto;
import com.example.quanlydathang.dto.KhachHangDto;
import com.example.quanlydathang.dto.Product;
import com.example.quanlydathang.dto.TTDDH_DTO;
import com.example.quanlydathang.utils.CustomToast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class TTDDH_Activity extends AppCompatActivity {
    public static int width;
    private static int selectedID;

    private DonHangDto mDonHangDto;
    private KhachHangDto mKhachHangDto;
    private Product mProductDto;

    private TTDDH_DAO ttddh_dao;
    private List<TTDDH_DTO> ttddh_dtoList;
    private TTDDH_Adapter ttddh_adapter;

    private RecyclerView rcvTTDDH;
    private TextView tvMaDH, tvNgayDH, tvTenKH, tvSDT, tvDiaChi, tvTongTien;
    private Button  btnThemSP, btnCapNhat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ttddh);
        DisplayMetrics metrics=getResources().getDisplayMetrics();
        width = metrics.widthPixels;
        ttddh_dao = new TTDDH_DAO(this);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle == null) {
            CustomToast.makeText(TTDDH_Activity.this,"Không lấy được dữ liệu đơn hàng!", CustomToast.LENGTH_LONG,CustomToast.ERROR).show();
            finish();
        }

        mDonHangDto = new DonHangDto(bundle.getInt("maDH"), bundle.getString("ngayDH"), bundle.getInt("maKH"), bundle.getString("tenKH"));
//        CustomToast.makeText(TTDDH_Activity.this,mDonHangDto.toString(),CustomToast.LENGTH_LONG, CustomToast.CONFUSING).show();
        mKhachHangDto = ttddh_dao.TimKhachHang(mDonHangDto.getMaKH());

        setControl();

        btnCapNhat.setOnClickListener(view -> {
            CustomToast.makeText(TTDDH_Activity.this,"Cập nhật đơn hàng thành công!"
                    , CustomToast.LENGTH_SHORT, CustomToast.SUCCESS).show();
            finish();
        });

        btnThemSP.setOnClickListener(view -> {
            Dialog dialog = ttddh_adapter.getDialogThemSP_TTDDH(this);
            List<String> dsMaVaTenSP = ttddh_adapter.dsMaVaTenSP();
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this
                    , androidx.appcompat.R.layout.support_simple_spinner_dropdown_item
                    , dsMaVaTenSP );
            ttddh_adapter.spinnerSP_dialogThemSP.setAdapter(arrayAdapter);
            suKienSpinner();
            Log.e("selectedProductID",selectedID+"");

            dialog.show();

            ttddh_adapter.btnHuy_dialogThemSP.setOnClickListener(view01 -> {
                dialog.cancel();
            });

            ttddh_adapter.btnThem_dialogThemSP.setOnClickListener(view02 -> {
                if(ttddh_adapter.etSoLuong_dialogThemSP.getText().toString().isEmpty()) {
                    CustomToast.makeText(TTDDH_Activity.this, "Vui lòng nhập số lượng!",
                            CustomToast.LENGTH_LONG, CustomToast.WARNING).show();
                }
                else {
                    TTDDH_DTO ttddh_dto = new TTDDH_DTO();
                    ttddh_dto.setMaDH(mDonHangDto.getMaDH());
                    ttddh_dto.setMaSP(selectedID);
                    ttddh_dto.setSL(Integer.parseInt(ttddh_adapter.etSoLuong_dialogThemSP.getText().toString()));

                    int id = (int) ttddh_dao.them_ttddh_dao(ttddh_dto);
                    if(id!=-1) {
                        CustomToast.makeText(TTDDH_Activity.this, "Thêm TTDDH thành công!",
                                CustomToast.LENGTH_LONG, CustomToast.SUCCESS).show();
                    }
                    else {
                        CustomToast.makeText(TTDDH_Activity.this, "Thêm TTDDH thất bại!",
                                CustomToast.LENGTH_LONG, CustomToast.ERROR).show();
                    }
                    dialog.cancel();
                    onResume();
                }
            });
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvTTDDH.setLayoutManager(linearLayoutManager);
        capNhatDuLieuTTDDH();
    }

    private void setControl() {
        rcvTTDDH = findViewById(R.id.rcv_ttddh);
        btnThemSP = findViewById(R.id.btnThemSP_ttddh);
        btnCapNhat = findViewById(R.id.btnCapNhat_ttddh);
        tvMaDH = findViewById(R.id.tvMaDH_ttddh);
        tvNgayDH = findViewById(R.id.tvNgayDH_ttddh);
        tvTenKH = findViewById(R.id.tvTenKH_ttddh);
        tvSDT = findViewById(R.id.tvSDT_ttddh);
        tvDiaChi = findViewById(R.id.tvDiaChi_ttddh);
        tvTongTien = findViewById(R.id.tvTongTien_ttddh);

        tvMaDH.setText(mDonHangDto.getMaDH()+"");
        tvNgayDH.setText(mDonHangDto.getNgayDH());
        tvTenKH.setText(mKhachHangDto.getName());
        tvSDT.setText(mKhachHangDto.getPhone());
        tvDiaChi.setText(mKhachHangDto.getAddress());
        tvTongTien.setText("-");
    }

    private void suKienSpinner() {

        ttddh_adapter.spinnerSP_dialogThemSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String str = ttddh_adapter.spinnerSP_dialogThemSP.getSelectedItem().toString();
                Log.e("TAG", ""+str ,null );
                String[] words=str.split("-");
                if (!str.isEmpty()){
                    selectedID = Integer.valueOf(words[0].trim());
                }
                else {
                    selectedID = 0;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                selectedID = 1;
            }
        });
    }

    private void capNhatDuLieuTTDDH() {
        ttddh_dtoList = ttddh_dao.ttddh_dtoList();
        ttddh_adapter = new TTDDH_Adapter(this,ttddh_dtoList,mDonHangDto.getMaDH());
        rcvTTDDH.setAdapter(ttddh_adapter);
        tvTongTien.setText(NumberFormat.getNumberInstance(Locale.US).format(ttddh_adapter.TongTien()) + " USD");
    }

    @Override
    public void onResume() {
        super.onResume();
        capNhatDuLieuTTDDH();
    }
}