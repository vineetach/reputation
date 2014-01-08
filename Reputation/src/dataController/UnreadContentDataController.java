package dataController;

import contentProvider.UnreadContentProviderMetaData.UnreadContentTableMetaData;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

public class UnreadContentDataController {
    
    private static final String TAG = UnreadContentDataController.class.getSimpleName();
    
    private static volatile UnreadContentDataController mUnreadContentDataController;
    
    private Context mContext;
    
    private UnreadContentDataController(Context context) {
        mContext = context;
    }
    
    public static synchronized UnreadContentDataController getSharedInstance(Context context) {
        if (mUnreadContentDataController == null) {
            mUnreadContentDataController = new UnreadContentDataController(context.getApplicationContext());
        }
        return mUnreadContentDataController;
    }
    
    public void deleteUnreadContent(String rowId) {
        try {
            Uri delUri = Uri.withAppendedPath(UnreadContentTableMetaData.CONTENT_URI, rowId);
            mContext.getContentResolver().delete(delUri, null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateUnreadContent(int rowId, ContentValues values) {
        try {
            Uri upUri = Uri.withAppendedPath(UnreadContentTableMetaData.CONTENT_URI, String.valueOf(rowId));
            mContext.getContentResolver().update(upUri,values, null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public int getCount() {
        Cursor c = mContext.getContentResolver().query(UnreadContentTableMetaData.CONTENT_URI,null, null, null,null,null);
        int numberOfRecords = c.getCount(); 
        c.close();
        return numberOfRecords;
    }
    
    public Cursor getAllUnreadContentInfo() {
        //TODO: Handle query limits and increments here
        Cursor c = null ;
        try {
            c = mContext.getContentResolver().query(UnreadContentTableMetaData.CONTENT_URI, null, null, null, null);
        } catch (Throwable t) {
             if(c != null) {
                 c.close();
                 c = null;
             }
        }
        return c;
    }
}