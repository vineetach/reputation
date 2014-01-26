package com.example.reputation;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MyContentFeedActivity extends FragmentActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_content_feed_ui);
        
        setListeners();
    }
    
    private void setListeners() {
        Button configure_settings = (Button)findViewById(R.id.contentfeedscreen_settings);
        configure_settings.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //Open the notifs/feed settings activity
                Fragment fragment = new ConfigureFeedNotifsFragment();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(android.R.id.content, fragment, getClass().getSimpleName()).commit();
                getSupportFragmentManager().executePendingTransactions();
            }
        });
        
        SpannableString textUnderline = new SpannableString(getString(R.string.use_default_setting));
        textUnderline.setSpan(new UnderlineSpan(), 0,textUnderline.length(), 0);
        TextView use_default_settings = (TextView)findViewById(R.id.contentfeedscreen_default_setting);
        use_default_settings.setText(textUnderline);
        
        use_default_settings.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MyContentFeedActivity.this.finish();
            }
        });
        
    }
}