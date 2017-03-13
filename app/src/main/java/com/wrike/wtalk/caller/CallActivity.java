package com.wrike.wtalk.caller;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.telecom.PhoneAccount;
import android.telecom.PhoneAccountHandle;
import android.telecom.TelecomManager;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class CallActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);
        Button button = (Button) findViewById(R.id.drop);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TelecomManager telecomManager = (TelecomManager) CallActivity.this.getSystemService(Context.TELECOM_SERVICE);
                ComponentName testCaller = new ComponentName(getPackageName(), CallConnectionService.class.getName());
                PhoneAccountHandle accountHandle = new PhoneAccountHandle(testCaller, MainActivity.label);
                ArrayList<String> shemas = new ArrayList<>();
                shemas.add("wtalk");
                PhoneAccount account = PhoneAccount.builder(accountHandle, MainActivity.label)
                        .setAddress(Uri.parse("KUBFAX6T@wrike.user/android")) // represents the destination from which outgoing calls using this {@code PhoneAccount} will appear to come, if applicable, and the destination to which incoming calls using this {@code PhoneAccount} may be addressed
                        .setCapabilities(PhoneAccount.CAPABILITY_CONNECTION_MANAGER
                                | PhoneAccount.CAPABILITY_VIDEO_CALLING
                                | PhoneAccount.CAPABILITY_CALL_SUBJECT)
                        .setSupportedUriSchemes(shemas)
                        .build();
                telecomManager.registerPhoneAccount(account);
            }
        });
    }
}
