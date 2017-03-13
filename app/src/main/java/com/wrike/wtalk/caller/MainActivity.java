package com.wrike.wtalk.caller;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telecom.PhoneAccount;
import android.telecom.PhoneAccountHandle;
import android.telecom.TelecomManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static final String label = "WTalk caller";
    public static final String extra_from = "from";
    public static final int CALL_PERMISSION_CODE = 12;
    private TelecomManager telecomManager;
    private PhoneAccountHandle accountHandle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        registerPhoneAccount();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                imitateIncomingCall();
                checkCallingPermission();
            }
        });
    }

    private void imitateIncomingCall() {
        Bundle params = new Bundle();
        params.putString(extra_from, label);
        telecomManager.addNewIncomingCall(accountHandle, params);
    }

    private void checkCallingPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, CALL_PERMISSION_CODE);
        } else {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, CALL_PERMISSION_CODE);
            } else {
                imitateOutgoingCall();
            }
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            imitateOutgoingCall();
        } else {
            Log.w("Main Activity", "permission was NOT granted.");
        }
    }

    private void imitateOutgoingCall() {
        try {
            Uri uri = Uri.fromParts("wtalk", "12345", null);
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.putExtra(TelecomManager.EXTRA_PHONE_ACCOUNT_HANDLE, accountHandle);
            intent.setData(uri);
            ComponentName componentName = intent.resolveActivity(getPackageManager());
            if (componentName != null) {
                startActivity(intent);
            }
        } catch (Throwable any) {
            any.printStackTrace();
        }
    }

    private void registerPhoneAccount() {
        telecomManager = (TelecomManager) this.getSystemService(Context.TELECOM_SERVICE);
        ComponentName testCaller = new ComponentName(getPackageName(), CallConnectionService.class.getName());
        accountHandle = new PhoneAccountHandle(testCaller, label);
        ArrayList<String> shemas = new ArrayList<>();
        shemas.add("wtalk");
        PhoneAccount account = PhoneAccount.builder(accountHandle, label)
                .setAddress(Uri.parse("KUBFAX6T@wrike.user/android")) // represents the destination from which outgoing calls using this {@code PhoneAccount} will appear to come, if applicable, and the destination to which incoming calls using this {@code PhoneAccount} may be addressed
                .setCapabilities(PhoneAccount.CAPABILITY_CONNECTION_MANAGER
                        | PhoneAccount.CAPABILITY_VIDEO_CALLING
                        | PhoneAccount.CAPABILITY_CALL_SUBJECT)
                .setSupportedUriSchemes(shemas)
                .build();
        telecomManager.registerPhoneAccount(account);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            showCallingSettings();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showCallingSettings() {
        Intent intent = new Intent(TelecomManager.ACTION_CHANGE_PHONE_ACCOUNTS);
        startActivity(intent);
    }
}
