package com.winters.invtracker.activities;

import android.os.Bundle;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.widget.NestedScrollView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.winters.invtracker.InputValidation;
import com.winters.invtracker.R;
import com.winters.invtracker.classes.User;
import com.winters.invtracker.helpers.UserDatabaseHelper;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private final AppCompatActivity activity = RegisterActivity.this;
    private NestedScrollView nestedScrollView;
    private TextInputLayout textInputLayoutUser;
    private TextInputLayout textInputLayoutPassword;
    private TextInputLayout textInputLayoutConfirmPassword;
    private TextInputEditText textInputEditTextUser;
    private TextInputEditText textInputEditTextPassword;
    private TextInputEditText textInputEditTextConfirmPassword;
    private AppCompatButton appCompatButtonRegister;
    private AppCompatTextView appCompatTextViewLoginLink;
    private InputValidation inputValidation;
    private UserDatabaseHelper userDatabaseHelper;
    private User user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Objects.requireNonNull(getSupportActionBar()).hide();
        initViews();
        initListeners();
        initObjects();
    }

    /**
     * Initialize views, listeners, and objects
     */
    private void initViews() {
        nestedScrollView = (NestedScrollView) findViewById(R.id.nestedScrollView);
        textInputLayoutUser = (TextInputLayout) findViewById(R.id.textInputLayoutUser);
        textInputLayoutPassword = (TextInputLayout) findViewById(R.id.textInputLayoutPassword);
        textInputLayoutConfirmPassword = (TextInputLayout) findViewById(R.id.textInputLayoutConfirmPassword);
        textInputEditTextUser = (TextInputEditText) findViewById(R.id.textInputEditTextUser);
        textInputEditTextPassword = (TextInputEditText) findViewById(R.id.textInputEditTextPassword);
        textInputEditTextConfirmPassword = (TextInputEditText) findViewById(R.id.textInputEditTextConfirmPassword);
        appCompatButtonRegister = (AppCompatButton) findViewById(R.id.appCompatButtonRegister);
        appCompatTextViewLoginLink = (AppCompatTextView) findViewById(R.id.appCompatTextViewLoginLink);
    }

    private void initListeners() {
        appCompatButtonRegister.setOnClickListener(this);
        appCompatTextViewLoginLink.setOnClickListener(this);
    }

    private void initObjects() {
        inputValidation = new InputValidation(activity);
        userDatabaseHelper = new UserDatabaseHelper(activity);
        user = new User();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.appCompatButtonRegister) {
            postDataToSQLite();
        } else if (id == R.id.appCompatTextViewLoginLink) {
            finish();
        }
    }

    /**
     * Input validation to ensure username/password information is valid
     */
    private void postDataToSQLite() {
        if (!inputValidation.isInputEditTextFilled(textInputEditTextUser, textInputLayoutUser, getString(R.string.error_message_user))) {
            return;
        }
        if (!inputValidation.isInputEditTextUser(textInputEditTextUser, textInputLayoutUser, getString(R.string.error_message_user))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditTextPassword, textInputLayoutPassword, getString(R.string.error_message_password))) {
            return;
        }
        if (!inputValidation.isInputEditTextMatches(textInputEditTextPassword, textInputEditTextConfirmPassword,
                textInputLayoutConfirmPassword, getString(R.string.error_password_match))) {
            return;
        }
        if (!userDatabaseHelper.checkUser(Objects.requireNonNull(textInputEditTextUser.getText()).toString().trim())) {
            user.setUser(textInputEditTextUser.getText().toString().trim());
            user.setPassword(Objects.requireNonNull(textInputEditTextPassword.getText()).toString().trim());
            userDatabaseHelper.addUser(user);
            // Snack bar notification to display successful registration
            Snackbar.make(nestedScrollView, getString(R.string.success_message), Snackbar.LENGTH_LONG).show();
            emptyInputEditText();
        } else {
            // Snack bar notification to display unsuccessful registration
            Snackbar.make(nestedScrollView, getString(R.string.error_user_exists), Snackbar.LENGTH_LONG).show();
        }
    }

    private void emptyInputEditText() {
        textInputEditTextUser.setText(null);
        textInputEditTextPassword.setText(null);
        textInputEditTextConfirmPassword.setText(null);
    }
}