package com.example.withstudy.main.data;

public class BasicItemData {
    private String title;  // Item 제목
    private String result; // Item 결과

    // Item 제목 설정
    public void setTitle(String title) {
        this.title = title;
    }

    // Item 제목 가져오기
    public String getTitle() {
        return title;
    }

    // Item 결과 설정
    public void setResult(String result) {
        this.result = result;
    }

    // Item 결과 가져오기
    public String getResult() {
        return result;
    }
}