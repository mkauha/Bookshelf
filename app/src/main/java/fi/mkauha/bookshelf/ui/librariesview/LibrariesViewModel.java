package fi.mkauha.bookshelf.ui.librariesview;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LibrariesViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public LibrariesViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Nothing to see here");
    }

    public LiveData<String> getText() {
        return mText;
    }
}