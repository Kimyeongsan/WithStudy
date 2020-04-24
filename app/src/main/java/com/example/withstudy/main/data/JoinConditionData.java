package com.example.withstudy.main.data;

public class JoinConditionData {
    private String title;   // 가입조건 제목
    private String content; // 가입조건 내용

    // 가입조건 제목 설정
    public void setTitle(String title) {
        this.title = title;
    }

    // 가입조건 제목 가져오기
    public String getTitle() {
        return title;
    }

    // 가입조건 내용 설정
    public void setContent(String content) {
        this.content = content;
    }

    // 가입조건 내용 가져오기
    public String getContent() {
        return content;
    }
}
