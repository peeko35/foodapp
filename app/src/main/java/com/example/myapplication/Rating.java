package com.example.myapplication;

public class Rating {
    float rating;
    String comment,name;
    Rating(){

    }
    public Rating(float rating, String comment, String name) {
        this.rating = rating;
        this.comment = comment;
        this.name = name;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
