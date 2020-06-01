package com.example.withstudy.main.data;

import java.util.ArrayList;

// 앱상에서 전반적인 데이터 관리
public class ManagementData {
    private static ManagementData mData = new ManagementData();
    private UserData userData;      // 유저 정보
    private ArrayList<StudyData> joinStudys;    // 해당 유저가 가입한 스터디

    private ManagementData() {
        joinStudys = new ArrayList<StudyData>();
    }

    public static ManagementData getInstance() {
        return mData;
    }

    // UserData 설정
    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    // UserData 반환
    public UserData getUserData() {
        return userData;
    }

    // 유저가 가입한 스터디 추가
    public void addJoinStudy(StudyData studyData) {
        joinStudys.add(studyData);
    }

    // 유저가 가입한 스터디 목록 반환
    public ArrayList<StudyData> getJoinStudys() {
        return joinStudys;
    }
}
