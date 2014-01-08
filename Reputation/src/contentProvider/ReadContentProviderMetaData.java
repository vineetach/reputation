package contentProvider;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class ReadContentProviderMetaData {
    public static final String AUTHORITY                = "contentProvider.ReadContentProvider";
    public static final int DATABASE_VERSION            = 1;
    public static final String DATABASE_NAME            = "readContentManager";
    public static final String READ_CONTENT_BASE_PATH    = "readContentAppStore";

    private ReadContentProviderMetaData() {}

    public static final class ReadContentTableMetaData implements BaseColumns {
        private ReadContentTableMetaData() {}

        public static final String DEFAULT_SORT_ORDER = "modified DESC";

        public static final String READ_CONTENT_TABLE_NAME = "readContent";

        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + READ_CONTENT_BASE_PATH); 

        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
                + "/mt-readcontent";
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
                + "/mt-readcontent";

        public static final String READ_CONTENT_ID     = "id";
        public static final String FIRST_NAME   = "firstname";
        public static final String LAST_NAME  = "lastname";
        public static final String CONTENT_TEXT = "content";
        public static final String RATING  = "rating";
        
        public static final String CREATED_DATE     = "created";
        public static final String MODIFIED_DATE    = "modified";

    }
}