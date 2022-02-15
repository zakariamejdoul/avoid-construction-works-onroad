package com.example.reseauroutier;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class Database extends SQLiteOpenHelper {
    private static final int version = 1;
    private static String DATABASE_NAME = "Chantiers";

    public Database(@Nullable Context context) {
        super(context, DATABASE_NAME, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+DATABASE_NAME+" (" +
                "id INTEGER PRIMARY KEY," +
                "avenue TEXT NOT NULL," +
                "ville TEXT NOT NULL," +
                "lat TEXT NOT NULL," +
                "lng TEXT NOT NULL," +
                "date_debut TEXT NOT NULL," +
                "date_fin TEXT NOT NULL," +
                "observation TEXT NOT NULL" +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public boolean insert(int id, String avenue, String ville, String lat, String lng, String date_debut, String date_fin, String observation)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", id);
        contentValues.put("avenue", avenue);
        contentValues.put("ville", ville);
        contentValues.put("lat", lat);
        contentValues.put("lng", lng);
        contentValues.put("date_debut", date_debut);
        contentValues.put("date_fin", date_fin);
        contentValues.put("observation", observation);
        long result = db.insert(DATABASE_NAME, null, contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    public boolean update(int id, String avenue, String ville, String lat, String lng, String date_debut, String date_fin, String observation)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", id);
        contentValues.put("avenue", avenue);
        contentValues.put("ville", ville);
        contentValues.put("lat", lat);
        contentValues.put("lng", lng);
        contentValues.put("date_debut", date_debut);
        contentValues.put("date_fin", date_fin);
        contentValues.put("observation", observation);
        Cursor cursor = db.rawQuery("select * from "+DATABASE_NAME+" where id=?",
                new String[] {id+""});
        if(cursor.getCount()>0)
        {
            long result = db.update(DATABASE_NAME, contentValues,
                    "id=?", new String[] {id+""});
            if(result == -1)
                return false;
            else
                return true;
        }
        return false;
    }

    public boolean delete(int id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from "+DATABASE_NAME+" where id=?",
                new String[] {id+""});
        if(cursor.getCount()>0)
        {
            long result = db.delete(DATABASE_NAME, "id=?",
                    new String[] {id+""});
            if(result == -1)
                return false;
            else
                return true;
        }
        return false;
    }
    public Cursor select()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("select * from " + DATABASE_NAME,null);
    }
}

