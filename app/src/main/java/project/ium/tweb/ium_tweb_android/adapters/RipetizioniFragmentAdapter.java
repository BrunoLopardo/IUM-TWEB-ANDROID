package project.ium.tweb.ium_tweb_android.adapters;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import project.ium.tweb.ium_tweb_android.PrenotazioneActivity;
import project.ium.tweb.ium_tweb_android.R;
import project.ium.tweb.ium_tweb_android.dao.daos.Dao;
import project.ium.tweb.ium_tweb_android.dao.dto.Prenotazione;
import project.ium.tweb.ium_tweb_android.dao.dto.Ripetizione;
import project.ium.tweb.ium_tweb_android.viewmodels.MainViewModel;

public class RipetizioniFragmentAdapter extends Fragment implements SwipeRefreshLayout.OnRefreshListener, MainActivityAdapter.ItemClickListener, SearchView.OnQueryTextListener {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private MainViewModel vm;
    private Dao dao;
    private SwipeRefreshLayout swipeLayout;
    private int day_index = 0;
    private RecyclerView rv;
    private RecyclerView.Adapter oldAdapter = null;
    private SearchView searchView;
    private TextView emptyView;

    public RipetizioniFragmentAdapter() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static RipetizioniFragmentAdapter newInstance(int sectionNumber) {
        RipetizioniFragmentAdapter fragment = new RipetizioniFragmentAdapter();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        day_index = getArguments().getInt(ARG_SECTION_NUMBER); //starts from 1
        vm = ViewModelProviders.of(getActivity()).get(MainViewModel.class);
        dao = Dao.getInstance(getActivity().getApplicationContext());

        View rootView = inflater.inflate(R.layout.fragment_blank, container, false);

        rv = rootView.findViewById(R.id.recycleView);
        emptyView = rootView.findViewById(R.id.empty_view);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);

        swipeLayout = rootView.findViewById(R.id.swiperefresh);
        swipeLayout.setOnRefreshListener(this);

        if (oldAdapter != null) {
            rv.setAdapter(oldAdapter);
        }

        vm.getRipetizioni().observe(this, ripetizioni -> {
            ripetizioni = ripetizioni.stream()
                    .filter(r -> r.getGiorno() == Prenotazione.DayName.values()[day_index-1])
                    .filter(distinctByKey(Ripetizione::getCorso))
                    .filter(searchView != null ? r -> r.getCorso().getTitolo().toLowerCase().contains(searchView.getQuery()) : r -> true)
                    .sorted(Comparator.comparing(r2 -> r2.getCorso().getTitolo()))
                    .collect(Collectors.toList());

            if (ripetizioni.isEmpty()) {
                rv.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);
            } else {
                rv.setVisibility(View.VISIBLE);
                emptyView.setVisibility(View.GONE);
            }

            if (oldAdapter == null) {
                RecyclerView.Adapter adapter = new MainActivityAdapter(ripetizioni);
                ((MainActivityAdapter) adapter).setClickListener(this);
                rv.setAdapter(adapter);
                oldAdapter = rv.getAdapter();
            } else {
                ((MainActivityAdapter) rv.getAdapter()).swapItems(ripetizioni);
            }
        });

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MenuItem searchItem = menu.findItem(R.id.action_search);
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
    public boolean onQueryTextSubmit(String query) {
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (newText == null || newText.trim().isEmpty()) {
            resetSearch();
        }

        ArrayList<Ripetizione> filteredList = new ArrayList<>(vm.getRipetizioni().getValue().stream()
                .filter(r -> r.getGiorno() == Prenotazione.DayName.values()[day_index - 1])
                .filter(distinctByKey(Ripetizione::getCorso))
                .filter(r -> r.getCorso().getTitolo().toLowerCase().contains(newText.toLowerCase()))
                .sorted(Comparator.comparing(r2 -> r2.getCorso().getTitolo()))
                .collect(Collectors.toList()));
            ((MainActivityAdapter) rv.getAdapter()).swapItems(filteredList);

        if (rv.getAdapter().getItemCount() == 0) {
            rv.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            rv.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
        return false;
    }

    public void resetSearch() {
        ((MainActivityAdapter) rv.getAdapter()).swapItems(vm.getRipetizioni().getValue().stream()
                .filter(r -> r.getGiorno() == Prenotazione.DayName.values()[day_index-1])
                .filter(distinctByKey(Ripetizione::getCorso))
                .sorted(Comparator.comparing(r2 -> r2.getCorso().getTitolo()))
                .collect(Collectors.toList()));
    }

    @Override
    public void onRefresh() {
        dao.getRipetizioneDAO().findAll(result -> {
            if (result != null) {
                vm.setRipetizioni(result);
                Toast.makeText(getActivity(), getString(R.string.success_msg_update), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getActivity(), getString(R.string.error_msg_internet_offline), Toast.LENGTH_LONG).show();
            }
            swipeLayout.setRefreshing(false);
        });
    }

    @Override
    public void onItemClick(View view, int position) {
        int corso = ((MainActivityAdapter) rv.getAdapter()).getItem(position).getCorso().getID();
        Context context = view.getContext();
        Intent intent = new Intent(context, PrenotazioneActivity.class);
        intent.putExtra("giorno", day_index);
        intent.putExtra("corso", corso);
        ArrayList<Ripetizione> list = new ArrayList<>(vm.getRipetizioni().getValue().stream()
                .filter(r -> r.getGiorno() == Prenotazione.DayName.values()[day_index - 1])
                .filter(r -> r.getCorso().getID() == corso)
                .collect(Collectors.toList()));

        intent.putExtra("list", list);
        context.startActivity(intent);
    }

    @Override
    public void onResume() {
        if (searchView != null) {
            searchView.clearFocus();
            onQueryTextChange(searchView.getQuery().toString());
        }
        super.onResume();
    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
        Map<Object, Boolean> map = new ConcurrentHashMap<>();
        return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }
}
