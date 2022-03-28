package com.example.quanlydathang.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.quanlydathang.database.CreateDatabase;
import com.example.quanlydathang.dto.DonHangDto;

public class DonHangDao {
    SQLiteDatabase database;
    public DonHangDao(Context context ) {
        CreateDatabase createDatabase=new CreateDatabase(context);
        database = createDatabase.open();
    }
    public long themDonHang(DonHangDto donHangDto){
        ContentValues contentValues=new ContentValues();
        contentValues.put(CreateDatabase.TB_DONDATHANG_NGAYDH,donHangDto.getNgayDH());
        contentValues.put(CreateDatabase.TB_DONDATHANG_MAKH,donHangDto.getMaKH());
        long id=database.insert(CreateDatabase.TB_DONDATHANG,null,contentValues);
        return id;
    }
}
