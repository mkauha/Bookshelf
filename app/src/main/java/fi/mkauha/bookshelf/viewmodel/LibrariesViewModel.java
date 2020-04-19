package fi.mkauha.bookshelf.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import fi.mkauha.bookshelf.models.Consortium;

public class LibrariesViewModel extends ViewModel {

    private MutableLiveData<Consortium> mConsortium;
    private MutableLiveData<Double> mLatitude;
    private MutableLiveData<Double> mLongitude;



    public LibrariesViewModel() {
        mConsortium = new MutableLiveData<>();
        mConsortium.setValue(new Consortium(2090,"PIKI-kirjastot"));

        mLatitude = new MutableLiveData<>();
        mLatitude.setValue(61.4977606);
        mLongitude = new MutableLiveData<>();
        mLongitude.setValue(23.7507924);
    }

    public void setConsortium(Consortium consortium) {
        mConsortium.setValue(consortium);
    }

    public void setLatitude(Double latitude) {
        mLatitude.setValue(latitude);
    }

    public void setLongitude(Double longitude) {
        mLongitude.setValue(longitude);
    }

    public LiveData<Consortium> getConsortium() {
        return mConsortium;
    }

    public LiveData<Double> getLatitude() {
        return mLatitude;
    }

    public LiveData<Double> getLongitude() {
        return mLongitude;
    }
}