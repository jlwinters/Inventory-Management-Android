package com.winters.invtracker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.widget.NestedScrollView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.winters.invtracker.InputValidation;
import com.winters.invtracker.R;
import com.winters.invtracker.helpers.UserDatabaseHelper;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private final AppCompatActivity activity = LoginActivity.this;
    private NestedScrollView nestedScrollView;
    private TextInputLayout textInputLayoutUser;
    private TextInputLayout textInputLayoutPassword;
    private TextInputEditText textInputEditTextUser;
    private TextInputEditText textInputEditTextPassword;
    private AppCompatButton appCompatButtonLogin;
    private AppCompatTextView textViewLinkRegister;
    private InputValidation inputValidation;
    private UserDatabaseHelper userDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Objects.requireNonNull(getSupportActionBar()).hide();
        initViews();
        initListeners();
        initObjects();
    }

    private void initViews() {
        nestedScrollView = findViewById(R.id.nestedScrollView);
        textInputLayoutUser = findViewById(R.id.textInputLayoutUser);
        textInputLayoutPassword = findViewById(R.id.textInputLayoutPassword);
        textInputEditTextUser = findViewById(R.id.textInputEditTextUser);
        textInputEditTextPassword = findViewById(R.id.textInputEditTextPassword);
        appCompatButtonLogin = findViewById(R.id.appCompatButtonLogin);
        textViewLinkRegister = findViewById(R.id.textViewLinkRegister);
    }

    private void initListeners() {
        appCompatButtonLogin.setOnClickListener(this);
        textViewLinkRegister.setOnClickListener(this);
    }

    private void initObjects() {
        userDatabaseHelper = new UserDatabaseHelper(activity);
        inputValidation = new InputValidation(activity);
    }

    /**
     * Implements the login/register redirection
     */
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.appCompatButtonLogin) {
            verifyFromSQLite();
        } else if (id == R.id.textViewLinkRegister) {
            Intent intentRegister = new Intent(getApplicationContext(), RegisterActivity.class);
            startActivity(intentRegister);
        }
    }
    /**
     * Verify login credentials from SQLite
     */
    private void verifyFromSQLite() {
        if (!inputValidation.isInputEditTextFilled(textInputEditTextUser, textInputLayoutUser, getString(R.string.error_message_user))) {
            return;
        }
        if (!inputValidation.isInputEditTextUser(textInputEditTextUser, textInputLayoutUser, getString(R.string.error_message_user))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditTextPassword, textInputLayoutPassword, getString(R.string.error_message_user))) {
            return;
        }
        if (userDatabaseHelper.checkUser(Objects.requireNonNull(textInputEditTextUser.getText()).toString().trim()
            , Objects.requireNonNull(textInputEditTextPassword.getText()).toString().trim())) {
            Intent accountsIntent = new Intent(activity, ItemActivity.class);
            accountsIntent.putExtra("USER", textInputEditTextUser.getText().toString().trim());
            emptyInputEditText();
            startActivity(accountsIntent);
        }
        else
        {
            // Snack bar notification if information is invalid
            Snackbar.make(nestedScrollView, getString(R.string.error_valid_user_password), Snackbar.LENGTH_LONG).show();
        }
    }

    private void emptyInputEditText() {
        textInputEditTextUser.setText(null);
        textInputEditTextPassword.setText(null);
    }
}
