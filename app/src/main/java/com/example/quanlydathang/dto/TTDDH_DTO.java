package com.example.quanlydathang.dto;

public class TTDDH_DTO {
    private int maDH;
    private int maSP;
    private int SL;

    public TTDDH_DTO() {
    }

    public TTDDH_DTO(int maDH, int maSP, int SL) {
        this.maDH = maDH;
        this.maSP = maSP;
        this.SL = SL;
    }

    public int getMaDH() {
        return maDH;
    }

    public void setMaDH(int maDH) {
        this.maDH = maDH;
    }

    public int getMaSP() {
        return maSP;
    }

    public void setMaSP(int maSP) {
        this.maSP = maSP;
    }

    public int getSL() {
        return SL;
    }

    public void setSL(int SL) {
        this.SL = SL;
    }
}
