package com.example.myapplication;

public class MainModelrecy {
    String stallName,foodName,imageUrl,price,description,vendorId;
    float averageRating;
    MainModelrecy(){

    }


    public MainModelrecy(String stallName, String foodName, String imageUrl, String price, String description, String vendorId,float averageRating) {
        this.stallName = stallName;
        this.vendorId=vendorId;
        this.foodName = foodName;
        this.imageUrl = imageUrl;
        this.price=price;
        this.description=description;
        this.averageRating = averageRating;
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

}
