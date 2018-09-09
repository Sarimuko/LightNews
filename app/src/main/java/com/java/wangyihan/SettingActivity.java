package com.java.wangyihan;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTheme(Configure.noBarTheme);

        setContentView(R.layout.fragment_setting);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        Switch nightSwitch = findViewById(R.id.night_mode_switch);
        Switch saveSwitch = findViewById(R.id.save_net_switch);
        if (Configure.night)
        {

            nightSwitch.setChecked(true);
        }
        else
        {
            nightSwitch.setChecked(false);
        }

        if (Configure.save)
        {
            saveSwitch.setChecked(true);
        }else
        {
            saveSwitch.setChecked(false);
        }

        nightSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Configure.night = b;
                if (b)
                {
                    Configure.theme = R.style.NightAppTheme;
                    Configure.noBarTheme = R.style.NightAppTheme_NoActionBar;
                }
                else
                {
                    Configure.theme = R.style.LightAppTheme;
                    Configure.noBarTheme = R.style.LightAppTheme_NoActionBar;

                }

                recreate();
            }
        });

        saveSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Configure.save = b;
            }
        });

    }

}
