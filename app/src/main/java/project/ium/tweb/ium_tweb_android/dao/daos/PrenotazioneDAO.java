package project.ium.tweb.ium_tweb_android.dao.daos;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import project.ium.tweb.ium_tweb_android.dao.dto.Prenotazione;
import project.ium.tweb.ium_tweb_android.dao.utils.HttpRequestManager;
import project.ium.tweb.ium_tweb_android.dao.utils.OnDaoCallCompleted;

import static android.icu.lang.UCharacter.toUpperCase;

public class PrenotazioneDAO {

    private HttpRequestManager http;
    private Gson gson;

    public PrenotazioneDAO(HttpRequestManager http) {
        this.http = http;
        this.gson = new Gson();
    }

    /**
     * Ottiene informazioni sulle prenotazioni dell'utente loggato
     *
     * @param callback callback usata per restituire il risultato di tipo List<Prenotazione>
     *                 che è null se è avvenuto un errore
     */
    public void findAll(OnDaoCallCompleted<List<Prenotazione>> callback) {
        Map<String, String> params = new HashMap<>();
        params.put("action", "get_prenotazioni_utente");

        http.sendRequest(params, result -> {
            try {
                List<Prenotazione> prenotazioni = null;

                if(result.getResult().getStatusCode() == 200) {
                    Type listType = new TypeToken<ArrayList<Prenotazione>>(){}.getType();

                    // Se contiene statuscode è avvenuto un errore
                    if(!result.getResult().getData().contains("statusCode"))
                        prenotazioni = gson.fromJson(result.getResult().getData(), listType);
                }
                if (callback != null) callback.onDaoCallCompleted(prenotazioni);
            } catch (IOException e) {
                Log.e("PrenotazioneDAOError", e.getMessage(), e);

                // Richiamo la callback passando null per indicare la presenza di un errore
                if (callback != null) callback.onDaoCallCompleted(null);
            }
        });
    }

    /**
     * Crea una nuova prenotazione per l'utente loggato
     *
     * @param prenotazione oggetto prenotazione, i campi id, created_at possono essere lasciati null
     * @param callback callback usata per restituire il risultato di tipo Integer
     *                 che è null se è avvenuto un errore di tipo IOException, altrimenti contiene
     *                 lo StatusCode inviato dal server.
     */
	public void create(Prenotazione prenotazione, OnDaoCallCompleted<Integer> callback) {
        Map<String, String> params = new HashMap<>();
        params.put("action", "new_prenotazione");
        params.put("idCorso",  Integer.toString(prenotazione.getCorso().getID()));
        params.put("idDocente", Integer.toString(prenotazione.getDocente().getID()));
        params.put("ora", Integer.toString(prenotazione.getOraInizio()));
        params.put("day", toUpperCase(prenotazione.getGiorno().toString()));

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

    /**
     * Disdice una prenotazione dell'utente loggato
     *
     * @param id id prenotazione da disdire
     * @param callback callback usata per restituire il risultato di tipo Integer
     *                 che è null se è avvenuto un errore di tipo IOException, altrimenti contiene
     *                 lo StatusCode inviato dal server.
     */
    public void deleteByID(int id, OnDaoCallCompleted<Integer> callback) {
        Map<String, String> params = new HashMap<>();
        params.put("action", "cancel_prenotazioni_utente");
        params.put("idPrenotazione",  Integer.toString(id));

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
