package fi.mkauha.bookshelf.ui.readinglist;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ReadingListViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ReadingListViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is dashboard fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}