package com.example.quanlydathang.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.quanlydathang.database.CreateDatabase;
import com.example.quanlydathang.dto.Product;
import com.example.quanlydathang.dto.UserDto;

public class UserDao {
    SQLiteDatabase database;

    public UserDao(Context context ) {
        CreateDatabase createDatabase=new CreateDatabase(context);
        database = createDatabase.open();
    }
    public long themUser(UserDto userDto){
        ContentValues contentValues=new ContentValues();
        contentValues.put(CreateDatabase.TB_USER_USERNAME,userDto.getUsername());
        contentValues.put(CreateDatabase.TB_USER_PASSWD,userDto.getPassword());
        return database.insert(CreateDatabase.TB_USER,null,contentValues);
    }
    public boolean kiemTraDangNhap(String username,String passwd){
        String scripts="SELECT * FROM "+ CreateDatabase.TB_USER
                + " WHERE "+CreateDatabase.TB_USER_USERNAME
                + " = '"+username + "' AND " + CreateDatabase.TB_USER_PASSWD
                + " = '"+passwd + "'";
        Cursor cursor=database.rawQuery(scripts,null);

        if (cursor.getCount()!=0 ){
            return true;
        }
        return false;
    }
    public boolean getSDT(String sdt){
        String script="SELECT "+ CreateDatabase.TB_USER_SDT+ " FROM "
                +CreateDatabase.TB_USER +" WHERE " +CreateDatabase.TB_USER_SDT
                + "="+ sdt;
        Cursor cursor=database.rawQuery(script,null);
        if (cursor.getCount()!=0){
            return true;
        }
        return false;
    }
    public String getUserNameFromSDT(String sdt){
        String script="SELECT "+ CreateDatabase.TB_USER_USERNAME+ " FROM "
                +CreateDatabase.TB_USER +" WHERE " +CreateDatabase.TB_USER_SDT
                + "="+ sdt;
        String userName="";
        Log.e("script", ""+script,null );
        Cursor cursor=database.rawQuery(script,null);
        Log.e("TAG", "getUserNameFromSDT: "+cursor.getCount(),null );
        if (cursor.getCount()==1){
            cursor.moveToFirst();
            userName= cursor.getString(0);
        }
        cursor.close();
        return userName;
    }
}
