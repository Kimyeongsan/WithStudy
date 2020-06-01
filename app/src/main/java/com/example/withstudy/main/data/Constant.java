package com.example.withstudy.main.data;

// 상수 관련 인터페이스
public interface Constant {
    /////////////////////////////
    // database child 설정
    String DB_CHILD_STUDYROOM    = "studyRooms";
    String DB_CHILD_USER         = "users";
    /////////////////////////////

    /////////////////////////////
    // item 항목 세부 정보
    int DETAIL_ITEM       = 0;
    int BASIC_ITEM        = 1;
    /////////////////////////////

    /////////////////////////////
    // 성별 관련 상수
    int MALE           = 0;
    int FEMALE         = 1;
    int ALLGENDER      = 2;
    /////////////////////////////

    /////////////////////////////
    // 나이 관련 상수
    int ALLAGE          = 0;
    /////////////////////////////

    /////////////////////////////
    // Study 공개여부 상수
    int VISIBLE        = 0;
    int NAMEVISIBLE    = 1;
    int INVISIBLE      = 2;
    /////////////////////////////
}
