package com.berkay22demirel.otoyakthesaplama.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by BerkayDemirel on 14.02.2018.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String FIRST_DATABASE_CREATE = "CREATE TABLE " + OtoYakitHesaplamaDatabaseContract.FIRST_TABLE_NAME + " (" +
            OtoYakitHesaplamaDatabaseContract.Kayit._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            OtoYakitHesaplamaDatabaseContract.Kayit.COLUMN_UCRET + " VARCHAR(20) NOT NULL," +
            OtoYakitHesaplamaDatabaseContract.Kayit.COLUMN_YOL + " VARCHAR(20) NOT NULL," +
            OtoYakitHesaplamaDatabaseContract.Kayit.COLUMN_FIYAT + " VARCHAR(20) NOT NULL," +
            OtoYakitHesaplamaDatabaseContract.Kayit.COLUMN_BASLANGIC_TARIH + " VARCHAR(30) NOT NULL," +
            OtoYakitHesaplamaDatabaseContract.Kayit.COLUMN_BITIS_TARIH + " VARCHAR(30) NOT NULL," +
            OtoYakitHesaplamaDatabaseContract.Kayit.COLUMN_YUZ_KM_YAKIT_SONUC + " VARCHAR(20) NOT NULL," +
            OtoYakitHesaplamaDatabaseContract.Kayit.COLUMN_BIR_KM_UCRET_SONUC + " VARCHAR(20) NOT NULL," +
            OtoYakitHesaplamaDatabaseContract.Kayit.COLUMN_BIR_GUN_UCRET_SONUC + " VARCHAR(20) NOT NULL," +
            OtoYakitHesaplamaDatabaseContract.Kayit.COLUMN_PLAKA + " VARCHAR(20) NOT NULL );";

    public static final String SECOND_DATABASE_CREATE = "CREATE TABLE " + OtoYakitHesaplamaDatabaseContract.SECOND_TABLE_NAME + " (" +
            OtoYakitHesaplamaDatabaseContract.Plaka._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            OtoYakitHesaplamaDatabaseContract.Plaka.COLUMN_PLAKA + " VARCHAR(20) NOT NULL );";

    public static final String DATABASE_DROP = "DROP TABLE IF EXISTS " + OtoYakitHesaplamaDatabaseContract.FIRST_TABLE_NAME + "; DROP TABLE IF EXISTS " + OtoYakitHesaplamaDatabaseContract.SECOND_TABLE_NAME + ";";

    public DatabaseHelper(Context context) {
        super(context, OtoYakitHesaplamaDatabaseContract.DATABASE_NAME, null, OtoYakitHesaplamaDatabaseContract.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(FIRST_DATABASE_CREATE);
        db.execSQL(SECOND_DATABASE_CREATE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w("DatabaseHelper","Veritabani" + oldVersion + "\'dan" + newVersion + "\'a guncelleniyor");
        db.execSQL(DATABASE_DROP);
        onCreate(db);
    }


}
