package fi.mkauha.bookshelf.viewmodel;

import android.app.Application;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class CustomViewModelFactory implements ViewModelProvider.Factory {
    private Application mApplication;

    public CustomViewModelFactory(Application application) {
        mApplication = application;
    }


    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new BooksViewModel(mApplication);
    }
}
