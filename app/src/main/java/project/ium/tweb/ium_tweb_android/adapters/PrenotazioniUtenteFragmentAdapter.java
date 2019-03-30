package project.ium.tweb.ium_tweb_android.adapters;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import project.ium.tweb.ium_tweb_android.R;
import project.ium.tweb.ium_tweb_android.callbacks.ItemTouchHelperCallback;
import project.ium.tweb.ium_tweb_android.callbacks.SwipeCallback;
import project.ium.tweb.ium_tweb_android.dao.daos.Dao;
import project.ium.tweb.ium_tweb_android.dao.dto.LoginData;
import project.ium.tweb.ium_tweb_android.dao.dto.Prenotazione;
import project.ium.tweb.ium_tweb_android.models.AppModel;
import project.ium.tweb.ium_tweb_android.viewmodels.PrenotazioniUtenteViewModel;

public class PrenotazioniUtenteFragmentAdapter extends Fragment implements SwipeRefreshLayout.OnRefreshListener, SwipeCallback, SearchView.OnQueryTextListener {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private PrenotazioniUtenteViewModel vm;
    private Dao dao;
    private AppModel model;
    private SwipeRefreshLayout swipeLayout;
    private int state_index = 0;
    private RecyclerView rv;
    private RecyclerView.Adapter oldAdapter = null; // salva la lista quando si cambia la tab
    private SearchView searchView;
    private TextView emptyView;

    public PrenotazioniUtenteFragmentAdapter() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static PrenotazioniUtenteFragmentAdapter newInstance(int sectionNumber) {
        PrenotazioniUtenteFragmentAdapter fragment = new PrenotazioniUtenteFragmentAdapter();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        state_index = getArguments().getInt(ARG_SECTION_NUMBER); //starts from 1
        vm = ViewModelProviders.of(getActivity()).get(PrenotazioniUtenteViewModel.class);
        dao = Dao.getInstance(getActivity().getApplicationContext());
        model = AppModel.getInstance();

        View rootView = inflater.inflate(R.layout.fragment_blank, container, false);

        rv = rootView.findViewById(R.id.recycleView);
        emptyView = rootView.findViewById(R.id.empty_view);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);

        if(state_index == 1) {// abilita lo swipe per cancellare una prenotazione attiva
            ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelperCallback(0, ItemTouchHelper.LEFT, this);
            new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(rv);
        }

        swipeLayout = rootView.findViewById(R.id.swiperefresh);
        swipeLayout.setOnRefreshListener(this);

        if(oldAdapter != null) {
            rv.setAdapter(oldAdapter);
        }

