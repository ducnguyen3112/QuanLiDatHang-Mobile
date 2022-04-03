package com.example.quanlydathang.dao;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.quanlydathang.database.CreateDatabase;
import com.example.quanlydathang.dto.DonHangDto;

import java.util.ArrayList;
import java.util.List;

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


    public List<DonHangDto> danhSachDonHang(){
        List<DonHangDto> ds=new ArrayList<>();
        String script="SELECT MADH,NGAYDH,TENKH,DONDATHANG.MAKH FROM "+ CreateDatabase.TB_DONDATHANG + ","
                + CreateDatabase.TB_KHACHHANG + " WHERE DONDATHANG.MAKH=KHACHHANG.MAKH" ;
        Cursor cursor=database.rawQuery(script,null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            DonHangDto donHangDto=new DonHangDto();
            donHangDto.setMaDH(cursor.getInt(0));
            donHangDto.setNgayDH(cursor.getString(1));
            donHangDto.setTenKH(cursor.getString(2));
            donHangDto.setMaKH(cursor.getInt(3));

            ds.add(donHangDto);
            cursor.moveToNext();
        }
        return ds;
    }
    public boolean xoaDonHang(int id){
        long check=database.delete(CreateDatabase.TB_DONDATHANG,
                CreateDatabase.TB_DONDATHANG_MADH + "=" +id,null);
        if (check!=0){
            return true;
        }else {
            return false;
        }
    }
    public long suaDonHang(DonHangDto donHangDto){
        ContentValues cv = new ContentValues();
        cv.put(CreateDatabase.TB_DONDATHANG_MAKH, donHangDto.getMaKH());
        cv.put(CreateDatabase.TB_DONDATHANG_NGAYDH, donHangDto.getNgayDH());
        return database.update(CreateDatabase.TB_DONDATHANG
        ,cv
        ,CreateDatabase.TB_DONDATHANG_MADH+"="+donHangDto.getMaDH()
        ,null);
    }
}
