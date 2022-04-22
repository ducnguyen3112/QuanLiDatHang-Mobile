package com.example.quanlydathang.dto;

import java.io.Serializable;

public class KhachHangDto implements Serializable {
    private int id;
    private String name, address, phone;
    private byte[] image;

    //thêm contructor rỗng - linh
    public KhachHangDto() {
    }

    public KhachHangDto(String name, String address, String phone) {
        this.name = name;
        this.address = address;
        this.phone = phone;
    }

    public KhachHangDto(int id, String name, String address, String phone) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phone = phone;
    }

    public KhachHangDto(int id, String name, String address, String phone, byte[] image) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "KhachHangDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
