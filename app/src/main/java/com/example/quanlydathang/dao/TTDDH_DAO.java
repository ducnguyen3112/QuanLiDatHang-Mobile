package com.example.quanlydathang.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.quanlydathang.database.CreateDatabase;
import com.example.quanlydathang.dto.KhachHangDto;
import com.example.quanlydathang.dto.Product;
import com.example.quanlydathang.dto.TTDDH_DTO;

import java.util.ArrayList;
import java.util.List;

public class TTDDH_DAO {
    SQLiteDatabase database;

    public TTDDH_DAO(Context context) {
        CreateDatabase createDatabase = new CreateDatabase(context);
        database = createDatabase.open();
    }

    public List<TTDDH_DTO> ttddh_dtoList(){
        List<TTDDH_DTO> list = new ArrayList<>();
        String script=" SELECT MADH, MASP, SLDAT FROM "+ CreateDatabase.TB_TTDDH;
        Cursor cursor=database.rawQuery(script,null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            TTDDH_DTO ttddh_dto = new TTDDH_DTO();
            ttddh_dto.setMaDH(cursor.getInt(0));
            ttddh_dto.setMaSP(cursor.getInt(1));
            ttddh_dto.setSL(cursor.getInt(2));

            list.add(ttddh_dto);
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public long them_ttddh_dao(TTDDH_DTO ttddh_dto){
        ContentValues contentValues=new ContentValues();
        contentValues.put(CreateDatabase.TB_TTDDH_MADH,ttddh_dto.getMaDH());
        contentValues.put(CreateDatabase.TB_TTDDH_MASP,ttddh_dto.getMaSP());
        contentValues.put(CreateDatabase.TB_TTDDH_SLDAT,ttddh_dto.getSL());
        long id = database.insert(CreateDatabase.TB_TTDDH,null,contentValues);
        return id;
    }

    public boolean capNhat_ttddh_dao(TTDDH_DTO ttddh_dto) {
        String str = "update TTDDH set SLDAT= ? where MADH=? and MASP=?";
        String[] values = {ttddh_dto.getSL()+"",ttddh_dto.getMaDH()+"",ttddh_dto.getMaSP()+""};
        try {
            database.execSQL(str,values);
            return true;
        }
        catch (SQLException ex) {
            return false;
        }
    }

    public boolean xoa_ttddh_dao(TTDDH_DTO ttddh_dto) {
        String query = " delete from TTDDH where MADH=? and MASP=? ";
        String[] values = {ttddh_dto.getMaDH()+"",ttddh_dto.getMaSP()+""};
        try {
            database.execSQL(query,values);
            return true;
        }
        catch (SQLException ex) {
            return false;
        }
    }

    public KhachHangDto TimKhachHang(int maKH) {
        KhachHangDto khachHangDto = new KhachHangDto();

        String query = "SELECT * FROM KHACHHANG WHERE MAKH = ?";
        String[] values = {maKH+""};
        Cursor cursor = database.rawQuery(query,values);
        cursor.moveToFirst();
        khachHangDto.setId(cursor.getInt(0));
        khachHangDto.setName(cursor.getString(1));
        khachHangDto.setAddress(cursor.getString(2));
        khachHangDto.setPhone(cursor.getString(3));

        cursor.close();
        return khachHangDto;
    }

    public Product TimSanPham(int maSP) {
        Product product = new Product();

        String query = "SELECT * FROM SANPHAM WHERE MASP=?";
        String[] values = {maSP+""};
        Cursor cursor = database.rawQuery(query,values);

        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            product.setMaSP(cursor.getInt(0));
            product.setTenSP(cursor.getString(1));
            product.setXuatXu(cursor.getString(2));
            product.setDonGia(Integer.parseInt(cursor.getString(3)));
            cursor.moveToNext();
        }

        cursor.close();
        return product;
    }

    public List<Product> getListProducts() {
        List<Product> list = new ArrayList<>();
        String query = "SELECT * FROM SANPHAM";
        Cursor cursor = database.rawQuery(query,null);

        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            Product product = new Product();
            product.setMaSP(cursor.getInt(0));
            product.setTenSP(cursor.getString(1));
            product.setXuatXu(cursor.getString(2));
            product.setDonGia(Integer.parseInt(cursor.getString(3)));

            list.add(product);
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }
}
