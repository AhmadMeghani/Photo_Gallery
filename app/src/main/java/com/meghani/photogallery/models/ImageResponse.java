package com.meghani.photogallery.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ImageResponse {
    @SerializedName("total")
    public int total;

    @SerializedName("totalHits")
    public int totalHits;

    @SerializedName("hits")
    public List<Image> imageList;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getTotalHits() {
        return totalHits;
    }

    public void setTotalHits(int totalHits) {
        this.totalHits = totalHits;
    }

    public List<Image> getImageList() {
        return imageList;
    }

    public void setImageList(List<Image> imageList) {
        this.imageList = imageList;
    }
}
