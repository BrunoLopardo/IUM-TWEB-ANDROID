package project.ium.tweb.ium_tweb_android.dao.daos;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import project.ium.tweb.ium_tweb_android.dao.dto.Ripetizione;
import project.ium.tweb.ium_tweb_android.dao.utils.HttpRequestManager;
import project.ium.tweb.ium_tweb_android.dao.utils.OnDaoCallCompleted;

public class RipetizioneDAO {

    private HttpRequestManager http;
    private Gson gson;

    public RipetizioneDAO(HttpRequestManager http) {
        this.http = http;
        this.gson = new Gson();
    }

    /**
     * Ottiene le informazioni su tutte le ripetizioni disponibili
     *
     * @param callback callback usata per restituire il risultato di tipo List<Ripetizione>
     *                 che è null se è avvenuto un errore
     */
    public void findAll(OnDaoCallCompleted<List<Ripetizione>> callback) {

        Map<String, String> params = new HashMap<>();
        params.put("action", "get_ripetizioni_disponibili");

        http.sendRequest(params, result -> {
            try {
                List<Ripetizione> ripetizioni = null;

                if(result.getResult().getStatusCode() == 200) {
                    Type listType = new TypeToken<ArrayList<Ripetizione>>(){}.getType();
                    ripetizioni = gson.fromJson(result.getResult().getData(), listType);
                }

                callback.onDaoCallCompleted(ripetizioni);
            } catch (IOException e) {
                Log.e("RipetizioneDAOError", e.getMessage(), e);

                // Richiamo la callback passando null per indicare la presenza di un errore
                if (callback != null) callback.onDaoCallCompleted(null);
            }
        });

    }
}
