package com.example.withstudy.main.menu;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


import com.example.withstudy.R;
import com.example.withstudy.SignInUpActivity;
import com.example.withstudy.main.data.ManagementData;
import com.google.firebase.auth.FirebaseAuth;


public class MenuFragment extends Fragment {
    private FirebaseAuth firebaseAuth;
    private View root;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_menu, container, false);
        initialize();

        return root;
    }

    private void initialize() {
        Button btnLogOut, btnSignOut;
        TextView userNameTV, joinStudyCountTV;

        btnLogOut = root.findViewById(R.id.btnLogOut);
        btnSignOut = root.findViewById(R.id.btnSignOut);

        userNameTV = root.findViewById(R.id.menu_userNameTV);
        joinStudyCountTV = root.findViewById(R.id.menu_joinStudyCountTV);

        userNameTV.setText(ManagementData.getInstance().getUserData().getUser_Name());
        joinStudyCountTV.setText("참여모임: " + ManagementData.getInstance().getJoinStudys().size() + "개");

        firebaseAuth = FirebaseAuth.getInstance();

        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOut();
                Intent intent = new Intent(
                        getContext(), SignInUpActivity.class);

                // 데이터 초기화 및 생성
                ManagementData.getInstance().delAllData();
                ManagementData.getInstance().findAllStudy();

                startActivity(intent);

            }
        });

        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        getContext(), SignInUpActivity.class);

                // 데이터 초기화 및 생성
                ManagementData.getInstance().delAllData();
                ManagementData.getInstance().findAllStudy();

                startActivity(intent);

                signOut();
            }
        });
    }
    private void logOut () {
        firebaseAuth.getInstance().signOut();
    }

    private void signOut () {
        firebaseAuth.getCurrentUser().delete();
    }

}