package com.example.quanlydathang.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.example.quanlydathang.R;
import com.example.quanlydathang.activity.SanPhamActivity;
import com.example.quanlydathang.activity.ThemSPActivity;
import com.example.quanlydathang.database.DBSanPham;
import com.example.quanlydathang.dto.SanPham;
import com.example.quanlydathang.utils.CustomToast;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class SanPhamAdapter extends BaseAdapter {

    Context context;
    ArrayList<SanPham> list;
    TextView tenSP, maSP, xuatXu, donGia;
    ImageButton delete;
    int RESULT_PRODUCT_ACTIVITY = 0;

    public SanPhamAdapter(Context context, ArrayList<SanPham> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public SanPham getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        view = LayoutInflater.from(context).inflate(R.layout.item_sanpham, null);
        setControl(view);
        SanPham sanPham = list.get(position);
        maSP.setText(sanPham.getMaSP());
        tenSP.setText(sanPham.getTenSP());
        xuatXu.setText(sanPham.getXuatXu());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,##0");
        donGia.setText(decimalFormat.format(sanPham.getDonGia()));

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(sanPham,"Xóa sản phẩm ?");
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setBackgroundColor(Color.rgb(141, 216, 158));
                Intent intent = new Intent(((Activity) context), ThemSPActivity.class);
                /*intent.putExtra("isupdate", true);*/
                intent.putExtra("MASP", sanPham.getMaSP());
                ((Activity) context).startActivityForResult(intent, RESULT_PRODUCT_ACTIVITY);
            }
        });
        return view;
    }

    public void setControl(View view){
        tenSP = view.findViewById(R.id.textViewTenSP);
        maSP = view.findViewById(R.id.textViewMaSP);
        xuatXu = view.findViewById(R.id.textViewXuatXu);
        donGia = view.findViewById(R.id.textViewDonGia);
        delete = view.findViewById(R.id.buttonDelete);
    }

    public void showDialog(SanPham sanPham, String msg){
        Dialog dialog = new Dialog(context);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_dialog);
        TextView text = (TextView) dialog.findViewById(R.id.title);
        text.setText(msg);
        Button buttonHuy = (Button) dialog.findViewById(R.id.buttonHuy);
        buttonHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        Button buttonDongY = (Button) dialog.findViewById(R.id.buttonDongY);
        buttonDongY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBSanPham db = new DBSanPham(context.getApplicationContext());
                db.xoaSP(sanPham.getMaSP());
                db.loadDb(list);
                notifyDataSetChanged();
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
