package com.example.withstudy.main.data;

// 커리큘럼 데이터 클래스
public class CurriculumData {
    private int year;   // 연도
    private int month;  // 달
    private int day;    // 일
    private String content;   // 커리큘럼 내용

    // 커리큘럼 내용 설정
    public void setContent(String content) {
        this.content = content;
    }

    // 커리큘럼 내용 가져오기
    public String getContent() {
        return content;
    }
}
