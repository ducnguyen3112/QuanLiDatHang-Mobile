package com.example.quanlydathang.dto;

public class Product {

    private String tenSP,xuatXu;
    private int maSP, donGia;
    byte[] image;
    private int typeDisplay;

    public Product() {}

    public Product(String tenSP, String xuatXu, int donGia) {
        this.tenSP = tenSP;
        this.xuatXu = xuatXu;
        this.donGia = donGia;
    }

    public Product(int maSP , String tenSP, String xuatXu, int donGia, byte[] image) {
        this.maSP = maSP;
        this.tenSP = tenSP;
        this.xuatXu = xuatXu;
        this.maSP = maSP;
        this.donGia = donGia;
        this.image = image;
    }

    public int getTypeDisplay() {
        return typeDisplay;
    }

    public void setTypeDisplay(int typeDisplay) {
        this.typeDisplay = typeDisplay;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public Product(int maSP, String tenSP, String xuatXu, int donGia) {
        this.maSP = maSP;
        this.tenSP = tenSP;
        this.xuatXu = xuatXu;
        this.donGia = donGia;
    }

    public int getMaSP() {
        return maSP;
    }

    public void setMaSP(int maSP) {
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
