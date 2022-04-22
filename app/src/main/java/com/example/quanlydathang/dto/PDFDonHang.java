package com.example.quanlydathang.dto;

public class PDFDonHang {
    ChiTietDonHang chiTietDonHang;
    Product product;

    public PDFDonHang(ChiTietDonHang chiTietDonHang, Product product) {
        this.chiTietDonHang = chiTietDonHang;
        this.product = product;
    }

    public PDFDonHang() {

    }

    public ChiTietDonHang getChiTietDonHang() {
        return chiTietDonHang;
    }

    public void setChiTietDonHang(ChiTietDonHang chiTietDonHang) {
        this.chiTietDonHang = chiTietDonHang;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
