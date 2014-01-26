package com.example.reputation;

import imageDownload.ImageCacheListener;
import imageDownload.ImageInfo;
import imageDownload.RemoteImageCache;
import android.app.Activity;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;
    
public class ContentUIController implements LoaderCallbacks<Cursor>{
    
    private CustomDataAdapter  mAdapter;
    private CustomContentObserver mDataObserver;
    private Cursor mCursor;
    
    private ListView mListView;
    private boolean mStartLoading;
    
    private FragmentDataSource mDataSource;
    
    
    private static final String TAG = ContentUIController.class.getSimpleName();
    
    private RemoteImageCache mRemoteImageCache;
    
    private static final int MAX_PARALLEL_IMAGE_ACTIVE_DOWNLOAD = 2;
    private static final int MAX_IMAGES_IN_MEMORY = 200;
    
    public ContentUIController(Fragment fragment) {
        //initFragment(fragment);
    }
    
    public void initFragment(Fragment fragment) {
        
        mListView = (ListView) fragment.getView().findViewById(R.id.content_list);
        
        mAdapter = new CustomDataAdapter(fragment.getActivity().getApplicationContext(), R.layout.content_ui_cell, null, null, null);
        
        mListView.setAdapter(mAdapter);
        
        mAdapter.setViewBinder(new ContentListRowViewBinder());
        
        mRemoteImageCache = new RemoteImageCache(mDataSource.getFragmentActivity().getApplicationContext(),
                                MAX_PARALLEL_IMAGE_ACTIVE_DOWNLOAD, 
                                null, 
                                MAX_IMAGES_IN_MEMORY);
        
        mListView.setOnScrollListener(new ContentListScrollListener());
    }
    
    public void setDataSource(FragmentDataSource source) {
        mDataSource = source;
    }
    
    public void refreshData() {
        //TODO:Refresh cursor when this UI opens and update UI
    }
    
    public void setDataController() {
        
    }
    
    private class ContentListScrollListener implements OnScrollListener {
        private boolean mScrollStateChanged;
        @Override
        public void onScroll(AbsListView view, int firstVisibleItem,
                int visibleItemCount, int totalItemCount) {
          
            if (mScrollStateChanged && !mStartLoading && mListView.getChildCount() > 0 && 
                    mListView.getLastVisiblePosition() == mListView.getAdapter().getCount() - 1 && 
                    mListView.getChildAt(mListView.getChildCount() - 1).getBottom() <= mListView.getHeight()) {
                mStartLoading = true;
                updateCursorAndView(true);
                mScrollStateChanged = false;
                
            }
        }
        
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            mScrollStateChanged = true;
        }
    }
    
    private class CustomDataAdapter extends SimpleCursorAdapter {
        int mLayout;
        
        public CustomDataAdapter(Context context, int layout, Cursor c,
                String[] from, int[] to) {
            super(context, layout, c, from, to, FLAG_REGISTER_CONTENT_OBSERVER);
            mLayout = layout;
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            final LayoutInflater inflater = LayoutInflater.from(context);
            View v = inflater.inflate(mLayout, parent, false);
            return v;
        }
    }
    
    public class ContentListRowViewBinder implements SimpleCursorAdapter.ViewBinder {
        @Override
        public boolean setViewValue(View v, final Cursor c, int columnIndex) {
            
            if(v.getId() == R.id.content_cell_name) {
                //TextView view = (TextView)v;
                
            }
            
            if(v.getId() == R.id.content_cell_review_text) {
                
            }
            
            if(v.getId() == R.id.content_cell_responded) {
                SpannableString contentUnderline = new SpannableString(
                        mDataSource.getFragmentActivity().getResources().getString(R.string.responded));
                contentUnderline.setSpan(new UnderlineSpan(), 0,
                        contentUnderline.length(), 0);
                TextView view = (TextView)v;
                view.setText(contentUnderline);
            }
            
            if(v.getId() == R.id.content_cell_read_more) {
                SpannableString contentUnderline = new SpannableString(
                        mDataSource.getFragmentActivity().getResources().getString(R.string.read_more));
                contentUnderline.setSpan(new UnderlineSpan(), 0,
                        contentUnderline.length(), 0);
                TextView view = (TextView)v;
                view.setText(contentUnderline);
                
                v.setOnClickListener(new OnClickListener() {
                    
                    @Override
                    public void onClick(final View v) {
                        
                    }
                });
            }
            
            if(v.getId() == R.id.content_cell_image) {
                ImageView view = (ImageView)v;
                
                //TODO - get the actual url from cursor
                String url = "";
                int hash = url.hashCode();
                
                if (Integer.MIN_VALUE == hash) {
                    hash++;
                }
                
                final int imgKey = Math.abs(hash);
                view.setTag(imgKey);
                
                final int position = c.getPosition();
                
                ImageInfo imageInfo = new ImageInfo();
                //String id = url;
                //imageInfo.id = id;
                //imageInfo.imageUrl = url;
                imageInfo.width = v.getWidth();
                imageInfo.height = v.getHeight();
                imageInfo.format = Bitmap.CompressFormat.JPEG;
                imageInfo.quality = 80;
                imageInfo.sample = true;
                
                imageInfo.listener = new ImageCacheListener() {

                    @Override
                    public void onSuccess(ImageInfo imageInfo, Bitmap bitmap) {

                        View view = mListView.findViewWithTag(imageInfo.id);
                        if (view instanceof ImageView) {
                            
                            if (mListView.getPositionForView(view) == position) {
                                //TODO:Check if rounding needs to be done
                                ((ImageView) view).setImageBitmap(bitmap);
                                
                                view.setTag(null);
                            }
                        }
                    }

                  };
                  
                  Bitmap image = mRemoteImageCache.getImage(imageInfo);
                  if (image == null) {
                    //TODO- Have a default image
                    //image = defaultImage;
                  } else {
                      view.setImageBitmap(image);
                  }
            }
            
            return true;
        }
        
    }
    
    private class CustomContentObserver extends ContentObserver {
        
        public CustomContentObserver() {
            super(new Handler());
        }
        
        @Override
        public boolean deliverSelfNotifications() {
            return true;
        }

        @Override
        public void onChange(boolean selfChange) {
            
        }
    }
    
    public void updateCursorAndView(boolean loadIncrement) {
        Activity activity = mDataSource.getFragmentActivity();
                
        //TODO Handle all the cursorLoader activity here
    }

    @Override
    public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onLoaderReset(Loader<Cursor> arg0) {
        // TODO Auto-generated method stub
        
    }
}