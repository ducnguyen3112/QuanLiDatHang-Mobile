package com.example.quanlydathang.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.quanlydathang.database.CreateDatabase;
import com.example.quanlydathang.dto.KhachHangDto;
import com.example.quanlydathang.dto.Product;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class KhachHangDao {
    SQLiteDatabase database;

    public KhachHangDao(Context context) {
        CreateDatabase createDatabase = new CreateDatabase(context);
        database = createDatabase.open();
    }

    public long addKH(KhachHangDto dto) {
        ContentValues cv = new ContentValues();
        cv.put(CreateDatabase.TB_KHACHHANG_TENKH, dto.getName());
        cv.put(CreateDatabase.TB_KHACHHANG_DIACHI, dto.getAddress());
        cv.put(CreateDatabase.TB_KHACHHANG_SDT, dto.getPhone());
        cv.put(CreateDatabase.TB_KHACHHANG_ANH, dto.getImage());
        return database.insert(CreateDatabase.TB_KHACHHANG, null, cv);
    }

    public List<KhachHangDto> getListKH() {
        String query = "SELECT * FROM " + CreateDatabase.TB_KHACHHANG;
        List<KhachHangDto> list = new ArrayList<>();
        Cursor cursor = null;
        cursor = database.rawQuery(query, null);
        while (cursor.moveToNext()) {
            list.add(new KhachHangDto(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getBlob(4)));
        }
        return list;
    }

    public KhachHangDto getKHById(int maSP) {
        KhachHangDto dto = null;
        String query = "SELECT * FROM " + CreateDatabase.TB_KHACHHANG + " WHERE "+ CreateDatabase.TB_KHACHHANG_MAKH +" = ? ";

        Cursor cursor = database.rawQuery(query, new String[]{maSP + ""});
        while (cursor.moveToNext()) {
            dto = new KhachHangDto(cursor.getInt(0),cursor.getString(1),
                    cursor.getString(2), cursor.getString(3),cursor.getBlob(4));
        }
        return dto;
    }

    public long updateKH(KhachHangDto dto) {
        ContentValues cv = new ContentValues();
        cv.put(CreateDatabase.TB_KHACHHANG_TENKH, dto.getName());
        cv.put(CreateDatabase.TB_KHACHHANG_DIACHI, dto.getAddress());
        cv.put(CreateDatabase.TB_KHACHHANG_SDT, dto.getPhone());
        cv.put(CreateDatabase.TB_KHACHHANG_ANH, dto.getImage());
        return database.update(CreateDatabase.TB_KHACHHANG, cv, "MAKH=?", new String[]{String.valueOf(dto.getId())});
    }

    public long deleteKH(int id) {
        return database.delete(CreateDatabase.TB_KHACHHANG, "MAKH=?", new String[]{String.valueOf(id)});
    }
}
