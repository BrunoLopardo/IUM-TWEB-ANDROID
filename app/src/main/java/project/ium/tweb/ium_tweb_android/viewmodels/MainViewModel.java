package project.ium.tweb.ium_tweb_android.viewmodels;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import project.ium.tweb.ium_tweb_android.dao.dto.Ripetizione;

public class MainViewModel extends ViewModel{

    private MutableLiveData<List<Ripetizione>> ripetizioni;

    public MainViewModel() {
        ripetizioni = new MutableLiveData<>();
        ripetizioni.setValue(new ArrayList<>());
    }

    public MutableLiveData<List<Ripetizione>> getRipetizioni() {
        return ripetizioni;
    }

    public void setRipetizioni(List<Ripetizione> ripetizioni) {
        this.ripetizioni.setValue(ripetizioni);
    }
}