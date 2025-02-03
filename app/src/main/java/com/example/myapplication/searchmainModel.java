package com.example.myapplication;

public class searchmainModel {
    String stallName,foodName,location,imageUrl;

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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public searchmainModel(String stallName, String foodName, String location, String imageUrl) {
        this.stallName = stallName;
        this.foodName = foodName;
        this.location = location;
        this.imageUrl = imageUrl;
    }
}
