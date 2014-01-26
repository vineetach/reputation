package com.example.reputation;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class NavigationActivity extends FragmentActivity {
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    
    private String[] mTitles;
    
    static class ViewHolder {
        public TextView text;
        public ImageView image;
    }
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_ui);
        
        mTitle = mDrawerTitle = getTitle();
        
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.navigation_list);
        
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        
        mTitles = getResources().getStringArray(R.array.navigation_array);
        
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                    R.layout.drawer_list_item, mTitles));
        
        //TODO:Customize the navigation drawer list item to show icon
        //mDrawerList.setAdapter(new DrawerListAdapter());
        
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
                ) {
            
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            selectItem(0);
        }
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        //inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        //menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        
        return super.onOptionsItemSelected(item);
    }
    
    /* The click listner for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }
    
    private void selectItem(int position) {
        Fragment fragment = null;
        
        switch(position) {
            case 0:
                //Summary
                fragment  = new ReputationSummaryFragment();
                break;
            case 1:
                //Unread content
                fragment  = new UnreadContentFragment();
                break;
            case 2:
                //Read content
                fragment = new ReadContentFragment();
                break;
            case 3:
                //Settings
                fragment = new SettingsPersonalFragment();
                break;
            case 4:
                //Help
                break;
            case 5:
                //Rate this app
                break;
                
        }
        
        if(fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment, getClass().getSimpleName()).commit();
            getSupportFragmentManager().executePendingTransactions();
            
            //Update selected item and title, then close the drawer
            mDrawerList.setItemChecked(position, true);
            setTitle(mTitles[position]);
            mDrawerLayout.closeDrawer(mDrawerList);
        }
    }
    
    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }
    
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
    
    private class DrawerListAdapter extends ArrayAdapter<String> {
        
        public DrawerListAdapter(Activity context, String[] names) {
            super(context, R.layout.navigation_ui_cell, mTitles);
        }
        
        public DrawerListAdapter() {
            super(getApplicationContext(), R.layout.navigation_ui_cell, mTitles);
        }
        
        public View getView(int position, View convertView, ViewGroup parent) {
            View rowView = convertView;
            if (rowView == null) {
              LayoutInflater inflater = getLayoutInflater();
              rowView = inflater.inflate(R.layout.navigation_ui_cell, null);
              ViewHolder viewHolder = new ViewHolder();
              viewHolder.text = (TextView) rowView.findViewById(R.id.navigation_cell_name);
              viewHolder.image = (ImageView) rowView
                  .findViewById(R.id.navigation_cell_image);
              rowView.setTag(viewHolder);
            }
            
            ViewHolder holder = (ViewHolder) rowView.getTag();
            String title = mTitles[position];
            holder.text.setText(title);
            
            //holder.image.setImageResource(R.drawable.toggle_button_circle);
            
            return rowView;
        }
    }
}