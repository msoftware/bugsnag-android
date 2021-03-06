package com.bugsnag.android.example;

import java.util.HashMap;
import java.util.Map;
import java.util.Collection;
import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bugsnag.android.Bugsnag;
import com.bugsnag.android.Error;
import com.bugsnag.android.MetaData;
import com.bugsnag.android.BeforeNotify;
import com.bugsnag.android.Severity;

public class ExampleActivity extends Activity
{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // Initialize the Bugsnag client
        Bugsnag.init(this);

        // Execute some code before every bugsnag notification
        Bugsnag.beforeNotify(new BeforeNotify() {
            @Override
            public boolean run(Error error) {
                System.out.println(String.format("In beforeNotify - %s", error.getExceptionName()));
                return true;
            }
        });

        // Set the user information
        Bugsnag.setUser("123456", "james@example.com", "James Smith");

        // Add some global metaData
        Bugsnag.addToTab("user", "age", 31);
        Bugsnag.addToTab("custom", "account", "something");

        Bugsnag.leaveBreadcrumb("onCreate");

        new Thread(new Runnable() {
            public void run() {
                try {
                    sleepSoundly();
                } catch (java.lang.InterruptedException e) {

                }
            }

            private void sleepSoundly() throws java.lang.InterruptedException {
                Thread.sleep(100000);
            }
        }).start();
    }

    public void sendError(View view) {
        actuallySendError();
    }

    private void actuallySendError() {
        Bugsnag.notify(new RuntimeException("Non-fatal error"), Severity.ERROR);
        Toast.makeText(this, "Sent error", 1000).show();
    }

    public void sendWarning(View view) {
        actuallySendWarning();
    }

    private void actuallySendWarning() {
        Bugsnag.notify(new RuntimeException("Non-fatal warning"), Severity.WARNING);
        Toast.makeText(this, "Sent warning", 1000).show();
    }

    public void sendInfo(View view) {
        Bugsnag.notify(new RuntimeException("Non-fatal info"), Severity.INFO);
        Toast.makeText(this, "Sent info", 1000).show();
    }

    public void sendErrorWithMetaData(View view) {
        Map<String, String> nested = new HashMap<String, String>();
        nested.put("normalkey", "normalvalue");
        nested.put("password", "s3cr3t");

        Collection list = new ArrayList();
        list.add(nested);

        MetaData metaData = new MetaData();
        metaData.addToTab("user", "payingCustomer", true);
        metaData.addToTab("user", "password", "p4ssw0rd");
        metaData.addToTab("user", "credentials", nested);
        metaData.addToTab("user", "more", list);

        Bugsnag.notify(new RuntimeException("Non-fatal error with metaData"), Severity.ERROR, metaData);
        Toast.makeText(this, "Sent error with metaData", 1000).show();
    }

    public void crash(View view) {
        throw new RuntimeException("Something broke!");
    }
}
