package com.example.withstudy.main.data;

// 스터디 방의 정보를 가지는 클래스
public class StudyData {
//    private int                         studyId;        // 스터디 고유 번호(스터디 구별용)
    private String                      studyName;      // 스터디 이름
    private String                      president;      // 스터디 생성한 사람(방장)
    private int                         minMember;      // 최소 인원
    private int                         limitGender;    // 성별 제한
    private int                         minAge;         // 나이 제한
    private int                         visible;        // 공개 여부
    private int                         duration;       // 모임 지속기간
    private String                      frequency;      // 모임 빈도
    private int                         memberCount;    // 멤버수
    private String                      iconUri;        // 아이콘 uri
    private double                      latitude;       // 위도
    private double                      longitude;      // 경도
    private String                      address;        // 주소
    private String                      category;       // 스터디 분야

    // 생성자
    public StudyData(String studyName, String president, int minMember, int limitGender, int minAge, int visible, int duration, String frequency
                        , double latitude, double longitude, String address, String category) {
        this.studyName = studyName;
        this.president = president;
        this.minMember = minMember;
        this.limitGender = limitGender;
        this.minAge = minAge;
        this.visible = visible;
        this.duration = duration;
        this.frequency = frequency;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.category = category;

        memberCount = 1;
    }

    public StudyData() {
    }

    // 스터디 이름 설정
    public void setStudyName(String studyName) {
        this.studyName = studyName;
    }

    // 스터디 이름 가져오기
    public String getStudyName() {
        return studyName;
    }

    // 스터디 방장 설정
    public void setPresident(String president) { this.president = president; }

    // 스터디 방장 가져오기
    public String getPresident() { return president; }

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

    // 멤버 수 설정
    public void setMemberCount(int memberCount) { this.memberCount = memberCount; }

    // 멤버 수 반환
    public int getMemberCount() { return memberCount; }

    // 아이콘 Uri 설정
    public void setIconUri(String iconUri) { this.iconUri = iconUri; }

    // 아이콘 Uri 반환
    public String getIconUri() { return iconUri; }

    // 위도 설정
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    // 위도 가져오기
    public double getLatitude() {
        return latitude;
    }

    // 경도 설정
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    // 경도 반환
    public double getLongitude() {
        return longitude;
    }

    // 주소 설정
    public void setAddress(String address) {
        this.address = address;
    }

    // 주소 반환
    public String getAddress() {
        return address;
    }

    // 스터디 분야 설정
    public void setCategory(String category) {
        this.category = category;
    }

    // 스터디 분야 반환
    public String getCategory() {
        return category;
    }
}
