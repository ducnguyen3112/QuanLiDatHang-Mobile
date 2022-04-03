package com.example.quanlydathang.dto;

public class SanPham {

    private String maSP , tenSP,xuatXu;
    private int donGia;

    public SanPham() {
    }

    public SanPham(String maSP, String tenSP, String xuatXu, int donGia) {
        this.maSP = maSP;
        this.tenSP = tenSP;
        this.xuatXu = xuatXu;
        this.donGia = donGia;
    }

    public String getMaSP() {
        return maSP;
    }

    public void setMaSP(String maSP) {
        this.maSP = maSP;
    }

    public String getTenSP() {
        return tenSP;
    }

    public void setTenSP(String tenSP) {
        this.tenSP = tenSP;
    }

    public String getXuatXu() {
        return xuatXu;
    }

    public void setXuatXu(String xuatXu) {
        this.xuatXu = xuatXu;
    }

    public int getDonGia() {
        return donGia;
    }

    public void setDonGia(int donGia) {
        this.donGia = donGia;
    }
}
