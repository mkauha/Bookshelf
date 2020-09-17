package fi.mkauha.bookshelf.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ImageSelectViewModel extends ViewModel {

    private final MutableLiveData<String> mSelectedImageFile = new MutableLiveData<>();

    public ImageSelectViewModel() {
        super();
    }

    public void selectImageFile(String imageFile) {
        mSelectedImageFile.setValue(imageFile);
    }

    public MutableLiveData<String> getSelectedImageFile() {
        return mSelectedImageFile;
    }


}