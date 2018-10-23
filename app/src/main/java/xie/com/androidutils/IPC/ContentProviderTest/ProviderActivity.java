package xie.com.androidutils.IPC.ContentProviderTest;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import xie.com.androidutils.R;

public class ProviderActivity extends AppCompatActivity {
    private static final String TAG ="ProviderActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider);
        Uri uri = Uri.parse("content://xie.com.androidutils.IPC.BindTest.book.provider/book");
        ContentValues values = new ContentValues();
        values.put("_id",6);
        values.put("name","程序设计艺术");
        getContentResolver().insert(uri,values);
        Cursor cursor = getContentResolver().query(uri, new String[]{"_id", "name"}, null, null, null);
        while (cursor.moveToNext()){
            int id = cursor.getInt(0);
            String name =cursor.getString(1);
            Book book = new Book(name, id);
            Log.d(TAG,"book info is :"+book.toString());
        }
        cursor.close();

        Uri userUri = Uri.parse("content://xie.com.androidutils.IPC.BindTest.book.provider/user");
        Cursor userCursor = getContentResolver().query(userUri, new String[]{"_id", "name", "sex"}, null, null, null);
        while (userCursor.moveToNext()){
            int id = userCursor.getInt(0);
            String name = userCursor.getString(1);
            String sex = userCursor.getString(2);
            User user = new User(id, name, sex);
            Log.d(TAG,"user info is :"+user.toString());
        }
        userCursor.close();
    }
}
