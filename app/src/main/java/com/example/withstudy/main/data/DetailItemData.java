package com.example.withstudy.main.data;

public class DetailItemData extends BasicItemData {
    private String content; // Item 내용

    // Item 내용 설정
    public void setContent(String content) { this.content = content; }

    // Item 내용 가져오기
    public String getContent() { return content; }
}
