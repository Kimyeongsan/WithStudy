package com.example.withstudy.main.data;

// 스터디 방의 정보를 가지는 클래스
public class StudyInfo {
    private short minMember;    // 최소 인원
    private char limitGender;   // 성별 제한
    private short minAge;       // 나이 제한
    private boolean visible;  // 공개 여부

    // 생성자
    StudyInfo(short minMember, char limitGender, short minAge, boolean visible) {
        this.minMember = minMember;
        this.limitGender = limitGender;
        this.minAge = minAge;
        this.visible = visible;
    }

    // 최소 인원 설정
    public void setMinMember(short minMember) {
        this.minMember = minMember;
    }

    // 최소 인원 가져오기
    public short getMinMember() {
        return minMember;
    }

    // 성별 제한 설정
    public void setLimitGender(char limitGender) {
        this.limitGender = limitGender;
    }

    // 성별 제한 가져오기
    public char getLimitGender() {
        return limitGender;
    }

    // 나이 제한 설정
    public void setMinAge(short minAge) {
        this.minAge = minAge;
    }

    // 나이 제한 가져오기
    public short getMinAge() {
        return minAge;
    }

    // 공개 여부 설정
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    // 공개 여부 반환
    public boolean isVisible() {
        return visible;
    }
}
