package com.example.withstudy.main.data;

import android.net.Uri;

import com.google.firebase.storage.StorageReference;

public class StudyItemData {
    private String title;  // study 제목
    private String location; // study 위치
    private StorageReference ref = null;   // storage 이미지 참조 위치

    // study 제목 설정
    public void setTitle(String title) {
        this.title = title;
    }

    // study 제목 가져오기
    public String getTitle() {
        return title;
    }

    // study 위치 설정
    public void setLocation(String location) {
        this.location = location;
    }

    // study 위치 가져오기
    public String getLocation() {
        return location;
    }

    // storage 이미지 참조 위치 설정
    public void setRef(StorageReference ref) {
        this.ref = ref;
    }

    // storage 이미지 참조 위치 반환
    public StorageReference getRef() {
        return ref;
    }
}