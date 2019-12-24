package com.example.user.mymp3_2;

public class MainData {
    String fileName;
    String star;

    public MainData(String fileName,String star) {
        this.fileName = fileName;
        this.star = star;
    }

    public String getFileName() {
        return fileName;
    }

    public String getStar() {
        return star;
    }

    public void setStar(String star) {
        this.star = star;
    }
}
