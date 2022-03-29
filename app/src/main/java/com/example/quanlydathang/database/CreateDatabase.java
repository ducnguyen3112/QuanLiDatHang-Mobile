package com.example.quanlydathang.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class CreateDatabase extends SQLiteOpenHelper {

    public static final String DB_NAME= "QLDH";

    public static final String TB_KHACHHANG= "KHACHHANG";
    public static final String TB_SANPHAM= "SANPHAM";
    public static final String TB_DONDATHANG= "DONDATHANG";
    public static final String TB_TTDDH= "TTDDH";
    public static final String TB_USER="USER";

    public static final String TB_KHACHHANG_MAKH= "MAKH";
    public static final String TB_KHACHHANG_TENKH= "TENKH";
    public static final String TB_KHACHHANG_DIACHI= "DIACHI";
    public static final String TB_KHACHHANG_SDT= "SDT";

    public static final String TB_SANPHAM_MASP= "MASP";
    public static final String TB_SANPHAM_TENSP= "TENSP";
    public static final String TB_SANPHAM_XUATXU= "XUATXU";
    public static final String TB_SANPHAM_DONGIA= "DONGIA";

    public static final String TB_DONDATHANG_MADH= "MADH";
    public static final String TB_DONDATHANG_NGAYDH= "NGAYDH";
    public static final String TB_DONDATHANG_MAKH= "MAKH";

    public static final String TB_TTDDH_MADH= "MADH";
    public static final String TB_TTDDH_MASP= "MASP";
    public static final String TB_TTDDH_SLDAT= "SLDAT";

    public static final String TB_USER_ID="USERID";
    public static final String TB_USER_USERNAME="USERNAME";
    public static final String TB_USER_PASSWD="PASSWORD";



    public CreateDatabase(@Nullable Context context) {
        super(context, DB_NAME, null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String tbKhachHang="CREATE TABLE "+ TB_KHACHHANG + "("
                + TB_KHACHHANG_MAKH + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TB_KHACHHANG_TENKH + " TEXT, "
                + TB_KHACHHANG_DIACHI + " TEXT, "
                + TB_KHACHHANG_SDT + " TEXT )";

        String tbSanPham="CREATE TABLE "+ TB_SANPHAM + "("
                + TB_SANPHAM_MASP + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TB_SANPHAM_TENSP + " TEXT, "
                + TB_SANPHAM_XUATXU + " TEXT, "
                + TB_SANPHAM_DONGIA + " TEXT )";

        String tbDDH="CREATE TABLE "+ TB_DONDATHANG + "("
                + TB_DONDATHANG_MADH + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TB_DONDATHANG_NGAYDH + " TEXT, "
                + TB_DONDATHANG_MAKH + " INTEGER) ";

        String tbTTDDH="CREATE TABLE "+ TB_TTDDH + "("
                + TB_TTDDH_MADH + " INTEGER, "
                + TB_TTDDH_MASP + " INTEGER, "
                + TB_TTDDH_SLDAT + " INTEGER, "
                +"PRIMARY KEY ( " +TB_TTDDH_MADH+","+TB_TTDDH_MASP +"))";

        String tbUSER="CREATE TABLE "+ TB_USER + "("
                + TB_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TB_USER_USERNAME + " TEXT, "
                + TB_USER_PASSWD + " TEXT )";
        sqLiteDatabase.execSQL(tbKhachHang);
        sqLiteDatabase.execSQL(tbSanPham);
        sqLiteDatabase.execSQL(tbDDH);
        sqLiteDatabase.execSQL(tbTTDDH);
        sqLiteDatabase.execSQL(tbUSER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    public SQLiteDatabase open(){
        return this.getWritableDatabase();
    }
}
