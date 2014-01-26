package com.example.reputation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainReputationActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_ui);
        
        setListeners();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    private void setListeners() {
        Button getStarted = (Button)findViewById(R.id.welcomescreen_getStarted);
        getStarted.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                final Intent mainIntent = new Intent(MainReputationActivity.this, LoginActivity.class);
                MainReputationActivity.this.startActivity(mainIntent);
                MainReputationActivity.this.finish();
                
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
