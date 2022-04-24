package com.example.quanlydathang.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.example.quanlydathang.database.CreateDatabase;
import com.example.quanlydathang.dto.DonHangDto;
import com.example.quanlydathang.dto.KhachHangDto;
import com.example.quanlydathang.dto.Product;

import java.util.ArrayList;

public class ProductDao {
    SQLiteDatabase database;

    public ProductDao(Context context) {
        CreateDatabase createDatabase = new CreateDatabase(context);
        database = createDatabase.open();
    }

    public void insertProduct(String ten, String xuatxu, Integer gia, byte[] hinh) {
        String sql = "Insert into SANPHAM values (null,?,?,?,?)";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();
        statement.bindString(1, ten);
        statement.bindString(2, xuatxu);
        statement.bindLong(3, gia);
        statement.bindBlob(4, hinh);
        statement.executeInsert();
    }

    public Product getProduct(int maSP) {
        Product product = null;
        Cursor cursor = database.rawQuery("SELECT * from SANPHAM where MASP = ?", new String[]{maSP + ""});
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            product = new Product(cursor.getInt(0), cursor.getString(1),
                    cursor.getString(2), cursor.getInt(3), cursor.getBlob(4));
            cursor.moveToNext();
        }
        cursor.close();
        return product;
    }

    public KhachHangDto getUser(int maSP) {
        KhachHangDto user = null;
        Cursor cursor = database.rawQuery("SELECT TENKH, DIACHI, SDT from KHACHHANG where MAKH = ?", new String[]{maSP + ""});
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            user = new KhachHangDto(cursor.getString(0), cursor.getString(1),
                    cursor.getString(2));
            cursor.moveToNext();
        }
        cursor.close();
        return user;
    }

    public DonHangDto getDonHang(int maSP) {
        DonHangDto user = null;
        Cursor cursor = database.rawQuery("SELECT * from DONDATHANG where MADH = ?", new String[]{maSP + ""});
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            user = new DonHangDto(cursor.getInt(0), cursor.getString(1),
                    cursor.getInt(2));
            cursor.moveToNext();
        }
        cursor.close();
        return user;
    }

    public long updateProduct(Integer ma, String ten, String xuatxu, Integer gia, byte[] hinh) {
        ContentValues data = new ContentValues();
        data.put("TENSP", ten);
        data.put("XUATXU", xuatxu);
        data.put("DONGIA", gia);
        data.put("HINHANH", hinh);
        return database.update("SANPHAM", data, "MASP=" + ma, null);
    }

    public long deleteProduct(int maSP) {
        return database.delete("SANPHAM", "MASP=" + maSP, null);
    }

    public int countLoadDBAllTypeDisplay(ArrayList<Product> list, int typeDisplay) {
        int count = 0;
        list.clear();
        Cursor cursor = database.rawQuery("select * from SANPHAM", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Product product = new Product(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                    cursor.getInt(3), cursor.getBlob(4));
            product.setTypeDisplay(typeDisplay);
            list.add(product);
            ++count;
            cursor.moveToNext();
        }
        cursor.close();
        return count;
    }

    public int totalItem() {
        int count = 0;
        Cursor cursor = database.rawQuery("select * from SANPHAM", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            ++count;
            cursor.moveToNext();
        }
        cursor.close();
        return count;
    }

    public int countLoadDBTypeDisplay(ArrayList<Product> list, int page, int number, int typeDisplay) {
        int count = 0;
        String sql = "select * from SANPHAM limit  ('" + page + "' - 1) * '" + number + "' , '" + number + "'";
        Cursor cursor = database.rawQuery(sql, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Product product = new Product(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                    cursor.getInt(3), cursor.getBlob(4));
            product.setTypeDisplay(typeDisplay);
            list.add(product);
            ++count;
            cursor.moveToNext();
        }
        cursor.close();
        return count;
    }
}
