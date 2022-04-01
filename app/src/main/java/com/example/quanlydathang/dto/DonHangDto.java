package com.example.quanlydathang.dto;

public class DonHangDto {
    int maDH;
    String ngayDH;
    int maKH;
    String tenKH;

    public DonHangDto() {
    }

    public DonHangDto( String ngayDH, int maKH, String tenKH) {
        this.ngayDH = ngayDH;
        this.maKH = maKH;
        this.tenKH = tenKH;
    }

    public int getMaDH() {
        return maDH;
    }
    public String getTenKH() {
        return tenKH;
    }

    public void setTenKH(String tenKH) {
        this.tenKH = tenKH;
    }

    public void setMaDH(int maDH) {
        this.maDH = maDH;
    }

    public String getNgayDH() {
        return ngayDH;
    }

    public void setNgayDH(String ngayDH) {
        this.ngayDH = ngayDH;
    }

    public int getMaKH() {
        return maKH;
    }

    public void setMaKH(int maKH) {
        this.maKH = maKH;
    }
}
