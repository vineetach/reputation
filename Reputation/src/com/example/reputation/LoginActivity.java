package com.example.reputation;

import connection.ConnectionListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends FragmentActivity implements ConnectionListener<Boolean> {
    private EditText mUserName;
    private EditText mPassword;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_ui);
        
        setListeners();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }
    
    private void setListeners() {
        mUserName = (EditText)findViewById(R.id.loginscreen_loginText);
        mUserName.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                
            }
            
        });
        
        mPassword = (EditText)findViewById(R.id.loginscreen_passText);
        mPassword.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                
            }
            
        });
        
        
        Button loginButton = (Button)findViewById(R.id.loginscreen_login_button);
        loginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                verifyAccount(mUserName.getText().toString(), mPassword.getText().toString());
            }
        });
    }
    
    private void verifyAccount(String userName, String passWord) {
        //TODO- Make a connection to the server with login credentials
        //Pass this as the listener. Upon response back open the next activity.
        
        //TODO-Move this to onReqComplete
        /*Fragment fragment = new UnreadContentFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(android.R.id.content, fragment, getClass().getSimpleName()).commit();
        getSupportFragmentManager().executePendingTransactions();*/
        
        final Intent mainIntent = new Intent(LoginActivity.this, NavigationActivity.class);
        LoginActivity.this.startActivity(mainIntent);
        LoginActivity.this.finish();
        
       this.finish();
    }
    
    @Override
    public void onRequestComplete(Boolean response) {
        // TODO Auto-generated method stub
        //Pass this as the listener. Upon response back open the next activity.
        
    }

    @Override
    public void onError(int status, String error) {
        // TODO Auto-generated method stub
        
    }
}