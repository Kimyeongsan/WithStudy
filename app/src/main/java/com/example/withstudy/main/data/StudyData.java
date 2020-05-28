package com.example.withstudy.main.data;

import java.util.LinkedList;

// 스터디 방의 정보를 가지는 클래스
public class StudyData {
//    private int                         studyId;        // 스터디 고유 번호(스터디 구별용)
    private String                      studyName;      // 스터디 이름
    private int                         minMember;      // 최소 인원
    private int                         limitGender;    // 성별 제한
    private int                         minAge;         // 나이 제한
    private int                         visible;        // 공개 여부
    private int                         duration;       // 모임 지속기간
    private String                      frequency;      // 모임 빈도
//    private String                      goal;           // 스터디 목표
//    private LinkedList<CurriculumData>  curriculums;    // 커리큘럼 데이터
//    private ChatRoomData                chatRoom;       // 채팅방

    // 생성자
    public StudyData(String studyName, int minMember, int limitGender, int minAge, int visible, int duration, String frequency) {
        this.studyName = studyName;
        this.minMember = minMember;
        this.limitGender = limitGender;
        this.minAge = minAge;
        this.visible = visible;
        this.duration = duration;
        this.frequency = frequency;

        initialize();
    }

    public StudyData() {
    }

    // 스터디 방 처음 생성시 초기화
    private void initialize() {
//        curriculums = new LinkedList<CurriculumData>();
//        chatRoom = new ChatRoomData();
    }

    // 스터디 이름 설정
    public void setStudyName(String studyName) {
        this.studyName = studyName;
    }

    // 스터디 이름 가져오기
    public String getStudyName() {
        return studyName;
    }

    // 최소 인원 설정
    public void setMinMember(int minMember) {
        this.minMember = minMember;
    }

    // 최소 인원 가져오기
    public int getMinMember() {
        return minMember;
    }

    // 성별 제한 설정
    public void setLimitGender(int limitGender) {
        this.limitGender = limitGender;
    }

    // 성별 제한 가져오기
    public int getLimitGender() {
        return limitGender;
    }

    // 나이 제한 설정
    public void setMinAge(int minAge) {
        this.minAge = minAge;
    }

    // 나이 제한 가져오기
    public int getMinAge() {
        return minAge;
    }

    // 공개 여부 설정
    public void setVisible(int visible) {
        this.visible = visible;
    }

    // 공개 여부 반환
    public int isVisible() {
        return visible;
    }

    // 모임 지속기간 설정
    public void setDuration(int duration) { this.duration = duration; }

    // 모임 지속기간 반환
    public int getDuration() { return duration; }

    // 모임 빈도 설정
    public void setFrequency(String frequency) { this.frequency = frequency; }

    // 모임 빈도 반환
    public String getFrequency() { return frequency; }
}
