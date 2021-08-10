package com.app.deliver2me.activities.ui.logout;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CourierLogoutFragmentViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public CourierLogoutFragmentViewModel()
    {
        mText = new MutableLiveData<>();
        mText.setValue("Courier logout");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
