package com.example.quanlydathang.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.quanlydathang.database.CreateDatabase;
import com.example.quanlydathang.dto.KhachHangDto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class KhachHangDao  {
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
        return database.insert(CreateDatabase.TB_KHACHHANG, null, cv);
    }

    public List<KhachHangDto> getListKH() {
        String query = "SELECT * FROM " + CreateDatabase.TB_KHACHHANG;
        List<KhachHangDto> list = new ArrayList<>();
        Cursor cursor = null;
        cursor = database.rawQuery(query, null);
        while (cursor.moveToNext()) {
            list.add(new KhachHangDto(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3)));
        }
        return list;
    }

    public long updateKH(KhachHangDto dto) {
        ContentValues cv = new ContentValues();
        cv.put(CreateDatabase.TB_KHACHHANG_TENKH, dto.getName());
        cv.put(CreateDatabase.TB_KHACHHANG_DIACHI, dto.getAddress());
        cv.put(CreateDatabase.TB_KHACHHANG_SDT, dto.getPhone());
        return database.update(CreateDatabase.TB_KHACHHANG, cv, "MAKH=?", new String[]{String.valueOf(dto.getId())});
    }

    public long deleteKH(int id) {
        return database.delete(CreateDatabase.TB_KHACHHANG, "MAKH=?", new String[]{String.valueOf(id)});
    }
}
