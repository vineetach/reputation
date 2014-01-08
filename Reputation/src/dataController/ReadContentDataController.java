package dataController;

import contentProvider.ReadContentProviderMetaData.ReadContentTableMetaData;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

public class ReadContentDataController {
    
    private static final String TAG = ReadContentDataController.class.getSimpleName();
    
    private static volatile ReadContentDataController mReadContentDataController;
    
    private Context mContext;
    
    private ReadContentDataController(Context context) {
        mContext = context;
    }
    
    public static synchronized ReadContentDataController getSharedInstance(Context context) {
        if (mReadContentDataController == null) {
            mReadContentDataController = new ReadContentDataController(context.getApplicationContext());
        }
        return mReadContentDataController;
    }
    
    public void deleteReadContent(String rowId) {
        try {
            Uri delUri = Uri.withAppendedPath(ReadContentTableMetaData.CONTENT_URI, rowId);
            mContext.getContentResolver().delete(delUri, null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateReadContent(int rowId, ContentValues values) {
        try {
            Uri upUri = Uri.withAppendedPath(ReadContentTableMetaData.CONTENT_URI, String.valueOf(rowId));
            mContext.getContentResolver().update(upUri,values, null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public int getCount() {
        Cursor c = mContext.getContentResolver().query(ReadContentTableMetaData.CONTENT_URI,null, null, null,null,null);
        int numberOfRecords = c.getCount(); 
        c.close();
        return numberOfRecords;
    }
    
    public Cursor getAllReadContentInfo() {
        //TODO: Handle query limits and increments here
        Cursor c = null ;
        try {
            c = mContext.getContentResolver().query(ReadContentTableMetaData.CONTENT_URI, null, null, null, null);
        }catch (Throwable t) {
             if(c != null) {
                 c.close();
                 c = null;
             }
        }
        return c;
    }
}