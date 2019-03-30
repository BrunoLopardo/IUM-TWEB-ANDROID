package project.ium.tweb.ium_tweb_android.dao.utils;

public class HttpResponse {
    private String data;
    private int statusCode;

    public HttpResponse(String data, int statusCode) {
        this.data = data;
        this.statusCode = statusCode;
    }

    public String getData() {
        return data;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
