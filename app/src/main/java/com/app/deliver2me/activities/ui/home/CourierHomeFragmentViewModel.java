package com.app.deliver2me.activities.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CourierHomeFragmentViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public CourierHomeFragmentViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Courier home");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
