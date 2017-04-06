package aktorius.com.android.todolist.Contracts;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Aktorius on 06/04/2017.
 */

public class TodoListContract {

    public static final String CONTENT_AUTHORITHY = "aktorius.com.todolist";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITHY);
    public static final String PATH_TODO = "todo";

    public static final class TodoEntry implements BaseColumns{
        public static final String TABLE_NAME = "todo";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_DONE = "done";

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_TODO).build();
        public static final String CONTENT_TYPE = "vmd.android.cursor.dir/"
                                                    + CONTENT_AUTHORITHY +"/" + PATH_TODO;
        public static final String CONTENT_ITEM_TYPE = "vmd.android.cursor.item/"
                                                    + CONTENT_AUTHORITHY +"/" + PATH_TODO;

    }
}
