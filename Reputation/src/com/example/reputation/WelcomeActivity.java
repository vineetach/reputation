package com.example.reputation;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class WelcomeActivity extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_ui);
        
        setListeners();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }
    
    private void setListeners() {
        Button getStarted = (Button)findViewById(R.id.welcomescreen_getStarted);
        getStarted.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                
            }
        });
        
        Button configureApp = (Button)findViewById(R.id.welcomescreen_configureApp);
        configureApp.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                
            }
        });
    }
}