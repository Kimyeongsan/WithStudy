package com.example.withstudy.main.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.withstudy.R;

public class HomeFragment extends Fragment implements View.OnClickListener {

    private HomeViewModel homeViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        initialize(root);

        final TextView textView = root.findViewById(R.id.section_home);
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        return root;
    }

    private void initialize(View root) {
        Button addMeetingBtn; // 모임 만들기 버튼

        addMeetingBtn = (Button)root.findViewById(R.id.addMeetingBtn);

        // Click Listener 추가
        addMeetingBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.addMeetingBtn:    // 모임 만들기 버튼
                // activity_init_study 레이아웃으로 변경해야 함
                Intent intent = new Intent(getActivity(), InitStudyActivity.class);

                startActivity(intent);
                break;
        }
    }
}