package project.ium.tweb.ium_tweb_android.dao.utils;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Questa classe si occupa di effettuare una singola richiesta http in maniera asincrona
 */
public class HttpTask extends AsyncTask<OnHttpTaskCompleted<HttpResponse>, Void, HttpTaskResult<HttpResponse>> {

    private URL url;
    private Map<String, String> params;
    private OnHttpTaskCompleted<HttpResponse>[] callbacks;

    public HttpTask(String url) throws MalformedURLException {
        this.url = new URL(url);
        this.params = new HashMap<>();
    }

    public HttpTask(String url, Map<String, String> params) throws MalformedURLException {
        this.url = new URL(url);
        this.params = new HashMap<>(params); // Clone
    }

    public void addParam(String key, String value) {
        params.put(key, value);
    }

    @SafeVarargs
    @Override
    protected final HttpTaskResult<HttpResponse> doInBackground(OnHttpTaskCompleted<HttpResponse>... callbacks) {
        this.callbacks = callbacks;

        StringBuilder builder = new StringBuilder();
        int responseCode;
        HttpURLConnection conn = null;

        try {

            StringBuilder postData = new StringBuilder();
            for (Map.Entry<String, String> param : params.entrySet()) {
                if (postData.length() != 0) postData.append('&');
                postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                postData.append('=');
                postData.append(URLEncoder.encode(param.getValue(), "UTF-8"));
            }
            byte[] postDataBytes = postData.toString().getBytes("UTF-8");

            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(500);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
            conn.setDoOutput(true);
            conn.getOutputStream().write(postDataBytes);

            InputStream in;
            if ((responseCode = conn.getResponseCode()) < HttpURLConnection.HTTP_BAD_REQUEST) {
                in = conn.getInputStream();
            } else {
                in = conn.getErrorStream();
                if (in == null) {
                    return new HttpTaskResult<>(new HttpResponse("", responseCode));
                }
            }

            in = new BufferedInputStream(in);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }

        } catch (IOException e) {
            Log.e("HttpTaskError", e.getMessage(), e);
            return new HttpTaskResult<>(e);
        } finally {
            conn.disconnect();
        }

        return new HttpTaskResult<>(new HttpResponse(builder.toString(), responseCode));
    }

    @Override
    protected void onPostExecute(HttpTaskResult<HttpResponse> result) {
        for (OnHttpTaskCompleted<HttpResponse> callback: callbacks) {
            callback.onHttpTaskCompleted(result);
        }
    }
}
