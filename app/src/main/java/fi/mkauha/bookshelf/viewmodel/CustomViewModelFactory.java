package fi.mkauha.bookshelf.viewmodel;

import android.app.Application;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

/**
 * Factory class for creating custom ViewModels with application context.
 *
 * @author  Miko Kauhanen
 * @version 1.0
 */
public class CustomViewModelFactory implements ViewModelProvider.Factory {
    private Application mApplication;

    /**
     * Instantiates a new Custom view model factory.
     *
     * @param application the application context
     */
    public CustomViewModelFactory(Application application) {
        mApplication = application;
    }

    /**
     * Creates a custom ViewModel.
     *
     * @param modelClass the application context
     */
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new BooksViewModel(mApplication);
    }
}
