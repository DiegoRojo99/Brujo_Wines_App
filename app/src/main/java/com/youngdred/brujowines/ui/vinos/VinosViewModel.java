package com.youngdred.brujowines.ui.vinos;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class VinosViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public VinosViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Nuestros Vinos");
    }

    public LiveData<String> getText() {
        return mText;
    }
}