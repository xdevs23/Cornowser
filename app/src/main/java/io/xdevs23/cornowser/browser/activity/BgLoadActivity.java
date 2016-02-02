package io.xdevs23.cornowser.browser.activity;


import android.content.Intent;
import android.os.Bundle;

import org.xdevs23.android.app.XquidCompatActivity;

import io.xdevs23.cornowser.browser.CornBrowser;

public class BgLoadActivity extends XquidCompatActivity {

    public static final String bgLoadKey = "bg_load_url";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            CornBrowser.isBgBoot = true;
            startActivity((new Intent(getApplicationContext(), CornBrowser.class))
                    .putExtra(bgLoadKey, getIntent().getDataString()));
            finish();
        } catch(Exception ex) {
            finish();
        }
    }

}
