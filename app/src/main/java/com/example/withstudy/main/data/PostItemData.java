package com.example.withstudy.main.data;

public class PostItemData {
    private String writer;  // post 작성자
    private String date;    // post 작성 날짜
    private String content; // post 내용

    // post 작성자 설정
    public void setWriter(String writer) {
        this.writer = writer;
    }

    // post 작성자 가져오기
    public String getWriter() {
        return writer;
    }

    // post 작성 날짜 설정
    public void setDate(String date) {
        this.date = date;
    }

    // post 작성 날짜 가져오기
    public String getDate() {
        return date;
    }

    // post 내용 설정
    public void setContent(String content) {
        this.content = content;
    }

    // post 내용 가져오기
    public String getContent() {
        return content;
    }
}
