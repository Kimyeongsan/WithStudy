package com.example.withstudy.main.data;

// 채팅 데이터를 묶는 클래스
public class ChatData {
    private String userName;    // 메세지를 보낸 사용자 이름
    private String message;     // 메세지 내용

    // 생성자
    public ChatData(String userName, String message) {
        this.userName = userName;
        this.message = message;
    }

    // 메세지를 보낸 사용자 이름 설정
    public void setUserName(String userName) {
        this.userName = userName;
    }

    // 메세지를 보낸 사용자 이름 반환
    public String getUserName() {
        return userName;
    }

    // 메세지 내용 설정
    public void setMessage(String message) {
        this.message = message;
    }

    // 메세지 내용 반환
    public String getMessage() {
        return message;
    }
}
