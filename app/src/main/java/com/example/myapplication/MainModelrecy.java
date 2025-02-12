package com.example.myapplication;

public class MainModelrecy {
    String stallName,foodName,imageUrl,price,description,vendorId,address,location;
    float averageRating;
    MainModelrecy(){

    }





    public MainModelrecy(String stallName, String foodName, String imageUrl, String price, String description, String vendorId, float averageRating, String address, String location) {
        this.stallName = stallName;
        this.vendorId=vendorId;
        this.foodName = foodName;
        this.imageUrl = imageUrl;
        this.price=price;
        this.description=description;
        this.averageRating = averageRating;
        this.address=address;
        this.location=location;
    }
    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public String getStallName() {
        return stallName;
    }

    public void setStallName(String stallName) {
        this.stallName = stallName;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public float getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double avgRating) {
        this.averageRating = averageRating;
    }
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

}
