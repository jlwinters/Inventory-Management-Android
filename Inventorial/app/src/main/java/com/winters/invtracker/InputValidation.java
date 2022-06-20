package com.winters.invtracker;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class InputValidation {
    private final Context context;

    public InputValidation(Context context) {
        this.context = context;
    }

    /**
     * Check for filled input
     */
    public boolean isInputEditTextFilled(TextInputEditText textInputEditText, TextInputLayout textInputLayout, String message) {
        String value = Objects.requireNonNull(textInputEditText.getText()).toString().trim();
        if (value.isEmpty()) {
            textInputLayout.setError(message);
            hideKeyboardFrom(textInputEditText);
            return false;
        } else {
            textInputLayout.setErrorEnabled(false);
        }
        return true;
    }

    /**
     * Check for non-null input
     */
    public boolean isInputEditTextUser(TextInputEditText textInputEditText, TextInputLayout textInputLayout, String message) {
        String value = Objects.requireNonNull(textInputEditText.getText()).toString().trim();
        if (value.isEmpty()) {
            textInputLayout.setError(message);
            hideKeyboardFrom(textInputEditText);
            return false;
        } else {
            textInputLayout.setErrorEnabled(false);
        }
        return true;
    }

    public boolean isInputEditTextMatches(TextInputEditText textInputEditText1, TextInputEditText textInputEditText2, TextInputLayout textInputLayout, String message) {
        String value1 = Objects.requireNonNull(textInputEditText1.getText()).toString().trim();
        String value2 = Objects.requireNonNull(textInputEditText2.getText()).toString().trim();
        if (!value1.contentEquals(value2)) {
            textInputLayout.setError(message);
            hideKeyboardFrom(textInputEditText2);
            return false;
        } else {
            textInputLayout.setErrorEnabled(false);
        }
        return true;
    }

    /**
     * Hide the keyboard
     */
    private void hideKeyboardFrom(View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
}