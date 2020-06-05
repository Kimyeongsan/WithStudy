package com.example.withstudy.main.data;

import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.renderscript.Sampler;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

// 앱상에서 전반적인 데이터 관리
public class ManagementData {
    private static ManagementData mData = new ManagementData();
    private UserData userData;                  // 유저 정보
    private String userAddress;                 // 앱 사용중 유저 위치
    private ArrayList<StudyData> joinStudys;    // 해당 유저가 가입한 스터디
    private HashMap<String, StudyData> studys;  // 생성된 모든 스터디
    private HashMap<String, Boolean> categorys; // 생성된 스터디들의 카테고리

    private ManagementData() {
        userData = null;
        userAddress = null;
        joinStudys = new ArrayList<StudyData>();
        studys = new HashMap<String, StudyData>();
        categorys = new HashMap<String, Boolean>();

        // 생성된 모든 스터디와 그에 해당하는 카테고리 찾기
        findAllStudy();
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

    // User Address 설정
    public void setUserAddress(String userAddress) { this.userAddress = userAddress; }

    // User Address 반환
    public String getUserAddress() {
        return userAddress;
    }

    // 유저가 가입한 스터디 추가(이미 존재하면 추가하면 안됨)
    public void addJoinStudy(StudyData studyData) {
        // 이미 존재하는지 찾기
        for(StudyData data : joinStudys) {
            // 이미 존재
            if(data.getStudyName().equals(studyData.getStudyName())) {
                return;
            }
        }

        joinStudys.add(studyData);
    }

    // 유저가 가입한 스터디 목록 반환
    public ArrayList<StudyData> getJoinStudys() {
        return joinStudys;
    }

    // 생성된 모든 스터디와 그에 해당하는 카테고리 찾기
    public void findAllStudy() {
        // 모든 스터디 목록 받아와서 띄워주기
        FirebaseDatabase.getInstance().getReference()
            .child(Constant.DB_CHILD_STUDYROOM)
                .addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // 각 스터디뽑아와서 집어넣기
                    for(DataSnapshot data : dataSnapshot.getChildren()) {
                        StudyData studyData;

                        studyData = data.getValue(StudyData.class);

                        // 해당 스터디를 포함시키지 않았을 때만
                        if(!studys.containsKey(studyData.getStudyName())) {
                            studys.put(studyData.getStudyName(), studyData);
                        }

                        // 해당 카테고리를 아직 추가 안했으면 추가하기
                        if(!studys.containsKey(studyData.getCategory())) {
                            categorys.put(studyData.getCategory(), true);
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            }
        );
    }

    // 카테고리들 반환
    public HashMap<String, Boolean>getCategorys() {
        return categorys;
    }

    // 스터디들 반환
    public HashMap<String, StudyData>getStudys() {
        return studys;
    }

    // 주소를 기반으로 한글만 반환
    public static String convertAddressToHanGul(String str) {
        StringBuffer hangul;

        hangul = new StringBuffer();

        for(int i = 0; i < str.length(); i++) {
            // 한글이면
            if(str.charAt(i) >= 0xAC00 && str.charAt(i) <= 0xD743) {
                hangul.append(str.charAt(i));
            }
        }

        return hangul.toString();
    }

    // 회전 각 찾기
    public static int exifOrientationToDegrees(int exifOrientation) {
        if(exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if(exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if(exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }

        return 0;
    }

    // 비트맵 회전
    public static Bitmap rotate(Bitmap bitmap, int degrees) {
        if(degrees != 0 && bitmap != null) {
            Matrix m;

            m = new Matrix();

            m.setRotate(degrees, (float)bitmap.getWidth() / 2, (float)bitmap.getHeight() / 2);

            try {
                Bitmap converted;

                converted = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);

                if(bitmap != converted) {
                    bitmap.recycle();
                    bitmap = converted;
                }
            } catch(OutOfMemoryError e) {
                // 메모리가 부족한 경우
                e.printStackTrace();
            }
        }

        return bitmap;
    }

    // 상대경로를 가진 Uri에서 절대경로를 가진 Uri를 구해 반환
    public static Uri getAbsolutePathFromUri(ContentResolver cr, Uri fromUri) {
        String[] proj = { MediaStore.Images.Media.DATA};
        Cursor cursor;
        String path;
        Uri uri;

        cursor = cr.query(fromUri, proj, null, null, null);
        cursor.moveToNext();

        path = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA));
        uri = Uri.fromFile(new File(path));
        cursor.close();

        return uri;
    }

    // 디비에 유저 등록
    public static void registerUser(FirebaseUser user) {
        ManagementData mData;   // 싱글톤 객체(앱상에서 전반적인 데이터 관리)

        // 싱글톤 객체에 유저 정보 등록
        mData = ManagementData.getInstance();
        mData.setUserData(new UserData(user.getUid(), user.getDisplayName(), user.getEmail(), null));

        // 이미 DB에 존재하는 유저면 화면만 넘기기
        FirebaseDatabase.getInstance().getReference().child(Constant.DB_CHILD_USER).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserData userData;

                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    // Uid를 비교해서 같으면 디비에 등록X
                    if (data.getValue(UserData.class).getUser_Id().equals(user.getUid())) {
                        return;
                    }
                }

                // 유저 정보 생성
                userData = new UserData(user.getUid(), user.getDisplayName(), user.getEmail(), null);

                // DB에 유저 정보 등록
                insertUserToDatabase(userData);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    // 유저 정보를 디비에 등록
    private static void insertUserToDatabase(UserData userData) {
        DatabaseReference userRef;

        userRef = FirebaseDatabase.getInstance().getReference().child(Constant.DB_CHILD_USER).child(userData.getUser_Id());

        // 디비에 유저 생성
        userRef.setValue(userData);
    }
}
