package com.example.quanlydathang.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.quanlydathang.dto.SanPham;

import java.util.ArrayList;

public class DBSanPham extends SQLiteOpenHelper {

    public DBSanPham(@Nullable Context context) {
        super(context, "QLDH", null, 1);
    }

    public void QueryData(String sql) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(sql);
    }

    public Cursor GetData(String sql) {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery(sql, null);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "create table if not exists SANPHAM(MASP VARCHAR(255) PRIMARY KEY," +
                "TENSP VARCHAR(255), XUATXU VARCHAR(255), DONGIA INTEGER)";
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void themSanPham(SanPham sanPham) {
        SQLiteDatabase database = getWritableDatabase();
        String sql = "insert into SANPHAM values(?,?,?,?)";
        database.execSQL(sql, new Object[]{sanPham.getMaSP(), sanPham.getTenSP(), sanPham.getXuatXu(), sanPham.getDonGia()});
    }

    public SanPham laySanPham(String maSP) {
        SanPham sanPham = null;
        Cursor cursor = getReadableDatabase().rawQuery("SELECT * from SANPHAM where MASP = ?", new String[]{maSP});
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String ma = cursor.getString(0);
            String ten = cursor.getString(1);
            String xx = cursor.getString(2);
            int dongia = cursor.getInt(3);
            sanPham = new SanPham(ma, ten, xx, dongia);
            cursor.moveToNext();
        }
        cursor.close();
        return sanPham;
    }

    public long suaSanPham(SanPham sanPham) {
        ContentValues data = new ContentValues();
        data.put("TENSP", sanPham.getTenSP());
        data.put("XUATXU", sanPham.getXuatXu());
        data.put("DONGIA", sanPham.getDonGia());
        return getWritableDatabase().update("SANPHAM", data, "MASP=" + sanPham.getMaSP(), null);
    }

    public long xoaSP(String maSP) {
        return getWritableDatabase().delete("SANPHAM", "MASP=" + maSP, null);
    }

    public void loadDb(ArrayList<SanPham> list) {
        list.clear();
        Cursor cursor = getWritableDatabase().rawQuery("SELECT * from SANPHAM", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String maSP = cursor.getString(0);
            String tenSP = cursor.getString(1);
            String xuatXu = cursor.getString(2);
            int donGia = cursor.getInt(3);
            SanPham sanPham = new SanPham(maSP, tenSP, xuatXu, donGia);
            list.add(sanPham);
            cursor.moveToNext();
        }

        cursor.close();
    }

    public void insertData() {
        QueryData("create table if not exists SANPHAM(MASP VARCHAR(255) PRIMARY KEY," +
                "TENSP VARCHAR(255), XUATXU VARCHAR(255), DONGIA INTEGER)");
        QueryData("insert into SANPHAM values('1','Tivi SamSung','Hàn Quốc',10500000)");
        QueryData("insert into SANPHAM values('2','Máy giặt','Nhật',9000000)");
        QueryData("insert into SANPHAM values('3','Máy lạnh','Nhật',7500000)");
        QueryData("insert into SANPHAM values('4','Quạt','Việt Nam',300000)");
        QueryData("insert into SANPHAM values('5','Bếp nướng','Nhật',5000000)");
        QueryData("insert into SANPHAM values('6','Tủ lạnh','Hàn Quốc',12000000)");
    }
}
