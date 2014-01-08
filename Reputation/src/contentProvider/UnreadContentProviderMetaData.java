package contentProvider;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class UnreadContentProviderMetaData {
    public static final String AUTHORITY                = "contentProvider.UnreadContentProvider";
    public static final int DATABASE_VERSION            = 1;
    public static final String DATABASE_NAME            = "unreadContentManager";
    public static final String UNREAD_CONTENT_BASE_PATH    = "unreadContentAppStore";

    private UnreadContentProviderMetaData() {}

    public static final class UnreadContentTableMetaData implements BaseColumns {
        private UnreadContentTableMetaData() {}

        public static final String DEFAULT_SORT_ORDER = "modified DESC";

        public static final String UNREAD_CONTENT_TABLE_NAME = "unreadContent";

        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + UNREAD_CONTENT_BASE_PATH); 

        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
                + "/mt-unreadcontent";
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
                + "/mt-unreadcontent";

        public static final String UNREAD_CONTENT_ID     = "id";
        public static final String FIRST_NAME   = "firstname";
        public static final String LAST_NAME  = "lastname";
        public static final String CONTENT_TEXT = "content";
        public static final String RATING  = "rating";
        
        public static final String CREATED_DATE     = "created";
        public static final String MODIFIED_DATE    = "modified";

    }
}