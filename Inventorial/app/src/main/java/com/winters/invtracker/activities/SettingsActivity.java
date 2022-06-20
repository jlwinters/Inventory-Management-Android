package com.winters.invtracker.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import com.google.android.material.snackbar.Snackbar;
import com.winters.invtracker.NotificationTimer;
import com.winters.invtracker.R;

public class SettingsActivity extends AppCompatActivity
        implements ActivityCompat.OnRequestPermissionsResultCallback {

    private NotificationTimer notificationService;

    private static final int PERMISSION_REQUEST_SMS = 0;

    private View mLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        mLayout = findViewById(R.id.settings_layout);

        // Register a listener for the 'Enable SMS Notifications' button.
        findViewById(R.id.button_enable_sms).setOnClickListener(view -> showSmsPreview());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_SMS) {
            // Request for SMS permission
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission has been granted
                Snackbar.make(mLayout, R.string.sms_permission_granted,
                                Snackbar.LENGTH_SHORT)
                        .show();
                sendSmsThroughSmsManager();
            } else {
                // Permission request was denied
                Snackbar.make(mLayout, R.string.sms_permission_denied,
                                Snackbar.LENGTH_SHORT)
                        .show();
            }
        }
    }

    private void showSmsPreview() {
        // Check if the SMS permission has been granted
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            // Permission is already available, start SMS function
            Snackbar.make(mLayout,
                    R.string.sms_permission_available,
                    Snackbar.LENGTH_SHORT).show();
            sendSmsThroughSmsManager();
        } else {
            // Permission is missing and must be requested
            requestSmsPermission();
        }
    }

    /**
     * Requests SMS permissions and provides rationale if denied
     */
    private void requestSmsPermission() {
        // Permission has not been granted and must be requested
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.SEND_SMS)) {
            Snackbar.make(mLayout, R.string.sms_access_required,
                    Snackbar.LENGTH_INDEFINITE).setAction(R.string.ok, view -> {
                        // Request the permission
                        ActivityCompat.requestPermissions(SettingsActivity.this,
                                new String[]{Manifest.permission.SEND_SMS},
                                PERMISSION_REQUEST_SMS);
                    }).show();

        } else {
            Snackbar.make(mLayout, R.string.sms_unavailable, Snackbar.LENGTH_SHORT).show();
            // Request the permission. The result will be received in onRequestPermissionResult()
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.SEND_SMS}, PERMISSION_REQUEST_SMS);
        }
    }

    public void sendSmsThroughSmsManager() {
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage("+1234", null, "Item stock is running low", null, null);
    }
}