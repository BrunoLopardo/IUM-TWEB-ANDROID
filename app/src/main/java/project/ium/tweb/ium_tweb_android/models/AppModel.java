package project.ium.tweb.ium_tweb_android.models;

import android.arch.lifecycle.MutableLiveData;

import project.ium.tweb.ium_tweb_android.dao.dto.LoginData;

public class AppModel {
    private MutableLiveData<LoginData> loginData;

    private static AppModel instance;

    private AppModel() {}

    public MutableLiveData<LoginData> getLoginData() {
        if (loginData == null) {
            loginData = new MutableLiveData<>();
            loginData.setValue(new LoginData(false, null, false));
        }
        return loginData;
    }

    public void setLoginData(LoginData data) {
        loginData = getLoginData();
        loginData.postValue(data);
    }

    public static synchronized AppModel getInstance() {
        if (instance == null) {
            instance = new AppModel();
        }
        return instance;
    }
}
