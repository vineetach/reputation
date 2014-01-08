package com.example.reputation;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;


public class UnreadContentActivity extends FragmentActivity {
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.fragment_container_activity);
        loadInitialFragment();
    }
    
    public void loadInitialFragment() {
        UnreadContentFragment fragment  = new UnreadContentFragment();
        
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(android.R.id.content, fragment, getClass().getSimpleName()).commit();
        getSupportFragmentManager().executePendingTransactions();
    }
}