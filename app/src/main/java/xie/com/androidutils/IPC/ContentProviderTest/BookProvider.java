package xie.com.androidutils.IPC.ContentProviderTest;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

public class BookProvider extends ContentProvider {
    private static final String TAG = "BookProvider";
    private static final String AUTHORITY = "xie.com.androidutils.IPC.BindTest.book.provider";
    private static final int BOOK_URI_CODE = 0;
    private static final int USER_URI_CODE = 1;
    private static final Uri BOOK_CONTENT_URI = Uri.parse("content://"+AUTHORITY+"/book");
    private static final Uri USER_CONTENT_URI = Uri.parse("content://"+AUTHORITY+"/user");
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private Context mContext;
    private SQLiteDatabase mDb;
    static {
        sUriMatcher.addURI(AUTHORITY,"book",BOOK_URI_CODE);
        sUriMatcher.addURI(AUTHORITY,"user",USER_URI_CODE);
    }

    private String getTableName(Uri uri){
        String tableName = null;
        switch (sUriMatcher.match(uri)){
            case BOOK_URI_CODE:
                tableName = DbOpenHelper.BOOK_TABLE_NAME;
                break;
            case USER_URI_CODE:
                tableName = DbOpenHelper.USER_TABLE_NAME;
                break;
        }
        return tableName;
    }

    @Override
    public boolean onCreate() {
        mContext = getContext();
        Log.d(TAG,"onCreate,current thread :"+Thread.currentThread().getName());
        initProviderData();
        return true;
    }

    private void initProviderData() {
        mDb = new DbOpenHelper(mContext).getWritableDatabase();
        mDb.execSQL("delete from "+DbOpenHelper.BOOK_TABLE_NAME);
        mDb.execSQL("delete from "+DbOpenHelper.USER_TABLE_NAME);
        mDb.execSQL("insert into book values(3,'android');");
        mDb.execSQL("insert into book values(1,'Ios');");
        mDb.execSQL("insert into book values(2,'html5');");
        mDb.execSQL("insert into book values(4,'python');");
        mDb.execSQL("insert into user values(1,'xiezheng','famale');");
        mDb.execSQL("insert into user values(2,'xiezheng1','male');");
        mDb.execSQL("insert into user values(3,'xiezheng2','famale');");
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Log.d(TAG,"query,current thread :"+Thread.currentThread().getName());
        String tableName = getTableName(uri);
        if (tableName == null){
            throw new IllegalArgumentException("unsopported URI:"+uri);
        }
        return mDb.query(tableName,projection,selection,selectionArgs,null,null,sortOrder,null);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        Log.d(TAG,"getType");
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        Log.d(TAG,"insert");
        String tableName = getTableName(uri);
        if (tableName == null){
            throw new IllegalArgumentException("unsopported URI:"+uri);
        }
        mDb.insert(tableName,null,values);
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        Log.d(TAG,"delete");
        String tableName = getTableName(uri);
        if (tableName == null){
            throw new IllegalArgumentException("unsopported URI:"+uri);
        }
        int delete = mDb.delete(tableName, selection, selectionArgs);
        return delete;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        Log.d(TAG,"update");
        String tableName = getTableName(uri);
        if (tableName == null){
            throw new IllegalArgumentException("unsopported URI:"+uri);
        }
        int row = mDb.update(tableName, values, selection, selectionArgs);
        return row;
    }
}
