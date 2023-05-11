package com.example.movierecommendation;

public class Movies {
    String name, imglink, Categ;
    int likeStatus;

    public Movies(String name, String imglink, String categ, int likeStatus) {
        this.name = name;
        this.imglink = imglink;
        this.Categ = categ;
        this.likeStatus = likeStatus;
    }

    public void setLikeStatus(int i) {
        this.likeStatus = i;
    }
}
