package com.app.deliver2me.activities.ui.logout;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class LogoutViewModel {
    private MutableLiveData<String> mText;

    public LogoutViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is logout fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
