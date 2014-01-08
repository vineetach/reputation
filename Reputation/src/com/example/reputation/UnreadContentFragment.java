package com.example.reputation;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class UnreadContentFragment extends Fragment implements FragmentDataSource {
    ContentUIController mUIController;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    
    @Override  
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {  
        // Inflate the layout for this fragment  
        View view =  inflater.inflate(R.layout.content_ui, container, false);
        return view;  
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        
        mUIController = new ContentUIController(this);
    }
    
    @Override
    public void onStart() {
        super.onStart();
        
        refreshData();
    }
    
    private void refreshData() {
        mUIController.refreshData();
    }
    
    public Activity getFragmentActivity() {
        return getFragmentActivity();
    }
}