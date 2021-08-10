package com.app.deliver2me.activities.ui.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CourierProfileFragmentViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public CourierProfileFragmentViewModel()
    {
        mText = new MutableLiveData<>();
        mText.setValue("Courier profile");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
