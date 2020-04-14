package com.example.withstudy.main.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

//public class HomeViewModel extends ViewModel {
//
//    private MutableLiveData<Integer> mIndex = new MutableLiveData<>();
//    private LiveData<String> mText = Transformations.map(mIndex, new Function<Integer, String>() {
//        @Override
//        public String apply(Integer input) {
//            return "Hello world from section: " + input;
//        }
//    });
//
//    public void setIndex(int index) {
//        mIndex.setValue(index);
//    }
//
//    public LiveData<String> getText() {
//        return mText;
//    }
//}

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}