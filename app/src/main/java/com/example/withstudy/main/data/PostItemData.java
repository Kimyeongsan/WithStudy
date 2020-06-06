package com.example.withstudy.main.data;

public class PostItemData {
    private String writer;  // post 작성자
    private String date;    // post 작성 날짜
    private String content; // post 내용

    private String cl_title; // post 내용
    private String cl_contnet; // post 내용
    private String when; /// Calendar 데이터

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

    // calendar data

    // calendar 제목 설정
    public void setCl_title(String cl_title) {
        this.cl_title = cl_title;
    }

    // calendar 제목 가져오기
    public String getCl_title() {
        return cl_title;
    }

    // calendar 일정 설정
    public void setCl_contnet(String cl_contnet) {
        this.cl_contnet = cl_contnet;
    }

    // calendar 일정 가져오기
    public String getCl_contnet() {
        return cl_contnet;
    }

    // calendar 일자 설정
    public void setWhen(String when) {
        this.when = when;
    }

    // calendar 일자 가져오기
    public String getWhen() {
        return when;
    }
}
