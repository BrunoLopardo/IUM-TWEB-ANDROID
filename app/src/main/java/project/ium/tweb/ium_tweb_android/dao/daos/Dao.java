package project.ium.tweb.ium_tweb_android.dao.daos;

import android.content.Context;

import project.ium.tweb.ium_tweb_android.R;
import project.ium.tweb.ium_tweb_android.dao.utils.HttpRequestManager;

public class Dao {

    private HttpRequestManager http;
    private LoginDAO loginDAO;
    private PrenotazioneDAO prenotazioneDAO;
    private RipetizioneDAO ripetizioneDAO;

    private static Dao instance;

    private Dao(Context appContext) {
        String ip = appContext.getString(R.string.ip);
        String port = appContext.getString(R.string.port);
        String context = appContext.getString(R.string.context);
        String servlet = appContext.getString(R.string.servlet);

        http = new HttpRequestManager(ip, port, context, servlet);

        loginDAO = new LoginDAO(http);
        prenotazioneDAO = new PrenotazioneDAO(http);
        ripetizioneDAO = new RipetizioneDAO(http);
    }

    public LoginDAO getLoginDAO() {
        return loginDAO;
    }

    public PrenotazioneDAO getPrenotazioneDAO() {
        return prenotazioneDAO;
    }

    public RipetizioneDAO getRipetizioneDAO() {
        return ripetizioneDAO;
    }

    public static synchronized Dao getInstance(Context appContext) {
        if (instance == null) {
            instance = new Dao(appContext);
        }
        return instance;
    }
}
