package project.ium.tweb.ium_tweb_android;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import project.ium.tweb.ium_tweb_android.adapters.RecyclerViewPrenotazioneAdapter;
import project.ium.tweb.ium_tweb_android.dao.daos.Dao;
import project.ium.tweb.ium_tweb_android.dao.dto.Prenotazione;
import project.ium.tweb.ium_tweb_android.dao.dto.Ripetizione;
import project.ium.tweb.ium_tweb_android.utils.Converter;

public class PrenotazioneActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerViewPrenotazioneAdapter mAdapter;
    private Dao dao;
    private Ripetizione ripetizione;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prenotazione);

        dao = Dao.getInstance(getApplicationContext());

        recyclerView = findViewById(R.id.recycler_view);

        List<RecyclerViewPrenotazioneAdapter.Categoria> categorie = new ArrayList<>();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        Intent intent = getIntent();
        List<Ripetizione> list = (List<Ripetizione>) intent
                .getSerializableExtra("list");
        final int giorno = intent.getIntExtra("giorno",0);
        final int corso = intent.getIntExtra("corso",0);

        categorie.add(new RecyclerViewPrenotazioneAdapter.Categoria(15, list.stream()
                .filter(r -> r.getOraInizio() == 15).collect(Collectors.toList())));
        categorie.add(new RecyclerViewPrenotazioneAdapter.Categoria(16, list.stream()
                .filter(r -> r.getOraInizio() == 16).collect(Collectors.toList())));
        categorie.add(new RecyclerViewPrenotazioneAdapter.Categoria(17, list.stream()
                .filter(r -> r.getOraInizio() == 17).collect(Collectors.toList())));
        categorie.add(new RecyclerViewPrenotazioneAdapter.Categoria(18, list.stream()
                .filter(r -> r.getOraInizio() == 18).collect(Collectors.toList())));

        categorie = categorie.stream().filter(c -> !c.getList().isEmpty()).collect(Collectors.toList());

        mAdapter = new RecyclerViewPrenotazioneAdapter(this, categorie);
        recyclerView.setAdapter(mAdapter);

        mAdapter.SetOnItemClickListener((view, position, model) -> {
            ripetizione = model;
            builder.setMessage("Corso: " + model.getCorso().getTitolo() +
                    "\nDocente: " + model.getDocente().getNome() + " " + model.getDocente().getCognome() +
                    "\nGiorno: " + Converter.convertDay(model.getGiorno().toString())+
                    "\nOra: " + Converter.convertHour(model.getOraInizio()))
                    .setPositiveButton("Conferma", dialogClickListener)
                    .setNegativeButton("Annulla", null).show();
            }
        );
    }

    public static void showSnackBar(View view, String text, int color) {
        Snackbar sb = Snackbar.make(view, text, Snackbar.LENGTH_LONG);
        sb.getView().setBackgroundColor(color);
        sb.show();
    }

    public void showLoginSnackBar() {
        Snackbar sb = Snackbar.make(findViewById(android.R.id.content), getString(R.string.error_msg_login_required), Snackbar.LENGTH_LONG);
        sb.getView().setBackgroundColor(getColor(R.color.colorRed));
        sb.setAction(getString(R.string.login_button_text), view -> {
            startActivity(new Intent(this, LoginActivity.class));
        });
        sb.setActionTextColor(Color.WHITE);
        sb.show();
    }

    DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
        Prenotazione prenotazione = new Prenotazione(null, ripetizione.getCorso(), ripetizione.getDocente(),
                null, null, ripetizione.getGiorno(), ripetizione.getOraInizio(), null);
        dao.getPrenotazioneDAO().create(prenotazione, result -> {
            if (result == null) {
                showSnackBar(findViewById(android.R.id.content), getString(R.string.error_msg_internet_offline), getColor(R.color.colorRed));
                return;
            }

            switch (result) {
                case 0: // Success
                    Toast.makeText(this, getString(R.string.success_msg_prenotazione_created), Toast.LENGTH_LONG).show();
                    finish();
                    break;
                case 3: // Non loggato
                    showLoginSnackBar();
                    break;
                case 5: // Ripetizione non presente o non disponibile. (può accadere quando un altro utente la prenota)
                    showSnackBar(findViewById(android.R.id.content), getString(R.string.error_msg_ripetizione_not_available), getColor(R.color.colorRed));
                    break;
                case 6: // Utente con una prenotazione nello stesso giorno e orario.
                    showSnackBar(findViewById(android.R.id.content), getString(R.string.error_msg_user_is_busy), getColor(R.color.colorRed));
                    break;
                case 7: // Docente con una prenotazione nello stesso giorno e orario. (può accadere quando un altro utente prenota una ripetizione con stesso docente)
                    showSnackBar(findViewById(android.R.id.content), getString(R.string.error_msg_ripetizione_not_available), getColor(R.color.colorRed));
                    break;
                default:
                    showSnackBar(findViewById(android.R.id.content), getString(R.string.error_msg_generic), getColor(R.color.colorRed));
                    break;
            }
        });
    };
}
