package com.example.myapplication;

public class catModel {

    String foodName,imageUrl,price,description,vendorId;
    public catModel(String foodName, String imageUrl, String price, String description) {
        this.foodName = foodName;
        this.imageUrl = imageUrl;
        this.price = price;
        this.description = description;

    }
   public catModel(){

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

    public String getVendorId() {
        return vendorId;
    }




}
