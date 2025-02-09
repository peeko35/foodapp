package com.example.myapplication;

public class searchmainModel {
    String stallName,foodName,imageUrl,location,price,description,vendorId;
    float averageRating;



    public searchmainModel(String imageUrl, String stallName, String foodName, String location, String price, String description, String vendorId, float averageRating) {
        this.imageUrl = imageUrl;
        this.stallName = stallName;
        this.foodName = foodName;
        this.location = location;
        this.vendorId=vendorId;
        this.price=price;
        this.description=description;
        this.averageRating = averageRating;

    }
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public float getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(float averageRating) {
        this.averageRating = averageRating;
    }


}
