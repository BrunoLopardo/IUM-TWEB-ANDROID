package project.ium.tweb.ium_tweb_android.dao.utils;

import android.util.Log;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.MalformedURLException;
import java.util.Map;

/**
 * Questa classe gestisce la sessione http e fornisce metodi per inviare richieste al server.
 */
public class HttpRequestManager {
    // https://stackoverflow.com/questions/5140539/android-config-file
    // https://stackoverflow.com/questions/14860087/should-httpurlconnection-with-cookiemanager-automatically-handle-session-cookies

    private String url;

    public HttpRequestManager(String ip, String port, String context, String servlet) {
        // Abilito la gestione automatica dei cookie, per mantenere la sessione
        CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
        url = "http://" + ip + ":" + port + "/" + context + "/" + servlet;
    }

    public void sendRequest(Map<String, String> params, OnHttpTaskCompleted<HttpResponse> callback) {
        try {
            new HttpTask(url, params).execute(callback);
        } catch (MalformedURLException e) {
            Log.e("HttpRequestManagerError", e.getMessage(), e);
        }
    }
}
