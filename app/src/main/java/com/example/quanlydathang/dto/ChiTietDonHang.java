package com.example.quanlydathang.dto;

public class ChiTietDonHang {
    private Integer maSP;
    private Integer maDH;
    private Integer soLuong;

    public ChiTietDonHang() {
    }

    public ChiTietDonHang(Integer maSP, Integer maDH, Integer soLuong) {
        this.maSP = maSP;
        this.maDH = maDH;
        this.soLuong = soLuong;
    }

    public Integer getMaSP() {
        return maSP;
    }

    public void setMaSP(Integer maSP) {
        this.maSP = maSP;
    }

    public Integer getMaDH() {
        return maDH;
    }

    public void setMaDH(Integer maDH) {
        this.maDH = maDH;
    }

    public Integer getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(Integer soLuong) {
        this.soLuong = soLuong;
    }
}
