package project.ium.tweb.ium_tweb_android.viewmodels;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import project.ium.tweb.ium_tweb_android.dao.dto.Prenotazione;

public class PrenotazioniUtenteViewModel extends ViewModel {

    private MutableLiveData<List<Prenotazione>> prenotazioniUtente;

    public PrenotazioniUtenteViewModel() {
        prenotazioniUtente = new MutableLiveData<>();
        prenotazioniUtente.setValue(new ArrayList<>());
    }

    public MutableLiveData<List<Prenotazione>> getPrenotazioniUtente() {
        return prenotazioniUtente;
    }

    public void setPrenotazioniUtente(List<Prenotazione> prenotazioniUtente) {
        this.prenotazioniUtente.setValue(prenotazioniUtente);
    }
}
