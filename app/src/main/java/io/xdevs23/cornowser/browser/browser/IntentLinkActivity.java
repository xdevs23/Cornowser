package io.xdevs23.cornowser.browser.browser;

import android.content.Intent;
import android.os.Bundle;

import org.xdevs23.android.app.XquidCompatActivity;

import io.xdevs23.cornowser.browser.CornBrowser;

public class IntentLinkActivity extends XquidCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(getApplicationContext(), CornBrowser.class);
        intent.putExtra("intent_link_uri_load", getIntent().getData().toString());
        startActivity(intent);
    }
}
