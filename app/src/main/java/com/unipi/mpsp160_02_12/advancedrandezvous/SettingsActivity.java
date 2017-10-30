package com.unipi.mpsp160_02_12.advancedrandezvous;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.Toast;

import java.util.Locale;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;
import static com.unipi.mpsp160_02_12.advancedrandezvous.R.string.language;

/**
 * Created by Nick on 22/10/2017.
 */

public class SettingsActivity extends AppCompatActivity {

    private RadioButton languageRadio1;
    private RadioButton languageRadio2;
    private Button saveSettings;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.settings_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle(R.string.settings);
        if (getIntent().getBooleanExtra("EXIT", false))
        {
            finish();
        }

        languageRadio1 = (RadioButton) findViewById(R.id.languageRadio1);
        languageRadio1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    languageRadio2.setChecked(false);
                }
            }
        });


        languageRadio2 = (RadioButton) findViewById(R.id.languageRadio2);
        languageRadio2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    languageRadio1.setChecked(false);
                }
            }
        });


        SharedPreferences languagepref = getSharedPreferences("language",MODE_PRIVATE);
        String language = languagepref.getString("languageToLoad", "novalue");
        if ("el".equals(language)){
            languageRadio2.setChecked(true);
            languageRadio1.setChecked(false);
        }
        else if("en".equals(language)){
            languageRadio1.setChecked(true);
            languageRadio2.setChecked(false);
        }

        saveSettings = (Button)findViewById(R.id.saveSettingsButton);
        saveSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String languageToLoad = "en"; // your language
                if (languageRadio2.isChecked()){
                    languageToLoad = "el";
                }
                Locale locale = new Locale(languageToLoad);
                Locale.setDefault(locale);
                Configuration config = new Configuration();
                config.locale = locale;
                getBaseContext().getResources().updateConfiguration(config,
                        getBaseContext().getResources().getDisplayMetrics());
                SharedPreferences languagepref = getSharedPreferences("language",MODE_PRIVATE);
                SharedPreferences.Editor editor = languagepref.edit();
                editor.putString("languageToLoad",languageToLoad );
                editor.commit();

//                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                intent.putExtra("EXIT", true);
//                startActivity(intent);
                finish();

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

}
