package project.ium.tweb.ium_tweb_android.dao.utils;

import java.io.IOException;

public class HttpTaskResult<T> {
    private T result;
    private IOException ex;

    public HttpTaskResult(T result) {
        super();
        this.result = result;
    }

    public HttpTaskResult(IOException ex) {
        super();
        this.ex = ex;
    }

    public T getResult() throws IOException {
        if (ex != null) throw ex;
        return result;
    }
}
