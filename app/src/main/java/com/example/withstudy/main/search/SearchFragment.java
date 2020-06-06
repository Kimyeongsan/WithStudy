package com.example.withstudy.main.search;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.withstudy.R;
import com.example.withstudy.main.data.Constant;
import com.example.withstudy.main.data.ManagementData;
import com.example.withstudy.main.data.StudyData;
import com.example.withstudy.main.data.StudyItemData;
import com.example.withstudy.ui.studyroom.MapActivity;
import com.example.withstudy.ui.studyroom.StudyItemRVAdapter;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.example.withstudy.ui.studyroom.StudyRoomViewPager;

import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapReverseGeoCoder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SearchFragment extends Fragment implements View.OnClickListener, MapReverseGeoCoder.ReverseGeoCodingResultListener {
    private ArrayList<Button> categoryBtns;
    private RecyclerView myAroundStudyRV, popularStudyRV, resultSearchRV;
    private StudyItemRVAdapter myAroundStudyRVAdapter, popularStudyRVAdapter, resultSearchRVAdapter;
    private TextView myAddressTV;
    private EditText searchStudy;
    private int REQUEST_MAP = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v;
        Button categoryBtn1, categoryBtn2, categoryBtn3, categoryBtn4;
        ImageView findLocationIV;

        v = inflater.inflate(R.layout.fragment_search, container, false);

        //////////////////////////////////////////////////
        // 레이아웃
        // 검색 결과 레이아웃 안보이게
        v.findViewById(R.id.search_resultSearchLayout).setVisibility(View.INVISIBLE);
        //////////////////////////////////////////////////

        //////////////////////////////////////////////////
        // 카테고리 항목 설정
        categoryBtns = new ArrayList<Button>();

        categoryBtns.add((Button)v.findViewById(R.id.search_categoryBtn1));
        categoryBtns.add((Button)v.findViewById(R.id.search_categoryBtn2));
        categoryBtns.add((Button)v.findViewById(R.id.search_categoryBtn3));
        categoryBtns.add((Button)v.findViewById(R.id.search_categoryBtn4));

        // click listener 등록하고 일단 모두 안보이게
        for(Button categoryBtn : categoryBtns) {
            categoryBtn.setOnClickListener(this);
            categoryBtn.setVisibility(View.INVISIBLE);
        }
        //////////////////////////////////////////////////

        myAroundStudyRV = v.findViewById(R.id.myAroundStudyRV);
        popularStudyRV = v.findViewById(R.id.popularStudyRV);
        resultSearchRV = v.findViewById(R.id.search_resultSearchRV);

        // 이미지 뷰
        findLocationIV = v.findViewById(R.id.search_findLocationIV);
        findLocationIV.setOnClickListener(this);

        // 텍스트 뷰
        myAddressTV = (TextView)v.findViewById(R.id.search_myAddressTV);

        // 만약 이미 유저가 위치를 설정했으면
        if(ManagementData.getInstance().getUserAddress() != null) {
            myAddressTV.setText(ManagementData.getInstance().getUserAddress());
        }

        // 검색 창
        searchStudy = (EditText)v.findViewById(R.id.searchStudyName);
        searchStudy.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // 입력하기 직전
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                HashMap<String, StudyData> studyDatas;
                Set<Map.Entry<String, StudyData>> studySet;

                studyDatas = ManagementData.getInstance().getStudys();
                studySet = studyDatas.entrySet();

                if(studySet != null) {
                    // 입력칸에 변화 발생 시
                    Iterator<Map.Entry<String, StudyData>> studyIt;

                    studyIt = studySet.iterator();

                    // 일단 아이템 초기화
                    resultSearchRVAdapter.delAllItem();
                    resultSearchRVAdapter.notifyDataSetChanged();

                    while (studyIt.hasNext()) {
                        Map.Entry<String, StudyData> studyDataMap;
                        StudyData studyData;

                        studyDataMap = (Map.Entry<String, StudyData>) studyIt.next();
                        studyData = studyDataMap.getValue();

                        // 비공개면 보여주면 안됨
                        if(studyData.isVisible() == Constant.INVISIBLE) {
                            continue;
                        }

                        // 스터디 명 또는 분야랑 비교해보기(입력한 문자열이 포함되는지)
                        if (studyData.getStudyName().contains(s) || studyData.getCategory().contains(s)) {
                            StudyItemData studyItem;

                            studyItem = new StudyItemData();

                            studyItem.setTitle(studyData.getStudyName());
                            studyItem.setAddress(studyData.getAddress());

                            // 아이콘 설정한 것이 있으면
                            if (!studyData.getIconUri().equals("")) {
                                StorageReference studyIconRef;

                                studyIconRef = FirebaseStorage.getInstance().getReferenceFromUrl(studyData.getIconUri());

                                studyItem.setRef(studyIconRef);

                                resultSearchRVAdapter.setContext(getContext());
                            }

                            // 포함되면 검색결과에 추가
                            resultSearchRVAdapter.addItem(studyItem);
                        }
                    }

                    // 변경된 값 표시
                    resultSearchRVAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // 입력 후
                ConstraintLayout mainLayout, searchLayout;

                mainLayout = v.findViewById(R.id.search_mainLayout);
                searchLayout = v.findViewById(R.id.search_resultSearchLayout);

                // 아무 글씨도 없다면 메인 레이아웃으로 변경
                if(s.length() == 0) {
                    searchLayout.setVisibility(View.INVISIBLE);
                    mainLayout.setVisibility(View.VISIBLE);
                } else {
                    // 있다면 검색 레이아웃 보이게
                    mainLayout.setVisibility(View.INVISIBLE);
                    searchLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        initialize();

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        // 생성된 스터디들이 앱 상 데이터에 추가되었을 때만
        if(ManagementData.getInstance().getCategorys().size() > 0) {
            refreshCategory();
        }

        // 유저 위치가 설정되었을 때만
        if(ManagementData.getInstance().getUserAddress() != null) {
            myAroundStudyRVAdapter.delAllItem();
            myAroundStudyRVAdapter.notifyDataSetChanged();

            refreshMyAroundStudy();
        }
    }

    private void initialize() {
        //////////////////////////////////////////////////
        // Item 항목을 둘 RecyclerView 및 LinearLayout 설정
        initAllRecyclerView();
        //////////////////////////////////////////////////

        //////////////////////////////////////////////////
        // Item 항목 설정
        setData();
        //////////////////////////////////////////////////
    }

    // 카테고리들 새로고침
    private void refreshCategory() {
        Set<String> categorys;
        int i = 0;

        // 스터디 카테고리들 뽑아오기
        categorys = ManagementData.getInstance().getCategorys().keySet();

        // 카테고리 텍스트 변경후 보이게
        for(String category : categorys) {
            categoryBtns.get(i).setText(category);
            categoryBtns.get(i++).setVisibility(View.VISIBLE);
        }

        // 카테고리 수가 버튼 수보다 적으면 이후 버튼은 안보여야 함
        while(i < categoryBtns.size()) {
            categoryBtns.get(i++).setVisibility(View.INVISIBLE);
        }
    }

    // 내 주변 스터디 새로고침
    private void refreshMyAroundStudy() {
        ManagementData mData;
        HashMap<String, StudyData> studys;
        Set<Map.Entry<String, StudyData>> set;
        Iterator<Map.Entry<String, StudyData>> it;
        String myAddress;

        mData = ManagementData.getInstance();

        // 내주소 가져와서 한글 추출
        myAddress = mData.getUserAddress();
        myAddress = ManagementData.convertAddressToHanGul(myAddress);

        // (스터디명, 스터디 데이터)쌍 가져오기
        studys = mData.getStudys();
        set = studys.entrySet();
        it = set.iterator();

        // 내 주소와 스터디 주소 한글명으로 변환
        while(it.hasNext()) {
            Map.Entry<String, StudyData> studyDataMap;
            StudyData studyData;
            String studyAddress;

            studyDataMap = (Map.Entry<String, StudyData>)it.next();
            studyData = studyDataMap.getValue();

            studyAddress = studyData.getAddress();

            studyAddress = ManagementData.convertAddressToHanGul(studyAddress);

            // 일치하면 주변 스터디 목록에 추가
            if(myAddress.equals(studyAddress)) {
                StudyItemData data;

                data = new StudyItemData();

                data.setTitle(studyData.getStudyName());
                data.setAddress(studyData.getAddress());

                // 등록된 스터디 아이콘이 존재할 때만
                if(!studyData.getIconUri().equals("")) {
                    StorageReference studyIconRef;

                    studyIconRef = FirebaseStorage.getInstance().getReferenceFromUrl(studyData.getIconUri());

                    data.setRef(studyIconRef);

                    myAroundStudyRVAdapter.setContext(getContext());
                }

                // Adapter에 추가
                myAroundStudyRVAdapter.addItem(data);
            }
        }

        // Adapter의 값이 변경됨을 알려줌으로써 변경된 값 표시
        myAroundStudyRVAdapter.notifyDataSetChanged();
    }

    // 모든 RecyclerView 및 adapter 초기화, click listener 추가
    private void initAllRecyclerView() {
        myAroundStudyRVAdapter   = new StudyItemRVAdapter();
        popularStudyRVAdapter    = new StudyItemRVAdapter();
        resultSearchRVAdapter    = new StudyItemRVAdapter();

        myAroundStudyRV.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, true));
        popularStudyRV.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, true));
        resultSearchRV.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, true));

        initStudyRVAdapter(myAroundStudyRVAdapter);
        initStudyRVAdapter(popularStudyRVAdapter);
        initStudyRVAdapter(resultSearchRVAdapter);

        myAroundStudyRV.setAdapter(myAroundStudyRVAdapter);
        popularStudyRV.setAdapter(popularStudyRVAdapter);
        resultSearchRV.setAdapter(resultSearchRVAdapter);
    }

    private void initStudyRVAdapter(StudyItemRVAdapter studyItemRVAdapter) {
        studyItemRVAdapter.setOnItemClickListener(new StudyItemRVAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                StudyItemData studyItem;
                Intent intent;

                // 클릭한 스터디 항목 가져오기
                studyItem = studyItemRVAdapter.getItem(pos);

                // 클릭한 스터디 화면으로 이동해야함
                intent = new Intent(getActivity(), StudyRoomViewPager.class);

                intent.putExtra("studyName", studyItem.getTitle());

                startActivity(intent);
            }
        });
    }

    // RecyclerView의 Item 항목 설정
    private void setData() {
        List<String> popularStudyTitles;
        List<String> popularStudyAddresss;

        popularStudyTitles = Arrays.asList("인기있는 스터디1", "인기있는 스터디2");
        popularStudyAddresss = Arrays.asList("인기있는 스터디 위치1", "인기있는 스터디 위치2");

        // List의 값들을 StudyItemData 객체에 설정
        for(int i = 0; i < popularStudyTitles.size(); i++) {
            StudyItemData data;

            data = new StudyItemData();

            data.setTitle(popularStudyTitles.get(i));
            data.setAddress(popularStudyAddresss.get(i));

            // Adapter에 추가
            popularStudyRVAdapter.addItem(data);
        }

        // Adapter의 값이 변경됨을 알려줌으로써 변경된 값 표시
        popularStudyRVAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            // 카테고리 버튼
            case R.id.search_categoryBtn1:
                searchStudy.setText(categoryBtns.get(0).getText());

                break;

            case R.id.search_categoryBtn2:
                searchStudy.setText(categoryBtns.get(1).getText());

                break;
            case R.id.search_categoryBtn3:
                searchStudy.setText(categoryBtns.get(2).getText());

                break;
            case R.id.search_categoryBtn4:
                searchStudy.setText(categoryBtns.get(3).getText());

                break;

            // 내 위치 설정
            case R.id.search_findLocationIV:
                Intent intent;

                intent = new Intent(getActivity(), MapActivity.class);
                intent.putExtra("activity", "SearchFragment");

                startActivityForResult(intent, REQUEST_MAP);

                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        MapReverseGeoCoder reverseGeoCoder;
        MapPoint mapPoint;
        double latitude, longitude;

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK)
            return;

        // 위도, 경도 받아오기
        latitude = data.getDoubleExtra("latitude", 0);
        longitude = data.getDoubleExtra("longitude", 0);

        // 위도, 경도로 MapPoint 변환
        mapPoint = MapPoint.mapPointWithGeoCoord(latitude, longitude);

        reverseGeoCoder = new MapReverseGeoCoder("d63aae8019a77e3a15b350bda76bc561", mapPoint
                , this, getActivity());

        reverseGeoCoder.startFindingAddress();
    }

    // 유저의 주소를 찾은 경우
    @Override
    public void onReverseGeoCoderFoundAddress(MapReverseGeoCoder mapReverseGeoCoder, String s) {
        // 유저 주소 설정
        ManagementData.getInstance().setUserAddress(s);

        myAddressTV.setText("내 위치: " + s);

        // 내 주변 스터디 갱신해주기
        refreshMyAroundStudy();
    }

    @Override
    public void onReverseGeoCoderFailedToFindAddress(MapReverseGeoCoder mapReverseGeoCoder) {

    }
}