package com.youngdred.brujowines.ui.vinos;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class VinosViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public VinosViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Este es el fragmento de vinos");
    }

    public LiveData<String> getText() {
        return mText;
    }
}