        vm.getPrenotazioniUtente().observe(this, prenotazioniUtente -> {
            prenotazioniUtente = prenotazioniUtente.stream()
                    .filter(r -> r.getStato() == Prenotazione.Stato.values()[state_index-1])
                    .filter(searchView != null ? r -> filterItem(r, searchView.getQuery().toString()) : r -> true)
                    .sorted(Comparator.comparing(Prenotazione::getCreatedAt).reversed())
                    .collect(Collectors.toList());

            if (prenotazioniUtente.isEmpty()) {
                rv.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);
            } else {
                rv.setVisibility(View.VISIBLE);
                emptyView.setVisibility(View.GONE);
            }

            if (oldAdapter == null) {
                rv.setAdapter(new PrenotazioniUtenteAdapter(prenotazioniUtente));
                oldAdapter = rv.getAdapter();
            }else{
                ((PrenotazioniUtenteAdapter) rv.getAdapter()).swapItems(prenotazioniUtente);// ripristina la lista
            }

        });

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MenuItem searchItem = menu.findItem(R.id.action_search_prenotazione);
        searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                getActivity().invalidateOptionsMenu();
                return true;
            }
        });

        searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint("Cerca...");
        searchView.setOnQueryTextListener(this);
        searchView.setIconified(true);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onRefresh() {
        dao.getPrenotazioneDAO().findAll(result -> {
            if (result != null) {
                vm.setPrenotazioniUtente(result);
                Toast.makeText(getActivity(), getString(R.string.success_msg_update), Toast.LENGTH_LONG).show();
                swipeLayout.setRefreshing(false);
            } else {
                dao.getLoginDAO().loggedIn(res -> {
                    if (res == null) {
                        // Problemi di connessione
                        Toast.makeText(getActivity(), getString(R.string.error_msg_internet_offline), Toast.LENGTH_LONG).show();
                    } else if (!res.getLoggedIn()) {
                        // Sessione scaduta
                        Toast.makeText(getActivity(), getString(R.string.error_msg_user_session_expired), Toast.LENGTH_LONG).show();
                        model.setLoginData(res);
                        getActivity().finish();
                    } else {
                        // Nessun errore, eccetto per quello di getPrenotazioneDAO
                        Toast.makeText(getActivity(), getString(R.string.error_msg_generic), Toast.LENGTH_LONG).show();
                    }
                    swipeLayout.setRefreshing(false);
                });
            }
        });
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {// elimina una prenotazione dal server
        PrenotazioniUtenteAdapter mAdapter = (PrenotazioniUtenteAdapter) oldAdapter;
        int idPrenotazione = mAdapter.getItem(position).getID();

        if(viewHolder instanceof PrenotazioniUtenteAdapter.ViewHolder){
            dao.getPrenotazioneDAO().deleteByID(idPrenotazione, result -> {
                if (result != null) {
                    if (result == 0) {
                        mAdapter.cancelPrenotazioneUtente(position);
                        vm.getPrenotazioniUtente().getValue().stream()
                                .filter(prenotazione -> idPrenotazione == prenotazione.getID())
                                .findAny().ifPresent(prenotazioneCancellata -> prenotazioneCancellata.setStato(Prenotazione.Stato.DISDETTA));

                        if (mAdapter.getItemCount() == 0) {
                            rv.setVisibility(View.GONE);
                            emptyView.setVisibility(View.VISIBLE);
                        } else {
                            rv.setVisibility(View.VISIBLE);
                            emptyView.setVisibility(View.GONE);
                        }

                    } else if (result == 3) {
                        // Sessione utente scaduta
                        mAdapter.notifyItemChanged(position); // Annullo lo swipe
                        Toast.makeText(getActivity(), getString(R.string.error_msg_user_session_expired), Toast.LENGTH_LONG).show();
                        model.setLoginData(new LoginData(false, null, false));
                        getActivity().finish();
                    } else if (result == 4) {
                        // Tentativo di disdire una prenotazione gia disdetta (per esempio dal sito o un altro smartphone)
                        mAdapter.notifyItemChanged(position);
                        Toast.makeText(getActivity(), getString(R.string.error_msg_prenotazione_not_active), Toast.LENGTH_LONG).show();
                    } else {
                        // Altri errori
                        mAdapter.notifyItemChanged(position);
                        Toast.makeText(getActivity(), getString(R.string.error_msg_generic), Toast.LENGTH_LONG).show();
                    }
                } else {
                    // Errore di connessione
                    mAdapter.notifyItemChanged(position); // Annullo lo swipe
                    Toast.makeText(getActivity(), getString(R.string.error_msg_internet_offline), Toast.LENGTH_LONG).show();
                }
            });

        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (newText == null || newText.trim().isEmpty()) {
            resetSearch();
        }

        List<Prenotazione> filteredList = vm.getPrenotazioniUtente().getValue().stream()
                .filter(r -> r.getStato() == Prenotazione.Stato.values()[state_index-1])
                .filter(r -> filterItem(r, newText))
                .sorted(Comparator.comparing(Prenotazione::getCreatedAt).reversed())
                .collect(Collectors.toList());
        ((PrenotazioniUtenteAdapter) rv.getAdapter()).swapItems(filteredList);

        if (rv.getAdapter().getItemCount() == 0) {
            rv.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            rv.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
        return false;
    }

    public static boolean filterItem(Prenotazione p, String query) {
        boolean keep = false;

        keep = keep || p.getDocente().getNome().toLowerCase().contains(query.toLowerCase());
        keep = keep || p.getDocente().getCognome().toLowerCase().contains(query.toLowerCase());
        keep = keep || (p.getDocente().getCognome() + " " + p.getDocente().getNome()).toLowerCase().contains(query.toLowerCase());
        keep = keep || (p.getDocente().getNome() + " " + p.getDocente().getCognome()).toLowerCase().contains(query.toLowerCase());
        keep = keep || p.getCorso().getTitolo().toLowerCase().contains(query.toLowerCase());

        return keep;
    }

    public void resetSearch() {
        ((PrenotazioniUtenteAdapter) rv.getAdapter()).swapItems(vm.getPrenotazioniUtente().getValue().stream()
                .filter(r -> r.getStato() == Prenotazione.Stato.values()[state_index-1])
                .sorted(Comparator.comparing(Prenotazione::getCreatedAt).reversed())
                .collect(Collectors.toList()));
    }

    @Override
    public void onResume() {
        if (searchView != null) {
            searchView.clearFocus();
            onQueryTextChange(searchView.getQuery().toString());
        }
        super.onResume();
    }
}
