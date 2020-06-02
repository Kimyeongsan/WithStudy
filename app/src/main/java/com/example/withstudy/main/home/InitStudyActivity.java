package com.example.withstudy.main.home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.withstudy.R;
import com.example.withstudy.main.data.Constant;
import com.example.withstudy.main.data.StudyData;
import com.example.withstudy.ui.studyroom.MapActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

public class InitStudyActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int REQUEST_ALBUM   = 1;
    private Uri selectedImageUri;
    private int checkFlag = 0; // 중복명 검사 플래그(중복명이 아니었을 때 화면이 넘어가는데 시작하기 버튼을 여러번 누르면 여러개 생성 방지)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init_study);

        initialize();
    }

    private void initialize() {
        Button backFromInitStudyBtn, makeStudyBtn, addPictureBtn;
        Button test;
        ImageView studyImageIV;

        backFromInitStudyBtn = (Button)findViewById(R.id.backFromInitStudyBtn);
        makeStudyBtn = (Button)findViewById(R.id.makeStudyBtn);
        addPictureBtn = (Button)findViewById(R.id.addPictureBtn);
        studyImageIV = (ImageView)findViewById(R.id.initStudy_studyImageIV);

        // Click Listener 추가
        backFromInitStudyBtn.setOnClickListener(this);
        makeStudyBtn.setOnClickListener(this);
        addPictureBtn.setOnClickListener(this);
        studyImageIV.setOnClickListener(this);

        test = (Button)findViewById(R.id.test);
        test.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.test:
                Intent intent;

                intent = new Intent(InitStudyActivity.this, MapActivity.class);

                startActivity(intent);
                break;
            case R.id.backFromInitStudyBtn: // 뒤로가기 버튼
                onBackPressed();

                break;

            // 사진 추가 버튼

            case R.id.addPictureBtn:
                pickFromAlbum();

                break;

            case R.id.makeStudyBtn: // 시작하기 버튼
                final TextInputEditText studyNameText;

                // 이미 화면을 띄웠으면 나가기
                if(checkFlag == 1)
                    return;

                // 모임명을 입력했는지 확인
                studyNameText = (TextInputEditText)findViewById(R.id.studyName);

                // 비어있거나 기본 입력 메세지나 첫글자가 공백이면 다시 입력하게 하기
                if(studyNameText.getText().length() == 0 || studyNameText.getText().toString().equals(getString(R.string.InputStudyNameText)) || studyNameText.getText().charAt(0) == ' ') {
                    Toast.makeText(getApplicationContext(), "모임명을 입력해주세요", Toast.LENGTH_LONG).show();

                    break;
                }

                checkFlag = 1;

                // 스터디명 중복명 체크
                FirebaseDatabase.getInstance().getReference().child(Constant.DB_CHILD_STUDYROOM).addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for(DataSnapshot data : dataSnapshot.getChildren()) {
                                    // 중복이면 못만들게 하기
                                    if(data.getValue(StudyData.class).getStudyName().equals(studyNameText.getText().toString())) {
                                        Toast.makeText(getApplicationContext(), "중복된 모임명입니다", Toast.LENGTH_SHORT).show();

                                        checkFlag = 0;

                                        return;
                                    }
                                }

                                // activity_make_study 레이아웃으로 변경하기 위한 intent 설정
                                Intent intent = new Intent(InitStudyActivity.this, MakeStudyActivity.class);

                                // 모임명 및 이미지를 선택 했을 시 이미지 Uri 전달
                                intent.putExtra("studyName", studyNameText.getText().toString());

                                if(selectedImageUri != null)
                                    intent.putExtra("iconUri", selectedImageUri.toString());

                                startActivity(intent);

                                checkFlag = 0;
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        }
                );

                break;
        }
    }

    // 앨범에 사진 가져오기
    private void pickFromAlbum() {
        Intent intent;

        intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");

        startActivityForResult(intent, REQUEST_ALBUM);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        ImageView studyImageIV;
        Button addPictureBtn;

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK)
            return;

        selectedImageUri = null;

        // 앨범에서 이미지 선택한 경우
        if (requestCode == REQUEST_ALBUM) {
            selectedImageUri = data.getData();
        }

        // 이미지 표시 및 버튼 숨기기
        studyImageIV = (ImageView)findViewById(R.id.initStudy_studyImageIV);
        studyImageIV.setImageURI(selectedImageUri);

        addPictureBtn = (Button) findViewById(R.id.addPictureBtn);
        addPictureBtn.setVisibility(View.INVISIBLE);
    }
}