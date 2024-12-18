package com.example.myapplication;

public class Vendors {
    String PhoneNumber,selectedLocation,stallName,address,pinCode;


    public Vendors() {
    }

    public Vendors(String phoneno, String location, String stallnm, String addres, String pincode) {
        this.PhoneNumber = phoneno;
        this.selectedLocation = location;
        this.stallName = stallnm;
        this.address = addres;
        this.pinCode = pincode;
    }

    public String getPhoneno() {
        return PhoneNumber;
    }

    public void setPhoneno(String phoneno) {
        this.PhoneNumber = phoneno;
    }

    public String getLocation() {
        return selectedLocation;
    }

    public void setLocation(String location) {
        this.selectedLocation = location;
    }

    public String getStallnm() {
        return stallName;
    }

    public void setStallnm(String stallnm) {
        this.stallName= stallnm;
    }

    public String getAddres() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPincode() {
        return pinCode;
    }

    public void setPincode(String pincode) {
        this.pinCode = pincode;
    }
}
