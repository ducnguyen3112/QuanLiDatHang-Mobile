package com.example.quanlydathang.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.example.quanlydathang.R;
import com.example.quanlydathang.activity.MainActivity;
import com.example.quanlydathang.activity.SanPhamActivity;
import com.example.quanlydathang.activity.ThemSPActivity;
import com.example.quanlydathang.database.DBSanPham;
import com.example.quanlydathang.dto.SanPham;

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
        tenSP = view.findViewById(R.id.textViewTenSP);
        maSP = view.findViewById(R.id.textViewMaSP);
        xuatXu = view.findViewById(R.id.textViewXuatXu);
        donGia = view.findViewById(R.id.textViewDonGia);

        delete = view.findViewById(R.id.buttonDelete);

        SanPham sanPham = list.get(position);

        maSP.setText(sanPham.getMaSP());
        tenSP.setText(sanPham.getTenSP());

        xuatXu.setText(sanPham.getXuatXu());

        DecimalFormat decimalFormat = new DecimalFormat("###,###,##0");
        donGia.setText(decimalFormat.format(sanPham.getDonGia()));

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog(sanPham);

            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(((Activity) context), ThemSPActivity.class);
                intent.putExtra("isupdate", true);
                intent.putExtra("MASP", sanPham.getMaSP());
                ((Activity) context).startActivityForResult(intent, RESULT_PRODUCT_ACTIVITY);
            }
        });
        return view;
    }

    public void dialog(SanPham sanPham){
        AlertDialog.Builder dialogDelete = new AlertDialog.Builder(context);
        dialogDelete.setTitle("Xác nhận");
        dialogDelete.setMessage("Xóa sản phẩm này?");
        dialogDelete.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                DBSanPham db = new DBSanPham(context.getApplicationContext());
                db.xoaSP(sanPham.getMaSP());
                db.loadDb(list);
                notifyDataSetChanged();
                Toast.makeText(context.getApplicationContext(), "Đã xóa sản phẩm!", Toast.LENGTH_SHORT).show();
            }
        });
        dialogDelete.setNegativeButton("Không đồng ý", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog al = dialogDelete.create();
        al.show();
    }
}
