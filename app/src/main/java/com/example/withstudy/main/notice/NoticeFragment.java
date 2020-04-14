package com.example.withstudy.main.notice;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import com.example.withstudy.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class NoticeFragment extends Fragment {

    private NoticeViewModel noticeViewModel;

    public static NoticeFragment newInstance(int index) {
        NoticeFragment fragment = new NoticeFragment();
        Bundle bundle = new Bundle();
//        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        noticeViewModel = ViewModelProviders.of(this).get(NoticeViewModel.class);
////        int index = 1;
////        if (getArguments() != null) {
////            index = getArguments().getInt(ARG_SECTION_NUMBER);
////        }
////        noticeViewModel.setIndex(index);
//    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_notice, container, false);
//        final TextView textView = root.findViewById(R.id.section_notice);
//        noticeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        return root;
    }
}