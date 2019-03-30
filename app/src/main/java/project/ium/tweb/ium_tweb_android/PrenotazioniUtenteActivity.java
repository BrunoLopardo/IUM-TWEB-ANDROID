package project.ium.tweb.ium_tweb_android;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import project.ium.tweb.ium_tweb_android.adapters.PrenotazioniUtenteFragmentAdapter;
import project.ium.tweb.ium_tweb_android.dao.daos.Dao;
import project.ium.tweb.ium_tweb_android.dao.dto.LoginData;
import project.ium.tweb.ium_tweb_android.models.AppModel;
import project.ium.tweb.ium_tweb_android.viewmodels.PrenotazioniUtenteViewModel;

public class PrenotazioniUtenteActivity extends AppCompatActivity {

    private PrenotazioniUtenteViewModel vm;
    private AppModel model;
    private Dao dao;
    private RecyclerView recyclerView;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prenotazioni_utente);
      //  recyclerView = findViewById(R.id.recyclerView);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TabLayout tabLayout = findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        vm = ViewModelProviders.of(this).get(PrenotazioniUtenteViewModel.class);
        dao = Dao.getInstance(getApplicationContext());
        model = AppModel.getInstance();

        mViewPager.setAdapter(mSectionsPagerAdapter);

    }

    @Override
    protected void onResume() {
        super.onResume();

        // Aggiorno la lista quando per esempio si ritorna in questa app
        dao.getPrenotazioneDAO().findAll(result -> {
            if (result != null) {
                vm.setPrenotazioniUtente(result);
            } else {
                dao.getLoginDAO().loggedIn(res -> {
                    if (res == null) {
                        // Problemi di connessione
                        Toast.makeText(this, getString(R.string.error_msg_internet_offline), Toast.LENGTH_LONG).show();
                    } else if (!res.getLoggedIn()) {
                        // Sessione scaduta
                        Toast.makeText(this, getString(R.string.error_msg_user_session_expired), Toast.LENGTH_LONG).show();
                        model.setLoginData(res);
                        finish();
                    } else {
                        // Nessun errore, eccetto per quello di getPrenotazioneDAO
                        Toast.makeText(this, getString(R.string.error_msg_generic), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_prenotazioni_utente, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if (id == R.id.action_logout)
            dao.getLoginDAO().logout(result -> {
                if (result != null && result) {
                    model.setLoginData(new LoginData(false, null, false));
                    Toast.makeText(this, getString(R.string.success_msg_logout), Toast.LENGTH_LONG).show();
                    finish();
                } else if (result == null) {
                    Toast.makeText(this, getString(R.string.error_msg_internet_offline), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, getString(R.string.error_msg_generic), Toast.LENGTH_LONG).show();
                }
            });

        return super.onOptionsItemSelected(item);
    }

    /***
     * A placeholder fragment containing a simple view.
     */
     public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            //  RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
            return inflater.inflate(R.layout.fragment_prenotazioni_utente, container, false);
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PrenotazioniUtenteFragmentAdapter.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }
    }
}
