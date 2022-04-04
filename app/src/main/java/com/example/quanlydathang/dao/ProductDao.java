package com.example.quanlydathang.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.quanlydathang.database.CreateDatabase;
import com.example.quanlydathang.dto.KhachHangDto;
import com.example.quanlydathang.dto.Product;

import java.util.ArrayList;

public class ProductDao {

    SQLiteDatabase database;

    public ProductDao(Context context) {
        CreateDatabase createDatabase = new CreateDatabase(context);
        database = createDatabase.open();
    }

    public void addProduct(Product sanPham) {
        String sql = "insert into SANPHAM(TENSP,XUATXU,DONGIA) values(?,?,?)";
        database.execSQL(sql, new Object[]{sanPham.getTenSP(), sanPham.getXuatXu(), sanPham.getDonGia()});
    }

    public Product getProduct(int maSP) {
        Product sanPham = null;
        Cursor cursor = database.rawQuery("SELECT * from SANPHAM where MASP = ?", new String[]{maSP+""});
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int ma = cursor.getInt(0);
            String ten = cursor.getString(1);
            String xx = cursor.getString(2);
            int dongia = cursor.getInt(3);
            sanPham = new Product(ma, ten, xx, dongia);
            cursor.moveToNext();
        }
        cursor.close();
        return sanPham;
    }

    public long updateProduct(Product sanPham) {
        ContentValues data = new ContentValues();
        data.put("TENSP", sanPham.getTenSP());
        data.put("XUATXU", sanPham.getXuatXu());
        data.put("DONGIA", sanPham.getDonGia());
        return database.update("SANPHAM", data, "MASP=" + sanPham.getMaSP(), null);
    }

    public long deleteProduct(int maSP) {
        return database.delete("SANPHAM", "MASP=" + maSP, null);
    }

    public void loadDb(ArrayList<Product> list) {
        list.clear();
        Cursor cursor = database.rawQuery("SELECT * from SANPHAM", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int maSP = cursor.getInt(0);
            String tenSP = cursor.getString(1);
            String xuatXu = cursor.getString(2);
            int donGia = cursor.getInt(3);
            Product sanPham = new Product(maSP, tenSP, xuatXu, donGia);
            list.add(sanPham);
            cursor.moveToNext();
        }

        cursor.close();
    }
/*
    public void insertData() {
        QueryData("create table if not exists SANPHAM(MASP VARCHAR(255) PRIMARY KEY," +
                "TENSP VARCHAR(255), XUATXU VARCHAR(255), DONGIA INTEGER)");
        QueryData("insert into SANPHAM values('1','Tivi SamSung','Hàn Quốc',10500000)");
        QueryData("insert into SANPHAM values('2','Máy giặt','Nhật',9000000)");
        QueryData("insert into SANPHAM values('3','Máy lạnh','Nhật',7500000)");
        QueryData("insert into SANPHAM values('4','Quạt','Việt Nam',300000)");
        QueryData("insert into SANPHAM values('5','Bếp nướng','Nhật',5000000)");
        QueryData("insert into SANPHAM values('6','Tủ lạnh','Hàn Quốc',12000000)");
    }*/

}
