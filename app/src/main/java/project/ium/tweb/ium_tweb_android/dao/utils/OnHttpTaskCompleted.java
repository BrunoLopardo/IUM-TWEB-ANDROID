package project.ium.tweb.ium_tweb_android.dao.utils;

public interface OnHttpTaskCompleted<T> {
    void onHttpTaskCompleted(HttpTaskResult<T> result);
}
