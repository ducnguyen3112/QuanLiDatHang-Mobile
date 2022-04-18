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

    public void addProduct(Product product) {
        String sql = "insert into SANPHAM(TENSP,XUATXU,DONGIA) values(?,?,?)";
        database.execSQL(sql, new Object[]{product.getTenSP(), product.getXuatXu(), product.getDonGia()});
    }

    public Product getProduct(int maSP) {
        Product product = null;
        Cursor cursor = database.rawQuery("SELECT * from SANPHAM where MASP = ?", new String[]{maSP + ""});
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            product = new Product(cursor.getInt(0),cursor.getString(1),
                    cursor.getString(2), cursor.getInt(3));
            cursor.moveToNext();
        }
        cursor.close();
        return product;
    }

    public long updateProduct(Product product) {
        ContentValues data = new ContentValues();
        data.put("TENSP", product.getTenSP());
        data.put("XUATXU", product.getXuatXu());
        data.put("DONGIA", product.getDonGia());
        return database.update("SANPHAM", data, "MASP=" + product.getMaSP(), null);
    }

    public long deleteProduct(int maSP) {
        return database.delete("SANPHAM", "MASP=" + maSP, null);
    }

    public int countLoadDBAllTypeDisplay(ArrayList<Product> list,int typeDisplay) {
        int count = 0;
        list.clear();
        Cursor cursor = database.rawQuery("select * from SANPHAM", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Product product = new Product(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                    cursor.getInt(3));
            product.setTypeDisplay(typeDisplay);
            list.add(product);
            ++count;
            cursor.moveToNext();
        }
        cursor.close();return count;
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

    /*public int countLoadDB(ArrayList<Product> list, int page, int number) {
        int count = 0;
        String sql = "select * from SANPHAM limit  ('"+page+"' - 1) * '"+number+"' , '"+number+"'";
        Cursor cursor = database.rawQuery(sql, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Product product = new Product(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                    cursor.getInt(3));
            list.add(product);
            ++count;
            cursor.moveToNext();
        }
        cursor.close();return count;
    }*/

    public int countLoadDBTypeDisplay(ArrayList<Product> list, int page, int number, int typeDisplay) {
        int count = 0;
        String sql = "select * from SANPHAM limit  ('"+page+"' - 1) * '"+number+"' , '"+number+"'";
        Cursor cursor = database.rawQuery(sql, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Product product = new Product(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                    cursor.getInt(3));
            product.setTypeDisplay(typeDisplay);
            list.add(product);
            ++count;
            cursor.moveToNext();
        }
        cursor.close();return count;
    }
}
