package com.example.withstudy.main.search;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.withstudy.R;
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
    ArrayList<Button> categoryBtns;
    private RecyclerView myAroundStudyRV, popularStudyRV;
    private StudyItemRVAdapter myAroundStudyRVAdapter, popularStudyRVAdapter;
    TextView myAddressTV;
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

        // 이미지 뷰
        findLocationIV = v.findViewById(R.id.search_findLocationIV);
        findLocationIV.setOnClickListener(this);

        // 텍스트 뷰
        myAddressTV = (TextView)v.findViewById(R.id.search_myAddressTV);

        // 만약 이미 유저가 위치를 설정했으면
        if(ManagementData.getInstance().getUserAddress() != null) {
            myAddressTV.setText(ManagementData.getInstance().getUserAddress());
        }

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

        // 카테고리 텍스트 변경후 보이게 → 버튼이 겹치는것 같습니다..
//        for(String category : categorys) {
//            categoryBtns.get(i).setText(category);
//            categoryBtns.get(i++).setVisibility(View.VISIBLE);
//        }

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

            System.out.println(myAddress + " == " + studyAddress);

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

        myAroundStudyRV.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, true));
        popularStudyRV.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, true));

        initStudyRVAdapter(myAroundStudyRVAdapter);
        initStudyRVAdapter(popularStudyRVAdapter);

        myAroundStudyRV.setAdapter(myAroundStudyRVAdapter);
        popularStudyRV.setAdapter(popularStudyRVAdapter);
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
            case R.id.search_categoryBtn2:
            case R.id.search_categoryBtn3:
            case R.id.search_categoryBtn4:
                ManagementData mData;

                mData = ManagementData.getInstance();

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