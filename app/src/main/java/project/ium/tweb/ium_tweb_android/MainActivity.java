package project.ium.tweb.ium_tweb_android;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import project.ium.tweb.ium_tweb_android.adapters.RipetizioniFragmentAdapter;
import project.ium.tweb.ium_tweb_android.dao.daos.Dao;
import project.ium.tweb.ium_tweb_android.dao.dto.LoginData;
import project.ium.tweb.ium_tweb_android.models.AppModel;
import project.ium.tweb.ium_tweb_android.viewmodels.MainViewModel;

public class MainActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private MainViewModel vm;
    private AppModel model;
    private Dao dao;
    private Menu menu;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mViewPager = findViewById(R.id.container);
        TabLayout tabLayout = findViewById(R.id.tabs);
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        vm = ViewModelProviders.of(this).get(MainViewModel.class);
        dao = Dao.getInstance(getApplicationContext());
        model = AppModel.getInstance();

        mViewPager.setAdapter(mSectionsPagerAdapter);

        model.getLoginData().observeForever(data -> {
            if (menu == null) return;
            if (data.getLoggedIn()) {
                MenuItem item = menu.findItem(R.id.action_login);
                item.setVisible(false);
                item = menu.findItem(R.id.action_logout);
                item.setVisible(true);
                item = menu.findItem(R.id.action_prenotazioni_utente);
                item.setVisible(true);
            } else {
                MenuItem item = menu.findItem(R.id.action_login);
                item.setVisible(true);
                item = menu.findItem(R.id.action_logout);
                item.setVisible(false);
                item = menu.findItem(R.id.action_prenotazioni_utente);
                item.setVisible(false);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Aggiorno la lista quando per esempio si ritorna a questa activity dopo avere disdetto una prenotazione
        dao.getRipetizioneDAO().findAll(result -> {
            if (result != null) {
                vm.setRipetizioni(result);
            } else {
                Toast.makeText(this, getString(R.string.error_msg_internet_offline), Toast.LENGTH_LONG).show();
            }
        });

        dao.getLoginDAO().loggedIn(result -> {
            if (result != null) {
                model.setLoginData(result);
            } else {
                model.setLoginData(new LoginData(false, null, false));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        this.menu = menu;

        if (model.getLoginData().getValue().getLoggedIn()) {
            MenuItem item = menu.findItem(R.id.action_login);
            item.setVisible(false);
            item = menu.findItem(R.id.action_logout);
            item.setVisible(true);
            item = menu.findItem(R.id.action_prenotazioni_utente);
            item.setVisible(true);
        } else {
            MenuItem item = menu.findItem(R.id.action_login);
            item.setVisible(true);
            item = menu.findItem(R.id.action_logout);
            item.setVisible(false);
            item = menu.findItem(R.id.action_prenotazioni_utente);
            item.setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_login)
            startActivity(new Intent(this, LoginActivity.class));
        else if (id == R.id.action_logout)
            dao.getLoginDAO().logout(result -> {
                if (result != null && result) {
                    model.setLoginData(new LoginData(false, null, false));
                    Toast.makeText(this, getString(R.string.success_msg_logout), Toast.LENGTH_LONG).show();
                } else if (result == null) {
                    Toast.makeText(this, getString(R.string.error_msg_internet_offline), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, getString(R.string.error_msg_generic), Toast.LENGTH_LONG).show();
                }
            });
        else if (id == R.id.action_prenotazioni_utente)
            dao.getLoginDAO().loggedIn(result -> {
                if (result != null && result.getLoggedIn()) {
                    model.setLoginData(result);
                    startActivity(new Intent(this, PrenotazioniUtenteActivity.class));
                } else if (result == null) {
                    // Problemi di connessione
                    Toast.makeText(this, getString(R.string.error_msg_internet_offline), Toast.LENGTH_LONG).show();
                } else {
                    // Sessione scaduta / utente non loggato
                    model.setLoginData(result);
                    Toast.makeText(this, getString(R.string.error_msg_user_session_expired), Toast.LENGTH_LONG).show();
                }
            });


        return super.onOptionsItemSelected(item);
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
            return RipetizioniFragmentAdapter.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            return 5;
        }
    }
}
