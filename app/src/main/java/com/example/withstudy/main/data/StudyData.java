package com.example.withstudy.main.data;

import java.util.LinkedList;

// 스터디 방의 정보를 가지는 클래스
public class StudyData {
    private int                         studyId;        // 스터디 고유 번호(스터디 구별용)
    private String                      studyName;      // 스터디 이름
    private short                       minMember;      // 최소 인원
    private char                        limitGender;    // 성별 제한
    private short                       minAge;         // 나이 제한
    private char                        visible;        // 공개 여부
    private String                      goal;           // 스터디 목표
    private LinkedList<CurriculumData>  curriculums;    // 커리큘럼 데이터
//    private ChatRoomData                chatRoom;       // 채팅방

    // 생성자
    StudyData(String studyName, short minMember, char limitGender, short minAge, char visible) {
        this.studyName = studyName;
        this.minMember = minMember;
        this.limitGender = limitGender;
        this.minAge = minAge;
        this.visible = visible;

        initialize();
    }

    // 스터디 방 처음 생성시 초기화
    private void initialize() {
        curriculums = new LinkedList<CurriculumData>();
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
    public void setVisible(char visible) {
        this.visible = visible;
    }

    // 공개 여부 반환
    public char isVisible() {
        return visible;
    }
}
