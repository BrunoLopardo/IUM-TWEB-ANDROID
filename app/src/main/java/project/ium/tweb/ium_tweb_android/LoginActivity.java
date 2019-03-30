package project.ium.tweb.ium_tweb_android;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import project.ium.tweb.ium_tweb_android.dao.daos.Dao;
import project.ium.tweb.ium_tweb_android.dao.dto.LoginData;
import project.ium.tweb.ium_tweb_android.databinding.ActivityLoginBinding;
import project.ium.tweb.ium_tweb_android.models.AppModel;
import project.ium.tweb.ium_tweb_android.viewmodels.LoginViewModel;

public class LoginActivity extends AppCompatActivity {
    private LoginViewModel vm;
    private ActivityLoginBinding binding;
    private AppModel model;
    private Dao dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vm = ViewModelProviders.of(this).get(LoginViewModel.class);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        binding.setVm(vm);
        binding.setLifecycleOwner(this);
        dao = Dao.getInstance(getApplicationContext());
        model = AppModel.getInstance();
    }

    public void onLoginButtonClicked(View view) {
        String username = vm.getUsername().getValue();
        String password = vm.getPassword().getValue();

        if(!validate(username, password, this)) return;

        dao.getLoginDAO().login(username, password, result -> {
            if (result == null) {
                Toast.makeText(this, getString(R.string.error_msg_internet_offline), Toast.LENGTH_LONG).show();
                return;
            }

            model.setLoginData(result);
            if (result.getLoggedIn()) {
                // Login avvenuto con successo
                Toast.makeText(this, getString(R.string.success_msg_login, username), Toast.LENGTH_LONG).show();
                finish();
            } else {
                Toast.makeText(this, getString(R.string.error_msg_wrong_credentials), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void onSignupButtonClicked(View view) {
        String username = vm.getUsername().getValue();
        String password = vm.getPassword().getValue();

        if(!validate(username, password, this)) return;

        dao.getLoginDAO().signup(username, password, result -> {
            if (result != null) {
                switch (result) {
                    case 0: // Success
                        model.setLoginData(new LoginData(true, username, false));
                        Toast.makeText(this, getString(R.string.success_msg_login, username), Toast.LENGTH_LONG).show();
                        finish();
                        break;
                    case 2: // Lo username è già in uso da un altro utente
                        Toast.makeText(this, getString(R.string.error_msg_username_not_unique), Toast.LENGTH_LONG).show();
                        break;
                    default: // Unknown error
                        Toast.makeText(this, getString(R.string.error_msg_generic), Toast.LENGTH_LONG).show();
                        break;
                }
            } else {
                // Problemi di connessione
                Toast.makeText(this, getString(R.string.error_msg_internet_offline), Toast.LENGTH_LONG).show();
            }
        });
    }

    private static boolean validate(String username, String password, Context context) {
        if (username.isEmpty()) {
            Toast.makeText(context, context.getString(R.string.error_msg_empty_username), Toast.LENGTH_LONG).show();
            return false;
        }

        if (password.isEmpty()) {
            Toast.makeText(context, context.getString(R.string.error_msg_empty_password), Toast.LENGTH_LONG).show();
            return false;
        }

        if (username.length() > context.getResources().getInteger(R.integer.max_username_len)) {
            String msg = context.getString(R.string.error_msg_long_username, context.getResources().getInteger(R.integer.max_username_len));
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
            return false;
        }

        if (password.length() > context.getResources().getInteger(R.integer.max_password_len)) {
            String msg = context.getString(R.string.error_msg_long_password, context.getResources().getInteger(R.integer.max_password_len));
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }
}
