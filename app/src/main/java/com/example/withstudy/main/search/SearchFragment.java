package com.example.withstudy.main.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.withstudy.R;
import com.example.withstudy.main.data.StudyItemData;
import com.example.withstudy.ui.studyroom.StudyItemRVAdapter;

import java.util.Arrays;
import java.util.List;

public class SearchFragment extends Fragment {
    private RecyclerView myAroundStudyRV, popularStudyRV;
    private StudyItemRVAdapter myAroundStudyRVAdapter, popularStudyRVAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search, container, false);

        myAroundStudyRV = v.findViewById(R.id.myAroundStudyRV);
        popularStudyRV = v.findViewById(R.id.popularStudyRV);

        initialize();

        return v;
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

    // 모든 RecyclerView 및 adapter 초기화, click listener 추가
    private void initAllRecyclerView() {
        myAroundStudyRVAdapter   = new StudyItemRVAdapter();
        popularStudyRVAdapter    = new StudyItemRVAdapter();

        myAroundStudyRV.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, true));
        popularStudyRV.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, true));

        initMyAroundStudyRVAdapter();
        initPopularStudyRVAdapter();

        myAroundStudyRV.setAdapter(myAroundStudyRVAdapter);
        popularStudyRV.setAdapter(popularStudyRVAdapter);
    }

    private void initMyAroundStudyRVAdapter() {
        myAroundStudyRVAdapter.setOnItemClickListener(new StudyItemRVAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                System.out.println("내주변 스터디 클릭");
            }
        });
    }

    private void initPopularStudyRVAdapter() {
        popularStudyRVAdapter.setOnItemClickListener(new StudyItemRVAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                System.out.println("인기있는 스터디 클릭");
            }
        });
    }

    // RecyclerView의 Item 항목 설정
    private void setData() {
        List<String> myAroundStudyTitles, popularStudyTitles;
        List<String> myAroundStudyLocations, popularStudyLocations;

        myAroundStudyTitles = Arrays.asList("내 주변 스터디1", "내 주변 스터디2");
        myAroundStudyLocations = Arrays.asList("내 주변 스터디 위치1", "내 주변 스터디 위치2");

        popularStudyTitles = Arrays.asList("인기있는 스터디1", "인기있는 스터디2");
        popularStudyLocations = Arrays.asList("인기있는 스터디 위치1", "인기있는 스터디 위치2");

        // List의 값들을 StudyItemData 객체에 설정
        for(int i = 0; i < myAroundStudyTitles.size(); i++) {
            StudyItemData data;

            data = new StudyItemData();

            data.setTitle(myAroundStudyTitles.get(i));
            data.setLocation(myAroundStudyLocations.get(i));

            // Adapter에 추가
            myAroundStudyRVAdapter.addItem(data);
        }

        // List의 값들을 StudyItemData 객체에 설정
        for(int i = 0; i < popularStudyTitles.size(); i++) {
            StudyItemData data;

            data = new StudyItemData();

            data.setTitle(popularStudyTitles.get(i));
            data.setLocation(popularStudyLocations.get(i));

            // Adapter에 추가
            popularStudyRVAdapter.addItem(data);
        }

        // Adapter의 값이 변경됨을 알려줌으로써 변경된 값 표시
        myAroundStudyRVAdapter.notifyDataSetChanged();
        popularStudyRVAdapter.notifyDataSetChanged();
    }
}