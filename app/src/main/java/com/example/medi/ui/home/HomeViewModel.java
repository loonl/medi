package com.example.medi.ui.home;

import android.widget.TextView;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.medi.R;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private TextView textModel;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        //mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}