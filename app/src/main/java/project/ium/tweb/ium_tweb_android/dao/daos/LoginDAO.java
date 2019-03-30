package project.ium.tweb.ium_tweb_android.dao.daos;

import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import project.ium.tweb.ium_tweb_android.dao.dto.LoginData;
import project.ium.tweb.ium_tweb_android.dao.utils.HttpRequestManager;
import project.ium.tweb.ium_tweb_android.dao.utils.OnDaoCallCompleted;

public class LoginDAO {

    private HttpRequestManager http;
    private Gson gson;

    public LoginDAO(HttpRequestManager http) {
        this.http = http;
        this.gson = new Gson();
    }

    /**
     * Fa il login e ottiene informazioni sull'utente e relative al successo dell'operazione
     *
     * @param username username
     * @param password password
     * @param callback callback usata per restituire il risultato di tipo LoginData
     *                 che è null se è avvenuto un errore
     */
    public void login(String username, String password, OnDaoCallCompleted<LoginData> callback) {
        Map<String, String> params = new HashMap<>();
        params.put("action", "login");
        params.put("username", username);
        params.put("password", password);

        http.sendRequest(params, result -> {
            try {
                LoginData utente = null;

                if(result.getResult().getStatusCode() == 200) {
                    utente = gson.fromJson(result.getResult().getData(), LoginData.class);
                }

                if(callback != null) callback.onDaoCallCompleted(utente);

            } catch (IOException e) {
                Log.e("LoginDAOError", e.getMessage(), e);

                // Richiamo la callback passando null per indicare la presenza di un errore
                if (callback != null) callback.onDaoCallCompleted(null);
            }
        });
    }

    /**
     * Fa il logout
     *
     * @param callback callback usata per restituire il risultato di tipo Boolean
     *                 che è null oppure false se è avvenuto un errore, in particolare è
     *                 null se è avvenuta una IOException ed è false se la risposta non era 200 OK
     */
	public void logout(OnDaoCallCompleted<Boolean> callback) {
        Map<String, String> params = new HashMap<>();
        params.put("action", "logout");

        http.sendRequest(params, result -> {
            try {
                if(callback != null) {
                    callback.onDaoCallCompleted(result.getResult().getStatusCode() == 200);
                }

            } catch (IOException e) {
                Log.e("LogoutDAOError", e.getMessage(), e);

                // Richiamo la callback passando null per indicare la presenza di un errore
                if (callback != null) callback.onDaoCallCompleted(null);
            }
        });

    }

    /**
     * Ottiene informazioni sullo stato del login
     * 
     * @param callback callback usata per restituire il risultato di tipo LoginData
     *                 che è null se è avvenuto un errore
     */
	public void loggedIn(OnDaoCallCompleted<LoginData> callback) {
        Map<String, String> params = new HashMap<>();
        params.put("action", "logged_in");

        http.sendRequest(params, result -> {
            try {
                LoginData loggedIn = null;

                if(result.getResult().getStatusCode() == 200) {
                    loggedIn = gson.fromJson(result.getResult().getData(), LoginData.class);
                }

                if(callback != null) callback.onDaoCallCompleted(loggedIn);

            } catch (IOException e) {
                Log.e("LoggedInDAOError", e.getMessage(), e);

                // Richiamo la callback passando null per indicare la presenza di un errore
                if (callback != null) callback.onDaoCallCompleted(null);
            }
        });
    }

    public void signup(String username, String password, OnDaoCallCompleted<Integer> callback) {
        Map<String, String> params = new HashMap<>();
        params.put("action", "signup");
        params.put("username", username);
        params.put("password", password);

        http.sendRequest(params, result -> {
            try {
                Integer resultValue = null;

                if(result.getResult().getStatusCode() == 200) {
                    JSONObject resultJson = new JSONObject(result.getResult().getData());
                    resultValue = resultJson.optInt("statusCode");
                }

                if (callback != null) callback.onDaoCallCompleted(resultValue);
            } catch (JSONException | IOException e) {
                Log.e("PrenotazioneDAOError", e.getMessage(), e);

                // Richiamo la callback passando null per indicare la presenza di un errore
                if (callback != null) callback.onDaoCallCompleted(null);
            }
        });
    }
}
