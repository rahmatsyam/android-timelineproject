package io.github.rahmatsyam.sevimatimeline.data.provider;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import io.github.rahmatsyam.sevimatimeline.data.model.PostItem;


public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DBNAME = "db_timeline";
    private static final String TABLENAME = "timeline";
    private static final String colId = "id";
    private static final String colStatus = "status";
    private static final String colDate = "date";


    public DatabaseHelper(Context context) {
        super(context, DBNAME, null, VERSION);
    }

    private void createTable(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS timeline");
        db.execSQL("CREATE TABLE if not exists timeline(id INTEGER NOT NULL PRIMARY KEY," +
                "status Text, date Text);"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //  createTable(db);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTable(db);


    }

    public PostItem getData(int id) {
        PostItem model = null;
        String selectData = "SELECT * FROM timeline WHERE id=" + String.valueOf(id);
        @SuppressLint("Recycle") Cursor data = this.getWritableDatabase().rawQuery(selectData, null);
        if (data.moveToFirst()) {
            model = new PostItem(Integer.parseInt(data.getString(data.getColumnIndex(colId))),
                    data.getString(data.getColumnIndex(colStatus)),
                    data.getString(data.getColumnIndex(colDate)));
        }

        return model;
    }


    public List<PostItem> getAll() {

        List<PostItem> model = new ArrayList<>();
        model.clear();
        String selectData = "SELECT * FROM timeline ORDER BY date DESC ";
        // Cursor data = this.getWritableDatabase().rawQuery(selectData, null);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery(selectData, null);
        // Cursor data = this.getWritableDatabase().rawQuery(selectData, null);

        if (data.moveToFirst()) {
            do {
                model.add(new PostItem(Integer.parseInt(data.getString(data.getColumnIndex("id"))),
                        data.getString(data.getColumnIndex("status")),
                        data.getString(data.getColumnIndex("date"))));
            } while (data.moveToNext());

            data.close();

        }
        return model;
    }

    public void editData(int id, String status, String date) {

        String editData = "UPDATE "+TABLENAME+ " SET "+ colStatus + "= '"+status +"', "+colDate + "= '"+date + "' WHERE "+colId +" ="+id;

        this.getWritableDatabase().execSQL(editData);
    }

    public void deleteData(int id) {
        String deleteData = " DELETE FROM timeline WHERE id=" + id;
        this.getWritableDatabase().execSQL(deleteData);
    }
}
