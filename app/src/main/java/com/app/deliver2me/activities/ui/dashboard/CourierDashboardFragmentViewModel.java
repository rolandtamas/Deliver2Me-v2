package com.app.deliver2me.activities.ui.dashboard;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CourierDashboardFragmentViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public CourierDashboardFragmentViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Courier dashboard");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
